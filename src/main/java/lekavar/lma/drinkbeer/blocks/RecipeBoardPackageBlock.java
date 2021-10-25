package lekavar.lma.drinkbeer.blocks;

import lekavar.lma.drinkbeer.registries.BlockRegistry;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeBoardPackageBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public final static VoxelShape N_S_SHAPE = Block.box(0, 1, 1, 16, 10, 15);
    public final static VoxelShape E_W_SHAPE = Block.box(1, 0, 0, 15, 10, 16);

    public RecipeBoardPackageBlock() {
        super(Properties.of(Material.METAL).strength(1.0f).noOcclusion());
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isClientSide()) {
            world.playSound(null, pos, SoundEventRegistry.UNPACKING_PACKAGE.get(), SoundCategory.BLOCKS, 0.8f, 1f);
            getRecipeBoardDrop().forEach(itemStack -> InventoryHelper.dropItemStack(world,pos.getX(),pos.getY(),pos.getZ(),itemStack));
            world.setBlock(pos, Blocks.AIR.defaultBlockState(),1);
        }
        return ActionResultType.sidedSuccess(world.isClientSide);
    }

    private List<ItemStack> getRecipeBoardDrop(){
        return ForgeRegistries.BLOCKS.getValues().stream().filter(block -> {
            if(block instanceof RecipeBoardBlock){
                return ((RecipeBoardBlock)block).isAcquirableViaPackage();
            }
            else return false;
        }).map(block -> block.asItem().getDefaultInstance()).collect(Collectors.toList());
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
            case SOUTH:
                return N_S_SHAPE;
            default:
                return E_W_SHAPE;
        }
    }
}
