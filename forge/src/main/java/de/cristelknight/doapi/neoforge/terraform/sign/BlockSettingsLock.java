package de.cristelknight.doapi.neoforge.terraform.sign;

import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * An <b>internal</b> interface implemented on {@link BlockBehaviour.Properties} that is used to prevent sign blocks from overriding their block sound group to the default wood block sound group.
 */
public interface BlockSettingsLock {
	/**
	 * Locks the block sound group.
	 */
	void terraform$lock();

	/**
	 * Locks the block sound group.
	 */
	static BlockBehaviour.Properties lock(BlockBehaviour.Properties settings) {
		((BlockSettingsLock) settings).terraform$lock();
		return settings;
	}
}
