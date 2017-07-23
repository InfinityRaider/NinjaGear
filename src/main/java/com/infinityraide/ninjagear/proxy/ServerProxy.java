package com.infinityraider.ninjagear.proxy;

import com.infinityraider.infinitylib.proxy.base.IServerProxyBase;
import com.infinityraider.ninjagear.registry.PotionRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@SideOnly(Side.SERVER)
public class ServerProxy implements IProxy, IServerProxyBase {
    @Override
    public boolean isPlayerHidden(EntityPlayer player) {
        return player.isPotionActive(PotionRegistry.getInstance().potionNinjaHidden);
    }
}
