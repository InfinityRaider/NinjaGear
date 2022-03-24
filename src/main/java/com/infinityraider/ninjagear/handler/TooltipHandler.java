package com.infinityraider.ninjagear.handler;

import com.infinityraider.ninjagear.api.v1.IHiddenItem;
import com.infinityraider.ninjagear.capability.CapabilityNinjaArmor;
import com.infinityraider.ninjagear.reference.Reference;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TooltipHandler {
    private static final TooltipHandler INSTANCE = new TooltipHandler();

    private static final TextComponent EMPTY_STRING = new TextComponent("");

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

    private void addTooltipForHiddenItem(List<Component> tooltip, IHiddenItem item, ItemStack stack, Player player) {
        if (!item.shouldRevealPlayerWhenEquipped(player, stack)) {
            tooltip.add(EMPTY_STRING);
            tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:hidden_item_L1"));
        }
    }

    private void addTooltipForNinjaArmor(List<Component> tooltip) {
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:ninja_armor_L1"));
    }
}
