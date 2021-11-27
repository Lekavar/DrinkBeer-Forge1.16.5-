package lekavar.lma.drinkbeer.utils;

import lekavar.lma.drinkbeer.registries.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public enum BearType {
    MINER_PALE_ALE(2, true, (stack, world, livingEntity) -> {
        if (!world.isClientSide()) {
            livingEntity.addEffect(new EffectInstance(Effects.DIG_SPEED, 1200));
        }
    }),
    BLAZE_STOUT(2, true, (stack, world, livingEntity) -> {
        if (!world.isClientSide()) {
            livingEntity.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 1800));
        }
    }),
    BLAZE_MILK_STOUT(2, true, (stack, world, livingEntity) -> {
        if (!world.isClientSide()) {
            livingEntity.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 2400));
        }
    }),
    APPLE_LAMBIC(3, true, (stack, world, livingEntity) -> {
        if (!world.isClientSide()) {
            livingEntity.addEffect(new EffectInstance(Effects.REGENERATION, 300));
        }
    }),
    SWEET_BERRY_KRIEK(3, true, (stack, world, livingEntity) -> {
        if (!world.isClientSide()) {
            livingEntity.addEffect(new EffectInstance(Effects.REGENERATION, 400));
        }
    }),
    HAARS_ICEY_PALE_LAGER(1, true, (stack, world, livingEntity) -> {
        if (!world.isClientSide()) {
            livingEntity.addEffect(new EffectInstance(EffectRegistry.DRUNK_FROST_WALKER.get(), 1200));
        }
    }),
    PUMPKIN_KVASS(9, false, null),
    NIGHT_HOWL_KVASS(4, true, (stack, world, livingEntity) -> {
        if (!world.isClientSide()) {
            livingEntity.addEffect(new EffectInstance(Effects.NIGHT_VISION, MiscUtils.getNightVisionDurationByMoonPhase(MiscUtils.moonPhase(world.dayTime()))));
            world.playSound(null, livingEntity.blockPosition(), MiscUtils.getRandomNightHowlSound(), SoundCategory.PLAYERS, 1.2f, 1f);
        }
    }),
    FROTHY_PINK_EGGNOG(2, true, (stack, world, livingEntity) -> {
        //TODO need implement
    });

    BearType(int nutrition, boolean hasExtraTooltip, @Nullable IDrinkEffectHandler drinkEffectHandler) {
        this.nutrition = nutrition;
        this.hasExtraTooltip = hasExtraTooltip;
        this.drinkEffectHandler = drinkEffectHandler;
    }

    public final int nutrition;
    public final boolean hasExtraTooltip;
    public final IDrinkEffectHandler drinkEffectHandler;


    public interface IDrinkEffectHandler {
        void handle(ItemStack stack, World world, LivingEntity livingEntity);
    }

}
