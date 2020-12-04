package com.infinityraider.ninjagear.item;

import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.registry.EffectRegistry;
import com.infinityraider.infinitylib.item.IInfinityItem;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemNinjaArmor extends ArmorItem implements IInfinityItem {
    private final String internalName;

    public ItemNinjaArmor(String name, EquipmentSlotType equipmentSlot) {
        super(MATERIAL_NINJA_CLOTH, equipmentSlot, new Properties().group(ItemRegistry.CREATIVE_TAB));
        this.internalName = name;
    }

    public String getInternalName() {
        return internalName;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        int layer = slot == EquipmentSlotType.LEGS ? 2 : 1;
        return Reference.MOD_ID +":textures/models/armor/ninja_gear_layer_" + layer +".png";
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
        if (!(entity instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity) entity;
        if (slot != 36) {
            return;
        }
        if (!world.isRemote) {
            if (!player.isPotionActive(EffectRegistry.getInstance().potionNinjaAura)) {
                ItemStack helmet = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
                if (!(helmet.getItem() instanceof ItemNinjaArmor)) {
                    return;
                }
                ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
                if (!(chest.getItem() instanceof ItemNinjaArmor)) {
                    return;
                }
                ItemStack leggings = player.getItemStackFromSlot(EquipmentSlotType.LEGS);
                if (!(leggings.getItem() instanceof ItemNinjaArmor)) {
                    return;
                }
                ItemStack boots = player.getItemStackFromSlot(EquipmentSlotType.FEET);
                if (!(boots.getItem() instanceof ItemNinjaArmor)) {
                    return;
                }
                EffectInstance effect = new EffectInstance(EffectRegistry.getInstance().potionNinjaAura, Integer.MAX_VALUE, 0, false, false);
                player.addPotionEffect(effect);
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L1"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:ninjaGear_L1"));
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static final int[] ARMOR_VALUES = new int[]{2, 3, 4, 2};
    public static final Ingredient REPAIR_MATERIAL = Ingredient.fromTag(ItemTags.WOOL);
    public static final IArmorMaterial MATERIAL_NINJA_CLOTH = new IArmorMaterial() {
        @Override
        public int getDurability(EquipmentSlotType slot) {
            return ArmorMaterial.LEATHER.getDurability(slot);
        }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType slot) {
            return ARMOR_VALUES[slot.getIndex()];
        }

        @Override
        public int getEnchantability() {
            return 12;
        }

        @Override
        public SoundEvent getSoundEvent() {
            return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairMaterial() {
            return REPAIR_MATERIAL;
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public String getName() {
            return "ninja_cloth";
        }

        @Override
        public float getToughness() {
            return 1.0F;
        }

        @Override
        public float getKnockbackResistance() {
            return 0.1F;
        }
    };
}
