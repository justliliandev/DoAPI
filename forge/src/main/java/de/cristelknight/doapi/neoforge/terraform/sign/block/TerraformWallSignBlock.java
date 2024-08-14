package de.cristelknight.doapi.neoforge.terraform.sign.block;

import de.cristelknight.doapi.neoforge.terraform.sign.BlockSettingsLock;
import de.cristelknight.doapi.neoforge.terraform.sign.TerraformSign;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;

public class TerraformWallSignBlock extends WallSignBlock implements TerraformSign {
	private final ResourceLocation texture;

	public TerraformWallSignBlock(ResourceLocation texture, Properties settings) {
		super(WoodType.OAK, BlockSettingsLock.lock(settings));
		this.texture = texture;
	}

	@Override
	public ResourceLocation getTexture() {
		return texture;
	}
}
