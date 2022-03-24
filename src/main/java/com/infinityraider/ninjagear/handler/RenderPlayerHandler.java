package com.infinityraider.ninjagear.handler;

import com.infinityraider.ninjagear.registry.EffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
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

    private final HashMap<UUID, Boolean> invisibilityMap;

    private RenderPlayerHandler() {
        this.invisibilityMap = new HashMap<>();
    }

    public void setPlayerInvisibilityStatus(Player player, boolean invisible) {
        invisibilityMap.put(player.getUUID(), invisible);
    }

    public boolean isInvisible(Player player) {
        if(player == Minecraft.getInstance().player) {
            return player.hasEffect(EffectRegistry.getInstance().effectNinjaHidden);
        }
        return invisibilityMap.containsKey(player.getUUID()) && invisibilityMap.get(player.getUUID());
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onRenderLivingEvent(RenderLivingEvent.Pre<?,?> event) {
        LivingEntity entity = event.getEntity();
        if((entity instanceof Player) && isInvisible((Player) entity)) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onRenderLivingEvent(RenderPlayerEvent.Pre event) {
        Player entity = event.getPlayer();
        if(isInvisible(entity)) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
        }
    }
}
