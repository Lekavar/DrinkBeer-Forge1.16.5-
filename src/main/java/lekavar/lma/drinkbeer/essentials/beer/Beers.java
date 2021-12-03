package lekavar.lma.drinkbeer.essentials.beer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import lekavar.lma.drinkbeer.essentials.flavor.IFlavor;
import lekavar.lma.drinkbeer.registries.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Beers {
    private static final BiMap<IBeer, Byte> CARD_MAPPINGS = HashBiMap.create();
    public static IBeer MINER_PALE_ALE = register(MinerPaleAle::new,(byte)0);
    public static IBeer BLAZE_STOUT = register(BlazeStout::new,(byte)1);
    public static IBeer BLAZE_MILK_STOUT = register(BlazeMilkStout::new,(byte)2);
    public static IBeer APPLE_LAMBIC = register(AppleLambic::new,(byte)3);
    public static IBeer SWEET_BERRY_KRIEK = register(SweetBerryKriek::new,(byte)4);
    public static IBeer HAARS_ICEY_PALE_LAGER = register(HaarsIceyPaleLager::new,(byte)5);
    public static IBeer PUMPKIN_KVASS = register(PumpkinKvass::new,(byte)6);
    public static IBeer NIGHT_HOWL_KVASS = register(NightHowlKvass::new,(byte)7);
    public static IBeer FROTHY_PINK_EGGNOG = register(FrothyPinkEggnog::new,(byte)8);

    /**
     * Beer Need to be registered for serialization
     */
    public static <T extends IBeer> T register(Supplier<T> beer, byte id) {
        if (CARD_MAPPINGS.containsValue(id)) {
            throw new RuntimeException("Beer Registry ID " + id + " has been occupied!" + beer.get().getTranslationKey() + " is trying to register with duplicated id!");
        } else {
            CARD_MAPPINGS.put(beer.get(), id);
        }
        return beer.get();
    }

    public static byte toByte(IBeer beer) {
        Byte b = CARD_MAPPINGS.get(beer);
        if (b == null) {
            throw new RuntimeException("Beer " + beer.getTranslationKey() + " hasn't been registered yet!");
        }
        return b;
    }

    public static IBeer fromByte(byte b) {
        IBeer beer = CARD_MAPPINGS.inverse().get(b);
        if (beer == null) {
            throw new RuntimeException("Retrieve Beer Info by id " + b + " wrongly! Beer Registry ID might been changed!");
        }
        return beer;
    }

    static class MinerPaleAle extends AbstractBear{

        @Override
        public String getTranslationKey() {
            return "miner_pale_ale";
        }

        @Override
        public int getNutrition(World world, ItemStack itemStack, LivingEntity drinker) {
            return 2;
        }

        @Override
        public List<EffectInstance> getEffect(World world, ItemStack itemStack, LivingEntity drinker, List<IFlavor> flavors) {
            return Lists.newArrayList(new EffectInstance(Effects.DIG_SPEED, 1200));
        }
    }

    static class BlazeStout extends AbstractBear{
        @Override
        public String getTranslationKey() {
            return "blaze_stout";
        }

        @Override
        public int getNutrition(World world, ItemStack itemStack, LivingEntity drinker) {
            return 2;
        }

        @Override
        public List<EffectInstance> getEffect(World world, ItemStack itemStack, LivingEntity drinker, List<IFlavor> flavors) {
            return Lists.newArrayList(new EffectInstance(Effects.FIRE_RESISTANCE, 1800));
        }
    }

    static class BlazeMilkStout extends AbstractBear{
        @Override
        public String getTranslationKey() {
            return "blaze_milk_stout";
        }

        @Override
        public int getNutrition(World world, ItemStack itemStack, LivingEntity drinker) {
            return 2;
        }

        @Override
        public List<EffectInstance> getEffect(World world, ItemStack itemStack, LivingEntity drinker, List<IFlavor> flavors) {
            return Lists.newArrayList(new EffectInstance(Effects.FIRE_RESISTANCE, 2400));
        }
    }

    static class AppleLambic extends AbstractBear{
        @Override
        public String getTranslationKey() {
            return "apple_lambic";
        }

        @Override
        public int getNutrition(World world, ItemStack itemStack, LivingEntity drinker) {
            return 3;
        }

        @Override
        public List<EffectInstance> getEffect(World world, ItemStack itemStack, LivingEntity drinker, List<IFlavor> flavors) {
            return Lists.newArrayList(new EffectInstance(Effects.REGENERATION, 300));
        }
    }

    static class SweetBerryKriek extends AbstractBear{
        @Override
        public String getTranslationKey() {
            return "sweet_berry_kriek";
        }

        @Override
        public int getNutrition(World world, ItemStack itemStack, LivingEntity drinker) {
            return 3;
        }

        @Override
        public List<EffectInstance> getEffect(World world, ItemStack itemStack, LivingEntity drinker, List<IFlavor> flavors) {
            return Lists.newArrayList(new EffectInstance(Effects.REGENERATION, 400));
        }
    }

    static class HaarsIceyPaleLager extends AbstractBear{
        @Override
        public String getTranslationKey() {
            return "haars_icey_pale_lager";
        }

        @Override
        public int getNutrition(World world, ItemStack itemStack, LivingEntity drinker) {
            return 1;
        }

        @Override
        public List<EffectInstance> getEffect(World world, ItemStack itemStack, LivingEntity drinker, List<IFlavor> flavors) {
            return Lists.newArrayList(new EffectInstance(EffectRegistry.DRUNK_FROST_WALKER.get(), 1200));
        }
    }

    static class PumpkinKvass extends AbstractBear{
        @Override
        public String getTranslationKey() {
            return "pumpkin_kvass";
        }

        @Override
        public int getNutrition(World world, ItemStack itemStack, LivingEntity drinker) {
            return 9;
        }
    }

    static class NightHowlKvass extends AbstractBear{
        private final static int BASE_NIGHT_VISION_TIME = 2400;

        @Override
        public String getTranslationKey() {
            return "night_howl_kvass";
        }

        @Override
        public int getNutrition(World world, ItemStack itemStack, LivingEntity drinker) {
            return 2;
        }

        @Override
        public List<EffectInstance> getEffect(World world, ItemStack itemStack, LivingEntity drinker, List<IFlavor> flavors) {
            return Lists.newArrayList(new EffectInstance(Effects.NIGHT_VISION, getNightVisionDurationByMoonPhase(moonPhase(world.dayTime()))));
        }

        @Override
        public void onDrink(World world, ItemStack stack, LivingEntity livingEntity) {
            super.onDrink(world, stack, livingEntity);
            world.playSound(null, livingEntity.blockPosition(), getRandomNightHowlSound(), SoundCategory.PLAYERS, 1.2f, 1f);
        }

        private static int getNightVisionDurationByMoonPhase(int moonPhase) {
            return BASE_NIGHT_VISION_TIME + (moonPhase == 0 ? Math.abs(moonPhase - 1 - 4) * 1200 : Math.abs(moonPhase - 4) * 1200);
        }

        private static SoundEvent getRandomNightHowlSound() {
            List<SoundEvent> available = ForgeRegistries.SOUND_EVENTS.getValues().stream().filter(soundEvent -> soundEvent.getRegistryName().toString().contains("night_howl_drinking_effect")).collect(Collectors.toList());
            return available.get(new Random().nextInt(available.size()));
        }

        private static int moonPhase(long worldTime) {
            return (int) (worldTime / 24000L % 8L + 8L) % 8;
        }
    }

    static class FrothyPinkEggnog extends AbstractBear{
        @Override
        public String getTranslationKey() {
            return "frothy_pink_eggnog";
        }

        @Override
        public int getNutrition(World world, ItemStack itemStack, LivingEntity drinker) {
            return 2;
        }

        //TODO need implementation of effect
    }

}
