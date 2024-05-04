package de.cristelknight.doapi.mixin;

import de.cristelknight.doapi.common.util.datafixer.DataFixers;
import de.cristelknight.doapi.common.util.datafixer.StringPairs;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourceLocation.class)
public class DataFixerMixin {
    @Mutable
    @Shadow @Final private String path;

    @Inject(method = "<init>(Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/resources/ResourceLocation$Dummy;)V", at = @At("TAIL"))
    private void updateRL(String namespace, String path, ResourceLocation.Dummy dummy, CallbackInfo ci){
        if(!DataFixers.isFreezed()) DataFixers.freeze();
        StringPairs pairs = DataFixers.get(namespace);
        if(pairs != null && pairs.containsOldPath(namespace)) this.path = pairs.getNewPath(path);
    }
}
