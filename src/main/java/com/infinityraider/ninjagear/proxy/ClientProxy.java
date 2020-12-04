package com.infinityraider.ninjagear.proxy;

import com.infinityraider.ninjagear.config.Config;
import com.infinityraider.ninjagear.render.player.RenderNinjaGadget;
import com.infinityraider.infinitylib.proxy.base.IClientProxyBase;
import com.infinityraider.ninjagear.handler.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class ClientProxy implements IProxy, IClientProxyBase<Config> {

    @Override
    @SuppressWarnings("Unchecked")
    public Function<ForgeConfigSpec.Builder, Config> getConfigConstructor() {
        return Config.Client::new;
    }

    @Override
    public boolean isPlayerHidden(PlayerEntity player) {
        return RenderPlayerHandler.getInstance().isInvisible(player);
    }

    @Override
    public void registerEventHandlers() {
        IProxy.super.registerEventHandlers();
        this.registerEventHandler(RenderPlayerHandler.getInstance());
        this.registerEventHandler(TooltipHandler.getInstance());
        this.registerEventHandler(RenderNinjaGadget.getInstance());
        this.registerEventHandler(NinjaGadgetHandler.getInstance());
    }
}
