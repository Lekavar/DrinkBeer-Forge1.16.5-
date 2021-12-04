package lekavar.lma.drinkbeer.capability;

import lekavar.lma.drinkbeer.capability.beerinfo.BeerInfo;
import lekavar.lma.drinkbeer.capability.beerinfo.IBeerInfo;
import lekavar.lma.drinkbeer.essentials.flavor.BaseFlavors;
import lekavar.lma.drinkbeer.essentials.flavor.ComboFlavors;
import lekavar.lma.drinkbeer.essentials.flavor.IFlavor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Capabilities {
    @CapabilityInject(IBeerInfo.class)
    public static final Capability<IBeerInfo> BEER_INFO_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IBeerInfo.class, new BeerInfoStorage(), BeerInfo::new);
    }

    static class BeerInfoStorage implements Capability.IStorage<IBeerInfo>{
        @Nullable
        @Override
        public INBT writeNBT(Capability<IBeerInfo> capability, IBeerInfo instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putBoolean("is_flavored", instance.isFlavoredBeer());
            if(instance.isFlavoredBeer()){
                nbt.putInt("base_flavor_total",instance.getBaseFlavor().size());
                int i = 1;
                for(IFlavor flavor:instance.getBaseFlavor()){
                    nbt.putByte("base_flavor_"+i, BaseFlavors.toByte(flavor));
                    i++;
                }
                nbt.putInt("combo_flavor_total",instance.getComboFlavor().size());
                i = 1;
                for(IFlavor flavor:instance.getComboFlavor()){
                    nbt.putByte("combo_flavor_"+i, ComboFlavors.toByte(flavor));
                    i++;
                }
            }
            return nbt;
        }

        @Override
        public void readNBT(Capability<IBeerInfo> capability, IBeerInfo instance, Direction side, INBT nbt) {
            boolean isFlavored = ((CompoundNBT) nbt).getBoolean("is_flavored");
            instance.setFlavoredBeer(isFlavored);
            if(isFlavored){
                int i = ((CompoundNBT) nbt).getInt("base_flavor_total");
                if(i>0){
                    List<IFlavor> flavors = new ArrayList<>();
                    for(int j=1;j<=i;j++){
                        flavors.add(BaseFlavors.fromByte(((CompoundNBT) nbt).getByte("base_flavor_" + j)));
                    }
                    instance.setBaseFlavor(flavors);
                }
                i = ((CompoundNBT) nbt).getInt("combo_flavor_total");
                if(i>0){
                    List<IFlavor> flavors = new ArrayList<>();
                    for(int j=1;j<=i;j++){
                        flavors.add(ComboFlavors.fromByte(((CompoundNBT) nbt).getByte("combo_flavor_" + j)));
                    }
                    instance.setComboFlavor(flavors);
                }
            }
        }
    }
}
