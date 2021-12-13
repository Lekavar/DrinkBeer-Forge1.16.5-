package lekavar.lma.drinkbeer.blocks;

import lekavar.lma.drinkbeer.misc.MiscUtils;
import lekavar.lma.drinkbeer.registries.BlockRegistry;
import lekavar.lma.drinkbeer.registries.ParticleTypeRegistry;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
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
import net.minecraft.world.server.ServerWorld;

public class CallBellBlock extends Block {

    public final static VoxelShape SHAPE = Block.box(5.5f, 0, 5.5f, 10.5f, 4, 10.5f);

    public CallBellBlock() {
        super(Properties.of(Material.METAL).strength(1.0f));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, IBlockReader blockReader, BlockPos blockPos, ISelectionContext selectionContext) {
        return SHAPE;
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState1, IWorld iWorld, BlockPos blockPos, BlockPos blockPos1) {
        return direction == Direction.DOWN && !blockState.canSurvive(iWorld, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, blockState1, iWorld, blockPos, blockPos1);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isClientSide()) {
            double x = pos.getX() + 0.5D;
            double y = pos.getY() + 0.2D + player.getRandom().nextDouble() / 4;
            double z = pos.getZ() + 0.5D;
            if (state.getBlock() == BlockRegistry.IRON_CALL_BELL.get()) {
                world.playSound(null, pos, SoundEventRegistry.IRON_CALL_BELL_TINKLING.get(), SoundCategory.BLOCKS, 1.5f, 1f);
                MiscUtils.addParticle((ServerWorld) world,ParticleTypes.NOTE,x,y,z,0,0,0,0,1);
            } else if (state.getBlock() == BlockRegistry.GOLDEN_CALL_BELL.get()) {
                world.playSound(null, pos, SoundEventRegistry.GOLDEN_CALL_BELL_TINKLING.get(), SoundCategory.BLOCKS, 1.8f, 1f);
                MiscUtils.addParticle((ServerWorld) world,ParticleTypes.NOTE,x,y,z,0,0,0,0,1);
            }  else if (state.getBlock() == BlockRegistry.LEKAS_CALL_BELL.get()) {
                world.playSound(null, pos, SoundEventRegistry.LEKAS_CALL_BELL_TINKLING.get(), SoundCategory.BLOCKS, 0.9f, 1f);
                MiscUtils.addParticle((ServerWorld) world, ParticleTypeRegistry.CALL_BELL_TINKLE_PAW.get(),x,y,z,0,0,0,0,1);
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
        if (p_196260_2_.getBlockState(p_196260_3_.below()).getBlock() == Blocks.AIR) return false;
        return Block.canSupportCenter(p_196260_2_, p_196260_3_.below(), Direction.UP);
    }
}
