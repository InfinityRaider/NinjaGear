package com.infinityraider.ninjagear.handler;

import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.capability.CapabilityNinjaArmor;
import com.infinityraider.ninjagear.network.MessageInvisibility;
import com.infinityraider.ninjagear.api.v1.IHiddenItem;
import com.infinityraider.ninjagear.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Hand;
import net.minecraft.world.LightType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NinjaAuraHandler {
    private static final NinjaAuraHandler INSTANCE = new NinjaAuraHandler();

    public static NinjaAuraHandler getInstance() {
        return INSTANCE;
    }

    private NinjaAuraHandler() {}

    public void revealEntity(Player player, int duration, boolean breakSmoke) {
        boolean smoked = player.isPotionActive(EffectRegistry.getInstance().effectNinjaSmoked);
        if (smoked) {
            if(breakSmoke) {
                player.removeActivePotionEffect(EffectRegistry.getInstance().effectNinjaSmoked);
            } else {
                return;
            }
        }
        player.addPotionEffect(new EffectInstance(EffectRegistry.getInstance().effectNinjaRevealed, duration, 0, false, true));
    }

    private boolean isWearingFullNinjaArmor(PlayerEntity player) {
        return this.checkEquipment(player.getItemStackFromSlot(EquipmentSlotType.HEAD))
                && this.checkEquipment(player.getItemStackFromSlot(EquipmentSlotType.CHEST))
                && this.checkEquipment(player.getItemStackFromSlot(EquipmentSlotType.LEGS))
                && this.checkEquipment(player.getItemStackFromSlot(EquipmentSlotType.FEET));
    }

    private boolean checkEquipment(ItemStack stack) {
        return CapabilityNinjaArmor.isNinjaArmor(stack);
    }

    private boolean checkHidingRequirements(PlayerEntity player) {
        boolean revealed = player.isPotionActive(EffectRegistry.getInstance().effectNinjaRevealed);
        boolean smoked = player.isPotionActive(EffectRegistry.getInstance().effectNinjaSmoked);
        if(revealed && !smoked) {
            return false;
        }
        if(!player.isSneaking()) {
            return false;
        }
        ItemStack mainHand = player.getHeldItem(Hand.MAIN_HAND);
        ItemStack offHand = player.getHeldItem(Hand.OFF_HAND);
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
        int light_block = player.getEntityWorld().getLightFor(LightType.BLOCK, player.getPosition());
        boolean day = player.getEntityWorld().isDaytime();
        if(day) {
            int light_sky = player.getEntityWorld().getLightFor(LightType.SKY, player.getPosition());
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
        if(entity == null || !(entity instanceof PlayerEntity) || entity.getEntityWorld().isRemote()) {
            return;
        }
        PlayerEntity player = (PlayerEntity) entity;
        if(this.isWearingFullNinjaArmor(player)) {
            boolean shouldBeHidden = this.checkHidingRequirements(player);
            boolean isHidden = player.isPotionActive(EffectRegistry.getInstance().effectNinjaHidden);
            if(shouldBeHidden != isHidden) {
                if(shouldBeHidden) {
                    player.addPotionEffect(new EffectInstance(EffectRegistry.getInstance().effectNinjaHidden, Integer.MAX_VALUE, 0, false, true));
                    if(!entity.getEntityWorld().isRemote) {
                        new MessageInvisibility(player, true).sendToAll();
                    }
                } else {
                    player.removePotionEffect(EffectRegistry.getInstance().effectNinjaHidden);
                    this.revealEntity(player, NinjaGear.instance.getConfig().getHidingCooldown(), true);
                    if(!entity.getEntityWorld().isRemote) {
                        new MessageInvisibility(player, false).sendToAll();
                    }
                }
            }
        } else {
            if(player.isPotionActive(EffectRegistry.getInstance().effectNinjaHidden)) {
                player.removePotionEffect(EffectRegistry.getInstance().effectNinjaHidden);
            }
            if(player.isPotionActive(EffectRegistry.getInstance().effectNinjaRevealed)) {
                player.removePotionEffect(EffectRegistry.getInstance().effectNinjaRevealed);
            }
        }
    }
}
