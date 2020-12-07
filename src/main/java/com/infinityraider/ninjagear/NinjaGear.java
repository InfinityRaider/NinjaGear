package com.infinityraider.ninjagear;

import com.infinityraider.infinitylib.InfinityMod;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.ninjagear.apiimpl.APISelector;
import com.infinityraider.ninjagear.config.Config;
import com.infinityraider.ninjagear.proxy.IProxy;
import com.infinityraider.ninjagear.proxy.ServerProxy;
import com.infinityraider.ninjagear.proxy.ClientProxy;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.network.*;
import com.infinityraider.ninjagear.registry.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

@Mod(Reference.MOD_ID)
public class NinjaGear extends InfinityMod<IProxy, Config> {
    public static NinjaGear instance;

    public NinjaGear() {
        super();
    }

    @Override
    public String getModId() {
        return Reference.MOD_ID;
    }

    @Override
    protected void onModConstructed() {
        instance = this;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected IProxy createClientProxy() {
        return new ClientProxy();
    }

    @Override
    @OnlyIn(Dist.DEDICATED_SERVER)
    protected IProxy createServerProxy() {
        return new ServerProxy();
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
    public Object getModEffectRegistry() {
        return EffectRegistry.getInstance();
    }

    @Override
    public void registerMessages(INetworkWrapper wrapper) {
        wrapper.registerMessage(MessageInvisibility.class);
        wrapper.registerMessage(MessageUpdateGadgetRenderMaskClient.class);
        wrapper.registerMessage(MessageUpdateGadgetRenderMaskServer.class);
    }

    @Override
    public void initializeAPI() {
        APISelector.init();
    }
}