package com.InfinityRaider.ninjagear.registry;

import com.InfinityRaider.ninjagear.item.*;
import com.InfinityRaider.ninjagear.reference.Reference;
import com.InfinityRaider.ninjagear.render.item.ItemRendererRegistry;
import com.InfinityRaider.ninjagear.utility.LogHelper;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ItemRegistry {
    private static final ItemRegistry INSTANCE = new ItemRegistry();

    public static ItemRegistry getInstance() {
        return INSTANCE;
    }

    private ItemRegistry() {
        creativeTab = new CreativeTabs(Reference.MOD_ID.toLowerCase()+".creativeTab") {
            @Override
            public Item getTabIconItem() {
                return ItemRegistry.getInstance().itemShuriken;
            }
        };
        items = new ArrayList<>();
    }

    public final CreativeTabs creativeTab;

    public final List<Item> items;

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

    public void init() {
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

        LogHelper.debug("Registered items:");
        for(Item item : items()) {
            LogHelper.debug(" - " + item.getRegistryName());
        }
    }

    public void initRecipes() {
        items.stream().filter(item -> item instanceof IItemWithRecipe).forEach(item ->
                ((IItemWithRecipe) item).getRecipes().forEach(GameRegistry::addRecipe));
    }

    @SideOnly(Side.CLIENT)
    public void registerRenderers() {
        for(Item item : items) {
            if(item instanceof IItemWithModel) {
                for (Tuple<Integer, ModelResourceLocation> entry : ((IItemWithModel) item).getModelDefinitions()) {
                   ModelLoader.setCustomModelResourceLocation(item, entry.getFirst(), entry.getSecond());
                }
            }
            if(item instanceof ICustomRenderedItem) {
                ItemRendererRegistry.getInstance().registerCustomItemRenderer((ICustomRenderedItem<? extends Item>) item);
            }
        }

        for (ICustomRenderedItem item : ItemRendererRegistry.getInstance().getRegisteredItems()) {
            LogHelper.debug("Registered custom renderer for " + item.getItemModelResourceLocation());
        }
    }

    public CreativeTabs creativeTab() {
        return creativeTab;
    }

    public List<Item> items() {
        return ImmutableList.copyOf(items);
    }
}
