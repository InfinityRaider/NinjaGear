package com.InfinityRaider.ninjagear.proxy;

import com.InfinityRaider.ninjagear.apiimpl.APISelector;
import com.InfinityRaider.ninjagear.handler.ConfigurationHandler;
import com.InfinityRaider.ninjagear.handler.EntityTargetingHandler;
import com.InfinityRaider.ninjagear.handler.NinjaAuraHandler;
import com.InfinityRaider.ninjagear.network.NetworkWrapper;
import com.InfinityRaider.ninjagear.registry.BlockRegistry;
import com.InfinityRaider.ninjagear.registry.EntityRegistry;
import com.InfinityRaider.ninjagear.registry.ItemRegistry;
import com.InfinityRaider.ninjagear.registry.PotionRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@SuppressWarnings("unused")
public abstract class CommonProxy implements IProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        APISelector.init();
        this.initConfiguration(event);
        NetworkWrapper.getInstance().init();
        BlockRegistry.getInstance().init();
        ItemRegistry.getInstance().init();
        this.registerEventHandlers();
        this.registerRenderers();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        EntityRegistry.getInstance().init();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        PotionRegistry.getInstance().init();
        ItemRegistry.getInstance().initRecipes();
        BlockRegistry.getInstance().initRecipes();
    }

    @Override
    public void initConfiguration(FMLPreInitializationEvent event) {
        ConfigurationHandler.getInstance().init(event);
    }

    @Override
    public Entity getEntityById(int dimension, int id) {
        return getEntityById(getWorldByDimensionId(dimension), id);
    }

    @Override
    public Entity getEntityById(World world, int id) {
        return world.getEntityByID(id);
    }

    @Override
    public void registerEventHandlers() {
        MinecraftForge.EVENT_BUS.register(EntityTargetingHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(NinjaAuraHandler.getInstance());
    }

    @Override
    public void registerRenderers() {}
}
