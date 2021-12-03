package lekavar.lma.drinkbeer.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ModDamage {
    public static DamageSource causeAlcoholDamage() {
        return new SimpleDeathMessageDamageSource("drinkbeer.alcohol").bypassArmor();
    }

    public static class SimpleDeathMessageDamageSource extends DamageSource {
        public SimpleDeathMessageDamageSource(String damageTypeIn) {
            super(damageTypeIn);
        }

        @Override
        public ITextComponent getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
            String s = "death.attack." + msgId ;
            return new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName());
        }
    }
}
