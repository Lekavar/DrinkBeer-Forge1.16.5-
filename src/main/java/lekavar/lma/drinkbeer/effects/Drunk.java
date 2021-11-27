package lekavar.lma.drinkbeer.effects;

import lekavar.lma.drinkbeer.registries.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;

import java.awt.*;

public class Drunk extends Effect {
    public final static int MAX_DRUNK_AMPLIFIER = 4;
    private final static int BASE_DURATION = 1200;
    private static final int[] drunkDurations = {3600, 3000, 2400, 1800, 1200};
    private static final int[] nauseaDurations = {160, 160, 200, 300, 1200};
    private static final int[] slownessDurations = {0, 80, 160, 200, 600};
    private static final int[] harmulStatusEffectsIntervals = {200, 160, 200, 300, 20};

    public Drunk() {
        super(EffectType.HARMFUL, new Color(255, 222, 173, 255).getRGB());
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(!livingEntity.level.isClientSide()){
            int time = livingEntity.getEffect(EffectRegistry.DRUNK.get()).getDuration();
            giveHarmfulStatusEffects(livingEntity, amplifier, time);

            //Always give harmful status effects
            //Give next lower Drunk status effect when duration's out
            if (time == 1) {
                decreaseDrunkStatusEffefct(livingEntity, amplifier);
            }
        }

    }

    private void giveHarmfulStatusEffects(LivingEntity entity, int amplifier, int time) {
        if (amplifier >= MAX_DRUNK_AMPLIFIER) {
            int duration = entity.getEffect(EffectRegistry.DRUNK.get()).getDuration();
            entity.addEffect(new EffectInstance(Effects.CONFUSION, duration, 0, false, false));
            entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, duration, MAX_DRUNK_AMPLIFIER - 1, false, false));
        } else if (time % harmulStatusEffectsIntervals[amplifier] == 0) {
            int nauseaDuration = nauseaDurations[amplifier];
            int slownessDuration = slownessDurations[amplifier];
            entity.addEffect(new EffectInstance(Effects.CONFUSION, nauseaDuration, 0, false, false));
            if (amplifier > 0) {
                entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, slownessDuration, amplifier - 1, false, false));
            }
        }
    }

    private void decreaseDrunkStatusEffefct(LivingEntity entity, int amplifier) {
        if (!entity.level.isClientSide) {
            EffectInstance nextDrunkStatusEffect = getDecreasedDrunkStatusEffect(amplifier);
            if (nextDrunkStatusEffect != null) {
                entity.addEffect(nextDrunkStatusEffect);
            }
        }
    }

    private EffectInstance getDecreasedDrunkStatusEffect(int currentAmplifier) {
        int nextDrunkAmplifier = currentAmplifier - 1;
        if (nextDrunkAmplifier < 0) {
            return null;
        } else {
            return new EffectInstance(EffectRegistry.DRUNK.get(), getDrunkDuratioin(nextDrunkAmplifier), nextDrunkAmplifier);
        }
    }

    public static int getDrunkAmplifier(LivingEntity user) {
        EffectInstance effectInstance = user.getEffect(EffectRegistry.DRUNK.get());
        int drunkAmplifier = effectInstance == null ? -1 : effectInstance.getAmplifier();
        return drunkAmplifier < MAX_DRUNK_AMPLIFIER ? drunkAmplifier + 1 : drunkAmplifier;
    }

    public static int getDrunkDuratioin(int amplifier) {
        if(amplifier<5){
            return drunkDurations[amplifier];
        } else
            return BASE_DURATION;
    }

    @Override
    public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
        return true;
    }

}
