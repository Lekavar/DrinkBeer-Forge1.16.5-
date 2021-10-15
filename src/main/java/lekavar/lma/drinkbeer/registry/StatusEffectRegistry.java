package lekavar.lma.drinkbeer.registry;

import lekavar.lma.drinkbeer.statuseffects.DrunkFrostWalkerStatusEffect;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class StatusEffectRegistry {
    public static final DeferredRegister<Effect> STATUS_EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, "drinkbeer");
    public static final RegistryObject<Effect> DRUNK_FROST_WALKER = STATUS_EFFECTS.register ("drunk_frost_walker", DrunkFrostWalkerStatusEffect::new);
}
