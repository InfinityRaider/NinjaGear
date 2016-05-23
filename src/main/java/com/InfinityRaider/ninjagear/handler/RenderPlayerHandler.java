package com.InfinityRaider.ninjagear.handler;

import com.InfinityRaider.ninjagear.registry.PotionRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class RenderPlayerHandler {
    private static final RenderPlayerHandler INSTANCE = new RenderPlayerHandler();

    public static RenderPlayerHandler getInstance() {
        return INSTANCE;
    }

    private HashMap<UUID, Boolean> invisibilityMap;

    private RenderPlayerHandler() {
        this.invisibilityMap = new HashMap<>();
    }

    public void setPlayerInvisibilityStatus(EntityPlayer player, boolean invisible) {
        invisibilityMap.put(player.getUniqueID(), invisible);
    }

    public boolean isInvisible(EntityPlayer player) {
        if(player == Minecraft.getMinecraft().thePlayer) {
            return player.isPotionActive(PotionRegistry.getInstance().potionNinjaHidden);
        }
        return invisibilityMap.containsKey(player.getUniqueID()) && invisibilityMap.get(player.getUniqueID());
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onRenderLivingEvent(RenderLivingEvent.Pre event) {
        EntityLivingBase entity = event.getEntity();
        if((entity instanceof EntityPlayer) && isInvisible((EntityPlayer) entity)) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onRenderLivingSpecialsEvent(RenderLivingEvent.Specials.Pre event) {
        EntityLivingBase entity = event.getEntity();
        if ((entity instanceof EntityPlayer) && isInvisible((EntityPlayer) entity)) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
        }
    }
}
