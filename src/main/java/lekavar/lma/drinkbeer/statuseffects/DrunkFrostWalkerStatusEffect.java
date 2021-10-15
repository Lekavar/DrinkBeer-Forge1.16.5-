package lekavar.lma.drinkbeer.statuseffects;

import lekavar.lma.drinkbeer.registry.StatusEffectRegistry;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class DrunkFrostWalkerStatusEffect extends Effect {
    public DrunkFrostWalkerStatusEffect() {
        super(EffectType.NEUTRAL, new Color(30, 144, 255, 255).getRGB());
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int p_76394_2_) {
        int remainingTime = entity.getEffect(StatusEffectRegistry.DRUNK_FROST_WALKER.get()).getDuration();
        entity.addEffect(new EffectInstance(Effects.CONFUSION, remainingTime));
        FrostWalkerEnchantment.onEntityMoved(entity, entity.level, new BlockPos(entity.position()), 1);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}