package de.cristelknight.doapi.common.recipe.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ConditionalCodecs {
    public static final Codec<MiniConditional> MINI_CONDITIONAL_CODEC = RecordCodecBuilder.create(instance -> // Given an instance
            instance.group(
                    Codec.STRING.fieldOf("type").forGetter(MiniConditional::getType),
                    Codec.STRING.fieldOf("modid").forGetter(MiniConditional::getModid)
            ).apply(instance, MiniConditional::new)
    );

    public static final Codec<Conditionals> CONDITIONALS_CODEC = RecordCodecBuilder.create(instance -> // Given an instance
            instance.group(
                    Codec.list(MINI_CONDITIONAL_CODEC).fieldOf("conditions").forGetter(Conditionals::getConditionals)
            ).apply(instance, Conditionals::new)
    );
}
