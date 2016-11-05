package com.infinityraider.ninjagear.render.block;

import com.infinityraider.infinitylib.render.block.IBlockRenderingHandler;
import com.infinityraider.infinitylib.render.block.RenderBlockBase;
import com.infinityraider.infinitylib.render.tessellation.ITessellator;
import com.infinityraider.ninjagear.block.BlockSmoke;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class RenderBlockSmoke extends RenderBlockBase<BlockSmoke> implements IBlockRenderingHandler<BlockSmoke> {
    public RenderBlockSmoke(BlockSmoke smoke) {
        super(smoke, true);
    }

    @Override
    public List<ResourceLocation> getAllTextures() {
        return Collections.emptyList();
    }

    @Override
    public void renderWorldBlock(ITessellator tessellator, World world, BlockPos pos, IBlockState state, BlockSmoke block) {}

    @Override
    public void renderInventoryBlock(ITessellator tessellator, World world, IBlockState state, BlockSmoke block,
                                     ItemStack stack, EntityLivingBase entity, ItemCameraTransforms.TransformType type) {}

    @Override
    public TextureAtlasSprite getIcon() {
        return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
    }

    @Override
    public boolean applyAmbientOcclusion() {
        return false;
    }
}
