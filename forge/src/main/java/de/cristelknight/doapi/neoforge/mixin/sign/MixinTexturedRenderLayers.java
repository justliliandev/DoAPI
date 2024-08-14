package de.cristelknight.doapi.neoforge.mixin.sign;

import de.cristelknight.doapi.neoforge.terraform.sign.SpriteIdentifierRegistry;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(Sheets.class)
public class MixinTexturedRenderLayers {

	@Shadow @Final public static Map<WoodType, Material> SIGN_MATERIALS;

	@Inject(method = "getSignMaterial", at = @At("RETURN"))
	private static void injectTerrestriaSigns(WoodType arg, CallbackInfoReturnable<Material> cir) {
		for(Material material: SpriteIdentifierRegistry.INSTANCE.getIdentifiers()) {
			if(!SIGN_MATERIALS.containsValue(material)) {
				SIGN_MATERIALS.put(arg, material);
			}
		}
	}
}
