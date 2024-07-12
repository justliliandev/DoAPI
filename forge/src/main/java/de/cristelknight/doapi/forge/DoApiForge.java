package de.cristelknight.doapi.forge;

import de.cristelknight.doapi.DoApi;
import de.cristelknight.doapi.forge.common.packs.RepositorySourceMaker;
import net.minecraft.server.packs.PackType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@Mod(DoApi.MOD_ID)
public class DoApiForge {

    public DoApiForge(IEventBus modBus) {
        DoApi.init();
        modBus.addListener(this::injectPackRepositories);
    }

    private void injectPackRepositories(AddPackFindersEvent event) {
        event.addRepositorySource(new RepositorySourceMaker(event.getPackType().equals(PackType.CLIENT_RESOURCES)));
    }
}
