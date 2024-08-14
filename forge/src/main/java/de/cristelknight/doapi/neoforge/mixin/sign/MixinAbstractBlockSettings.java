package de.cristelknight.doapi.neoforge.mixin.sign;

import de.cristelknight.doapi.neoforge.terraform.sign.BlockSettingsLock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.level.block.state.BlockBehaviour;

@Mixin(BlockBehaviour.Properties.class)
public class MixinAbstractBlockSettings implements BlockSettingsLock {
	@Unique
	private boolean terraform$locked = false;

	@Inject(method = "sound", at = @At("HEAD"), cancellable = true)
	private void terraform$preventSoundsOverride(CallbackInfoReturnable<BlockBehaviour.Properties> ci) {
		if (this.terraform$locked) {
			ci.setReturnValue((BlockBehaviour.Properties) (Object) this);
			this.terraform$locked = false;
		}
	}

	@Override
	public void terraform$lock() {
		this.terraform$locked = true;
	}
}
