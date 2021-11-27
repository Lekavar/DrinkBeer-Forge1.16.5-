package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.effects.Drunk;
import lekavar.lma.drinkbeer.effects.DrunkFrostWalker;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectRegistry {
    public static final DeferredRegister<Effect> STATUS_EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, "drinkbeer");
    public static final RegistryObject<Effect> DRUNK_FROST_WALKER = STATUS_EFFECTS.register("drunk_frost_walker", DrunkFrostWalker::new);
    public static final RegistryObject<Effect> DRUNK = STATUS_EFFECTS.register("drunk", Drunk::new);

}
