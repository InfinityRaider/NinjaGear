package com.infinityraider.ninjagear.block;

import com.infinityraider.infinitylib.block.BlockBase;
import com.infinityraider.infinitylib.render.block.IBlockRenderingHandler;
import com.infinityraider.infinitylib.render.block.RenderBlockEmpty;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.registry.BlockRegistry;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlockSmoke extends BlockBase {
    public static final int MAX_AGE = 15;
    public static final Property<Integer> PROPERTY_AGE = IntegerProperty.create("age", 0, MAX_AGE);

    public static BlockState getBlockStateForDarkness(int darkness) {
        BlockSmoke block = (BlockSmoke) BlockRegistry.getInstance().blockSmoke;
        return block.getDefaultState().with(PROPERTY_AGE, darkness);
    }

    @OnlyIn(Dist.CLIENT)
    private IBlockRenderingHandler<BlockSmoke> renderer;

    public BlockSmoke() {
        super("smoke", Properties.create(Material.AIR));
    }

    @Override
    public int tickRate(World world) {
        return NinjaGear.instance.getConfig().getSmokeDispersion();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void randomTick(World world, BlockPos pos, BlockState state, Random random) {
        int age = state.get(PROPERTY_AGE);
        if (age < 15) {
            world.setBlockState(pos, this.getDefaultState().with(PROPERTY_AGE, age + 2 + random.nextInt(3)), 6);
        } else {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, BlockState state, float chance, int fortune) {
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isReplaceable(IBlockReader worldIn, BlockPos pos) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    public int getLightOpacity(BlockState state, IBlockReader world, BlockPos pos) {
        return MAX_AGE - state.get(PROPERTY_AGE) + 1;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return 0;
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    @Override
    public boolean canCollideCheck(BlockState state, boolean hitIfLiquid) {
        return false;
    }

    @Override
    public boolean doesSideBlockRendering(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isSideSolid(BlockState base_state, IBlockReader world, BlockPos pos, Direction side) {
        return false;
    }

    @Override
    public boolean isPassable(IBlockReader world, BlockPos pos) {
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
    public boolean isAir(BlockState state, IBlockReader world, BlockPos pos) {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, SpawnPlacementType type) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean flag) {}

    @Override
    protected RayTraceResult rayTrace(BlockPos pos, Vector3d start, Vector3d end, AxisAlignedBB boundingBox) {
        return null;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public IBlockRenderingHandler<BlockSmoke> getRenderer() {
        if(this.renderer == null) {
            this.renderer = RenderBlockEmpty.createEmptyRender(this);
        }
        return this.renderer;
    }
}
