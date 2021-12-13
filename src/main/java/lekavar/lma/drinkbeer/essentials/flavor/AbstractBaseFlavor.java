package lekavar.lma.drinkbeer.essentials.flavor;

import lekavar.lma.drinkbeer.registries.ParticleTypeRegistry;
import net.minecraft.particles.IParticleData;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBaseFlavor implements IFlavor {
    public List<IFlavor> getOverridableFlavor(){
        return new ArrayList<>();
    }

    public IParticleData getDisplayParticle(){
        return ParticleTypeRegistry.FLAVORED_BEER_DEFAULT.get();
    }
}
