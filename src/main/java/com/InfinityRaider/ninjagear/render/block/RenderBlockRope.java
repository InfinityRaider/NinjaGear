package com.infinityraider.ninjagear.render.block;

import com.google.common.collect.ImmutableList;
import com.infinityraider.infinitylib.render.block.IBlockRenderingHandler;
import com.infinityraider.infinitylib.render.block.RenderBlockBase;
import com.infinityraider.infinitylib.render.tessellation.ITessellator;
import com.infinityraider.ninjagear.block.BlockRope;
import com.infinityraider.ninjagear.reference.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class RenderBlockRope extends RenderBlockBase<BlockRope> implements IBlockRenderingHandler<BlockRope> {
    private final ResourceLocation texture;

    public RenderBlockRope(BlockRope rope) {
        super(rope, true);
        this.texture = new ResourceLocation(Reference.MOD_ID, "blocks/" + this.getBlock().getInternalName());
    }

    @Override
    public List<ResourceLocation> getAllTextures() {
        return ImmutableList.of(this.texture);
    }

    @Override
    public void renderWorldBlock(ITessellator tessellator, World world, BlockPos pos, IBlockState state, BlockRope block) {
        this.addVertices(tessellator, this.getIcon());
    }

    @Override
    public void renderInventoryBlock(ITessellator tessellator, World world, IBlockState state, BlockRope block,
                                     ItemStack stack, EntityLivingBase entity, ItemCameraTransforms.TransformType type) {}

    private void addVertices(ITessellator tessellator, TextureAtlasSprite icon) {
        //face-11 front
        tessellator.setNormal(-1, 0, 1);
        tessellator.addScaledVertexWithUV(0, 0, 0, icon, 0, 16);
        tessellator.addScaledVertexWithUV(16, 0, 16, icon, 16, 16);
        tessellator.addScaledVertexWithUV(16, 16, 16, icon, 16, 0);
        tessellator.addScaledVertexWithUV(0, 16, 0, icon, 0, 0);

        //face-11 back
        tessellator.setNormal(-1, 0, 1);
        tessellator.addScaledVertexWithUV(0, 0, 0, icon, 0, 16);
        tessellator.addScaledVertexWithUV(0, 16, 0, icon, 0, 0);
        tessellator.addScaledVertexWithUV(16, 16, 16, icon, 16, 0);
        tessellator.addScaledVertexWithUV(16, 0, 16, icon, 16, 16);

        //face-10 front
        tessellator.setNormal(1, 0, 1);
        tessellator.addScaledVertexWithUV(0, 0, 16, icon, 0, 16);
        tessellator.addScaledVertexWithUV(16, 0, 0, icon, 16, 16);
        tessellator.addScaledVertexWithUV(16, 16, 0, icon, 16, 0);
        tessellator.addScaledVertexWithUV(0, 16, 16, icon, 0, 0);

        //face-10 back
        tessellator.setNormal(-1, 0, -1);
        tessellator.addScaledVertexWithUV(0, 0, 16, icon, 0, 16);
        tessellator.addScaledVertexWithUV(0, 16, 16, icon, 0, 0);
        tessellator.addScaledVertexWithUV(16, 16, 0, icon, 16, 0);
        tessellator.addScaledVertexWithUV(16, 0, 0, icon, 16, 16);
    }

    @Override
    public TextureAtlasSprite getIcon() {
        return getIcon(this.texture);
    }

    @Override
    public boolean applyAmbientOcclusion() {
        return false;
    }
}
