package de.cristelknight.doapi.common.block.entity;

import de.cristelknight.doapi.Util;
import de.cristelknight.doapi.common.registry.DoApiBlockEntityTypes;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class StorageBlockEntity extends BlockEntity {

    private int size;

    private NonNullList<ItemStack> inventory;

    public StorageBlockEntity(BlockPos pos, BlockState state) {
        super(DoApiBlockEntityTypes.STORAGE_ENTITY.get(), pos, state);
    }

    public StorageBlockEntity(BlockPos pos, BlockState state, int size) {
        super(DoApiBlockEntityTypes.STORAGE_ENTITY.get(), pos, state);
        this.size = size;
        this.inventory = NonNullList.withSize(this.size, ItemStack.EMPTY);
    }

    public void setStack(int slot, ItemStack stack){
        if (slot >= 0 && slot < inventory.size()) {
            inventory.set(slot, stack);
            setChanged();
        }
    }

    public ItemStack removeStack(int slot){
        if (slot >= 0 && slot < inventory.size()) {
            ItemStack stack = inventory.set(slot, ItemStack.EMPTY);
            setChanged();
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setChanged() {
        if (level instanceof ServerLevel serverLevel) {
            if (!level.isClientSide()) {
                Packet<ClientGamePacketListener> updatePacket = getUpdatePacket();
                for (ServerPlayer player : Util.tracking(serverLevel, getBlockPos())) {
                    player.connection.send(updatePacket);
                }
            }
        }
        super.setChanged();
        
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        this.size = compoundTag.getInt("size");
        this.inventory = NonNullList.withSize(this.size, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.inventory, provider);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        ContainerHelper.saveAllItems(compoundTag, this.inventory, provider);
        compoundTag.putInt("size", this.size);
        super.saveAdditional(compoundTag, provider);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    public void setInventory(NonNullList<ItemStack> inventory) {
        for (int i = 0; i < inventory.size(); i++) {
            this.inventory.set(i, inventory.get(i));
        }
    }

    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }
}
