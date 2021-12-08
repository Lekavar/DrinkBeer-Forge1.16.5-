package lekavar.lma.drinkbeer.blocks;

import lekavar.lma.drinkbeer.gui.container.BartendingTableContainer;
import lekavar.lma.drinkbeer.items.BeerItem;
import lekavar.lma.drinkbeer.registries.ContainerTypeRegistry;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BartendingTableBlock extends HorizontalBlock {
    private static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("block.drinkbeer.bartending_table");
    private static final VoxelShape SHAPE = Block.box(0, 0.0, 0, 16, 16, 16);
    public static final BooleanProperty OPENED = BooleanProperty.create("opened");
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 1, 2);

    public BartendingTableBlock() {
        super(Properties.of(Material.WOOD).strength(2.0f).noOcclusion());
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(OPENED,true).setValue(TYPE,1));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, IBlockReader blockReader, BlockPos blockPos, ISelectionContext selectionContext) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(OPENED);
        builder.add(TYPE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.setBlockAndUpdate(pos,blockState.setValue(TYPE,world.getRandom().nextInt(2)+1));
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isClientSide) {
            ItemStack itemStack = player.getItemInHand(hand);
            if(itemStack.getItem() instanceof BeerItem){
                // Open Bartending GUI
                world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                // Actually this gui this just simply ... a gui
                NetworkHooks.openGui((ServerPlayerEntity) player, new SimpleNamedContainerProvider(new IContainerProvider() {
                            @Nullable
                            @Override
                            public Container createMenu(int id, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity playerEntity) {
                                return new BartendingTableContainer(id,playerInventory,IWorldPosCallable.create(world,pos));
                            }
                        }, CONTAINER_TITLE),
                        pos);

            } else {
                // Switch Bartending Table Model
                world.playSound(null, pos, state.getValue(OPENED) ? SoundEventRegistry.CLOSING_BARTENDING_TABLE.get():SoundEventRegistry.OPENING_BARTENDING_TABLE.get(), SoundCategory.BLOCKS, 1f, 1f);
                world.setBlockAndUpdate(pos,state.setValue(OPENED,!state.getValue(OPENED)));
            }

        }
        return ActionResultType.sidedSuccess(world.isClientSide);
    }
}
