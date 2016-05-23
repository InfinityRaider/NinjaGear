package com.InfinityRaider.ninjagear.proxy;

import com.InfinityRaider.ninjagear.handler.ConfigurationHandler;
import com.InfinityRaider.ninjagear.handler.NinjaGadgetHandler;
import com.InfinityRaider.ninjagear.handler.TooltipHandler;
import com.InfinityRaider.ninjagear.registry.BlockRegistry;
import com.InfinityRaider.ninjagear.registry.EntityRegistry;
import com.InfinityRaider.ninjagear.registry.ItemRegistry;
import com.InfinityRaider.ninjagear.handler.RenderPlayerHandler;
import com.InfinityRaider.ninjagear.render.player.RenderNinjaGadget;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        EntityRegistry.getInstance().initRenderers();
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getMinecraft().theWorld;
    }

    @Override
    public void initConfiguration(FMLPreInitializationEvent event) {
        super.initConfiguration(event);
        ConfigurationHandler.getInstance().initClientConfigs(event);
    }

    @Override
    public World getWorldByDimensionId(int dimension) {
        Side effectiveSide = FMLCommonHandler.instance().getEffectiveSide();
        if(effectiveSide == Side.SERVER) {
            return FMLClientHandler.instance().getServer().worldServerForDimension(dimension);
        } else {
            return getClientWorld();
        }
    }

    @Override
    public void queueTask(Runnable task) {
        if(getEffectiveSide() == Side.CLIENT) {
            Minecraft.getMinecraft().addScheduledTask(task);
        } else {
            FMLClientHandler.instance().getServer().addScheduledTask(task);
        }
    }

    @Override
    public boolean isPlayerHidden(EntityPlayer player) {
        return RenderPlayerHandler.getInstance().isInvisible(player);
    }

    @Override
    public Side getPhysicalSide() {
        return Side.CLIENT;
    }

    @Override
    public Side getEffectiveSide() {
        return FMLCommonHandler.instance().getEffectiveSide();
    }

    @Override
    public void registerEventHandlers() {
        super.registerEventHandlers();
        MinecraftForge.EVENT_BUS.register(RenderPlayerHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(TooltipHandler.getInstance());
        if(ConfigurationHandler.getInstance().renderGadgets) {
            MinecraftForge.EVENT_BUS.register(RenderNinjaGadget.getInstance());
        }
        MinecraftForge.EVENT_BUS.register(NinjaGadgetHandler.getInstance());
    }

    @Override
    public void registerRenderers() {
        //items
        ItemRegistry.getInstance().registerRenderers();
        //blocks
        BlockRegistry.getInstance().registerRenderers();
    }
}
