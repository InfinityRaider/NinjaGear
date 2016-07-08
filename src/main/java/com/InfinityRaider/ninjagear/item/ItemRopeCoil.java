package com.infinityraider.ninjagear.item;

import com.infinityraider.ninjagear.api.v1.IHiddenItem;
import com.infinityraider.ninjagear.entity.EntityRopeCoil;
import com.infinityraider.ninjagear.reference.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemRopeCoil extends ItemBase implements IHiddenItem {
    public ItemRopeCoil() {
        super("ropeCoil");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if(!world.isRemote) {
            EntityRopeCoil rope = new EntityRopeCoil(world, player);
            world.spawnEntityInWorld(rope);
            if (!player.capabilities.isCreativeMode) {
                player.inventory.decrStackSize(player.inventory.currentItem, 1);
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L1"));
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L2"));
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L3"));
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L4"));
    }

    @Override
    public boolean shouldRevealPlayerWhenEquipped(EntityPlayer entity, ItemStack stack) {
        return false;
    }
}
