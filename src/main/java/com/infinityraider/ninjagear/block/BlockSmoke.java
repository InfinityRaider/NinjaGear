package com.infinityraider.ninjagear.block;

import com.infinityraider.infinitylib.block.BlockBase;
import com.infinityraider.infinitylib.block.property.InfPropertyConfiguration;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.registry.BlockRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockSmoke extends BlockBase {
    public static final int MAX_AGE = 15;
    public static final Property<Integer> PROPERTY_AGE = IntegerProperty.create("age", 0, MAX_AGE);

    private static final InfPropertyConfiguration PROPERTIES = InfPropertyConfiguration.builder().add(PROPERTY_AGE, MAX_AGE).build();

    public static BlockState getBlockStateForDarkness(int darkness) {
        BlockSmoke block = (BlockSmoke) BlockRegistry.getInstance().getSmokeBlock();
        return block.defaultBlockState().setValue(PROPERTY_AGE, darkness);
    }

    public BlockSmoke() {
        super("smoke", Properties.of(Material.AIR)
                .randomTicks()
                .noOcclusion()
                .noCollission()
                .air()
                .noDrops()
                .isValidSpawn((a1, a2, a3, a4) -> false)
                .isViewBlocking((a1, a2, a3) -> true));
    }

    @Override
    protected InfPropertyConfiguration getPropertyConfiguration() {
        return PROPERTIES;
    }

    public int dispersionRate() {
        return NinjaGear.instance.getConfig().getSmokeDispersion() - 1;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        int age = state.getValue(PROPERTY_AGE) + random.nextInt(this.dispersionRate()) + 1;
        if (age <= MAX_AGE) {
            world.setBlock(pos, this.defaultBlockState().setValue(PROPERTY_AGE, age), 6);
        } else {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    @Deprecated
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return true;
    }

    @Override
    @Deprecated
    public boolean canBeReplaced(BlockState state, Fluid fluid) {
        return true;
    }

    @Override
    @Deprecated
    public int getLightBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return MAX_AGE - state.getValue(PROPERTY_AGE) + 1;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean isPossibleToRespawnInThis() {
        return false;
    }

    @Override
    public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
        return false;
    }

    @Override
    @Deprecated
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    @Deprecated
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    @Deprecated
    public VoxelShape getInteractionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }
}
