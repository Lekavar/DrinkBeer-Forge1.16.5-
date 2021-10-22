package lekavar.lma.drinkbeer.items;

import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;
import lekavar.lma.drinkbeer.utils.ModGroup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class BeerMugItem extends BlockItem {
    private final static double MAX_PLACE_DISTANCE = 2.0D;

    public BeerMugItem(Block block, int nutrition) {
        super(block, new Item.Properties().tab(ModGroup.BEAR).stacksTo(16)
                .food(new Food.Builder().nutrition(nutrition).alwaysEat().build()));
    }

    public BeerMugItem(Block block, int nutrition, Effect effect, int duration) {
        super(block, new Item.Properties().tab(ModGroup.BEAR).stacksTo(16)
                .food(new Food.Builder().nutrition(nutrition).alwaysEat().effect(() -> new EffectInstance(effect, duration), 1).build()));
    }

    public BeerMugItem(Block block, int nutrition, Supplier<EffectInstance> effectIn) {
        super(block, new Item.Properties().tab(ModGroup.BEAR).stacksTo(16)
                .food(new Food.Builder().nutrition(nutrition).alwaysEat().effect(effectIn,1).build()));
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
        if (hasEffectNoticeTooltip(stack.getItem())) {
            tooltip.add(new TranslationTextComponent("item.drinkbeer." + name + ".tooltip").setStyle(Style.EMPTY.applyFormat(TextFormatting.BLUE)));
        }
        String hunger = String.valueOf(stack.getItem().getFoodProperties().getNutrition());
        tooltip.add(new TranslationTextComponent("drinkbeer.restores_hunger").setStyle(Style.EMPTY.applyFormat(TextFormatting.BLUE)).append(hunger));
    }

    private boolean hasEffectNoticeTooltip(Item item){
        return item != ItemRegistry.BEER_MUG_PUMPKIN_KVASS.get() /*&& item != ItemRegistry.BEER_MUG_FROTHY_PINK_EGGNOG.get()*/;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity player) {
        ItemStack itemStack = super.finishUsingItem(stack, world, player);
        if (player instanceof PlayerEntity && ((PlayerEntity) player).isCreative()) {
            return itemStack;
        } else {
            ItemStack emptyMug = new ItemStack(ItemRegistry.EMPTY_BEER_MUG.get(), 1);
            ItemHandlerHelper.giveItemToPlayer((PlayerEntity) player, emptyMug);
            return itemStack;
        }
    }
}
