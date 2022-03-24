package com.infinityraider.ninjagear.registry;

import com.infinityraider.infinitylib.block.IInfinityBlock;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.item.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class ItemRegistry {
    public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(Reference.MOD_ID.toLowerCase() + ".creative_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemRegistry.itemShuriken);
        }
    };

    public static final Item itemNinjaArmorHead = new ItemNinjaArmor("ninja_gear_head", EquipmentSlot.HEAD);
    public static final Item itemNinjaArmorChest = new ItemNinjaArmor("ninja_gear_chest", EquipmentSlot.CHEST);
    public static final Item itemNinjaArmorLegs = new ItemNinjaArmor("ninja_gear_legs", EquipmentSlot.LEGS);
    public static final Item itemNinjaArmorFeet = new ItemNinjaArmor("ninja_gear_feet", EquipmentSlot.FEET);
    public static final Item itemKatana = new ItemKatana();
    public static final Item itemSai = new ItemSai();
    public static final Item itemShuriken = new ItemShuriken();
    public static final Item itemSmokeBomb = new ItemSmokeBomb();
    public static final Item itemRopeCoil = new ItemRopeCoil();
    public static final Item itemRope = new ItemRope((IInfinityBlock) BlockRegistry.blockRope);
}
