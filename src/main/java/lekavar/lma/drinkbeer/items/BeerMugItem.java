package lekavar.lma.drinkbeer.items;

import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;
import lekavar.lma.drinkbeer.utils.ModGroup;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
    public BeerMugItem(Block block, int nutrition) {
        super(block, new Item.Properties().tab(ModGroup.BEAR)
                .food(new Food.Builder().nutrition(nutrition).alwaysEat().build()));
    }

    public BeerMugItem(Block block, int nutrition, Effect effect, int duration) {
        super(block, new Item.Properties().tab(ModGroup.BEAR)
                .food(new Food.Builder().nutrition(nutrition).alwaysEat().effect(() -> new EffectInstance(effect, duration), 1).build()));
    }

    public BeerMugItem(Block block, int nutrition, Supplier<EffectInstance> effectIn) {
        super(block, new Item.Properties().tab(ModGroup.BEAR)
                .food(new Food.Builder().nutrition(nutrition).alwaysEat().effect(effectIn,1).build()));
    }


    @Override
    public SoundEvent getEatingSound() {
        return SoundEventRegistry.DRINKING_BEER.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        String name = this.asItem().toString();
        if (this.asItem() != ItemRegistry.BEER_MUG_PUMPKIN_KVASS.get()) {
            tooltip.add(new TranslationTextComponent("item.drinkbeer." + name + ".tooltip").setStyle(Style.EMPTY.applyFormat(TextFormatting.BLUE)));
        }
        String hunger = String.valueOf(asItem().getFoodProperties().getNutrition());
        tooltip.add(new TranslationTextComponent("drinkbeer.restores_hunger").setStyle(Style.EMPTY.applyFormat(TextFormatting.BLUE)).append(hunger));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity player) {
        ItemStack itemStack = super.finishUsingItem(stack, world, player);
        if (player instanceof PlayerEntity && ((PlayerEntity) player).isCreative()) {
            return itemStack;
        } else {
            ItemStack emptyMugItemStack = new ItemStack(ItemRegistry.EMPTY_BEER_MUG.get(), 1);
            ItemHandlerHelper.giveItemToPlayer((PlayerEntity) player, emptyMugItemStack);
            return itemStack;
        }
    }
}
