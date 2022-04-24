package com.infinityraider.ninjagear.item;

import com.infinityraider.infinitylib.item.BlockItemBase;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.api.v1.IHiddenItem;
import com.infinityraider.ninjagear.block.BlockRope;
import com.infinityraider.ninjagear.registry.BlockRegistry;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
public class ItemRope extends BlockItemBase implements IHiddenItem {
    public ItemRope() {
        super(BlockRegistry.getInstance().getRopeBlock(), new Properties().tab(ItemRegistry.CREATIVE_TAB));
    }

    public BlockRope getRopeBlock() {
        return BlockRegistry.getInstance().getRopeBlock();
    }

    @Override
    public void onUseTick(Level worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        super.onUseTick(worldIn, livingEntityIn, stack, count);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        Player player = context.getPlayer();
        Direction face = context.getClickedFace();
        ItemStack stack = context.getItemInHand();
        BlockPlaceContext blockContext = new BlockPlaceContext(context);
        if(block instanceof BlockRope) {
            return InteractionResult.PASS;
        }
        if (!block.canBeReplaced(state, blockContext)) {
            pos = pos.relative(context.getClickedFace());
        }
        if (stack.getCount() != 0 && player.mayUseItemAt(pos, face, stack) && blockContext.canPlace()) {
            BlockState newState = this.getRopeBlock().getStateForPlacement(blockContext);
            if (newState != null && newState.canSurvive(world, pos) && placeBlockAt(stack, player, world, pos, newState)) {
                SoundType soundtype = this.getRopeBlock().getSoundType(state, world, pos, player);
                world.playSound(player, pos, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                stack.setCount(stack.getCount() - 1);
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if(player.isDiscrete()) {
            if(world.isClientSide()) {
                return new InteractionResultHolder<>(InteractionResult.PASS, stack);
            } else {
                this.attemptToCreateRopeCoil(player);
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
            }
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, stack);
    }

    public void attemptToCreateRopeCoil(Player player) {
        ItemStack stack = player.getInventory().getSelected();
        if(stack.getItem() instanceof ItemRope && stack.getCount() >= NinjaGear.instance.getConfig().getRopeCoilLength()) {
            ItemStack coil = new ItemStack(ItemRegistry.getInstance().getRopeCoilItem(), 1);
            if(player.getInventory().add(coil) && !player.isCreative()) {
                player.getInventory().removeItem(player.getInventory().selected, NinjaGear.instance.getConfig().getRopeCoilLength());
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L1"));
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L2"));
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L3"));
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L4"));
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L5"));
    }

    public boolean placeBlockAt(ItemStack stack, Player player, Level world, BlockPos pos, BlockState newState) {
        if (!world.setBlock(pos, newState, 3)) {
            return false;
        }
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.getRopeBlock()) {
            this.getRopeBlock().setPlacedBy(world, pos, state, player, stack);
        }
        return true;
    }

    @Override
    public boolean shouldRevealPlayerWhenEquipped(Player entity, ItemStack stack) {
        return false;
    }
}
