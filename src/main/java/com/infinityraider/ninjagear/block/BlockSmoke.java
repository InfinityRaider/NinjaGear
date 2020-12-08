package com.infinityraider.ninjagear.block;

import com.infinityraider.infinitylib.block.BlockBase;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.registry.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockSmoke extends BlockBase {
    public static final int MAX_AGE = 15;
    public static final Property<Integer> PROPERTY_AGE = IntegerProperty.create("age", 0, MAX_AGE);

    public static BlockState getBlockStateForDarkness(int darkness) {
        BlockSmoke block = (BlockSmoke) BlockRegistry.getInstance().blockSmoke;
        return block.getDefaultState().with(PROPERTY_AGE, darkness);
    }

    public BlockSmoke() {
        super("smoke", Properties.create(Material.AIR)
                .tickRandomly()
                .doesNotBlockMovement().setAir().noDrops().notSolid().variableOpacity()
                .setAllowsSpawn((a1, a2, a3, a4) -> false)
                .setBlocksVision((a1, a2, a3) -> true));
        this.setDefaultState(this.stateContainer.getBaseState().with(PROPERTY_AGE, MAX_AGE));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(PROPERTY_AGE);
    }

    public int dispersionRate() {
        return NinjaGear.instance.getConfig().getSmokeDispersion() - 1;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(PROPERTY_AGE) + random.nextInt(this.dispersionRate()) + 1;
        if (age <= MAX_AGE) {
            world.setBlockState(pos, this.getDefaultState().with(PROPERTY_AGE, age), 6);
        } else {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }


    @Override
    public boolean isTransparent(BlockState state) {
        return true;
    }

    @Deprecated
    public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
        return true;
    }

    @Deprecated
    public boolean isReplaceable(BlockState state, Fluid fluid) {
        return true;
    }

    @Override
    @Deprecated
    public int getOpacity(BlockState state, IBlockReader world, BlockPos pos) {
        return MAX_AGE - state.get(PROPERTY_AGE) + 1;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return 0;
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
    @Deprecated
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    @Deprecated
    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    @Deprecated
    public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }
}
