package com.infinityraider.ninjagear.handler;

import com.infinityraider.ninjagear.api.v1.IHiddenItem;
import com.infinityraider.ninjagear.reference.Reference;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TooltipHandler {
    private static final TooltipHandler INSTANCE = new TooltipHandler();

    private static final ITextComponent EMPTY_STRING = new StringTextComponent("");

    public static TooltipHandler getInstance() {
        return INSTANCE;
    }

    private TooltipHandler() {}

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if(stack == null) {
            return;
        }
        if(stack.getItem() instanceof IHiddenItem) {
            addTooltipForHiddenItem(event.getToolTip(), (IHiddenItem) stack.getItem(), stack, event.getPlayer());
        }
    }

    private void addTooltipForHiddenItem(List<ITextComponent> tooltip, IHiddenItem item, ItemStack stack, PlayerEntity player) {
        if(!item.shouldRevealPlayerWhenEquipped(player, stack)) {
            tooltip.add(EMPTY_STRING);
            tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:hiddenItem_L1"));
        }
    }
}
