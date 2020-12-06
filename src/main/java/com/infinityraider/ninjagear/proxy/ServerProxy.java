package com.infinityraider.ninjagear.proxy;

import com.infinityraider.infinitylib.proxy.base.IServerProxyBase;
import com.infinityraider.ninjagear.config.Config;
import com.infinityraider.ninjagear.registry.EffectRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerProxy implements IProxy, IServerProxyBase<Config> {
    @Override
    public boolean isPlayerHidden(PlayerEntity player) {
        return player.isPotionActive(EffectRegistry.getInstance().effectNinjaHidden);
    }
}
