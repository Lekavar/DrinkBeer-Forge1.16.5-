package lekavar.lma.drinkbeer.items;

import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;
import lekavar.lma.drinkbeer.utils.ModGroup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.*;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BeerMugItem extends BlockItem {
    private final static double MAX_PLACE_DISTANCE = 2.0D;
    private final static int BASE_NIGHT_VISION_TIME = 2400;
    private final boolean hasExtraTooltip;

    public BeerMugItem(Block block, int nutrition, boolean hasExtraTooltip) {
        super(block, new Item.Properties().tab(ModGroup.BEAR).stacksTo(16)
                .food(new Food.Builder().nutrition(nutrition).alwaysEat().build()));
        this.hasExtraTooltip = hasExtraTooltip;
    }

    public BeerMugItem(Block block, int nutrition, Effect effect, int duration, boolean hasExtraTooltip) {
        super(block, new Item.Properties().tab(ModGroup.BEAR).stacksTo(16)
                .food(new Food.Builder().nutrition(nutrition).alwaysEat().effect(() -> new EffectInstance(effect, duration), 1).build()));
        this.hasExtraTooltip = hasExtraTooltip;
    }

    public BeerMugItem(Block block, int nutrition, Supplier<EffectInstance> effectIn, boolean hasExtraTooltip) {
        super(block, new Item.Properties().tab(ModGroup.BEAR).stacksTo(16)
                .food(new Food.Builder().nutrition(nutrition).alwaysEat().effect(effectIn, 1).build()));
        this.hasExtraTooltip = hasExtraTooltip;
    }


    @Override
    public SoundEvent getEatingSound() {
        return SoundEventRegistry.DRINKING_BEER.get();
    }

    @Override
    protected boolean canPlace(BlockItemUseContext p_195944_1_, BlockState p_195944_2_) {
        if ((p_195944_1_.getClickLocation().distanceTo(p_195944_1_.getPlayer().position()) > MAX_PLACE_DISTANCE))
            return false;
        else {
            return super.canPlace(p_195944_1_, p_195944_2_);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        String name = this.asItem().toString();
        if (hasEffectNoticeTooltip()) {
            tooltip.add(new TranslationTextComponent("item.drinkbeer." + name + ".tooltip").setStyle(Style.EMPTY.applyFormat(TextFormatting.BLUE)));
        }
        String hunger = String.valueOf(stack.getItem().getFoodProperties().getNutrition());
        tooltip.add(new TranslationTextComponent("drinkbeer.restores_hunger").setStyle(Style.EMPTY.applyFormat(TextFormatting.BLUE)).append(hunger));
    }

    private boolean hasEffectNoticeTooltip() {
        return this.hasExtraTooltip;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity livingEntity) {
        // Handle special drinking effect
        if (stack.getItem() == ItemRegistry.BEER_MUG_NIGHT_HOWL_KVASS.get()) {
            livingEntity.addEffect(new EffectInstance(Effects.NIGHT_VISION, getNightVisionTime(world.getMoonPhase())));
            if (!world.isClientSide()) {
                world.playSound(null, livingEntity.blockPosition(), getRandomNightHowlSound(), SoundCategory.PLAYERS, 1.2f, 1f);
            }
        }
        // Return empty mug
        if (livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).isCreative()) {
            return stack;
        } else {
            if (stack.getCount() == 1) {
                return new ItemStack(ItemRegistry.EMPTY_BEER_MUG.get());
            } else {
                ItemStack emptyMug = new ItemStack(ItemRegistry.EMPTY_BEER_MUG.get(), 1);
                if (livingEntity instanceof PlayerEntity) {
                    ItemHandlerHelper.giveItemToPlayer((PlayerEntity) livingEntity, emptyMug);
                } else {
                    InventoryHelper.dropItemStack(world, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), emptyMug);
                }
                return super.finishUsingItem(stack, world, livingEntity);
            }
        }
    }

    private int getNightVisionTime(int moonPhase) {
        return BASE_NIGHT_VISION_TIME + (moonPhase == 0 ? Math.abs(moonPhase - 1 - 4) * 1200 : Math.abs(moonPhase - 4) * 1200);
    }

    private SoundEvent getRandomNightHowlSound() {
        List<SoundEvent> available = ForgeRegistries.SOUND_EVENTS.getValues().stream().filter(soundEvent -> soundEvent.getRegistryName().toString().contains("night_howl_drinking_effect")).collect(Collectors.toList());
        return available.get(new Random().nextInt(available.size()));
    }
}
