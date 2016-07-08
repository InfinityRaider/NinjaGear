package com.infinityraider.ninjagear.block;

import com.infinityraider.ninjagear.block.blockstate.BlockStateSpecial;
import com.infinityraider.ninjagear.block.blockstate.IBlockStateSpecial;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.registry.BlockRegistry;
import com.infinityraider.ninjagear.utility.RegisterHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public abstract class BlockBase<T extends TileEntity> extends Block implements ICustomRenderedBlock<T> {
    private final String internalName;

    public BlockBase(String name, Material blockMaterial) {
        super(blockMaterial);
        this.internalName = name;
        this.setCreativeTab(BlockRegistry.getInstance().creativeTab());
        RegisterHelper.registerBlock(this, this.getInternalName(), this.getItemBlockClass());
        BlockRegistry.getInstance().blocks.add(this);
    }

    public String getInternalName() {
        return this.internalName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public IBlockStateSpecial<T, ? extends IBlockState> getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return new BlockStateSpecial<>(state, pos, (T) world.getTileEntity(pos));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getBlockModelResourceLocation() {
        return new ModelResourceLocation(Reference.MOD_ID.toLowerCase()+":"+getInternalName());
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    protected final BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, getPropertyArray());
    }

    /**
     * @return a property array containing all properties for this block's state
     */
    protected abstract IProperty[] getPropertyArray();

    /**
     * Retrieves the block's ItemBlock class, as a generic class bounded by the
     * ItemBlock class.
     *
     * @return the block's class, may be null if no specific ItemBlock class is
     * desired.
     */
    @Nullable
    protected abstract Class<? extends ItemBlock> getItemBlockClass();

    /**
     * @return The default bounding box for this block
     */
    public abstract AxisAlignedBB getDefaultBoundingBox();

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return getDefaultBoundingBox();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return getDefaultBoundingBox();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @SideOnly(Side.CLIENT)
    public  abstract ResourceLocation getTexture(EnumFacing side);

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
