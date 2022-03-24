package com.infinityraider.ninjagear.registry;

import com.infinityraider.infinitylib.block.IInfinityBlock;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.item.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemRegistry {
    public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(Reference.MOD_ID.toLowerCase() + ".creative_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemRegistry.getInstance().itemShuriken);
        }
    };

    private static final ItemRegistry INSTANCE = new ItemRegistry();

    public static ItemRegistry getInstance() {
        return INSTANCE;
    }

    private ItemRegistry() {
        itemNinjaArmorHead = new ItemNinjaArmor("ninja_gear_head", EquipmentSlot.HEAD);
        itemNinjaArmorChest = new ItemNinjaArmor("ninja_gear_chest", EquipmentSlot.CHEST);
        itemNinjaArmorLegs = new ItemNinjaArmor("ninja_gear_legs", EquipmentSlot.LEGS);
        itemNinjaArmorFeet = new ItemNinjaArmor("ninja_gear_feet", EquipmentSlot.FEET);
        itemKatana = new ItemKatana();
        itemSai = new ItemSai();
        itemShuriken = new ItemShuriken();
        itemSmokeBomb = new ItemSmokeBomb();
        itemRopeCoil = new ItemRopeCoil();
        itemRope = new ItemRope((IInfinityBlock) BlockRegistry.getInstance().blockRope);
    }

    public Item itemNinjaArmorHead;
    public Item itemNinjaArmorChest;
    public Item itemNinjaArmorLegs;
    public Item itemNinjaArmorFeet;
    public Item itemKatana;
    public Item itemSai;
    public Item itemShuriken;
    public Item itemSmokeBomb;
    public Item itemRopeCoil;
    public Item itemRope;
}
