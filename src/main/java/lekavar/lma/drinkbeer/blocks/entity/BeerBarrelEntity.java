package lekavar.lma.drinkbeer.blocks.entity;

import lekavar.lma.drinkbeer.container.BeerBarrelContainer;
import lekavar.lma.drinkbeer.registry.BlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BeerBarrelEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    private int remainingBrewTime;
    private int isMaterialCompleted;
    private int beerType;
    private int isBrewing;

    public BeerBarrelEntity() {
        super(BlockEntityRegistry.beerBarrelEntity.get());
    }

    public int get(int index) {
        switch (index) {
            case 0:
                return remainingBrewTime;
            case 1:
                return isMaterialCompleted;
            case 2:
                return beerType;
            case 3:
                return isBrewing;
            default:
                return 0;
        }
    }

    public void set(int index, int value) {
        switch (index) {
            case 0:
                remainingBrewTime = value;
                break;
            case 1:
                isMaterialCompleted = value;
                break;
            case 2:
                beerType = value;
                break;
            case 3:
                isBrewing = value;
        }
    }

    public int size() {
        return 4;
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        super.save(tag);
        tag.putShort("RemainingBrewTime", (short) this.remainingBrewTime);
        tag.putShort("IsMaterialCompleted", (short) this.isMaterialCompleted);
        tag.putShort("BeerType", (short) this.beerType);
        tag.putShort("IsBrewing", (short) this.isBrewing);

        return tag;
    }

    @Override
    public void load(BlockState state, @Nonnull CompoundNBT tag) {
        super.load(state, tag);
        this.remainingBrewTime = tag.getShort("RemainingBrewTime");
        this.isMaterialCompleted = tag.getShort("IsMaterialCompleted");
        this.beerType = tag.getShort("BeerType");
        this.isBrewing = tag.getShort("IsBrewing");
    }

    @Override
    public void tick() {
        if (!level.isClientSide()) {
            this.remainingBrewTime = this.remainingBrewTime > 0 ? --this.remainingBrewTime : 0;
        }
        this.setChanged();
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.drinkbeer.beer_barrel");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new BeerBarrelContainer(id, inventory, this);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (!this.level.isClientSide)
            this.level.setBlocksDirty(this.worldPosition, this.getBlockState(), this.getBlockState());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(level.getBlockState(pkt.getPos()), pkt.getTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.remainingBrewTime = tag.getShort("RemainingBrewTime");
        this.isMaterialCompleted = tag.getShort("IsMaterialCompleted");
        this.beerType = tag.getShort("BeerType");
        this.isBrewing = tag.getShort("IsBrewing");
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        tag.putShort("RemainingBrewTime", (short) this.remainingBrewTime);
        tag.putShort("IsMaterialCompleted", (short) this.isMaterialCompleted);
        tag.putShort("BeerType", (short) this.beerType);
        tag.putShort("IsBrewing", (short) this.isBrewing);
        return tag;
    }
}
