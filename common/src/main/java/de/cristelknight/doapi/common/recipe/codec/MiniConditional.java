package de.cristelknight.doapi.common.recipe.codec;

public class MiniConditional {
    private final String type;
    private final String modid;

    public MiniConditional(String type, String modid) {
        this.type = type;
        this.modid = modid;
    }

    public String getType() {
        return type;
    }

    public String getModid() {
        return modid;
    }
}
