package de.cristelknight.doapi.neoforge.terraform.boat.impl;

import de.cristelknight.doapi.DoApi;
import de.cristelknight.doapi.neoforge.terraform.boat.api.TerraformBoatTypeRegistry;
import de.cristelknight.doapi.terraform.boat.TerraformBoatType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;

public final class TerraformBoatTrackedData {
    public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, DoApi.MOD_ID);


	private TerraformBoatTrackedData() {
	}

	static StreamCodec<FriendlyByteBuf, Optional<TerraformBoatType>> TERRAFORM_BOAT = new StreamCodec<>() {
        @Override
        public void encode(FriendlyByteBuf byteBuf, Optional<TerraformBoatType> boat) {
            byteBuf.writeResourceLocation(TerraformBoatTypeRegistry.getId(boat.get()));
        }

        @Override
        public Optional<TerraformBoatType> decode(FriendlyByteBuf object) {
            return Optional.of(TerraformBoatTypeRegistry.get(object.readResourceLocation()));
        }
    };

	public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<Optional<TerraformBoatType>>> ENTITY_DATA_BOAT = DATA_SERIALIZERS.register("terraform_boat", () -> EntityDataSerializer.forValueType(TERRAFORM_BOAT));

	public static void register(IEventBus modEventBus) {
        DATA_SERIALIZERS.register(modEventBus);

	}
}
