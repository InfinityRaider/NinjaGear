package com.infinityraider.ninjagear.item;

import com.infinityraider.infinitylib.block.IInfinityBlock;
import com.infinityraider.infinitylib.item.BlockItemBase;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.infinitylib.item.IItemWithModel;
import com.infinityraider.ninjagear.api.v1.IHiddenItem;
import com.infinityraider.ninjagear.block.BlockRope;
import com.infinityraider.ninjagear.registry.BlockRegistry;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@MethodsReturnNonnullByDefault
public class ItemRope extends BlockItemBase implements IItemWithModel, IHiddenItem {
    private final BlockRope block;

    public ItemRope(IInfinityBlock block) {
        super(block, new Properties().group(ItemRegistry.CREATIVE_TAB));
        this.block = (BlockRope) BlockRegistry.getInstance().blockRope;
    }

    @Override
    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        super.onUse(worldIn, livingEntityIn, stack, count);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        PlayerEntity player = context.getPlayer();
        Direction face = context.getFace();
        Hand hand = context.getHand();
        BlockItemUseContext blockContext = new BlockItemUseContext(context);
        if(block instanceof BlockRope) {
            return ActionResultType.PASS;
        }
        if (!block.isReplaceable(state, blockContext)) {
            pos = pos.offset(context.getFace());
        }
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getCount() != 0 && player.canPlayerEdit(pos, face, stack) &&blockContext.canPlace()) {
            BlockState newState = this.block.getStateForPlacement(blockContext);
            if (this.block.canPlaceBlockAt(world, pos) && placeBlockAt(stack, player, world, pos, newState)) {
                SoundType soundtype = this.block.getSoundType(state, world, pos, player);
                world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                stack.setCount(stack.getCount() - 1);
            }
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.FAIL;
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(player.isSneaking()) {
            if(world.isRemote) {
                return new ActionResult<>(ActionResultType.PASS, stack);
            } else {
                this.attemptToCreateRopeCoil(player);
                return new ActionResult<>(ActionResultType.SUCCESS, stack);
            }
        }
        return new ActionResult<>(ActionResultType.PASS, stack);
    }

    public void attemptToCreateRopeCoil(PlayerEntity player) {
        ItemStack stack = player.inventory.getCurrentItem();
        if(stack.getItem() instanceof ItemRope && stack.getCount() >= NinjaGear.instance.getConfig().getRopeCoilLength()) {
            ItemStack coil = new ItemStack(ItemRegistry.getInstance().itemRopeCoil, 1);
            if(player.inventory.addItemStackToInventory(coil) && !player.isCreative()) {
                player.inventory.decrStackSize(player.inventory.currentItem, NinjaGear.instance.getConfig().getRopeCoilLength());
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L1"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L2"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L3"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L4"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L5"));
    }

    public boolean placeBlockAt(ItemStack stack, PlayerEntity player, World world, BlockPos pos, BlockState newState) {
        if (!world.setBlockState(pos, newState, 3)) {
            return false;
        }
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block) {
            this.block.onBlockPlacedBy(world, pos, state, player, stack);
        }
        return true;
    }

    @Override
    public boolean shouldRevealPlayerWhenEquipped(PlayerEntity entity, ItemStack stack) {
        return false;
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        List<Tuple<Integer, ModelResourceLocation>> list = new ArrayList<>();
        list.add(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + this.getInternalName(), "inventory")));
        return list;
    }
}
