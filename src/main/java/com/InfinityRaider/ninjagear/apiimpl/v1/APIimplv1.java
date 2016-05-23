package com.InfinityRaider.ninjagear.apiimpl.v1;

import com.InfinityRaider.ninjagear.NinjaGear;
import com.InfinityRaider.ninjagear.api.API;
import com.InfinityRaider.ninjagear.api.APIBase;
import com.InfinityRaider.ninjagear.api.APIStatus;
import com.InfinityRaider.ninjagear.api.v1.APIv1;
import com.InfinityRaider.ninjagear.handler.NinjaAuraHandler;
import net.minecraft.entity.player.EntityPlayer;

public class APIimplv1 implements APIv1 {
    private final int version;
    private final APIStatus status;

    public APIimplv1(int version, APIStatus status) {
        this.version = version;
        this.status = status;
    }

    @Override
    public APIBase getAPI(int maxVersion) {
        if (maxVersion == version && status == APIStatus.OK) {
            return this;
        } else {
            return API.getAPI(maxVersion);
        }
    }

    @Override
    public APIStatus getStatus() {
        return status;
    }

    @Override
    public int getVersion() {
        return version;
    }
    @Override
    public boolean isPlayerHidden(EntityPlayer player) {
        return NinjaGear.proxy.isPlayerHidden(player);
    }

    @Override
    public void revealPlayer(EntityPlayer player, int duration) {
        NinjaAuraHandler.getInstance().revealEntity(player, duration);
    }
}
