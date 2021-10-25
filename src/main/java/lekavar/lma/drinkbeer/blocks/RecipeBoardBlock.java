package lekavar.lma.drinkbeer.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class RecipeBoardBlock extends Block {
    private final boolean acquirableViaPackage;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public final static VoxelShape NORTH_SHAPE = Block.box(1, 0, 0, 15, 16, 1.5);
    public final static VoxelShape SOUTH_SHAPE = Block.box(1, 0, 14.5, 15, 16, 16);
    public final static VoxelShape EAST_SHAPE = Block.box(14.5, 0, 1, 16, 16, 15);
    public final static VoxelShape WEST_SHAPE = Block.box(0, 0, 1, 1.5, 16, 15);

    public RecipeBoardBlock(boolean acquirableViaPackage) {
        super(Properties.of(Material.WOOD).strength(1.0f).noOcclusion());
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
        this.acquirableViaPackage = acquirableViaPackage;
    }

    public boolean isAcquirableViaPackage() {
        return this.acquirableViaPackage;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        Direction dir = p_220053_1_.getValue(FACING);
        switch (dir) {
            case NORTH:
                return NORTH_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case EAST:
                return EAST_SHAPE;
            default:
                return WEST_SHAPE;
        }
    }
}
