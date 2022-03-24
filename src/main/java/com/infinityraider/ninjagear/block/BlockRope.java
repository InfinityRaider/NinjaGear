package com.infinityraider.ninjagear.block;

import com.infinityraider.infinitylib.block.property.InfPropertyConfiguration;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.api.v1.IRopeAttachable;
import com.infinityraider.ninjagear.item.ItemRope;
import com.infinityraider.infinitylib.block.BlockBase;
import com.infinityraider.ninjagear.item.ItemRopeCoil;
import com.infinityraider.ninjagear.reference.Names;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockRope extends BlockBase implements SimpleWaterloggedBlock, IRopeAttachable {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final InfPropertyConfiguration PROPERTIES = InfPropertyConfiguration.builder().waterloggable().build();

    private final VoxelShape shape;

    public BlockRope() {
        super(Names.Items.ROPE, Properties.of(Material.WOOL));
        this.shape = Block.box(7.5, 0, 7.5, 8.5, 16, 8.5);
    }

    public Item asItem() {
        return ItemRegistry.getInstance().itemRope;
    }

    @Override
    protected InfPropertyConfiguration getPropertyConfiguration() {
        return PROPERTIES;
    }

    @Override
    @Deprecated
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if(!this.canRopeStay(world, pos)) {
            this.breakRope(world, pos, state, false, true);
        }
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);
    }

    public boolean canRopeStay(LevelReader world, BlockPos pos) {
        BlockPos up = pos.above();
        BlockState state = world.getBlockState(up);
        if(state.isFaceSturdy(world, up, Direction.DOWN)) {
            return true;
        }
        FluidState fluid = world.getFluidState(pos);
        if(fluid.getType() != Fluids.WATER && fluid.getType() != Fluids.EMPTY) {
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
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockState current = world.getBlockState(pos);
        return current.getMaterial().isReplaceable() && this.canRopeStay(world, pos);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.getStateForPlacement(context.getLevel(), context.getClickedPos());
    }

    public BlockState getStateForPlacement(Level world, BlockPos pos) {
        BlockState state = this.defaultBlockState();
        if (state.canSurvive(world, pos)) {
            FluidState fluid = world.getFluidState(pos);
            return state.setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        BlockPos up = pos.above();
        BlockState stateUp = world.getBlockState(up);
        if(stateUp.getBlock() instanceof IRopeAttachable) {
            ((IRopeAttachable) stateUp.getBlock()).onRopeAttached(world, up, stateUp);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        if(heldItem.isEmpty()) {
            return InteractionResult.PASS;
        }
        if (!world.isClientSide()) {
            if (heldItem.getItem() instanceof ItemRope) {
                if (this.extendRope(world, pos) && !player.isCreative()) {
                    player.getInventory().removeItem(player.getInventory().selected, 1);
                }
                return InteractionResult.CONSUME;
            } else if(heldItem.getItem() instanceof ItemRopeCoil) {
                int remaining = this.extendRope(world, pos, NinjaGear.instance.getConfig().getRopeCoilLength());
                if(!player.isCreative()) {
                    player.getInventory().removeItem(player.getInventory().selected, 1);
                    if(remaining > 0) {
                        ItemStack stack = new ItemStack(ItemRegistry.getInstance().itemRope, remaining);
                        if (player.getInventory().add(stack)) {
                            ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), stack);
                            world.addFreshEntity(item);
                        }
                    }
                }
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    public int extendRope(Level world, BlockPos pos, int count) {
        boolean flag = true;
        while(flag && count > 0) {
            flag = this.extendRope(world, pos);
            if(flag) {
                count = count - 1;
            }
        }
        return count;
    }

    public boolean extendRope(Level world, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState state = world.getBlockState(below);
        if(state.getBlock() instanceof BlockRope) {
            return this.extendRope(world, below);
        }
        BlockState rope = this.getStateForPlacement(world, below);
        if(rope == null) {
            return false;
        }
        if(rope.canSurvive(world, below)) {
            world.setBlock(below, rope, 3);
            return true;
        }
        return false;
    }

    @Override
    public void attack(BlockState state, Level world, BlockPos pos, Player player) {
        this.breakRope(world, pos, state, player.isDiscrete(), !player.isCreative());
    }

    public void breakRope(Level world, BlockPos pos, BlockState state, boolean propagateUp, boolean doDrops) {
        if (propagateUp) {
            this.propagateRopeBreak(state, world, pos, true, doDrops);
        } else {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            if(doDrops) {
                Block.dropResources(state, world, pos);
            }
        }
    }

    private void propagateRopeBreak(BlockState state, Level world, BlockPos pos, boolean up, boolean doDrops) {
        if(!world.isClientSide()) {
            BlockPos posAt = pos.offset(0, up ? 1 : -1, 0);
            BlockState stateAt = world.getBlockState(posAt);
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            if(doDrops) {
                Block.dropResources(state, world, pos);
            }
            if (state.getBlock() instanceof BlockRope) {
                ((BlockRope) state.getBlock()).propagateRopeBreak(stateAt, world, posAt, up, doDrops);
            }
        }
    }

    @Override
    @Deprecated
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return this.shape;
    }

    @Override
    public boolean isPossibleToRespawnInThis() {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return false;
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader world, BlockPos pos, LivingEntity entity) {
        return true;
    }

    @Override
    public boolean canAttachRope(LevelReader world, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void onRopeAttached(Level world, BlockPos pos, BlockState state) {}

    @Override
    @OnlyIn(Dist.CLIENT)
    public RenderType getRenderType() {
        return RenderType.cutout();
    }
}
