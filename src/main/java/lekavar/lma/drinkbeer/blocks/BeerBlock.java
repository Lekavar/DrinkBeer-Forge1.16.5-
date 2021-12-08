package lekavar.lma.drinkbeer.blocks;

import com.google.common.collect.Lists;
import lekavar.lma.drinkbeer.blocks.legacy.BeerMugBlock;
import lekavar.lma.drinkbeer.capability.Capabilities;
import lekavar.lma.drinkbeer.capability.beerinfo.IBeerInfo;
import lekavar.lma.drinkbeer.items.BeerItem;
import lekavar.lma.drinkbeer.registries.BlockRegistry;
import lekavar.lma.drinkbeer.blocks.tileentity.BeerBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public class BeerBlock extends Block {

    protected static final VoxelShape[] SHAPE_BY_AMOUNT = new VoxelShape[]{
            Block.box(0, 0, 0, 16, 16, 16),
            Block.box(4, 0, 4, 12, 6, 12),
            Block.box(2, 0, 2, 14, 6, 14),
            Block.box(1, 0, 1, 15, 6, 15)
    };

    public BeerBlock() {
        super(Properties.of(Material.WOOD).strength(1.0f).noOcclusion());
    }

    @Override
    public VoxelShape getShape(BlockState blockState, IBlockReader blockReader, BlockPos blockPos, ISelectionContext selectionContext) {
        BeerBlockTileEntity beerBlockTileEntity = ((BeerBlockTileEntity) blockReader.getBlockEntity(blockPos));
        if (beerBlockTileEntity == null)
            return SHAPE_BY_AMOUNT[0];
        else
            return SHAPE_BY_AMOUNT[beerBlockTileEntity.getCount()];
    }

    @Override
    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        return p_196271_2_ == Direction.DOWN && !p_196271_1_.canSurvive(p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BeerBlockTileEntity();
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        BeerBlockTileEntity tileentity = (BeerBlockTileEntity) world.getBlockEntity(pos);
            ItemStack itemStack = tileentity.getBeerType().getItem().get().getDefaultInstance();
            IBeerInfo beerInfo = itemStack.getCapability(Capabilities.BEER_INFO_CAPABILITY, null)
                    .orElseThrow(() -> new RuntimeException("Things goes wrong! Server side cannot get BeerInfo from ItemStack " + itemStack + " when get beer by middle mouse bottom"));
            beerInfo.setFlavoredBeer(tileentity.isFlavored());
            if (tileentity.isFlavored()) {
                beerInfo.setBaseFlavor(tileentity.getBaseFlavor());
                beerInfo.setComboFlavor(tileentity.getComboFlavor());
            }
            itemStack.setCount(tileentity.getCount());
            return itemStack;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState blockState) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    //FIXME
    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        BeerBlockTileEntity tileEntity = (BeerBlockTileEntity) world.getBlockEntity(pos);
        // Stackup Bear
        if (itemStack.getItem() instanceof BeerItem) {
            if (world.isClientSide()) {
                return ActionResultType.SUCCESS;
            } else {
                IBeerInfo beerInfo = itemStack.getCapability(Capabilities.BEER_INFO_CAPABILITY, null)
                        .orElseThrow(() -> new RuntimeException("Things goes wrong! Server side cannot get BeerInfo from ItemStack " + itemStack + " when stack beer"));

                if ( !tileEntity.isFlavored() && tileEntity.matchBeerItem(((BeerItem) itemStack.getItem()).getBeer(), beerInfo)) {
                    int amount = tileEntity.getCount();
                    switch (amount) {
                        case 1:
                            tileEntity.updateData(2);
                            tileEntity.setChanged();
                            world.sendBlockUpdated(pos, state, state, Constants.BlockFlags.DEFAULT_AND_RERENDER);
                            if (!player.isCreative()) {
                                player.getItemInHand(hand).shrink(1);
                            }
                            world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                            return ActionResultType.CONSUME;
                        case 2:
                            tileEntity.updateData(3);
                            tileEntity.setChanged();
                            world.sendBlockUpdated(pos, state, state, Constants.BlockFlags.DEFAULT_AND_RERENDER);
                            if (!player.isCreative()) {
                                player.getItemInHand(hand).shrink(1);
                            }
                            world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                            return ActionResultType.CONSUME;
                    }
                }
            }
        }

        // Retrieve Beer
        else if (itemStack.isEmpty()) {
            if (world.isClientSide()) {
                return ActionResultType.SUCCESS;
            } else {
                ItemStack retrievedItemStack = tileEntity.getBeerType().getItem().get().getDefaultInstance();
                IBeerInfo beerInfo = retrievedItemStack.getCapability(Capabilities.BEER_INFO_CAPABILITY, null)
                        .orElseThrow(() -> new RuntimeException("Things goes wrong! Server side cannot get BeerInfo from ItemStack " + itemStack + " when retrieve beer"));
                beerInfo.setFlavoredBeer(tileEntity.isFlavored());
                if (tileEntity.isFlavored()) {
                    beerInfo.setBaseFlavor(tileEntity.getBaseFlavor());
                    beerInfo.setComboFlavor(tileEntity.getComboFlavor());
                }
                ItemHandlerHelper.giveItemToPlayer(player, retrievedItemStack);
                int amount = tileEntity.getCount();
                switch (amount) {
                    case 3:
                    case 2:
                        tileEntity.updateData(amount - 1);
                        tileEntity.setChanged();
                        world.sendBlockUpdated(pos, state, state, Constants.BlockFlags.DEFAULT_AND_RERENDER);
                        if (!world.isClientSide()) {
                            world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundCategory.AMBIENT, 0.5f, 0.5f);
                        }
                        return ActionResultType.CONSUME;
                    case 1:
                        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                        if (!world.isClientSide()) {
                            world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundCategory.AMBIENT, 0.5f, 0.5f);
                        }
                        return ActionResultType.CONSUME;
                }
            }
        }
        return ActionResultType.CONSUME;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.DESTROY;
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder builder) {
        Entity entity = builder.getOptionalParameter(LootParameters.THIS_ENTITY);
        if (entity instanceof PlayerEntity) {
            TileEntity tileentity = builder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
            if (tileentity instanceof BeerBlockTileEntity) {
                BeerBlockTileEntity beerBlockTileEntity = (BeerBlockTileEntity) tileentity;
                ItemStack itemStack = beerBlockTileEntity.getBeerType().getItem().get().getDefaultInstance();
                IBeerInfo beerInfo = itemStack.getCapability(Capabilities.BEER_INFO_CAPABILITY, null)
                        .orElseThrow(() -> new RuntimeException("Things goes wrong! Server side cannot get BeerInfo from ItemStack " + itemStack + " when get drop from placed beer."));
                beerInfo.setFlavoredBeer(beerBlockTileEntity.isFlavored());
                if (beerBlockTileEntity.isFlavored()) {
                    beerInfo.setBaseFlavor(beerBlockTileEntity.getBaseFlavor());
                    beerInfo.setComboFlavor(beerBlockTileEntity.getComboFlavor());
                }
                itemStack.setCount(beerBlockTileEntity.getCount());
                return Lists.newArrayList(itemStack);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public boolean canSurvive(BlockState blockState, IWorldReader iWorldReader, BlockPos blockPos) {
        if (iWorldReader.getBlockState(blockPos.below()).getBlock().equals(BlockRegistry.BEER.get())) return false;
        else if (iWorldReader.getBlockState(blockPos.below()).getBlock().equals(BlockRegistry.EMPTY_BEER_MUG.get()))
            return false;
            // Handle Legacy Part
        else if (iWorldReader.getBlockState(blockPos.below()).getBlock() instanceof BeerMugBlock) return false;
        return Block.canSupportCenter(iWorldReader, blockPos.below(), Direction.UP);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getShadeBrightness(BlockState p_220080_1_, IBlockReader p_220080_2_, BlockPos p_220080_3_) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, IBlockReader blockReader, BlockPos blockPos) {
        return true;
    }
}
