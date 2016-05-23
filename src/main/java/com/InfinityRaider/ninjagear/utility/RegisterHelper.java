package com.InfinityRaider.ninjagear.utility;

import com.InfinityRaider.ninjagear.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class RegisterHelper {
    public static void registerBlock(Block block, String name) {
        RegisterHelper.registerBlock(block, name, null);
    }

    public static void registerBlock(Block block, String name, Class<? extends ItemBlock> itemClass) {
        block.setUnlocalizedName(Reference.MOD_ID.toLowerCase() + ':' + name);
        if (itemClass != null) {
            GameRegistry.registerBlock(block, itemClass, name);
        } else {
            GameRegistry.registerBlock(block, name);
        }
    }


    public static void registerItem(Item item,String name) {
        item.setUnlocalizedName(Reference.MOD_ID.toLowerCase()+':'+name);
        GameRegistry.registerItem(item, name);
    }
}
