package com.infinityraider.ninjagear.api.v1;

import com.infinityraider.ninjagear.api.APIBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * <h1>This is the NinjaGear API, version 1.</h1>
 *
 * <p>
 * General notes:
 * </p>
 *
 * <ul>
 * <li>The methods of this API will never modify the parameter objects unless
 * explicitly stated.
 * <li>All parameters are required and may not be null unless stated otherwise.
 * <li>Return values will never be null unless stated otherwise.
 * </ul>
 *
 */
@SuppressWarnings("unused")
public interface APIv1 extends APIBase {
    /**
     * Checks if a player is hidden, a hidden player is not rendered and not targeted by mobs
     * @param player the player to check
     * @return if the player is invisible
     */
    boolean isPlayerHidden(EntityPlayer player);

    /**
     * Reveals a player, a revealed player can not hide and if the player is hidden, the hiding status will be removed.
     * Only call this in the server thread. Calling this in the client thread will have no effect
     * @param player the player to reveal
     * @param duration the amount of ticks the player should be revealed
     */
    void revealPlayer(EntityPlayer player, int duration);
}
