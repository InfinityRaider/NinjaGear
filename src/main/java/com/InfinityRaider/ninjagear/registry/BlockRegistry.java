package com.infinityraider.ninjagear.registry;

import com.infinityraider.ninjagear.block.BlockRope;
import com.infinityraider.ninjagear.block.BlockSmoke;
import com.infinityraider.ninjagear.block.ICustomRenderedBlock;
import com.infinityraider.ninjagear.item.IItemWithRecipe;
import com.infinityraider.ninjagear.render.block.BlockRendererRegistry;
import com.infinityraider.ninjagear.utility.LogHelper;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class BlockRegistry {
    private static final BlockRegistry INSTANCE = new BlockRegistry();

    public static BlockRegistry getInstance() {
        return INSTANCE;
    }

    private BlockRegistry() {
        ninjaGearTab = ItemRegistry.getInstance().creativeTab();

        propertyAge = PropertyInteger.create("age", 0, 15);

        blocks = new ArrayList<>();
    }

    public final CreativeTabs ninjaGearTab;

    public final IProperty<Integer> propertyAge;

    public final List<Block> blocks;

    public Block blockSmoke;
    public Block blockRope;

    public void init() {
        this.blockSmoke = new BlockSmoke();
        this.blockRope = new BlockRope();

        LogHelper.debug("Registered blocks:");
        for(Block block : blocks()) {
            LogHelper.debug(" - "+block.getRegistryName());
        }
    }

    public void initRecipes() {
        blocks.stream().filter(block -> block instanceof IItemWithRecipe).forEach(block ->
                ((IItemWithRecipe) block).getRecipes().forEach(GameRegistry::addRecipe));
    }

    @SideOnly(Side.CLIENT)
    public void registerRenderers() {
        blocks.stream().filter(block -> block instanceof ICustomRenderedBlock).forEach(
                block -> BlockRendererRegistry.getInstance().registerCustomBlockRenderer((ICustomRenderedBlock<? extends TileEntity>) block));

        for (ICustomRenderedBlock block : BlockRendererRegistry.getInstance().getRegisteredBlocks()) {
            LogHelper.debug("Registered custom renderer for " + block.getBlockModelResourceLocation());
        }
    }

    public CreativeTabs creativeTab() {
        return ninjaGearTab;
    }

    public List<Block> blocks() {
        return ImmutableList.copyOf(blocks);
    }
}
