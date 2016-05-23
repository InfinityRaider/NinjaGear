package com.InfinityRaider.ninjagear.api.v1;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Implement in entities which can see hidden players
 */
public interface IEntityTrueSight {
    /**
     * Checks if this entity can see the target player, when this method is called, the player is hidden.
     * @param player the player being targeted by this entity
     * @return if this entity can see the player
     */
    boolean canSeeTarget(EntityPlayer player);
}
