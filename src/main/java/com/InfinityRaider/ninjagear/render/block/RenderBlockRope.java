package com.InfinityRaider.ninjagear.render.block;

import com.InfinityRaider.ninjagear.block.BlockRope;
import com.InfinityRaider.ninjagear.render.RenderUtilBase;
import com.InfinityRaider.ninjagear.render.tessellation.ITessellator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class RenderBlockRope extends RenderUtilBase implements IBlockRenderingHandler {
    private final BlockRope rope;

    public RenderBlockRope(BlockRope rope) {
        this.rope = rope;
    }

    @Override
    public Block getBlock() {
        return rope;
    }

    @Override
    public TileEntity getTileEntity() {
        return null;
    }

    @Override
    public List<ResourceLocation> getAllTextures() {
        List<ResourceLocation> textures = new ArrayList<>();
        for(EnumFacing facing : EnumFacing.values()) {
            ResourceLocation texture = rope.getTexture(facing);
            if(texture != null && !textures.contains(texture)) {
                textures.add(texture);
            }
        }
        return textures;
    }

    @Override
    public void renderWorldBlock(ITessellator tessellator, World world, BlockPos pos, double x, double y, double z, IBlockState state, Block block, @Nullable TileEntity tile, boolean dynamicRender, float partialTick, int destroyStage) {
        this.addVertices(tessellator, this.getIcon());
    }

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
    public void renderInventoryBlock(ITessellator tessellator, World world, IBlockState state, Block block, @Nullable TileEntity tile, ItemStack stack, EntityLivingBase entity, ItemCameraTransforms.TransformType type) {

    }

    @Override
    public TextureAtlasSprite getIcon() {
        return this.getIcon(rope.getTexture(EnumFacing.UP));
    }

    @Override
    public boolean doInventoryRendering() {
        return true;
    }

    @Override
    public boolean hasDynamicRendering() {
        return false;
    }

    @Override
    public boolean hasStaticRendering() {
        return true;
    }
}
