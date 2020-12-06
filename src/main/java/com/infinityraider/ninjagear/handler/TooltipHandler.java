package com.infinityraider.ninjagear.handler;

import com.infinityraider.ninjagear.api.v1.IHiddenItem;
import com.infinityraider.ninjagear.capability.CapabilityNinjaArmor;
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

    private TooltipHandler() {
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) {
            return;
        }
        if (stack.getItem() instanceof IHiddenItem) {
            this.addTooltipForHiddenItem(event.getToolTip(), (IHiddenItem) stack.getItem(), stack, event.getPlayer());
        }
        if (CapabilityNinjaArmor.isNinjaArmor(stack)) {
            this.addTooltipForNinjaArmor(event.getToolTip());
        }
    }

    private void addTooltipForHiddenItem(List<ITextComponent> tooltip, IHiddenItem item, ItemStack stack, PlayerEntity player) {
        if (!item.shouldRevealPlayerWhenEquipped(player, stack)) {
            tooltip.add(EMPTY_STRING);
            tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:hiddenItem_L1"));
        }
    }

    private void addTooltipForNinjaArmor(List<ITextComponent> tooltip) {
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:ninja_armor_L"));
    }
}
