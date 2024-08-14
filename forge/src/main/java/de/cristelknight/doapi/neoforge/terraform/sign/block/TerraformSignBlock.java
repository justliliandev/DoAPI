package de.cristelknight.doapi.neoforge.terraform.sign.block;

import de.cristelknight.doapi.neoforge.terraform.sign.BlockSettingsLock;
import de.cristelknight.doapi.neoforge.terraform.sign.TerraformSign;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;

public class TerraformSignBlock extends StandingSignBlock implements TerraformSign {
	private final ResourceLocation texture;

	public TerraformSignBlock(ResourceLocation texture, Properties settings) {
		super(WoodType.OAK, BlockSettingsLock.lock(settings));
		this.texture = texture;
	}

	@Override
	public ResourceLocation getTexture() {
		return texture;
	}
}
