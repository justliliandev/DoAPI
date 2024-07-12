package de.cristelknight.doapi.common.block.entity;

import de.cristelknight.doapi.common.registry.DoApiBlockEntityTypes;
import de.cristelknight.doapi.common.util.GeneralUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FlowerBoxBlockEntity extends BlockEntity {

	private NonNullList<ItemStack> flowers;

	public FlowerBoxBlockEntity(BlockPos pos, BlockState state) {
		super(DoApiBlockEntityTypes.FLOWER_BOX_ENTITY.get(), pos, state);
		this.flowers = NonNullList.withSize(2, ItemStack.EMPTY);
	}

	@Override
	protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
		ContainerHelper.saveAllItems(compoundTag, this.flowers, provider);
		super.saveAdditional(compoundTag, provider);

	}

	@Override
	protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
		super.loadAdditional(compoundTag, provider);
		this.flowers = NonNullList.withSize(3, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(compoundTag, this.flowers, provider);

	}

	public void addFlower(ItemStack stack, int slot){
		flowers.set(slot, stack);
		setChanged();
	}

	public ItemStack removeFlower(int slot){
		ItemStack stack = flowers.set(slot, ItemStack.EMPTY);
		setChanged();
		return stack;
	}

	public ItemStack getFlower(int slot) {
		return flowers.get(slot);
	}

	public boolean isSlotEmpty(int slot) {
		return slot < flowers.size() && flowers.get(slot).isEmpty();
	}

	public Item[] getFlowers() {
		List<Item> items = new ArrayList<>();
		for (ItemStack stack : flowers) {
			if (!stack.isEmpty()) {
				items.add(stack.getItem());
			}
		}
		return items.toArray(new Item[0]);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
		return this.saveWithoutMetadata(provider);
	}

	@Override
	public void setChanged() {
		if(level != null && !level.isClientSide()) {
			Packet<ClientGamePacketListener> updatePacket = getUpdatePacket();

			for (ServerPlayer player : GeneralUtil.tracking((ServerLevel) level, getBlockPos())) {
				player.connection.send(updatePacket);
			}
		}
		super.setChanged();
	}
}