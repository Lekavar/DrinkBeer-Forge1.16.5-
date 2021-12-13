package lekavar.lma.drinkbeer.registries;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.HeartParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleFactoryRegistry {
    @SubscribeEvent
    public static void onParticleFactoryRegistration(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ParticleTypeRegistry.FLAVORED_BEER_DEFAULT.get(), FlameParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(ParticleTypeRegistry.CALL_BELL_TINKLE_PAW.get(), HeartParticle.AngryVillagerFactory::new);
    }
}
