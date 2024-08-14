package de.cristelknight.doapi.neoforge.terraform.sign.block;

import de.cristelknight.doapi.neoforge.terraform.sign.BlockSettingsLock;
import de.cristelknight.doapi.neoforge.terraform.sign.TerraformHangingSign;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;

public class TerraformHangingSignBlock extends CeilingHangingSignBlock implements TerraformHangingSign {
	private final ResourceLocation texture;
	private final ResourceLocation guiTexture;

	public TerraformHangingSignBlock(ResourceLocation texture, ResourceLocation guiTexture, Properties settings) {
		super(WoodType.OAK, BlockSettingsLock.lock(settings));
		this.texture = texture;
		this.guiTexture = guiTexture;
	}

	@Override
	public ResourceLocation getTexture() {
		return texture;
	}

	@Override
	public ResourceLocation getGuiTexture() {
		return guiTexture;
	}
}
