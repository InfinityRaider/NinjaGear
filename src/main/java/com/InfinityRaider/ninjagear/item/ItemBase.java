package com.infinityraider.ninjagear.item;

import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.infinityraider.ninjagear.utility.RegisterHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemBase extends Item implements IItemWithModel {
    private final String internalName;

    public ItemBase(String name) {
        super();
        this.internalName = name;
        this.setCreativeTab(ItemRegistry.getInstance().creativeTab());
        RegisterHelper.registerItem(this, name);
        ItemRegistry.getInstance().items.add(this);
    }

    public String getInternalName() {
        return internalName;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        List<Tuple<Integer, ModelResourceLocation>> list = new ArrayList<>();
        list.add(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase()+ ":" + internalName, "inventory")));
        return list;
    }
}