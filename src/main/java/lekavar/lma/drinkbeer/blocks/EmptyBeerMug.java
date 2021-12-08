package lekavar.lma.drinkbeer.blocks;

import lekavar.lma.drinkbeer.blocks.legacy.BeerMugBlock;
import lekavar.lma.drinkbeer.registries.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class EmptyBeerMug extends Block {
    public static final IntegerProperty AMOUNT = IntegerProperty.create("amount", 1, 3);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected static final VoxelShape[] SHAPE_BY_AMOUNT = new VoxelShape[]{
            Block.box(0, 0, 0, 16, 16, 16),
            Block.box(4, 0, 4, 12, 6, 12),
            Block.box(2, 0, 2, 14, 6, 14),
            Block.box(1, 0, 1, 15, 6, 15)
    };

    public EmptyBeerMug() {
        super(Properties.of(Material.WOOD).strength(1.0f).noOcclusion());
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE_BY_AMOUNT[p_220053_1_.getValue(AMOUNT)];
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AMOUNT, FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState1, IWorld iWorld, BlockPos blockPos, BlockPos blockPos1) {
        return direction == Direction.DOWN && !blockState.canSurvive(iWorld, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, blockState1, iWorld, blockPos, blockPos1);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        // Placing Bear
        if (itemStack.getItem().asItem() == state.getBlock().asItem()) {
            if (world.isClientSide()) {
                return ActionResultType.SUCCESS;
            } else {
                int amount = state.getValue(AMOUNT);
                int mugInHandCount = player.getItemInHand(hand).getCount();
                boolean isCreative = player.isCreative();
                switch (amount) {
                    case 1:
                        world.setBlockAndUpdate(pos, state.getBlock().defaultBlockState().setValue(AMOUNT, 2).setValue(FACING, state.getValue(FACING)));
                        if (!isCreative) {
                            player.getItemInHand(hand).setCount(mugInHandCount - 1);
                        }
                        world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                        return ActionResultType.CONSUME;
                    case 2:
                        world.setBlockAndUpdate(pos, state.getBlock().defaultBlockState().setValue(AMOUNT, 3).setValue(FACING, state.getValue(FACING)));
                        if (!isCreative) {
                            player.getItemInHand(hand).setCount(mugInHandCount - 1);
                        }
                        world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                        return ActionResultType.CONSUME;
                }
            }
        }
        // Retrieve Beer
        else if (itemStack.isEmpty()) {
            if (world.isClientSide()) {
                return ActionResultType.SUCCESS;
            } else {
                ItemStack takeBackBeer = state.getBlock().asItem().getDefaultInstance();
                ItemHandlerHelper.giveItemToPlayer(player, takeBackBeer);
                int amount = state.getValue(AMOUNT);
                switch (amount) {
                    case 3:
                    case 2:
                        world.setBlockAndUpdate(pos, state.getBlock().defaultBlockState().setValue(AMOUNT, amount - 1).setValue(FACING, state.getValue(FACING)));
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
        return ActionResultType.PASS;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.DESTROY;
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
}
