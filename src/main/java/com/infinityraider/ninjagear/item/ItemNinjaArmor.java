package com.infinityraider.ninjagear.item;

import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.infinitylib.item.IInfinityItem;
import com.infinityraider.ninjagear.registry.ItemRegistry;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemNinjaArmor extends ArmorItem implements IInfinityItem {
    private static Ingredient repairMaterial;

    private final String internalName;

    public ItemNinjaArmor(String name, EquipmentSlot equipmentSlot) {
        super(MATERIAL_NINJA_CLOTH, equipmentSlot, new Properties().tab(ItemRegistry.CREATIVE_TAB));
        this.internalName = name;
    }

    @Override
    @Nonnull
    public String getInternalName() {
        return internalName;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        int layer = slot == EquipmentSlot.LEGS ? 2 : 1;
        return Reference.MOD_ID +":textures/models/armor/ninja_gear_layer_" + layer +".png";
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L1"));
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:ninja_gear_L1"));
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:ninja_gear_L2"));
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private static final int[] ARMOR_VALUES = new int[]{2, 3, 4, 2};

    public static Ingredient getRepairMaterial() {
        if(repairMaterial == null) {
            repairMaterial = Ingredient.of(ItemTags.WOOL);
        }
        return repairMaterial;
    }

    public static final ArmorMaterial MATERIAL_NINJA_CLOTH = new ArmorMaterial() {
        @Override
        public int getDurabilityForSlot(EquipmentSlot slot) {
            return ArmorMaterials.LEATHER.getDurabilityForSlot(slot);
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot slot) {
            return ARMOR_VALUES[slot.getIndex()];
        }

        @Override
        public int getEnchantmentValue() {
            return 12;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairIngredient() {
            if(repairMaterial == null) {
                repairMaterial = Ingredient.of(ItemTags.WOOL);
            }
            return repairMaterial;
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
