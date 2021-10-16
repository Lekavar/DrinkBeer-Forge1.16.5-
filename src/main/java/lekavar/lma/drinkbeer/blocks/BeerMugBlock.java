package lekavar.lma.drinkbeer.blocks;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
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
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class BeerMugBlock extends Block {
    public static final IntegerProperty AMOUNT = IntegerProperty.create("amount", 1, 3);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected static final VoxelShape[] SHAPE_BY_AMOUNT = new VoxelShape[]{
            Block.box(0, 0, 0, 16, 16, 16),
            Block.box(4, 0, 4, 12, 6, 12),
            Block.box(2, 0, 2, 14, 6, 14),
            Block.box(1, 0, 1, 15, 6, 15)};

    public BeerMugBlock() {
        super(Properties.of(Material.WOOD).strength(1.0f));
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE_BY_AMOUNT[p_220053_1_.getValue(this.getAmountProperty())];
    }

    public IntegerProperty getAmountProperty() {
        return this.AMOUNT;
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
    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        return p_196271_2_ == Direction.DOWN && !p_196271_1_.canSurvive(p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        // Placing Bear
        if (itemStack.getItem().asItem() == state.getBlock().asItem()) {
            int amount = state.getValue(AMOUNT);
            int mugInHandCount = player.getItemInHand(hand).getCount();
            boolean isCreative = player.isCreative();
            switch (amount) {
                case 1:
                    world.setBlock(pos, state.getBlock().defaultBlockState().setValue(AMOUNT, 2).setValue(FACING, state.getValue(FACING)), 0);
                    if (!isCreative) {
                        player.getItemInHand(hand).setCount(mugInHandCount - 1);
                    }
                    if (!world.isClientSide()) {
                        world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    }
                    return ActionResultType.SUCCESS;
                case 2:
                    world.setBlock(pos, state.getBlock().defaultBlockState().setValue(AMOUNT, 3).setValue(FACING, state.getValue(FACING)), 0);
                    if (!isCreative) {
                        player.getItemInHand(hand).setCount(mugInHandCount - 1);
                    }
                    if (!world.isClientSide()) {
                        world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    }
                    return ActionResultType.SUCCESS;
                default:
                    return ActionResultType.FAIL;
            }
        }
        // Retrieve Beer
        else if (itemStack.isEmpty()){
            if (!world.isClientSide()){
                ItemStack takeBackBeer = state.getBlock().asItem().getDefaultInstance();
                ItemHandlerHelper.giveItemToPlayer(player,takeBackBeer);
            }
            int amount = state.getValue(AMOUNT);
            switch (amount) {
                case 3:
                case 2:
                    world.setBlock(pos, state.getBlock().defaultBlockState().setValue(AMOUNT, amount-1).setValue(FACING, state.getValue(FACING)), 0);
                    if (!world.isClientSide()) {
                        world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundCategory.AMBIENT, 0.5f, 0.5f);
                    }
                    return ActionResultType.SUCCESS;
                case 1:
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
                    if (!world.isClientSide()) {
                        world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundCategory.AMBIENT, 0.5f, 0.5f);
                    }
                    return ActionResultType.SUCCESS;
                default:
                    return ActionResultType.FAIL;
            }
        }
        else {
            return ActionResultType.FAIL;
        }
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_149656_1_) {
        return PushReaction.DESTROY;
    }

    // canPlaceAt
    @Override
    public boolean canSurvive(BlockState p_196260_1_, IWorldReader p_196260_2_, BlockPos p_196260_3_) {
        if(p_196260_2_.getBlockState(p_196260_3_.below()).getBlock() instanceof BeerMugBlock) return false;
        return Block.canSupportCenter(p_196260_2_,p_196260_3_.below(),Direction.UP);
    }
}
