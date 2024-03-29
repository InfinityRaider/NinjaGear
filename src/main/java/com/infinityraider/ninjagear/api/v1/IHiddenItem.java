package com.infinityraider.ninjagear.api.v1;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Implement in item classes which do not reveal the player when the player equips them while hidden
 */
public interface IHiddenItem {
    /**
     * Checks whether or not equipping this item will reveal a hidden entity
     * @param entity entity equipping the item
     * @param stack stack holding the item
     * @return true to reveal the player
     */
    boolean shouldRevealPlayerWhenEquipped(Player entity, ItemStack stack);
}
