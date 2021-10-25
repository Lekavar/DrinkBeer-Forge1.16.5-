package lekavar.lma.drinkbeer.blocks;

import lekavar.lma.drinkbeer.registries.BlockRegistry;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class CallBellBlock extends Block {

    public final static VoxelShape SHAPE = Block.box(5.5f, 0, 5.5f, 10.5f, 4, 10.5f);

    public CallBellBlock() {
        super(Properties.of(Material.METAL).strength(1.0f));
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        return p_196271_2_ == Direction.DOWN && !p_196271_1_.canSurvive(p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isClientSide()) {
            if (state.getBlock() == BlockRegistry.IRON_CALL_BELL.get()) {
                world.playSound(null, pos, SoundEventRegistry.IRON_CALL_BELL_TINKLING.get(), SoundCategory.BLOCKS, 1.2f, 1f);
            }else if(state.getBlock() == BlockRegistry.GOLDEN_CALL_BELL.get()) {
                world.playSound(null, pos, SoundEventRegistry.GOLDEN_CALL_BELL_TINKLING.get(), SoundCategory.BLOCKS, 1.2f, 1f);
            }
        }
        return ActionResultType.sidedSuccess(world.isClientSide);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_149656_1_) {
        return PushReaction.DESTROY;
    }

    @Override
    public boolean canSurvive(BlockState p_196260_1_, IWorldReader p_196260_2_, BlockPos p_196260_3_) {
        if(p_196260_2_.getBlockState(p_196260_3_.below()).getBlock() == Blocks.AIR) return false;
        return Block.canSupportCenter(p_196260_2_,p_196260_3_.below(), Direction.UP);
    }
}
