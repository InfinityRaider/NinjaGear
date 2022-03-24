package com.infinityraider.ninjagear.handler;

import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.capability.CapabilityNinjaArmor;
import com.infinityraider.ninjagear.network.MessageInvisibility;
import com.infinityraider.ninjagear.api.v1.IHiddenItem;
import com.infinityraider.ninjagear.registry.EffectRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NinjaAuraHandler {
    private static final NinjaAuraHandler INSTANCE = new NinjaAuraHandler();

    public static NinjaAuraHandler getInstance() {
        return INSTANCE;
    }

    private NinjaAuraHandler() {}

    public void revealEntity(Player player, int duration, boolean breakSmoke) {
        boolean smoked = player.hasEffect(EffectRegistry.effectNinjaSmoked);
        if (smoked) {
            if(breakSmoke) {
                player.removeEffect(EffectRegistry.effectNinjaSmoked);
            } else {
                return;
            }
        }
        player.addEffect(new MobEffectInstance(EffectRegistry.effectNinjaRevealed, duration, 0, false, true));
    }

    private boolean isWearingFullNinjaArmor(Player player) {
        return this.checkEquipment(player.getItemBySlot(EquipmentSlot.HEAD))
                && this.checkEquipment(player.getItemBySlot(EquipmentSlot.CHEST))
                && this.checkEquipment(player.getItemBySlot(EquipmentSlot.LEGS))
                && this.checkEquipment(player.getItemBySlot(EquipmentSlot.FEET));
    }

    private boolean checkEquipment(ItemStack stack) {
        return CapabilityNinjaArmor.isNinjaArmor(stack);
    }

    private boolean checkHidingRequirements(Player player) {
        boolean revealed = player.hasEffect(EffectRegistry.effectNinjaRevealed);
        boolean smoked = player.hasEffect(EffectRegistry.effectNinjaSmoked);
        if(revealed && !smoked) {
            return false;
        }
        if(!player.isDiscrete()) {
            return false;
        }
        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        if(!mainHand.isEmpty()) {
            if(!(mainHand.getItem() instanceof IHiddenItem) || ((IHiddenItem) mainHand.getItem()).shouldRevealPlayerWhenEquipped(player, mainHand)) {
                return false;
            }
        }
        if(!offHand.isEmpty()) {
            if(!(offHand.getItem() instanceof IHiddenItem) || ((IHiddenItem) offHand.getItem()).shouldRevealPlayerWhenEquipped(player, offHand)) {
                return false;
            }
        }
        if (smoked) {
            return true;
        }
        int light;
        int light_block = player.getLevel().getBrightness(LightLayer.BLOCK, player.getOnPos());
        boolean day = player.getLevel().isDay();
        if(day) {
            int light_sky = player.getLevel().getBrightness(LightLayer.SKY, player.getOnPos());
            light = Math.max(light_sky, light_block);
        } else {
            light = light_block;
        }
        return light < NinjaGear.instance.getConfig().getBrightnessLimit();
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if(entity == null || !(entity instanceof Player) || entity.getLevel().isClientSide()) {
            return;
        }
        Player player = (Player) entity;
        if(this.isWearingFullNinjaArmor(player)) {
            boolean shouldBeHidden = this.checkHidingRequirements(player);
            boolean isHidden = player.hasEffect(EffectRegistry.effectNinjaHidden);
            if(shouldBeHidden != isHidden) {
                if(shouldBeHidden) {
                    player.addEffect(new MobEffectInstance(EffectRegistry.effectNinjaHidden, Integer.MAX_VALUE, 0, false, true));
                    if(!entity.getLevel().isClientSide()) {
                        new MessageInvisibility(player, true).sendToAll();
                    }
                } else {
                    player.removeEffect(EffectRegistry.effectNinjaHidden);
                    this.revealEntity(player, NinjaGear.instance.getConfig().getHidingCooldown(), true);
                    if(!entity.getLevel().isClientSide()) {
                        new MessageInvisibility(player, false).sendToAll();
                    }
                }
            }
        } else {
            if(player.hasEffect(EffectRegistry.effectNinjaHidden)) {
                player.removeEffect(EffectRegistry.effectNinjaHidden);
            }
            if(player.hasEffect(EffectRegistry.effectNinjaRevealed)) {
                player.removeEffect(EffectRegistry.effectNinjaRevealed);
            }
        }
    }
}
