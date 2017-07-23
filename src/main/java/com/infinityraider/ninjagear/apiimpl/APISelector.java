package com.infinityraider.ninjagear.apiimpl;

import com.infinityraider.ninjagear.api.API;
import com.infinityraider.ninjagear.api.APIBase;
import com.infinityraider.ninjagear.api.APIStatus;
import com.infinityraider.ninjagear.apiimpl.v1.APIimplv1;

public class APISelector implements APIBase {
    private APISelector() {}

    public static void init() {
        API.setAPI(new APISelector());
    }

    @Override
    public APIBase getAPI(int maxVersion) {
        if (maxVersion <= 0) {
            return this;
        } else {
            switch(maxVersion) {
                case 1:
                    return new APIimplv1(1, APIStatus.OK);
                default:
                    return new APIimplv1(1, APIStatus.OK);
            }
        }
    }

    @Override
    public APIStatus getStatus() {
        return APIStatus.ERROR;
    }

    @Override
    public int getVersion() {
        return 0;
    }
}
