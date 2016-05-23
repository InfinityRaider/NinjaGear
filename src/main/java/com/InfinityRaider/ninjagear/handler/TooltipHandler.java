package com.InfinityRaider.ninjagear.handler;

import com.InfinityRaider.ninjagear.api.v1.IHiddenItem;
import com.InfinityRaider.ninjagear.reference.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class TooltipHandler {
    private static final TooltipHandler INSTANCE = new TooltipHandler();

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
            addTooltipForHiddenItem(event.getToolTip(), (IHiddenItem) stack.getItem(), stack, event.getEntityPlayer());
        }
    }

    private void addTooltipForHiddenItem(List<String> tooltip, IHiddenItem item, ItemStack stack, EntityPlayer player) {
        if(!item.shouldRevealPlayerWhenEquipped(player, stack)) {
            tooltip.add("");
            tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:hiddenItem_L1"));
        }
    }
}
