package com.infinityraider.ninjagear.handler;

import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.network.MessageInvisibility;
import com.infinityraider.ninjagear.api.v1.IHiddenItem;
import com.infinityraider.ninjagear.item.ItemNinjaArmor;
import com.infinityraider.ninjagear.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Hand;
import net.minecraft.world.LightType;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NinjaAuraHandler {
    private static final NinjaAuraHandler INSTANCE = new NinjaAuraHandler();

    public static NinjaAuraHandler getInstance() {
        return INSTANCE;
    }

    private NinjaAuraHandler() {}

    public void revealEntity(PlayerEntity player, int duration) {
        player.addPotionEffect(new EffectInstance(EffectRegistry.getInstance().potionNinjaRevealed, duration, 0, false, true));
    }

    private boolean shouldRemoveEffect(PlayerEntity player) {
        ItemStack helmet = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
        if (!(helmet.getItem() instanceof ItemNinjaArmor)) {
            return true;
        }
        ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
        if (!(chest.getItem() instanceof ItemNinjaArmor)) {
            return true;
        }
        ItemStack leggings = player.getItemStackFromSlot(EquipmentSlotType.LEGS);
        if (!(leggings.getItem() instanceof ItemNinjaArmor)) {
            return true;
        }
        ItemStack boots = player.getItemStackFromSlot(EquipmentSlotType.FEET);
        return !(boots.getItem() instanceof ItemNinjaArmor);
    }

    private boolean shouldBeHidden(PlayerEntity player) {
        if(!player.isPotionActive(EffectRegistry.getInstance().potionNinjaAura)) {
            return false;
        }
        if(player.isPotionActive(EffectRegistry.getInstance().potionNinjaRevealed)) {
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
        if(entity == null || entity.getEntityWorld().isRemote || !(entity instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity) entity;
        if(this.shouldRemoveEffect(player)) {
            player.removePotionEffect(EffectRegistry.getInstance().potionNinjaAura);
            if(player.isPotionActive(EffectRegistry.getInstance().potionNinjaHidden)) {
                player.removePotionEffect(EffectRegistry.getInstance().potionNinjaHidden);
            }
            if(player.isPotionActive(EffectRegistry.getInstance().potionNinjaRevealed)) {
                player.removePotionEffect(EffectRegistry.getInstance().potionNinjaRevealed);
            }
        } else {
            boolean shouldBeHidden = this.shouldBeHidden(player);
            boolean isHidden = player.isPotionActive(EffectRegistry.getInstance().potionNinjaHidden);
            if(shouldBeHidden != isHidden) {
                if(shouldBeHidden) {
                    player.addPotionEffect(new EffectInstance(EffectRegistry.getInstance().potionNinjaHidden, Integer.MAX_VALUE, 0, false, true));
                    new MessageInvisibility(player, true).sendToAll();
                } else {
                    player.removePotionEffect(EffectRegistry.getInstance().potionNinjaHidden);
                    this.revealEntity(player, NinjaGear.instance.getConfig().getHidingCooldown());
                    new MessageInvisibility(player, false).sendToAll();
                }
            }
        }
    }
}
