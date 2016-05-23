package com.InfinityRaider.ninjagear.render.block;

import com.InfinityRaider.ninjagear.block.ICustomRenderedBlock;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BE GONE, JSON!
 */
@SideOnly(Side.CLIENT)
public class BlockRendererRegistry extends StateMapperBase implements ICustomModelLoader {
    private static final BlockRendererRegistry INSTANCE = new BlockRendererRegistry();

    public static BlockRendererRegistry getInstance() {
        return INSTANCE;
    }

    private final Map<ResourceLocation, BlockRenderer<? extends TileEntity>> renderers;
    private final List<ICustomRenderedBlock<? extends TileEntity>> blocks;

    private BlockRendererRegistry() {
        this.renderers = new HashMap<>();
        this.blocks = new ArrayList<>();
        ModelLoaderRegistry.registerLoader(this);
    }

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        Block block = state.getBlock();
        if(block instanceof ICustomRenderedBlock) {
            return ((ICustomRenderedBlock) block).getBlockModelResourceLocation();
        }
        return null;
    }

    @Override
    public boolean accepts(ResourceLocation loc) {
        return renderers.containsKey(loc);
    }

    @Override
    public IModel loadModel(ResourceLocation loc) throws Exception {
        return renderers.get(loc);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {}

    public List<ICustomRenderedBlock<? extends TileEntity>> getRegisteredBlocks() {
        return ImmutableList.copyOf(blocks);
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void registerCustomBlockRenderer(ICustomRenderedBlock<? extends TileEntity> customRenderedBlock) {
        if (customRenderedBlock == null || !(customRenderedBlock instanceof Block)) {
            return;
        }
        Block block = (Block) customRenderedBlock;
        IBlockRenderingHandler renderer = customRenderedBlock.getRenderer();
        if (renderer == null) {
            return;
        }
        //create block rendering instance
        BlockRenderer instance = new BlockRenderer<>(renderer);
        //get the resource location for the block which acts as a key for the map
        ModelResourceLocation blockModel = customRenderedBlock.getBlockModelResourceLocation();
        //if the renderer has static rendering, register it to the renderer map
        if (renderer.hasStaticRendering()) {
            ModelLoader.setCustomStateMapper(block, this);
            renderers.put(blockModel, instance);
        }
        //if the renderer has dynamic rendering also register it as a TESR
        TileEntity tile = renderer.getTileEntity();
        if (renderer.hasDynamicRendering() && tile != null) {
            ClientRegistry.bindTileEntitySpecialRenderer(tile.getClass(), instance);
        }
        //if inventory rendering should be handled by the renderer, register the inventory variant
        if (renderer.doInventoryRendering()) {
            ModelResourceLocation itemModel = new ModelResourceLocation(blockModel.getResourceDomain() + ":" + blockModel.getResourcePath(), "inventory");
            renderers.put(itemModel, instance);
            ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(block), stack -> itemModel);
        }
        blocks.add(customRenderedBlock);
    }
}
