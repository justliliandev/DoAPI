package de.cristelknight.doapi.neoforge.terraform;

import de.cristelknight.doapi.neoforge.terraform.boat.impl.TerraformBoatInitializer;
import de.cristelknight.doapi.terraform.boat.item.TerraformBoatItemHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(TerraformApiForge.TERRAFORM_MOD_ID)
public class TerraformApiForge {

    public static final String TERRAFORM_MOD_ID = "terraform";
    public TerraformApiForge(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        TerraformBoatInitializer.init(modEventBus);
    }


    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(TerraformBoatItemHelper::registerDispenserBehaviours);
    }
}
