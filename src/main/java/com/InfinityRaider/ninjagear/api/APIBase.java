package com.InfinityRaider.ninjagear.api;

public interface APIBase {

    /**
     * internal use only
     */
    APIBase getAPI(int maxVersion);

    /**
     * Returns the status of this APi object. See {@link APIStatus} for details.
     */
    APIStatus getStatus();

    /**
     * The version number of this API object.
     *
     * This API's contract is that for any version number there is exactly one
     * API interface that will never change.
     *
     * Note: The exception is version 0, which indicates that the object is not
     * a functional API but a placeholder object.
     */
    int getVersion();
}