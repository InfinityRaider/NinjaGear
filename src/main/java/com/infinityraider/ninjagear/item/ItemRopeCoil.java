package com.infinityraider.ninjagear.item;

import com.infinityraider.ninjagear.entity.EntityRopeCoil;
import com.infinityraider.ninjagear.reference.Names;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.infinitylib.item.ItemBase;
import com.infinityraider.ninjagear.api.v1.IHiddenItem;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemRopeCoil extends ItemBase implements IHiddenItem {
    public ItemRopeCoil() {
        super(Names.Items.ROPE_COIL, new Properties().group(ItemRegistry.CREATIVE_TAB));
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if(!world.isRemote) {
            EntityRopeCoil rope = new EntityRopeCoil(world, player);
            world.addEntity(rope);
            if (!player.isCreative()) {
                player.inventory.decrStackSize(player.inventory.currentItem, 1);
            }
        }
        return new ActionResult<>(ActionResultType.CONSUME, player.getHeldItem(hand));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L1"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L2"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L3"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L4"));
    }

    @Override
    public boolean shouldRevealPlayerWhenEquipped(PlayerEntity entity, ItemStack stack) {
        return false;
    }
}
