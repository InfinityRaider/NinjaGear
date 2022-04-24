package com.infinityraider.ninjagear.proxy;

import com.infinityraider.infinitylib.proxy.base.IServerProxyBase;
import com.infinityraider.ninjagear.config.Config;
import com.infinityraider.ninjagear.registry.EffectRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerProxy implements IProxy, IServerProxyBase<Config> {
    @Override
    public boolean isPlayerHidden(Player player) {
        return player.hasEffect(EffectRegistry.getInstance().getNinjaHiddenEffect());
    }
}
