package lekavar.lma.drinkbeer.items;

import lekavar.lma.drinkbeer.registry.ItemRegistry;
import lekavar.lma.drinkbeer.registry.SoundEventRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BeerMugItem extends BlockItem {
    public BeerMugItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public net.minecraft.util.SoundEvent getEatingSound() {
        return SoundEventRegistry.DRINKING_BEER.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        String name = this.asItem().toString();
        if (this.asItem() != ItemRegistry.beerMugPumpkinKvass.get()) {
            tooltip.add(new TranslationTextComponent("item.drinkbeer." + name + ".tooltip").setStyle(Style.EMPTY.applyFormat(TextFormatting.BLUE)));
        }
        String hunger = String.valueOf(asItem().getFoodProperties().getNutrition());
        tooltip.add(new TranslationTextComponent("drinkbeer.restores_hunger").setStyle(Style.EMPTY.applyFormat(TextFormatting.BLUE)).append(hunger));
    }
}
