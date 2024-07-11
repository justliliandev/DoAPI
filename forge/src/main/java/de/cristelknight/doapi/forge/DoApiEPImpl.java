package de.cristelknight.doapi.forge;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import de.cristelknight.doapi.DoApi;
import de.cristelknight.doapi.forge.common.packs.BuiltInPackRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.KnownPack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.fml.loading.moddiscovery.ModInfo;
import net.neoforged.neoforgespi.locating.IModFile;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class DoApiEPImpl {
    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> T fromJson(ResourceLocation recipeId, JsonObject json) {
        JsonObject recipe = GsonHelper.getAsJsonObject(json, "recipe");
        JsonArray conditions = GsonHelper.getAsJsonArray(json, "conditions");

        JsonObject forgeRecipe = new JsonObject();
        forgeRecipe.addProperty("type", "forge:conditional");

        JsonArray recipes = new JsonArray();

        JsonObject newRecipe = new JsonObject();
        newRecipe.add("conditions", conditions);
        newRecipe.add("recipe", recipe);

        recipes.add(newRecipe);
        forgeRecipe.add("recipes", recipes);

        // TODO
        //return (T) ConditionalRecipe.SERIALZIER.fromJson(recipeId, forgeRecipe);
        return null;
    }

    public static boolean isModLoaded(String modid) {
        ModList modList = ModList.get();
        if(modList != null){
            return modList.isLoaded(modid);
        }
        return isModPreLoaded(modid);
    }

    public static void registerBuiltInPack(String modId, ResourceLocation location, boolean alwaysEnabled){
        ModContainer container = ModList.get().getModContainerById(modId).orElse(null);
        if(container == null){
            DoApi.LOGGER.warn("Mod container for modId:" + modId + " is null");
            return;
        }
        String stringPath = location.getPath();
        Path path = getResourceDirectory(modId, "resourcepacks/" + stringPath);
        if(path == null) return;
        String[] pathElements = stringPath.split("/");
        BuiltInPackRegistry.packResources.put(location, new Pair<>(new PathPackResources(
                new PackLocationInfo("mod/" + location, Component.literal(location.getNamespace() + "/" + location.getPath()), PackSource.BUILT_IN, Optional.of(new KnownPack(location.getNamespace(), "mod/" + location, container.getModInfo().getVersion().toString())))
                 ,path), alwaysEnabled));
        //         BuiltInPackRegistry.packResources.put(location, new Pair<>(new PathPackResources(pathElements[pathElements.length - 1], true, path), alwaysEnabled));
    }

    public static @Nullable Path getResourceDirectory(String modId, String subPath) {
        ModContainer container = ModList.get().getModContainerById(modId).orElse(null);
        if(container == null){
            DoApi.LOGGER.warn("Mod container for modId:" + modId + " is null");
            return null;
        }
        IModFile file = container.getModInfo().getOwningFile().getFile();
        Path path = file.findResource(subPath);
        if(path == null){
            DoApi.LOGGER.warn("Path for subPath: " + subPath + " in modId: " + modId + " is null");
        }
        return path;
    }

    public static <T> List<Pair<List<String>, T>> findAPIs(Class<T> returnClazz, String name, Class<?> annotationClazz) {
        return ApiFinder.scanForAPIs(annotationClazz, returnClazz);
    }

    public static boolean isModPreLoaded(String modid) {
        return getPreLoadedModInfo(modid) != null;
    }

    public static @Nullable ModInfo getPreLoadedModInfo(String modId){
        for(ModInfo info : LoadingModList.get().getMods()){
            if(info.getModId().equals(modId)) {
                return info;
            }
        }
        return null;
    }

    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
