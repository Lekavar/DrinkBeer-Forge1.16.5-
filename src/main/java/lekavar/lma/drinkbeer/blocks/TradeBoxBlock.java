package lekavar.lma.drinkbeer.blocks;

import lekavar.lma.drinkbeer.blocks.tileentity.BeerBarrelTileEntity;
import lekavar.lma.drinkbeer.blocks.tileentity.TradeBoxTileEntity;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class TradeBoxBlock extends HorizontalBlock {
    private static final VoxelShape SHAPE = Block.box(0, 0.0, 0, 16, 16, 16);

    public TradeBoxBlock() {
        super(Properties.of(Material.WOOD).strength(2.0f).noOcclusion());
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, IBlockReader blockReader, BlockPos blockPos, ISelectionContext selectionContext) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isClientSide) {
            world.playSound(null, pos, SoundEventRegistry.OPENING_TRADE_BOX.get(), SoundCategory.BLOCKS, 0.6f, 1f);
            TradeBoxTileEntity tradeBoxTileEntity = (TradeBoxTileEntity) world.getBlockEntity(pos);
            NetworkHooks.openGui((ServerPlayerEntity) player, tradeBoxTileEntity, (data)->{
                data.writeBlockPos(pos);
                data.writeUtf(tradeBoxTileEntity.getResidentNameKey());
                data.writeUtf(tradeBoxTileEntity.getLocationNameKey());
            });
        }
        return ActionResultType.sidedSuccess(world.isClientSide);
    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TradeBoxTileEntity();
    }
}
