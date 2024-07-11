package de.cristelknight.doapi.common.block.entity;

import de.cristelknight.doapi.common.registry.DoApiBlockEntityTypes;
import de.cristelknight.doapi.common.util.GeneralUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FlowerPotBlockEntity extends BlockEntity {
    private Item flower;
    public static final String FLOWER_KEY ="flower";

    public FlowerPotBlockEntity(BlockPos pos, BlockState state) {
        super(DoApiBlockEntityTypes.FLOWER_POT_ENTITY.get(), pos, state);
    }

    public Item getFlower() {
        return flower;
    }

    public void setFlower(Item flower) {
        this.flower = flower;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        writeFlower(compoundTag, flower, provider);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        flower = readFlower(compoundTag, provider);
    }

    public void writeFlower(CompoundTag nbt, Item flower, HolderLookup.Provider provider) {
        CompoundTag nbtCompound = new CompoundTag();
        if (flower != null) {
            flower.getDefaultInstance().save(provider);
        }
        nbt.put(FLOWER_KEY, nbtCompound);
    }

    public Item readFlower(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        if(nbt.contains(FLOWER_KEY)) {
            CompoundTag nbtCompound = nbt.getCompound(FLOWER_KEY);
            if (!nbtCompound.isEmpty()) {
                return ItemStack.parseOptional(provider, nbt).getItem();
            }
        }
        return null;
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
