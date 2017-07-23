package com.infinityraider.ninjagear.proxy;

import com.infinityraider.infinitylib.proxy.base.IClientProxyBase;
import com.infinityraider.ninjagear.handler.*;
import com.infinityraider.ninjagear.render.player.RenderNinjaGadget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public class ClientProxy implements IProxy, IClientProxyBase {
    @Override
    public void initConfiguration(FMLPreInitializationEvent event) {
        IProxy.super.initConfiguration(event);
        ConfigurationHandler.getInstance().initClientConfigs(event);
    }

    @Override
    public boolean isPlayerHidden(EntityPlayer player) {
        return RenderPlayerHandler.getInstance().isInvisible(player);
    }

    @Override
    public void registerEventHandlers() {
        IProxy.super.registerEventHandlers();
        this.registerEventHandler(RenderPlayerHandler.getInstance());
        this.registerEventHandler(TooltipHandler.getInstance());
        if(ConfigurationHandler.getInstance().renderGadgets) {
            this.registerEventHandler(RenderNinjaGadget.getInstance());
        }
        this.registerEventHandler(NinjaGadgetHandler.getInstance());
    }
}
