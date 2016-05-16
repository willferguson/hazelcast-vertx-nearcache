package com.github.willferguson.nearcache;

/**
 * Created by will on 15/05/2016.
 */
public enum SharedDataKeys {

    MAP_NAME("cluster_map"),
    MAP_KEY("key");

    private final String cacheKey;
    SharedDataKeys(String cacheKey) {
        this.cacheKey = cacheKey;
    }


    public String getCacheKey() {
        return cacheKey;
    }
}
