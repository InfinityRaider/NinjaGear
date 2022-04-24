package com.infinityraider.ninjagear.entity;

import com.infinityraider.infinitylib.entity.EntityThrowableBase;
import com.infinityraider.infinitylib.entity.IEntityRenderSupplier;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.block.BlockRope;
import com.infinityraider.ninjagear.registry.BlockRegistry;
import com.infinityraider.ninjagear.registry.EntityRegistry;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.infinityraider.ninjagear.render.entity.RenderEntityRopeCoil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public class EntityRopeCoil extends EntityThrowableBase {
    //For client side spawning
    private EntityRopeCoil(EntityType<? extends EntityRopeCoil> type, Level world) {
        super(type, world);
    }

    public EntityRopeCoil(Player thrower) {
        super(EntityRegistry.getInstance().getRopeCoilEntityType(), thrower);
        Vec3 vec = thrower.getLookAngle();
        this.shoot(vec.x(), vec.y(), vec.z(), 2F, 0.2F);
    }

    @Override
    public Player getOwner() {  //func_234616_v_ ----> getThrower
        return (Player) super.getOwner();
    }

    @Override
    protected float getGravity() {
        return 0.1F;
    }

    @Override
    protected void onHit(HitResult impact) {
        Vec3 hitVec = impact.getLocation();
        if(impact instanceof EntityHitResult) {
            this.dropAsItem(hitVec.x(), hitVec.y(), hitVec.z());
        }
        if(impact instanceof BlockHitResult) {
            Level world = this.getLevel();
            if (!world.isClientSide()) {
                BlockPos pos = this.getBlockPosFromImpact((BlockHitResult) impact);
                BlockState state = world.getBlockState(pos);
                BlockState rope = BlockRegistry.getInstance().getRopeBlock().defaultBlockState();
                if (state.getBlock() instanceof BlockRope) {
                    BlockRope ropeBlock = (BlockRope) state.getBlock();
                    this.addRemainingToInventory(ropeBlock.extendRope(world, pos, NinjaGear.instance.getConfig().getRopeCoilLength()));
                } else if (rope.canSurvive(world, pos)) {
                    this.addRemainingToInventory(this.placeRope(world, pos, NinjaGear.instance.getConfig().getRopeCoilLength()));
                } else {
                    this.dropAsItem(hitVec.x(), hitVec.y(), hitVec.z());
                }
            }
        }
    }

    public void dropAsItem(double x, double y, double z) {
        ItemEntity item = new ItemEntity(this.getLevel(), x, y, z,
                new ItemStack(ItemRegistry.getInstance().getRopeItem()));
        this.getLevel().addFreshEntity(item);
        this.kill();
    }

    private BlockPos getBlockPosFromImpact(BlockHitResult impact) {
        BlockState state = this.getLevel().getBlockState(impact.getBlockPos());
        if (state.getBlock() instanceof BlockRope) {
            return impact.getBlockPos();
        }
        return impact.getBlockPos().relative(impact.getDirection());
    }

    private int placeRope(Level world, BlockPos pos, int ropeCount) {
        BlockRope ropeBlock = (BlockRope) BlockRegistry.getInstance().getRopeBlock();
        BlockState rope = ropeBlock.getStateForPlacement(world, pos);
        world.setBlock(pos, rope, 3);
        return ropeBlock.extendRope(world, pos, ropeCount - 1);
    }

    private void addRemainingToInventory(int remaining) {
        if(remaining > 0) {
            ItemStack stack = new ItemStack(ItemRegistry.getInstance().getRopeItem(), remaining);
            if(this.getOwner() != null && !this.getOwner().getInventory().add(stack)) {
                ItemEntity item = new ItemEntity(this.getLevel(), this.getX(), this.getY(), this.getZ(), stack);
                this.getLevel().addFreshEntity(item);
            }
        }
        this.kill();
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void writeCustomEntityData(CompoundTag tag) {

    }

    @Override
    public void readCustomEntityData(CompoundTag tag) {

    }

    public static class SpawnFactory implements EntityType.EntityFactory<EntityRopeCoil> {
        private static final EntityRopeCoil.SpawnFactory INSTANCE = new EntityRopeCoil.SpawnFactory();

        public static EntityRopeCoil.SpawnFactory getInstance() {
            return INSTANCE;
        }

        private SpawnFactory() {}

        @Override
        public EntityRopeCoil create(EntityType<EntityRopeCoil> type, Level world) {
            return new EntityRopeCoil(type, world);
        }
    }

    public static class RenderFactory implements IEntityRenderSupplier<EntityRopeCoil> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @OnlyIn(Dist.CLIENT)
        public Supplier<EntityRendererProvider<EntityRopeCoil>> supplyRenderer() {
            return () -> RenderEntityRopeCoil::new;
        }
    }
}
