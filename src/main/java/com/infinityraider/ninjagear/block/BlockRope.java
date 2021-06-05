package com.infinityraider.ninjagear.block;

import com.infinityraider.infinitylib.block.property.InfPropertyConfiguration;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.api.v1.IRopeAttachable;
import com.infinityraider.ninjagear.item.ItemRope;
import com.infinityraider.infinitylib.block.BlockBase;
import com.infinityraider.ninjagear.item.ItemRopeCoil;
import com.infinityraider.ninjagear.reference.Names;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
public class BlockRope extends BlockBase implements IWaterLoggable, IRopeAttachable {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final InfPropertyConfiguration PROPERTIES = InfPropertyConfiguration.builder().waterloggable().build();

    private final VoxelShape shape;

    public BlockRope() {
        super(Names.Items.ROPE, Properties.create(Material.WOOL));
        this.shape = Block.makeCuboidShape(7.5, 0, 7.5, 8.5, 16, 8.5);
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
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if(!this.canRopeStay(world, pos)) {
            this.breakRope(world, pos, state, false, true);
        }
    }

    public boolean canRopeStay(IWorldReader world, BlockPos pos) {
        BlockPos up = pos.up();
        BlockState state = world.getBlockState(up);
        if(state.isSolidSide(world, up, Direction.DOWN)) {
            return true;
        }
        FluidState fluid = world.getFluidState(pos);
        if(fluid.getFluid() != Fluids.WATER && fluid.getFluid() != Fluids.EMPTY) {
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
        BlockState current = world.getBlockState(pos);
        return current.getMaterial().isReplaceable() && this.canRopeStay(world, pos);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getStateForPlacement(context.getWorld(), context.getPos());
    }

    public BlockState getStateForPlacement(IWorldReader world, BlockPos pos) {
        BlockState state = this.getDefaultState();
        if (state.isValidPosition(world, pos)) {
            FluidState fluid = world.getFluidState(pos);
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
        if(heldItem.isEmpty()) {
            return ActionResultType.PASS;
        }
        if (!world.isRemote) {
            if (heldItem.getItem() instanceof ItemRope) {
                if (this.extendRope(world, pos) && !player.isCreative()) {
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                }
                return ActionResultType.CONSUME;
            } else if(heldItem.getItem() instanceof ItemRopeCoil) {
                int remaining = this.extendRope(world, pos, NinjaGear.instance.getConfig().getRopeCoilLength());
                if(!player.isCreative()) {
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    if(remaining > 0) {
                        ItemStack stack = new ItemStack(ItemRegistry.getInstance().itemRope, remaining);
                        if (player.inventory.addItemStackToInventory(stack)) {
                            ItemEntity item = new ItemEntity(world, player.getPosX(), player.getPosY(), player.getPosZ(), stack);
                            world.addEntity(item);
                        }
                    }
                }
                return ActionResultType.CONSUME;
            }
        }
        return ActionResultType.PASS;
    }

    public int extendRope(World world, BlockPos pos, int count) {
        boolean flag = true;
        while(flag && count > 0) {
            flag = this.extendRope(world, pos);
            if(flag) {
                count = count - 1;
            }
        }
        return count;
    }

    public boolean extendRope(World world, BlockPos pos) {
        BlockPos below = pos.down();
        BlockState state = world.getBlockState(below);
        if(state.getBlock() instanceof BlockRope) {
            return this.extendRope(world, below);
        }
        BlockState rope = this.getStateForPlacement(world, below);
        if(rope == null) {
            return false;
        }
        if(rope.isValidPosition(world, below)) {
            world.setBlockState(below, rope, 3);
            return true;
        }
        return false;
    }

    @Override
    public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        this.breakRope(world, pos, state, player.isSneaking(), !player.isCreative());
    }

    public void breakRope(World world, BlockPos pos, BlockState state, boolean propagateUp, boolean doDrops) {
        if (propagateUp) {
            this.propagateRopeBreak(state, world, pos, true, doDrops);
        } else {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            if(doDrops) {
                spawnDrops(state, world, pos);
            }
        }
    }

    private void propagateRopeBreak(BlockState state, World world, BlockPos pos, boolean up, boolean doDrops) {
        if(!world.isRemote) {
            BlockPos posAt = pos.add(0, up ? 1 : -1, 0);
            BlockState stateAt = world.getBlockState(posAt);
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            if(doDrops) {
                spawnDrops(state, world, pos);
            }
            if (state.getBlock() instanceof BlockRope) {
                ((BlockRope) state.getBlock()).propagateRopeBreak(stateAt, world, posAt, up, doDrops);
            }
        }
    }

    @Override
    @Deprecated
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
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
    public boolean canAttachRope(IWorldReader world, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void onRopeAttached(World world, BlockPos pos, BlockState state) {}

    @Override
    @OnlyIn(Dist.CLIENT)
    public RenderType getRenderType() {
        return RenderType.getCutout();
    }
}
