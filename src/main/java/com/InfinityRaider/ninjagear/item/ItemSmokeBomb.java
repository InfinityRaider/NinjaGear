package com.InfinityRaider.ninjagear.item;

import com.InfinityRaider.ninjagear.api.v1.IHiddenItem;
import com.InfinityRaider.ninjagear.entity.EntitySmokeBomb;
import com.InfinityRaider.ninjagear.reference.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

public class ItemSmokeBomb extends ItemBase implements IHiddenItem, IItemWithRecipe {
    public ItemSmokeBomb() {
        super("smokebomb");
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
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @SideOnly(Side.CLIENT)
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
                Items.fire_charge,
                "dyeBlack",
                Items.paper,
                "dyeBlack"));
        return list;
    }
}
