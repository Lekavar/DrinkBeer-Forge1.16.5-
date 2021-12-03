package lekavar.lma.drinkbeer.items;

import lekavar.lma.drinkbeer.essentials.beer.IBeer;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;
import lekavar.lma.drinkbeer.utils.ModGroup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.*;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;

public class BeerMugItem extends BlockItem {
    private final static double MAX_PLACE_DISTANCE = 2.0D;
    private final IBeer beer;

    public BeerMugItem(Block block, IBeer beer) {
        super(block, new Item.Properties().tab(ModGroup.BEAR).stacksTo(16)
                .food(new Food.Builder().alwaysEat().build()));
        this.beer = beer;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEventRegistry.DRINKING_BEER.get();
    }

    @Override
    protected boolean canPlace(BlockItemUseContext blockItemUseContext, BlockState blockState) {
        if ((blockItemUseContext.getClickLocation().distanceTo(blockItemUseContext.getPlayer().position()) > MAX_PLACE_DISTANCE))
            return false;
        else {
            return super.canPlace(blockItemUseContext, blockState);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        // Translation Keys are legacy design, will be changed in 1.18
        String name = this.asItem().toString();
        tooltip.add(new TranslationTextComponent("item.drinkbeer." + name + ".tooltip").setStyle(Style.EMPTY.applyFormat(TextFormatting.BLUE)));
        String hunger = String.valueOf(beer.getNutrition(world, stack, Minecraft.getInstance().player));
        tooltip.add(new TranslationTextComponent("drinkbeer.restores_hunger").setStyle(Style.EMPTY.applyFormat(TextFormatting.BLUE)).append(hunger));
    }


    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity livingEntity) {
        beer.onDrink(world, stack, livingEntity);
        // Return empty mug
        if (livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).isCreative()) {
            ItemStack temp = stack.copy();
            livingEntity.eat(world, stack);
            return temp;
        } else {
            if (stack.getCount() == 1) {
                livingEntity.eat(world, stack);
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


}
