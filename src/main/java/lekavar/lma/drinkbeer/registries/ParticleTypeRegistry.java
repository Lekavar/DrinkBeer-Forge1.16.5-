package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.DrinkBeer;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ParticleTypeRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, DrinkBeer.MODID);
    public static final RegistryObject<BasicParticleType> FLAVORED_BEER_DEFAULT = PARTICLE_TYPES.register("flavored_beer_default", ()->new BasicParticleType(false));
    public static final RegistryObject<BasicParticleType> CALL_BELL_TINKLE_PAW = PARTICLE_TYPES.register("call_bell_tinkle_paw", ()->new BasicParticleType(false));
}
