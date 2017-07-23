package com.infinityraider.ninjagear;

import com.infinityraider.ninjagear.proxy.IProxy;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.infinitylib.InfinityMod;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import com.infinityraider.ninjagear.network.*;
import com.infinityraider.ninjagear.registry.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        version = Reference.VERSION,
        guiFactory = Reference.GUI_FACTORY_CLASS,
        dependencies = "required-after:infinitylib"
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
}