package de.cristelknight.doapi.common.recipe.codec;

import java.util.List;

public class Conditionals {
    private final List<MiniConditional> conditionals;

    public Conditionals(List<MiniConditional> conditionals) {
        this.conditionals = conditionals;
    }

    public List<MiniConditional> getConditionals() {
        return conditionals;
    }
}
