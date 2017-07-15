package com.infinityraider.ninjagear.item;

import com.infinityraider.infinitylib.item.IItemWithModel;
import com.infinityraider.infinitylib.item.ItemBase;
import com.infinityraider.infinitylib.utility.IRecipeRegister;
import com.infinityraider.ninjagear.api.v1.IHiddenItem;
import com.infinityraider.ninjagear.block.BlockRope;
import com.infinityraider.ninjagear.handler.ConfigurationHandler;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.registry.BlockRegistry;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@MethodsReturnNonnullByDefault
public class ItemRope extends ItemBase implements IItemWithModel, IHiddenItem, IRecipeRegister {
    private final BlockRope block;

    public ItemRope() {
        super("ropeItem");
        this.block = (BlockRope) BlockRegistry.getInstance().blockRope;
        this.setCreativeTab(ItemRegistry.CREATIVE_TAB);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if(block instanceof BlockRope) {
            return EnumActionResult.PASS;
        }
        if (!block.isReplaceable(world, pos)) {
            pos = pos.offset(face);
        }
        if (stack.stackSize != 0 && player.canPlayerEdit(pos, face, stack) && world.canBlockBePlaced(this.block, pos, false, face, null, stack)) {
            int i = this.getMetadata(stack.getMetadata());
            IBlockState newState = this.block.onBlockPlaced(world, pos, face, hitX, hitY, hitZ, i, player);
            if (this.block.canPlaceBlockAt(world, pos) && placeBlockAt(stack, player, world, pos, newState)) {
                SoundType soundtype = this.block.getSoundType();
                world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                --stack.stackSize;
            }
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if(player.isSneaking()) {
            if(world.isRemote) {
                return new ActionResult<>(EnumActionResult.PASS, stack);
            } else {
                this.attemptToCreateRopeCoil(player);
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    public void attemptToCreateRopeCoil(EntityPlayer player) {
        ItemStack stack = player.inventory.getCurrentItem();
        if(stack != null && stack.getItem() instanceof ItemRope && stack.stackSize >= ConfigurationHandler.getInstance().ropeCoilLength) {
            ItemStack coil = new ItemStack(ItemRegistry.getInstance().itemRopeCoil, 1, 0);
            if(player.inventory.addItemStackToInventory(coil) && !player.capabilities.isCreativeMode) {
                player.inventory.decrStackSize(player.inventory.currentItem, ConfigurationHandler.getInstance().ropeCoilLength);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("deprecation")
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L1"));
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L2"));
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L3"));
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L4"));
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L5"));
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, IBlockState newState) {
        if (!world.setBlockState(pos, newState, 3)) {
            return false;
        }
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block) {
            this.block.onBlockPlacedBy(world, pos, state, player, stack);
        }
        return true;
    }

    @Override
    public boolean shouldRevealPlayerWhenEquipped(EntityPlayer entity, ItemStack stack) {
        return false;
    }

    @Override
    public void registerRecipes() {
        this.getRecipes().forEach(GameRegistry::addRecipe);
    }

    public List<IRecipe> getRecipes() {
        List<IRecipe> list = new ArrayList<>();
        list.add(new ShapedOreRecipe(new ItemStack(this, 8),"s  ", "sss", "  s",
                's', "string"));
        list.add(new ShapelessOreRecipe(new ItemStack(this, ConfigurationHandler.getInstance().ropeCoilLength),
                ItemRegistry.getInstance().itemRopeCoil));
        return list;
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        List<Tuple<Integer, ModelResourceLocation>> list = new ArrayList<>();
        list.add(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + this.getInternalName(), "inventory")));
        return list;
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }
}
