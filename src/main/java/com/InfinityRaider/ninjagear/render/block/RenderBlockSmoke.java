package com.InfinityRaider.ninjagear.render.block;

import com.InfinityRaider.ninjagear.block.BlockSmoke;
import com.InfinityRaider.ninjagear.render.RenderUtilBase;
import com.InfinityRaider.ninjagear.render.tessellation.ITessellator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
public class RenderBlockSmoke extends RenderUtilBase implements IBlockRenderingHandler {
    private final BlockSmoke smoke;

    public RenderBlockSmoke(BlockSmoke smoke) {
        this.smoke = smoke;
    }

    @Override
    public Block getBlock() {
        return smoke;
    }

    @Override
    public TileEntity getTileEntity() {
        return null;
    }

    @Override
    public List<ResourceLocation> getAllTextures() {
        List<ResourceLocation> textures = new ArrayList<>();
        for(EnumFacing facing : EnumFacing.values()) {
            ResourceLocation texture = smoke.getTexture(facing);
            if(texture != null && !textures.contains(texture)) {
                textures.add(texture);
            }
        }
        return textures;
    }

    @Override
    public void renderWorldBlock(ITessellator tessellator, World world, BlockPos pos, double x, double y, double z, IBlockState state, Block block, @Nullable TileEntity tile, boolean dynamicRender, float partialTick, int destroyStage) {}

    @Override
    public void renderInventoryBlock(ITessellator tessellator, World world, IBlockState state, Block block, @Nullable TileEntity tile, ItemStack stack, EntityLivingBase entity, ItemCameraTransforms.TransformType type) {}

    @Override
    public TextureAtlasSprite getIcon() {
        return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
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
