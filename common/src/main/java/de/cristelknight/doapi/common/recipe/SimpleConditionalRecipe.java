package de.cristelknight.doapi.common.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import de.cristelknight.doapi.DoApiEP;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class SimpleConditionalRecipe {

    private static final MapCodec<Recipe<?>> CODEC = new MapCodec<Recipe<?>>() {
        @Override
        public <T> RecordBuilder<T> encode(Recipe<?> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
            throw new UnsupportedOperationException("DoAPI conditional recipe codec does not support encoding");
        }

        @Override
        public <T> DataResult<Recipe<?>> decode(DynamicOps<T> ops, MapLike<T> input) {
            try {
                //return de(ops, json);
                return DataResult.success(null);
            } catch (JsonSyntaxException e) {
                return DataResult.error(e::getMessage);
            }
        }

        @Override
        public <T> Stream<T> keys(DynamicOps<T> ops) {
            return Stream.empty();
        }
    }.stable();

    private static final StreamCodec<RegistryFriendlyByteBuf, Recipe<?>> STREAM_CODEC =
            new StreamCodec<>() {

                @Override
                public @NotNull Recipe<?> decode(RegistryFriendlyByteBuf buf) {
                    try {
                        return SimpleConditionalRecipe.decode(buf.readJsonWithCodec(CODEC.codec()));
                    } catch (JsonSyntaxException e) {
                        return null;
                    }
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, Recipe<?> recipe) {
                    throw new UnsupportedOperationException("DoAPI conditional recipe codec does not support encoding");
                }
            };

    private static Recipe<?> decode(Recipe<?> recipe){
        // ?
        return recipe;
    }

//    private static <T> DataResult<Pair<Recipe<?>, T>> decodeJson(DynamicOps<T> ops, Recipe<?> recipe) {
//        JsonObject object = GsonHelper.getAsJsonObject(json.getAsJsonObject(), "recipe");
//
//        ResourceLocation type = ResourceLocation.withDefaultNamespace(GsonHelper.getAsString(object, "type"));
//        RecipeSerializer<?> serializer = BuiltInRegistries.RECIPE_SERIALIZER.get(type);
//        if (serializer == null)
//            return DataResult.error(() -> "Invalid or unsupported recipe type '" + type + "'");
//        DataResult<Recipe<?>> parsed = (DataResult<Recipe<?>>) serializer.codec().parse(JsonOps.INSTANCE, object);
//        return SimpleConditionalRecipe.checkResult(parsed, ops);
//    }

    public static final RecipeSerializer<Recipe<?>> SERIALIZER = new RecipeSerializer<>() {
        @Override
        public @NotNull MapCodec<Recipe<?>> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, Recipe<?>> streamCodec() {
            return STREAM_CODEC;
        }
    };

    public static <T> DataResult<Pair<Recipe<?>, T>> checkResult(DataResult<Recipe<?>> parsed, DynamicOps<T> ops){
        if (parsed.error().isPresent())
            return DataResult.error(parsed.error().get()::message);
        else if (!parsed.result().isPresent())
            return DataResult.error(() -> "Recipe did not parse a valid return");
        else
            return DataResult.success(Pair.of(parsed.result().get(), ops.empty()));
    }

    public static boolean checkCondition(JsonObject c){
        String type = GsonHelper.getAsString(c, "type");

        if(type.equals("forge:mod_loaded")){
            String modId = c.get("modid").getAsString();
            return DoApiEP.isModLoaded(modId);
        }
        return false;
    }
}