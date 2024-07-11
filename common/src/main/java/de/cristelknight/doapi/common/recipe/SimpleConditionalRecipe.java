package de.cristelknight.doapi.common.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import de.cristelknight.doapi.DoApi;
import de.cristelknight.doapi.DoApiCommonEP;
import de.cristelknight.doapi.DoApiEP;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class SimpleConditionalRecipe {

    public static class Serializer<T extends Recipe<?>> implements RecipeSerializer<T> {

        @Override
        public MapCodec<T> codec() {
            return null;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
            return null;
        }
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
