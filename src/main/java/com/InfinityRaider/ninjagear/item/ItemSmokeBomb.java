package com.infinityraider.ninjagear.item;

import com.infinityraider.infinitylib.item.IItemWithModel;
import com.infinityraider.infinitylib.item.IItemWithRecipe;
import com.infinityraider.infinitylib.item.ItemBase;
import com.infinityraider.ninjagear.api.v1.IHiddenItem;
import com.infinityraider.ninjagear.entity.EntitySmokeBomb;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@MethodsReturnNonnullByDefault
public class ItemSmokeBomb extends ItemBase implements IHiddenItem, IItemWithRecipe, IItemWithModel {
    public ItemSmokeBomb() {
        super("smokebomb");
        this.setCreativeTab(ItemRegistry.CREATIVE_TAB);
    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 60;
    }

    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
        if(!world.isRemote) {
            float max = (float) this.getMaxItemUseDuration(stack);
            EntitySmokeBomb entitySmokeBomb = new EntitySmokeBomb(world, entity, 4*(max - timeLeft)/max);
            world.spawnEntityInWorld(entitySmokeBomb);
            if(entity instanceof EntityPlayer && !((EntityPlayer) entity).capabilities.isCreativeMode) {
                EntityPlayer player = (EntityPlayer) entity;
                player.inventory.decrStackSize(player.inventory.currentItem, 1);
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("deprecation")
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L1"));
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L2"));
    }

    @Override
    public boolean shouldRevealPlayerWhenEquipped(EntityPlayer entity, ItemStack stack) {
        return false;
    }

    @Override
    public List<IRecipe> getRecipes() {
        List<IRecipe> list = new ArrayList<>();
        list.add(new ShapelessOreRecipe(new ItemStack(this, 8),
                Items.FIRE_CHARGE,
                "dyeBlack",
                Items.PAPER,
                "dyeBlack"));
        return list;
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        List<Tuple<Integer, ModelResourceLocation>> list = new ArrayList<>();
        list.add(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + this.getInternalName(), "inventory")));
        return list;
    }
}
