package com.InfinityRaider.ninjagear.api.v0;

import com.InfinityRaider.ninjagear.api.APIBase;
import com.InfinityRaider.ninjagear.api.APIStatus;

/**
 * Filler object to represent the API until SettlerCraft had the chance to
 * initialize itself.
 *
 */
public class NoAPI implements APIBase {

    @Override
    public APIStatus getStatus() {
        return APIStatus.API_NOT_INITIALIZED;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public APIBase getAPI(int maxVersion) {
        return this;
    }

}