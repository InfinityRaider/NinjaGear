package com.infinityraider.ninjagear.block;

import com.infinityraider.ninjagear.api.v1.IRopeAttachable;
import com.infinityraider.ninjagear.item.ItemRope;
import com.infinityraider.ninjagear.reference.Constants;
import com.infinityraider.infinitylib.block.BlockBase;
import com.infinityraider.ninjagear.reference.Names;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
public class BlockRope extends BlockBase implements IWaterLoggable, IRopeAttachable {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final VoxelShape shape;

    public BlockRope() {
        super(Names.Items.ROPE, Properties.create(Material.WOOL));
        float u = Constants.UNIT;
        this.shape = Block.makeCuboidShape(7.5, 0, 7.5, 8.5, 1, 8.5);
        this.setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, Boolean.FALSE));
    }

    public Item asItem() {
        return ItemRegistry.getInstance().itemRope;
    }

    @Override
    @Deprecated
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if(!this.canRopeStay(world, pos)) {
            this.breakRope(world, pos, state, false);
        }
    }

    public boolean canRopeStay(IWorldReader world, BlockPos pos) {
        BlockPos up = pos.up();
        BlockState state = world.getBlockState(up);
        if(state.isSolidSide(world, up, Direction.DOWN)) {
            return true;
        }
        FluidState fluid = world.getFluidState(pos);
        if(fluid.getFluid() != Fluids.WATER) {
            return false;
        }
        if(state.getBlock() instanceof IRopeAttachable) {
            IRopeAttachable attachable = (IRopeAttachable) state.getBlock();
            return attachable.canAttachRope(world, up, state);
        }
        return false;
    }

    @Override
    @Deprecated
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        return this.canRopeStay(world, pos);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = this.getDefaultState();
        IWorldReader world = context.getWorld();
        BlockPos pos = context.getPos();
        if (state.isValidPosition(world, pos)) {
            FluidState fluid = context.getWorld().getFluidState(context.getPos());
            return state.with(WATERLOGGED, fluid.getFluid() == Fluids.WATER);
        }
        return null;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        BlockPos up = pos.up();
        BlockState stateUp = world.getBlockState(up);
        if(stateUp.getBlock() instanceof IRopeAttachable) {
            ((IRopeAttachable) stateUp.getBlock()).onRopeAttached(world, up, stateUp);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (!world.isRemote && heldItem.getItem() instanceof ItemRope) {
            if (this.extendRope(world, pos) && !player.abilities.isCreativeMode) {
                player.inventory.decrStackSize(player.inventory.currentItem, 1);
            }
        }
        return ActionResultType.CONSUME;
    }

    public boolean extendRope(World world, BlockPos pos) {
        BlockPos below = pos.down();
        BlockState state = world.getBlockState(below);
        if(state.getBlock() instanceof BlockRope) {
            return ((BlockRope) state.getBlock()).extendRope(world, below);
        }
        BlockState rope = this.getDefaultState();
        if(rope.isValidPosition(world, below)) {
            world.setBlockState(below, rope, 3);
            return true;
        }
        return false;
    }

    @Override
    public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        this.breakRope(world, pos, state, player.isSneaking());
    }

    public void breakRope(World world, BlockPos pos, BlockState state, boolean propagateUp) {
        if (propagateUp) {
            this.propagateRopeBreak(world, pos, true);
        } else {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            spawnDrops(state, world, pos);
        }
    }

    private void propagateRopeBreak(World world, BlockPos pos, boolean up) {
        if(!world.isRemote) {
            BlockPos posAt = pos.add(0, up ? 1 : -1, 0);
            BlockState state = world.getBlockState(posAt);
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            spawnDrops(state, world, pos);
            if (state.getBlock() instanceof BlockRope) {
                ((BlockRope) state.getBlock()).propagateRopeBreak(world, posAt, up);
            }
        }
    }

    @Deprecated
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return this.shape;
    }

    @Deprecated
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return this.shape;
    }

    @Deprecated
    public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return this.shape;
    }

    @Override
    public boolean canSpawnInBlock() {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
        return false;
    }

    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity) {
        return true;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean canAttachRope(IWorldReader world, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void onRopeAttached(World world, BlockPos pos, BlockState state) {}
}
