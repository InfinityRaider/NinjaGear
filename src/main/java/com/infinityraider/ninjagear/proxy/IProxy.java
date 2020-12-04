package com.infinityraider.ninjagear.proxy;

import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import com.infinityraider.ninjagear.config.Config;
import com.infinityraider.ninjagear.handler.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Function;

public interface IProxy extends IProxyBase<Config> {
    @Override
    default Function<ForgeConfigSpec.Builder, Config> getConfigConstructor() {
        return Config.Common::new;
    }

    @Override
    default void registerCapabilities() {}

    @Override
    default void registerEventHandlers() {
        this.registerEventHandler(EntityTargetingHandler.getInstance());
        this.registerEventHandler(NinjaAuraHandler.getInstance());
    }

    @Override
    default void activateRequiredModules() {}

    /**
     * Checks if a player is hidden
     * @param player player to check
     * @return if the player is hidden
     */
    boolean isPlayerHidden(PlayerEntity player);
}
