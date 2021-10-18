package lekavar.lma.drinkbeer.registries;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundEventRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "drinkbeer");
    public static final RegistryObject<SoundEvent> DRINKING_BEER = register("drinking_beer");
    public static final RegistryObject<SoundEvent> POURING = register("pouring");
    public static final RegistryObject<SoundEvent> POURING_CHRISTMAS_VER = register("pouring_christmas");
    public static final RegistryObject<SoundEvent> IRON_CALL_BELL_TINKLE = register("iron_call_bell_tinkle");
    public static final RegistryObject<SoundEvent> GOLDEN_CALL_BELL_TINKLE = register("golden_call_bell_tinkle");


    private static RegistryObject<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation("drinkbeer", name)));
    }
}
