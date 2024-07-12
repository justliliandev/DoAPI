package de.cristelknight.doapi.forge.terraform.boat.impl;

import de.cristelknight.doapi.forge.terraform.boat.api.TerraformBoatTypeRegistry;
import de.cristelknight.doapi.terraform.boat.TerraformBoatType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;

import java.util.Optional;

public final class TerraformBoatTrackedData {
	private TerraformBoatTrackedData() {
	}

	static StreamCodec<FriendlyByteBuf, Optional<TerraformBoatType>> TERRAFORM_BOAT = new StreamCodec<FriendlyByteBuf, Optional<TerraformBoatType>>() {
		@Override
		public void encode(FriendlyByteBuf byteBuf, Optional<TerraformBoatType> boat) {
			byteBuf.writeResourceLocation(TerraformBoatTypeRegistry.getId(boat.get()));
		}

		@Override
		public Optional<TerraformBoatType> decode(FriendlyByteBuf object) {
			return Optional.of(TerraformBoatTypeRegistry.get(object.readResourceLocation()));
		}
	};

	public static final EntityDataSerializer<Optional<TerraformBoatType>> ENTITY_DATA_BOAT = EntityDataSerializer.forValueType(TERRAFORM_BOAT);

	public static void register() {
		//NeoForgeRegistries.ENTITY_DATA_SERIALIZERS.
		//EntityDataSerializers.registerSerializer(ENTITY_DATA_BOAT);
	}
}
