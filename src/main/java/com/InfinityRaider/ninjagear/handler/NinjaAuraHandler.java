package com.InfinityRaider.ninjagear.handler;

import com.InfinityRaider.ninjagear.api.v1.IHiddenItem;
import com.InfinityRaider.ninjagear.item.ItemNinjaArmor;
import com.InfinityRaider.ninjagear.network.MessageInvisibility;
import com.InfinityRaider.ninjagear.network.NetworkWrapper;
import com.InfinityRaider.ninjagear.registry.PotionRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NinjaAuraHandler {
    private static final NinjaAuraHandler INSTANCE = new NinjaAuraHandler();

    public static NinjaAuraHandler getInstance() {
        return INSTANCE;
    }

    private NinjaAuraHandler() {}

    public void revealEntity(EntityPlayer player, int duration) {
        player.addPotionEffect(new PotionEffect(new PotionEffect(PotionRegistry.getInstance().potionNinjaRevealed, duration, 0, false, true)));
    }

    private boolean shouldRemoveEffect(EntityPlayer player) {
        ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (helmet == null || !(helmet.getItem() instanceof ItemNinjaArmor)) {
            return true;
        }
        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (chest == null || !(chest.getItem() instanceof ItemNinjaArmor)) {
            return true;
        }
        ItemStack leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        if (leggings == null || !(leggings.getItem() instanceof ItemNinjaArmor)) {
            return true;
        }
        ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        return boots == null || !(boots.getItem() instanceof ItemNinjaArmor);
    }

    private boolean shouldBeHidden(EntityPlayer player) {
        if(!player.isPotionActive(PotionRegistry.getInstance().potionNinjaAura)) {
            return false;
        }
        if(player.isPotionActive(PotionRegistry.getInstance().potionNinjaRevealed)) {
            return false;
        }
        if(!player.isSneaking()) {
            return false;
        }
        ItemStack mainHand = player.getHeldItem(EnumHand.MAIN_HAND);
        ItemStack offHand = player.getHeldItem(EnumHand.OFF_HAND);
        if(mainHand != null && (!(mainHand.getItem() instanceof IHiddenItem) || ((IHiddenItem) mainHand.getItem()).shouldRevealPlayerWhenEquipped(player, mainHand))) {
            return false;
        }
        if(offHand != null && (!(offHand.getItem() instanceof IHiddenItem) || ((IHiddenItem) offHand.getItem()).shouldRevealPlayerWhenEquipped(player, offHand))) {
            return false;
        }
        int light;
        int light_block = player.getEntityWorld().getLightFor(EnumSkyBlock.BLOCK, player.getPosition());
        boolean day = player.getEntityWorld().isDaytime();
        if(day) {
            int light_sky = player.getEntityWorld().getLightFor(EnumSkyBlock.SKY, player.getPosition());
            light = Math.max(light_sky, light_block);
        } else {
            light = light_block;
        }
        return light < ConfigurationHandler.getInstance().brightnessLimit;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if(entity == null || entity.getEntityWorld().isRemote || !(entity instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) entity;
        if(this.shouldRemoveEffect(player)) {
            player.removePotionEffect(PotionRegistry.getInstance().potionNinjaAura);
            if(player.isPotionActive(PotionRegistry.getInstance().potionNinjaHidden)) {
                player.removePotionEffect(PotionRegistry.getInstance().potionNinjaHidden);
            }
            if(player.isPotionActive(PotionRegistry.getInstance().potionNinjaRevealed)) {
                player.removePotionEffect(PotionRegistry.getInstance().potionNinjaRevealed);
            }
        } else {
            boolean shouldBeHidden = this.shouldBeHidden(player);
            boolean isHidden = player.isPotionActive(PotionRegistry.getInstance().potionNinjaHidden);
            if(shouldBeHidden != isHidden) {
                if(shouldBeHidden) {
                    player.addPotionEffect(new PotionEffect(PotionRegistry.getInstance().potionNinjaHidden, Integer.MAX_VALUE, 0, false, true));
                    NetworkWrapper.getInstance().sendToAll(new MessageInvisibility(player, true));
                } else {
                    player.removePotionEffect(PotionRegistry.getInstance().potionNinjaHidden);
                    this.revealEntity(player, ConfigurationHandler.getInstance().hidingCoolDown);
                    NetworkWrapper.getInstance().sendToAll(new MessageInvisibility(player, false));
                }
            }
        }
    }
}
