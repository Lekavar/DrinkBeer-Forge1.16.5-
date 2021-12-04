package lekavar.lma.drinkbeer.capability.beerinfo;

import lekavar.lma.drinkbeer.capability.Capabilities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BeerInfoProvider implements ICapabilitySerializable<CompoundNBT> {
    private final IBeerInfo imp = new BeerInfo();
    private final LazyOptional<IBeerInfo> impOptional = LazyOptional.of(() -> imp);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == Capabilities.BEER_INFO_CAPABILITY) {
            return impOptional.cast();
        } else return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (Capabilities.BEER_INFO_CAPABILITY == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) Capabilities.BEER_INFO_CAPABILITY.writeNBT(imp, null);
        }
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (Capabilities.BEER_INFO_CAPABILITY != null) {
            Capabilities.BEER_INFO_CAPABILITY.readNBT(imp, null, nbt);
        }
    }
}
