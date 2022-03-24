package com.infinityraider.ninjagear.proxy;

import com.infinityraider.infinitylib.modules.synchronizedeffects.ModuleSynchronizedEffects;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import com.infinityraider.ninjagear.capability.CapabilityNinjaArmor;
import com.infinityraider.ninjagear.config.Config;
import com.infinityraider.ninjagear.handler.*;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Function;

public interface IProxy extends IProxyBase<Config> {
    @Override
    default Function<ForgeConfigSpec.Builder, Config> getConfigConstructor() {
        return Config.Common::new;
    }

    @Override
    default void registerCapabilities() {
        this.registerCapability(CapabilityNinjaArmor.getInstance());
    }

    @Override
    default void registerEventHandlers() {
        this.registerEventHandler(EntityTargetingHandler.getInstance());
        this.registerEventHandler(NinjaAuraHandler.getInstance());
        this.registerEventHandler(AnvilHandler.getInstance());
    }

    @Override
    default void activateRequiredModules() {
        ModuleSynchronizedEffects.getInstance().activate();
    }

    /**
     * Checks if a player is hidden
     * @param player player to check
     * @return if the player is hidden
     */
    boolean isPlayerHidden(Player player);
}
