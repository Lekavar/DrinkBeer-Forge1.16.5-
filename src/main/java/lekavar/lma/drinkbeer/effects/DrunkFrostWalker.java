package lekavar.lma.drinkbeer.effects;

import lekavar.lma.drinkbeer.registries.EffectRegistry;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class DrunkFrostWalker extends Effect {
    public DrunkFrostWalker() {
        super(EffectType.NEUTRAL, new Color(30, 144, 255, 255).getRGB());
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int p_76394_2_) {
        int drunkAmplifier = Drunk.getDrunkAmplifier(entity);
        if(!entity.level.isClientSide()){
            entity.addEffect(new EffectInstance(EffectRegistry.DRUNK.get(), Drunk.getDrunkDuratioin(drunkAmplifier),drunkAmplifier));
        }
        FrostWalkerEnchantment.onEntityMoved(entity, entity.level, new BlockPos(entity.position()), 1);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}