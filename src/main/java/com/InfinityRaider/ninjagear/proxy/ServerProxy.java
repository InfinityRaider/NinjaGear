package com.InfinityRaider.ninjagear.proxy;

import com.InfinityRaider.ninjagear.registry.PotionRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.server.FMLServerHandler;

@SuppressWarnings("unused")
@SideOnly(Side.SERVER)
public class ServerProxy extends CommonProxy {
    @Override
    public EntityPlayer getClientPlayer() {
        return null;
    }

    @Override
    public World getClientWorld() {
        return null;
    }

    @Override
    public World getWorldByDimensionId(int dimension) {
        return FMLServerHandler.instance().getServer().worldServerForDimension(dimension);
    }

    @Override
    public void queueTask(Runnable task) {
        FMLServerHandler.instance().getServer().addScheduledTask(task);
    }

    @Override
    public boolean isPlayerHidden(EntityPlayer player) {
        return player.isPotionActive(PotionRegistry.getInstance().potionNinjaHidden);
    }

    @Override
    public Side getPhysicalSide() {
        return Side.SERVER;
    }

    @Override
    public Side getEffectiveSide() {
        return getPhysicalSide();
    }

    @Override
    public void registerEventHandlers() {
        super.registerEventHandlers();
    }
}
