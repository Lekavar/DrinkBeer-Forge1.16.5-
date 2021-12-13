package lekavar.lma.drinkbeer.blocks.tileentity;

import lekavar.lma.drinkbeer.capability.beerinfo.IBeerInfo;
import lekavar.lma.drinkbeer.essentials.beer.Beers;
import lekavar.lma.drinkbeer.essentials.beer.IBeer;
import lekavar.lma.drinkbeer.essentials.flavor.BaseFlavors;
import lekavar.lma.drinkbeer.essentials.flavor.ComboFlavors;
import lekavar.lma.drinkbeer.essentials.flavor.IFlavor;
import lekavar.lma.drinkbeer.registries.TileEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BeerTileEntity extends TileEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    boolean isFlavored;
    int count;
    IBeer beerType;
    List<IFlavor> baseFlavor;
    List<IFlavor> comboFlavor;
    IFlavor particleFlavor; //The of base flavor

    public BeerTileEntity() {
        super(TileEntityRegistry.BEER_TILEENTITY.get());
        count = 1;
        beerType = Beers.MINER_PALE_ALE;
        isFlavored = false;
        baseFlavor = new ArrayList<>();
        comboFlavor = new ArrayList<>();
    }


    public void initializeData(IBeer beerType, int count, List<IFlavor> baseFlavor, List<IFlavor> comboFlavor) {
        this.beerType = beerType;
        this.count = count;
        this.baseFlavor = baseFlavor;
        this.comboFlavor = comboFlavor;
        if (!baseFlavor.isEmpty()) {
            isFlavored = true;
            particleFlavor = this.baseFlavor.get(this.baseFlavor.size() - 1);
        }
    }

    public void updateData(int count) {
        this.count = count;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "dummy_controller", 1, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public IBeer getBeerType() {
        return beerType;
    }

    public int getCount() {
        return count;
    }

    public boolean isFlavored() {
        return isFlavored;
    }

    public List<IFlavor> getBaseFlavor() {
        return baseFlavor;
    }

    public List<IFlavor> getComboFlavor() {
        return comboFlavor;
    }

    public Optional<IFlavor> getParticleFlavor() {
        return Optional.ofNullable(particleFlavor);
    }

    /**
     * Server side only. Not all data will be synced to slient.
     */
    public boolean matchBeerItem(IBeer beerType, IBeerInfo beerInfo) {
        if (isFlavored == beerInfo.isFlavoredBeer()) {
            if (isFlavored) {
                return this.beerType == beerType && comboFlavor.equals(beerInfo.getComboFlavor()) && baseFlavor.equals(beerInfo.getBaseFlavor());
            } else
                return this.beerType == beerType;
        } else
            return false;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        beerType = Beers.fromByte(nbt.getByte("type"));
        count = nbt.getInt("count");
        isFlavored = nbt.getBoolean("is_flavored");
        if (isFlavored) {
            int baseFlavorCount = nbt.getInt("base_flavor_count");
            int comboFlavorCount = nbt.getInt("combo_flavor_count");
            baseFlavor = BaseFlavors.fromByteArray(nbt.getByteArray("base_flavor"), 10, baseFlavorCount);
            if (comboFlavorCount == 0) {
                comboFlavor = new ArrayList<>();
            } else {
                comboFlavor = ComboFlavors.fromByteArray(nbt.getByteArray("combo_flavor"), 10, comboFlavorCount);
            }
            particleFlavor = baseFlavor.get(baseFlavor.size() - 1);
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        CompoundNBT compoundNBT = super.save(nbt);
        compoundNBT.putByte("type", Beers.toByte(beerType));
        compoundNBT.putInt("count", count);
        compoundNBT.putBoolean("is_flavored", isFlavored);
        if (isFlavored) {
            nbt.putInt("base_flavor_count", baseFlavor.size());
            nbt.putByteArray("base_flavor", BaseFlavors.toByteArray(baseFlavor, 10));
            if (comboFlavor.isEmpty()) {
                nbt.putInt("combo_flavor_count", 0);
            } else {
                nbt.putInt("combo_flavor_count", comboFlavor.size());
                nbt.putByteArray("combo_flavor", ComboFlavors.toByteArray(comboFlavor, 10));
            }
        }
        return compoundNBT;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(level.getBlockState(pkt.getPos()), pkt.getTag());

    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT compoundNBT = super.getUpdateTag();
        compoundNBT.putByte("type", Beers.toByte(beerType));
        compoundNBT.putInt("count", count);
        compoundNBT.putBoolean("is_flavored", isFlavored);
        if (isFlavored)
            compoundNBT.putByte("particle_flavor", BaseFlavors.toByte(particleFlavor));
        return compoundNBT;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        beerType = Beers.fromByte(nbt.getByte("type"));
        count = nbt.getInt("count");
        isFlavored = nbt.getBoolean("is_flavored");
        if (isFlavored)
            particleFlavor = BaseFlavors.fromByte(nbt.getByte("particle_flavor"));

    }
}
