package de.cristelknight.doapi.neoforge.terraform.boat.impl.client;

import de.cristelknight.doapi.neoforge.terraform.boat.impl.TerraformBoatInitializer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class TerraformBoatClientInitializer  {

	public static void init(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(TerraformBoatInitializer.BOAT.get(), context -> new CustomBoatEntityRenderer(context, false));
		event.registerEntityRenderer(TerraformBoatInitializer.CHEST_BOAT.get(), context -> new CustomBoatEntityRenderer(context, true));
	}
}
