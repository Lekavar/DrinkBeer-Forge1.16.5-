package lekavar.lma.drinkbeer.items;

import lekavar.lma.drinkbeer.essentials.flavor.IFlavor;
import lekavar.lma.drinkbeer.essentials.spice.ISpice;
import lekavar.lma.drinkbeer.essentials.spice.ISpiceProvider;
import lekavar.lma.drinkbeer.misc.I18nKey;
import lekavar.lma.drinkbeer.misc.ModColor;
import lekavar.lma.drinkbeer.misc.ModGroup;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class SpiceItem extends BlockItem implements ISpiceProvider {
    ISpice spice;

    public SpiceItem(Block block, ISpice spice, int hunger, Supplier<EffectInstance> effectInstance) {
        super(block, new Properties().tab(ModGroup.GENERAL).food(new Food.Builder().alwaysEat().nutrition(hunger).effect(effectInstance, 1).build()));
        this.spice = spice;
    }

    public SpiceItem(Block block, ISpice spice, int hunger) {
        super(block, new Properties().tab(ModGroup.GENERAL).food(new Food.Builder().alwaysEat().nutrition(hunger).build()));
        this.spice = spice;
    }

    public SpiceItem(Block block, ISpice spice) {
        super(block, new Properties().tab(ModGroup.GENERAL).food(new Food.Builder().alwaysEat().build()));
        this.spice = spice;
    }

    @Override
    public ISpice getSpice() {
        return spice;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        //Spice title
        tooltip.add(new TranslationTextComponent("item.drinkbeer.spice.tooltip").setStyle(Style.EMPTY.withColor(ModColor.CURRY_DUST_YELLOW).withBold(true)));
        //Flavor and tooltip
        List<IFlavor> flavors = spice.getFlavor();
        for(IFlavor flavor:flavors){
            tooltip.add(new TranslationTextComponent(I18nKey.of(flavor)).setStyle(Style.EMPTY.withColor(ModColor.PAPRIKA_RED)));
            tooltip.add(new TranslationTextComponent(I18nKey.of(flavor) + ".description").setStyle(Style.EMPTY.withColor(ModColor.STAR_ANISE_BROWN)));
        }
    }
}
