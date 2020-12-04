package com.infinityraider.ninjagear.registry;

import com.infinityraider.infinitylib.block.IInfinityBlock;
import com.infinityraider.infinitylib.crafting.FallbackIngredient;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.item.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ItemRegistry {
    public static final ItemGroup CREATIVE_TAB = new ItemGroup(Reference.MOD_ID.toLowerCase() + ".creative_tab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ItemRegistry.getInstance().itemShuriken);
        }
    };

    public static final Ingredient REPAIR_ITEM_STEEL = new FallbackIngredient(
            ItemTags.getCollection().getTagByID(new ResourceLocation("forge", "ingots/steel")),
            Ingredient.fromTag(ItemTags.getCollection().getTagByID(new ResourceLocation("forge", "ingots/iron")))
    );

    private static final ItemRegistry INSTANCE = new ItemRegistry();

    public static ItemRegistry getInstance() {
        return INSTANCE;
    }

    private ItemRegistry() {
        itemNinjaArmorHead = new ItemNinjaArmor("ninja_gear_head", EquipmentSlotType.HEAD);
        itemNinjaArmorChest = new ItemNinjaArmor("ninja_gear_chest", EquipmentSlotType.CHEST);
        itemNinjaArmorLegs = new ItemNinjaArmor("ninja_gear_legs", EquipmentSlotType.LEGS);
        itemNinjaArmorFeet = new ItemNinjaArmor("ninja_gear_feet", EquipmentSlotType.FEET);
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
