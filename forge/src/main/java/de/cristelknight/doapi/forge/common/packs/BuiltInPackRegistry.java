package de.cristelknight.doapi.forge.common.packs;

import com.mojang.datafixers.util.Pair;
import de.cristelknight.doapi.DoApi;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.KnownPack;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class BuiltInPackRegistry {
    public static final Map<ResourceLocation, Pair<PackResources, Boolean>> packResources = new HashMap<>();

    public static void getPacks(Consumer<Pack> consumer, boolean client){
        if(packResources.isEmpty()) return;
        for (Map.Entry<ResourceLocation, Pair<PackResources, Boolean>> entry : packResources.entrySet()) {
            Pair<PackResources, Boolean> pair = entry.getValue();
            PackResources packResources = pair.getFirst();
            ResourceLocation id = entry.getKey();
            Boolean alwaysEnabled = pair.getSecond();

            if(packResources == null){
                DoApi.LOGGER.error("Pack for location: {} is null, skipping it!", id);
                continue;
            }
            if(client && packResources.getNamespaces(PackType.CLIENT_RESOURCES).isEmpty()){
                DoApi.LOGGER.debug(packResources.packId() + " has no assets, skipping it!");
                continue;
            }
            else if (!client && packResources.getNamespaces(PackType.SERVER_DATA).isEmpty()){
                DoApi.LOGGER.debug(packResources.packId() + " has no data, skipping it!");
                continue;
            }

            IModInfo modInfo = ModList.get().getModContainerById(id.getNamespace()).orElseThrow(() ->
                    new IllegalArgumentException("Mod not found: " + id.getNamespace())).getModInfo();



            Pack pack = Pack.readMetaAndCreate(
                    new PackLocationInfo("mod/" + packResources.location(), Component.literal(id.getNamespace() + "/" + id.getPath()), PackSource.BUILT_IN, Optional.of(new KnownPack(id.getNamespace(), "mod/" + packResources.location(), modInfo.getVersion().toString()))),
                    BuiltInPackSource.fromName((path) -> new PathPackResources(path, modInfo.getOwningFile().getFile().findResource(id.getPath()))),
                    PackType.SERVER_DATA,
                    new PackSelectionConfig(alwaysEnabled, Pack.Position.TOP, false));
            if (pack != null) {
                consumer.accept(pack);
            }
            else DoApi.LOGGER.error(packResources.packId() + " couldn't be created");
        }
    }
}
