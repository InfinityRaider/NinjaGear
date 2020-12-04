package com.infinityraider.ninjagear.handler;

import com.infinityraider.ninjagear.registry.EffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class RenderPlayerHandler {
    private static final RenderPlayerHandler INSTANCE = new RenderPlayerHandler();

    public static RenderPlayerHandler getInstance() {
        return INSTANCE;
    }

    private HashMap<UUID, Boolean> invisibilityMap;

    private RenderPlayerHandler() {
        this.invisibilityMap = new HashMap<>();
    }

    public void setPlayerInvisibilityStatus(PlayerEntity player, boolean invisible) {
        invisibilityMap.put(player.getUniqueID(), invisible);
    }

    public boolean isInvisible(PlayerEntity player) {
        if(player == Minecraft.getInstance().player) {
            return player.isPotionActive(EffectRegistry.getInstance().potionNinjaHidden);
        }
        return invisibilityMap.containsKey(player.getUniqueID()) && invisibilityMap.get(player.getUniqueID());
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onRenderLivingEvent(RenderLivingEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        if((entity instanceof PlayerEntity) && isInvisible((PlayerEntity) entity)) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
        }
    }
}
