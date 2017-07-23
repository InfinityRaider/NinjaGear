package com.infinityraider.ninjagear.proxy;

import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import com.infinityraider.ninjagear.apiimpl.APISelector;
import com.infinityraider.ninjagear.handler.*;
import com.infinityraider.ninjagear.registry.PotionRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy extends IProxyBase {
    @Override
    default void preInitStart(FMLPreInitializationEvent event) {
        APISelector.init();
    }

    @Override
    default void postInitStart(FMLPostInitializationEvent event) {
        PotionRegistry.getInstance().init();
    }

    @Override
    default void initConfiguration(FMLPreInitializationEvent event) {
        ConfigurationHandler.getInstance().init(event);
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
    boolean isPlayerHidden(EntityPlayer player);
}
