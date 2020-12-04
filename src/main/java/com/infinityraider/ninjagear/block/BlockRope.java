package com.infinityraider.ninjagear.block;

import com.infinityraider.ninjagear.api.v1.IRopeAttachable;
import com.infinityraider.ninjagear.item.ItemRope;
import com.infinityraider.ninjagear.reference.Constants;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.infinitylib.block.BlockBase;
import com.infinityraider.infinitylib.render.block.IBlockRenderingHandler;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
public class BlockRope extends BlockBase implements IRopeAttachable {
    private final AxisAlignedBB box;

    public BlockRope() {
        super("ropeBlock", Properties.create(Material.WOOL));
        float u = Constants.UNIT;
        this.box = new AxisAlignedBB(7.5*u, 0, 7.5*u, 8.5*u, 1, 8.5*u);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if(!this.canRopeStay(world, pos)) {
            this.breakRope(world, pos, state, false);
        }
    }

    public boolean canRopeStay(World world, BlockPos pos) {
        BlockPos up = pos.up();
        BlockState state = world.getBlockState(up);
        if(state.isSolidSide(world, up, Direction.DOWN)) {
            return true;
        }
        if(state.getBlock() instanceof IRopeAttachable) {
            IRopeAttachable attachable = (IRopeAttachable) state.getBlock();
            return attachable.canAttachRope(world, up, state);
        }
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().isReplaceable(world, pos) && canRopeStay(world, pos);
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
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (!world.isRemote && heldItem.getItem() instanceof ItemRope) {
            if (this.extendRope(world, pos) && !player.abilities.isCreativeMode) {
                player.inventory.decrStackSize(player.inventory.currentItem, 1);
            }
        }
        return false;
    }

    public boolean extendRope(World world, BlockPos pos) {
        BlockPos below = pos.down();
        BlockState state = world.getBlockState(below);
        if(state.getBlock() instanceof BlockRope) {
            return ((BlockRope) state.getBlock()).extendRope(world, below);
        }
        if(this.canPlaceBlockAt(world, below)) {
            world.setBlockState(below, this.getDefaultState(), 3);
            return true;
        }
        return false;
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, PlayerEntity player) {
        this.breakRope(world, pos, world.getBlockState(pos), player.isSneaking());
    }

    public void breakRope(World world, BlockPos pos, BlockState state, boolean propagateUp) {
        if (propagateUp) {
            this.propagateRopeBreak(world, pos, true);
        } else {
            world.setBlockToAir(pos);
            this.dropBlockAsItem(world, pos, state, 0);
        }
    }

    private void propagateRopeBreak(World world, BlockPos pos, boolean up) {
        if(!world.isRemote) {
            BlockPos posAt = pos.add(0, up ? 1 : -1, 0);
            BlockState state = world.getBlockState(posAt);
            world.setBlockToAir(pos);
            this.dropBlockAsItem(world, pos, state, 0);
            if (state.getBlock() instanceof BlockRope) {
                ((BlockRope) state.getBlock()).propagateRopeBreak(world, posAt, up);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader source, BlockPos pos) {
        return this.box;
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public ItemStack getItem(World worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(ItemRegistry.getInstance().itemRope);
    }

    @Override
    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return ItemRegistry.getInstance().itemRope;
    }

    @Override
    public boolean canSpawnInBlock() {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isReplaceable(IBlockReader world, BlockPos pos) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, Direction side) {
        return side == Direction.UP || side == Direction.DOWN;
    }

    @Override
    public boolean canCollideCheck(BlockState state, boolean hitIfLiquid) {
        return true;
    }

    @Override
    public boolean doesSideBlockRendering(BlockState state, Direction world, BlockPos pos, Direction face) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isSideSolid(BlockState base_state, IBlockReader world, BlockPos pos, Direction side) {
        return false;
    }

    @Override
    public boolean isLadder(BlockState state, IBlockReader world, BlockPos pos, LivingEntity entity) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return CUTOUT;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public IBlockRenderingHandler getRenderer() {
        return new RenderBlockRope(this);
    }

    @Override
    public ModelResourceLocation getBlockModelResourceLocation() {
        return new ModelResourceLocation(Reference.MOD_ID, this.getInternalName());
    }

    @Override
    public boolean canAttachRope(World world, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void onRopeAttached(World world, BlockPos pos, BlockState state) {}
}
