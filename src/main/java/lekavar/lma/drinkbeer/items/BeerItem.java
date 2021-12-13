package lekavar.lma.drinkbeer.items;

import lekavar.lma.drinkbeer.capability.Capabilities;
import lekavar.lma.drinkbeer.capability.beerinfo.BeerInfoProvider;
import lekavar.lma.drinkbeer.capability.beerinfo.IBeerInfo;
import lekavar.lma.drinkbeer.essentials.beer.IBeer;
import lekavar.lma.drinkbeer.essentials.flavor.IFlavor;
import lekavar.lma.drinkbeer.misc.I18nKey;
import lekavar.lma.drinkbeer.misc.ModColor;
import lekavar.lma.drinkbeer.misc.ModGroup;
import lekavar.lma.drinkbeer.registries.BlockRegistry;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;
import lekavar.lma.drinkbeer.blocks.tileentity.BeerTileEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.ItemHandlerHelper;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BeerItem extends Item implements IAnimatable {
    public AnimationFactory factory = new AnimationFactory(this);
    private final static double MAX_PLACE_DISTANCE = 2.0D;
    private final IBeer beer;

    public BeerItem(IBeer beer) {
        super(new Item.Properties().tab(ModGroup.BEAR).stacksTo(16).food(new Food.Builder().alwaysEat().build()));
        this.beer = beer;
    }

    @Override
    @Nonnull
    public ActionResultType useOn(ItemUseContext itemUseContext) {
        // This piece only handle the first cup of beer.
        // Stack up and retrieve beer is on BeerBlock#use
        if (itemUseContext.getClickedFace() == Direction.UP) {
            return place(new BlockItemUseContext(itemUseContext));
        } else {
            return ActionResultType.FAIL;
        }
    }

    public ActionResultType place(BlockItemUseContext context) {
        if (!context.canPlace()) {
            return ActionResultType.FAIL;
        } else {
            BlockState blockstate = BlockRegistry.BEER.get().defaultBlockState();
            // Try Place Block
            if (!this.placeBlock(context, blockstate)) {
                return ActionResultType.FAIL;
            } else {
                // Handle TileEntity Data Update
                BlockPos blockpos = context.getClickedPos();
                World world = context.getLevel();
                PlayerEntity playerentity = context.getPlayer();
                ItemStack itemstack = context.getItemInHand();
                BlockState blockstate1 = world.getBlockState(blockpos);
                Block block = blockstate1.getBlock();
                if (block == blockstate.getBlock()) {
                    // blockstate1 = this.updateBlockStateFromTag(blockpos, world, itemstack, blockstate1);
                    updateCustomBlockEntityTag(world, playerentity, blockpos, itemstack);
                    block.setPlacedBy(world, blockpos, blockstate1, playerentity, itemstack);
                    if (playerentity instanceof ServerPlayerEntity) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) playerentity, blockpos, itemstack);
                    }
                }

                SoundType soundtype = blockstate1.getSoundType(world, blockpos, context.getPlayer());
                world.playSound(playerentity, blockpos, this.getPlaceSound(blockstate1, world, blockpos, context.getPlayer()), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

                if (playerentity == null || !playerentity.abilities.instabuild) {
                    itemstack.shrink(1);
                }

                return ActionResultType.sidedSuccess(world.isClientSide);
            }
        }
    }

    public IBeer getBeer() {
        return beer;
    }

    protected boolean updateCustomBlockEntityTag(World level, @Nullable PlayerEntity player, BlockPos blockPos, ItemStack itemStack) {
        MinecraftServer minecraftserver = level.getServer();
        if (minecraftserver == null) {
            return false;
        }
        BeerTileEntity tileentity = (BeerTileEntity) level.getBlockEntity(blockPos);
        if (tileentity != null) {
            if (!level.isClientSide && tileentity.onlyOpCanSetNbt() && (player == null || !player.canUseGameMasterBlocks())) {
                return false;
            }

            IBeerInfo beerInfo = itemStack.getCapability(Capabilities.BEER_INFO_CAPABILITY, null)
                    .orElseThrow(() -> new RuntimeException("Things goes wrong! Server side cannot get BeerInfo from ItemStack " + itemStack + "when place beer"));

            tileentity.initializeData(beer, 1, beerInfo.getBaseFlavor(), beerInfo.getComboFlavor());

            tileentity.setChanged();
            return true;
        }
        return false;
    }

    // Drink Logic
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

    protected boolean placeBlock(BlockItemUseContext blockItemUseContext, BlockState blockState) {
        if (blockItemUseContext.getClickLocation().distanceTo(blockItemUseContext.getPlayer().position()) < MAX_PLACE_DISTANCE
                && blockState.getBlock().canSurvive(blockState, blockItemUseContext.getLevel(), blockItemUseContext.getClickedPos()))
            return blockItemUseContext.getLevel().setBlockAndUpdate(blockItemUseContext.getClickedPos(), blockState);
        else return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        // Translation Keys are legacy design, will be changed in 1.18
        IBeerInfo beerInfo = stack.getCapability(Capabilities.BEER_INFO_CAPABILITY, null)
                .orElseThrow(() -> new RuntimeException("Things goes wrong! Server side cannot get BeerInfo from ItemStack " + stack + "when render beer tooltip"));
        // Render is beer flavored
        if(beerInfo.isFlavoredBeer()){
            tooltip.add(new TranslationTextComponent("item.drinkbeer.flavored_beer.tooltip").setStyle(Style.EMPTY.withColor(ModColor.CURRY_DUST_YELLOW).withBold(true)));
        } else {
            tooltip.add(new TranslationTextComponent("item.drinkbeer.original_flavor.tooltip").setStyle(Style.EMPTY.withColor(ModColor.CURRY_DUST_YELLOW).withBold(true)));
        }

        // Render Bear Effect
        if(beer.hasExtraTooltip())
            tooltip.add(new TranslationTextComponent(I18nKey.of(beer) + ".description").setStyle(Style.EMPTY.withColor(ModColor.STAR_ANISE_BROWN)));
        String hunger = String.valueOf(beer.getNutrition(world, stack, Minecraft.getInstance().player));
        tooltip.add(new TranslationTextComponent("drinkbeer.restores_hunger").setStyle(Style.EMPTY.withColor(ModColor.STAR_ANISE_BROWN)).append(hunger));

        // Render Flavor
        if(beerInfo.isFlavoredBeer()){
            for(IFlavor flavor:beerInfo.getBaseFlavor()){
                tooltip.add(new TranslationTextComponent(I18nKey.of(flavor)).setStyle(Style.EMPTY.withColor(ModColor.PAPRIKA_RED))
                        .append(new StringTextComponent(" "))
                        .append(new TranslationTextComponent(I18nKey.of(flavor) + ".description").setStyle(Style.EMPTY.withColor(ModColor.STAR_ANISE_BROWN))));
            }
            for(IFlavor flavor:beerInfo.getComboFlavor()){
                tooltip.add(new TranslationTextComponent(I18nKey.of(flavor)).setStyle(Style.EMPTY.withColor(ModColor.TANGERINE_PEEL_ORANGE)));
            }
        }

    }

    protected SoundEvent getPlaceSound(BlockState state, World world, BlockPos pos, PlayerEntity entity) {
        return state.getSoundType(world, pos, entity).getPlaceSound();
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEventRegistry.DRINKING_BEER.get();
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new BeerInfoProvider();
    }


    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "beer_item_dummy_controller", 20, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
