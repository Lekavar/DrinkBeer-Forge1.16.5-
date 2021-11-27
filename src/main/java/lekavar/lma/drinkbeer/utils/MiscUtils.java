package lekavar.lma.drinkbeer.utils;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MiscUtils {
    private final static int BASE_NIGHT_VISION_TIME = 2400;

    public static int getNightVisionDurationByMoonPhase(int moonPhase) {
        return BASE_NIGHT_VISION_TIME + (moonPhase == 0 ? Math.abs(moonPhase - 1 - 4) * 1200 : Math.abs(moonPhase - 4) * 1200);
    }

    public static SoundEvent getRandomNightHowlSound() {
        List<SoundEvent> available = ForgeRegistries.SOUND_EVENTS.getValues().stream().filter(soundEvent -> soundEvent.getRegistryName().toString().contains("night_howl_drinking_effect")).collect(Collectors.toList());
        return available.get(new Random().nextInt(available.size()));
    }

    public static int moonPhase(long worldTime) {
        return (int) (worldTime / 24000L % 8L + 8L) % 8;
    }
}
