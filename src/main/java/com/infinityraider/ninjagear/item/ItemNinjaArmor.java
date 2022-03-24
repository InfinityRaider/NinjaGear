package com.infinityraider.ninjagear.item;

import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.infinitylib.item.IInfinityItem;
import com.infinityraider.ninjagear.registry.ItemRegistry;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemNinjaArmor extends ArmorItem implements IInfinityItem {
    private static Ingredient repairMaterial;

    private final String internalName;

    public ItemNinjaArmor(String name, EquipmentSlot equipmentSlot) {
        super(MATERIAL_NINJA_CLOTH, equipmentSlot, new Properties().group(ItemRegistry.CREATIVE_TAB));
        this.internalName = name;
    }

    @Override
    @Nonnull
    public String getInternalName() {
        return internalName;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        int layer = slot == EquipmentSlotType.LEGS ? 2 : 1;
        return Reference.MOD_ID +":textures/models/armor/ninja_gear_layer_" + layer +".png";
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L1"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:ninja_gear_L1"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:ninja_gear_L2"));
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private static final int[] ARMOR_VALUES = new int[]{2, 3, 4, 2};

    public static Ingredient getRepairMaterial() {
        if(repairMaterial == null) {
            repairMaterial = Ingredient.fromTag(ItemTags.WOOL);
        }
        return repairMaterial;
    }

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
            if(repairMaterial == null) {
                repairMaterial = Ingredient.fromTag(ItemTags.WOOL);
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
