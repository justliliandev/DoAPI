package de.cristelknight.doapi;

import net.minecraft.resources.ResourceLocation;


public final class DoApiRL {

    public static ResourceLocation asResourceLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(DoApi.MOD_ID, path);
    }

    public static String asString(String path) {
        return (DoApi.MOD_ID + ":" + path);
    }
}
