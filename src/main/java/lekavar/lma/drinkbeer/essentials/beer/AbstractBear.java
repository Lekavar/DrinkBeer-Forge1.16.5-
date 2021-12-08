package lekavar.lma.drinkbeer.essentials.beer;

import com.google.common.collect.Maps;
import lekavar.lma.drinkbeer.capability.Capabilities;
import lekavar.lma.drinkbeer.capability.beerinfo.IBeerInfo;
import lekavar.lma.drinkbeer.essentials.flavor.IFlavor;
import lekavar.lma.drinkbeer.misc.ModDamage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractBear implements IBeer {
    @Override
    public float healthModify(World world, ItemStack itemStack, LivingEntity drinker) {
        return 0;
    }

    @Override
    public int drunkLevelModify(World world, ItemStack itemStack, LivingEntity drinker) {
        return 1;
    }

    @Override
    public List<EffectInstance> getEffect(World world, ItemStack itemStack, LivingEntity drinker, List<IFlavor> flavors) {
        return new ArrayList<>();
    }

    @Override
    public void onDrink(World world, ItemStack stack, LivingEntity livingEntity) {
        IBeerInfo beerInfo = stack.getCapability(Capabilities.BEER_INFO_CAPABILITY, null)
                .orElseThrow(() -> new RuntimeException("Things goes wrong! Server side cannot get BeerInfo from ItemStack " + stack + " when drink beer"));
        List<IFlavor> flavors = beerInfo.getBaseFlavor();
        List<IFlavor> comboFlavors = beerInfo.getComboFlavor();

        if (!world.isClientSide()) {
            int nutrition = getNutrition(world, stack, livingEntity);
            float healthModifier = healthModify(world, stack, livingEntity);
            int drunkLevelModifier = drunkLevelModify(world, stack, livingEntity);
            List<EffectInstance> effectInstances = getEffect(world, stack, livingEntity, flavors);
            Map<EffectInstance,EffectInstance> modifiedEffectInstanceMap = Maps.newHashMap(Maps.toMap(effectInstances,ei->ei));
            for (IFlavor flavor : flavors) {
                nutrition = flavor.modifyNutrition(world, stack, livingEntity, nutrition);
                healthModifier = flavor.modifyHealthModifier(world, stack, livingEntity, healthModifier);
                drunkLevelModifier = flavor.modifyDrunkLevelModifier(world, stack, livingEntity, drunkLevelModifier);
                modifiedEffectInstanceMap.replaceAll((i, v) -> flavor.modifyEffect(world, stack, livingEntity, modifiedEffectInstanceMap.get(i)));
            }
            for (IFlavor flavor : comboFlavors) {
                nutrition = flavor.modifyNutrition(world, stack, livingEntity, nutrition);
                healthModifier = flavor.modifyHealthModifier(world, stack, livingEntity, healthModifier);
                drunkLevelModifier = flavor.modifyDrunkLevelModifier(world, stack, livingEntity, drunkLevelModifier);
                modifiedEffectInstanceMap.replaceAll((i, v) -> flavor.modifyEffect(world, stack, livingEntity, modifiedEffectInstanceMap.get(i)));
            }

            //TODO Complete Drunk Level Modifying
            if (drunkLevelModifier > 0) {
                // - something
            } else if (drunkLevelModifier < 0) {
                // - somethingElse()
            }

            if (healthModifier > 0) {
                livingEntity.heal(healthModifier);
            } else if (healthModifier < 0) {
                livingEntity.hurt(ModDamage.causeAlcoholDamage(), healthModifier);
            }

            if (livingEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingEntity;
                player.getFoodData().eat(Math.max(nutrition, 0), 0);
            }

            for (EffectInstance effectInstance : effectInstances) {
                livingEntity.addEffect(modifiedEffectInstanceMap.get(effectInstance));
            }
        }

        for (IFlavor flavor : flavors) {
            flavor.onDrink(world, stack, livingEntity, flavors, comboFlavors);
        }
        for (IFlavor flavor : comboFlavors) {
            flavor.onDrink(world, stack, livingEntity, flavors, comboFlavors);
        }
    }
}
