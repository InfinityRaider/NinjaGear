package com.infinityraider.ninjagear.registry;

import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.item.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemRegistry {
    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(Reference.MOD_ID.toLowerCase()+".creative_tab") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ItemRegistry.getInstance().itemShuriken);
        }
    };

    private static final ItemRegistry INSTANCE = new ItemRegistry();

    public static ItemRegistry getInstance() {
        return INSTANCE;
    }

    private ItemRegistry() {
        itemNinjaArmorHead = new ItemNinjaArmor("ninjaGear_head", 0, EntityEquipmentSlot.HEAD);
        itemNinjaArmorChest = new ItemNinjaArmor("ninjaGear_chest", 0, EntityEquipmentSlot.CHEST);
        itemNinjaArmorLegs = new ItemNinjaArmor("ninjaGear_legs", 0, EntityEquipmentSlot.LEGS);
        itemNinjaArmorFeet = new ItemNinjaArmor("ninjaGear_feet", 0, EntityEquipmentSlot.FEET);
        itemKatana = new ItemKatana();
        itemSai = new ItemSai();
        itemShuriken = new ItemShuriken();
        itemSmokeBomb = new ItemSmokeBomb();
        itemRopeCoil = new ItemRopeCoil();
        itemRope = new ItemRope();
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
