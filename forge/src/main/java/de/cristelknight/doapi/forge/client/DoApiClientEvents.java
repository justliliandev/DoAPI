package de.cristelknight.doapi.forge.client;

import de.cristelknight.doapi.DoApi;
import de.cristelknight.doapi.client.DoApiClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = DoApi.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class DoApiClientEvents {
    private static boolean initialized = false;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        DoApiClient.onClientInit();
    }

    @SubscribeEvent
    public static void preClientSetup(RegisterEvent event) {
        if(!initialized){
            DoApiClient.preClientInit();
            initialized = true;
        }
    }
}
