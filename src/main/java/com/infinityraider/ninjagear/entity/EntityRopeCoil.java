package com.infinityraider.ninjagear.entity;

import com.infinityraider.infinitylib.entity.EntityThrowableBase;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.block.BlockRope;
import com.infinityraider.ninjagear.registry.BlockRegistry;
import com.infinityraider.ninjagear.registry.EntityRegistry;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.infinityraider.ninjagear.render.entity.RenderEntityRopeCoil;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntityRopeCoil extends EntityThrowableBase {
    //For client side spawning
    private EntityRopeCoil(EntityType<? extends EntityRopeCoil> type, World world) {
        super(type, world);
    }

    public EntityRopeCoil(World world, PlayerEntity thrower) {
        super(EntityRegistry.getInstance().entityRopeCoil, thrower, world);
        Vector3d vec = thrower.getLookVec();
        this.shoot(vec.getX(), vec.getY(), vec.getZ(), 2F, 0.2F);
    }

    @Override
    public PlayerEntity func_234616_v_() {  //func_234616_v_ ----> getThrower
        return (PlayerEntity) super.func_234616_v_();
    }

    @Override
    protected float getGravityVelocity() {
        return 0.1F;
    }

    @Override
    protected void onImpact(RayTraceResult impact) {
        Vector3d hitVec = impact.getHitVec();
        if(impact instanceof EntityRayTraceResult) {
            this.dropAsItem(hitVec.getX(), hitVec.getY(), hitVec.getZ());
        }
        if(impact instanceof BlockRayTraceResult) {
            World world = this.getEntityWorld();
            if (!world.isRemote) {
                BlockPos pos = this.getBlockPosFromImpact((BlockRayTraceResult) impact);
                BlockState state = this.getEntityWorld().getBlockState(pos);
                BlockState rope = BlockRegistry.getInstance().blockRope.getDefaultState();
                if (state.getBlock() instanceof BlockRope) {
                    this.addRemainingToInventory(this.extendRope(world, pos, NinjaGear.instance.getConfig().getRopeCoilLength()));
                } else if (rope.isValidPosition(world, pos)) {
                    this.addRemainingToInventory(this.placeRope(world, pos, NinjaGear.instance.getConfig().getRopeCoilLength()));
                } else {
                    this.dropAsItem(hitVec.getX(), hitVec.getY(), hitVec.getZ());
                }
            }
        }
    }

    public void dropAsItem(double x, double y, double z) {
        ItemEntity item = new ItemEntity(getEntityWorld(), x, y, z,
                new ItemStack(ItemRegistry.getInstance().itemRopeCoil));
        getEntityWorld().addEntity(item);
        this.setDead();
    }

    private BlockPos getBlockPosFromImpact(BlockRayTraceResult impact) {
        BlockState state = this.getEntityWorld().getBlockState(impact.getPos());
        if (state.getBlock() instanceof BlockRope) {
            return impact.getPos();
        }
        return impact.getPos().offset(impact.getFace());
    }

    private int placeRope(World world, BlockPos pos, int ropeCount) {
        BlockState rope = BlockRegistry.getInstance().blockRope.getDefaultState();
        world.setBlockState(pos, rope, 3);
        ropeCount = ropeCount - 1;
        if(ropeCount > 0) {
            BlockPos down = pos.down();
            if(rope.isValidPosition(world, down)) {
                return this.placeRope(world, down, ropeCount);
            } else {
                return ropeCount;
            }
        } else {
            return 0;
        }
    }

    private int extendRope(World world, BlockPos pos, int ropeCount) {
        BlockRope rope = (BlockRope) BlockRegistry.getInstance().blockRope;
        boolean flag = true;
        while(flag && ropeCount > 0) {
            flag = rope.extendRope(world, pos);
            if(flag) {
                ropeCount = ropeCount - 1;
            }
        }
        return ropeCount;
    }

    private void addRemainingToInventory(int remaining) {
        if(remaining > 0) {
            ItemStack stack = new ItemStack(ItemRegistry.getInstance().itemRope, remaining);
            if(func_234616_v_() != null && !func_234616_v_().inventory.addItemStackToInventory(stack)) {
                ItemEntity item = new ItemEntity(this.getEntityWorld(), this.getPosX(), this.getPosY(), this.getPosZ(), stack);
                this.getEntityWorld().addEntity(item);
            }
        }
        this.setDead();
    }

    @Override
    protected void registerData() {

    }

    @Override
    public void writeCustomEntityData(CompoundNBT tag) {

    }

    @Override
    public void readCustomEntityData(CompoundNBT tag) {

    }

    public static class SpawnFactory implements EntityType.IFactory<EntityRopeCoil> {
        private static final EntityRopeCoil.SpawnFactory INSTANCE = new EntityRopeCoil.SpawnFactory();

        public static EntityRopeCoil.SpawnFactory getInstance() {
            return INSTANCE;
        }

        private SpawnFactory() {}

        @Override
        public EntityRopeCoil create(EntityType<EntityRopeCoil> type, World world) {
            return new EntityRopeCoil(type, world);
        }
    }

    public static class RenderFactory implements IRenderFactory<EntityRopeCoil> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @OnlyIn(Dist.CLIENT)
        public EntityRenderer<? super EntityRopeCoil> createRenderFor(EntityRendererManager manager) {
            return new RenderEntityRopeCoil(manager);
        }
    }
}
