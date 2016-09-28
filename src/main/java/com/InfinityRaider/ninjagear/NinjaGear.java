package com.infinityraider.ninjagear;

import com.infinityraider.infinitylib.InfinityMod;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import com.infinityraider.ninjagear.network.*;
import com.infinityraider.ninjagear.proxy.IProxy;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.registry.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        version = Reference.VERSION,
        guiFactory = Reference.GUI_FACTORY_CLASS
)
public class NinjaGear extends InfinityMod {
    @Mod.Instance(Reference.MOD_ID)
    public static NinjaGear instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Override
    public IProxyBase proxy() {
        return proxy;
    }

    @Override
    public String getModId() {
        return Reference.MOD_ID;
    }

    @Override
    public Object getModBlockRegistry() {
        return BlockRegistry.getInstance();
    }

    @Override
    public Object getModItemRegistry() {
        return ItemRegistry.getInstance();
    }

    @Override
    public Object getModEntityRegistry() {
        return EntityRegistry.getInstance();
    }

    @Override
    public void registerMessages(INetworkWrapper wrapper) {
        wrapper.registerMessage(MessageInvisibility.class);
        wrapper.registerMessage(MessageUpdateGadgetRenderMaskClient.class);
        wrapper.registerMessage(MessageUpdateGadgetRenderMaskServer.class);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void onPreInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void onInit(FMLInitializationEvent event) {
        super.init(event);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void onPostInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        super.onServerAboutToStart(event);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void serverStart(FMLServerStartingEvent event) {
        super.onServerStarting(event);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void serverStarted(FMLServerStartedEvent event) {
        super.onServerStarted(event);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void serverStopping(FMLServerStoppingEvent event) {
        super.onServerStopping(event);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void serverStopped(FMLServerStoppedEvent event) {
        super.onServerStopped(event);
    }
}