package com.InfinityRaider.ninjagear.block;

import com.InfinityRaider.ninjagear.handler.ConfigurationHandler;
import com.InfinityRaider.ninjagear.registry.BlockRegistry;
import com.InfinityRaider.ninjagear.render.block.IBlockRenderingHandler;
import com.InfinityRaider.ninjagear.render.block.RenderBlockSmoke;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockSmoke extends BlockBase {

    public BlockSmoke() {
        super("smoke", Material.air);
        this.setCreativeTab(null);
        this.fullBlock = false;
        this.needsRandomTick = true;
        this.translucent = true;
    }

    @Override
    public int tickRate(World world) {
        return ConfigurationHandler.getInstance().smokeCloudDispersionFactor;
    }

    @Override
    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        int meta = this.getMetaFromState(state);
        if(meta < 15) {
            world.setBlockState(pos, this.getStateFromMeta(Math.min(meta + random.nextInt(3), 15)), 6);
        } else {
            world.setBlockToAir(pos);
        }
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {}

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 16 - this.getMetaFromState(state);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 0;
    }

    @Override
    protected IProperty[] getPropertyArray() {
        return new IProperty[] {BlockRegistry.getInstance().propertyAge};
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BlockRegistry.getInstance().propertyAge, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state == null || state.getPropertyNames().isEmpty()) {
            return 15;
        } else {
            return state.getValue(BlockRegistry.getInstance().propertyAge);
        }
    }

    @Override
    protected Class<? extends ItemBlock> getItemBlockClass() {
        return null;
    }

    @Override
    public AxisAlignedBB getDefaultBoundingBox() {
        return Block.NULL_AABB;
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
        return false;
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean canSpawnInBlock() {
        return false;
    }

    @Override
    public boolean isAir(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity entity) {}

    @Override
    protected RayTraceResult rayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox) {
        return null;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getTexture(EnumFacing side) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBlockRenderingHandler getRenderer() {
        return new RenderBlockSmoke(this);
    }
}
