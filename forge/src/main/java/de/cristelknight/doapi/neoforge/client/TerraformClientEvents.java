package de.cristelknight.doapi.neoforge.client;

import de.cristelknight.doapi.client.terraform.TerraformBoatClientHelper;
import de.cristelknight.doapi.neoforge.terraform.TerraformApiForge;
import de.cristelknight.doapi.neoforge.terraform.boat.impl.client.TerraformBoatClientInitializer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = TerraformApiForge.TERRAFORM_MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class TerraformClientEvents {
    private static boolean initialized = false;
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        TerraformBoatClientInitializer.init(event);
    }

    @SubscribeEvent
    public static void preClientSetup(RegisterEvent event) {
        if(!initialized){
            TerraformBoatClientHelper.preClientInit();
            initialized = true;
        }

    }
}
