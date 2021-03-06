package com.infinityraider.ninjagear.entity;

import com.infinityraider.infinitylib.entity.EntityThrowableBase;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.block.BlockSmoke;
import com.infinityraider.ninjagear.registry.EffectRegistry;
import com.infinityraider.ninjagear.registry.EntityRegistry;
import com.infinityraider.ninjagear.render.entity.RenderEntitySmokeBomb;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class EntitySmokeBomb extends EntityThrowableBase {
    //For client side spawning
    private EntitySmokeBomb(EntityType<? extends EntitySmokeBomb> type, World world) {
        super(type, world);
    }

    public EntitySmokeBomb(World world, LivingEntity thrower, float velocity) {
        super(EntityRegistry.getInstance().entitySmokeBomb, thrower, world);
        Vector3d vec = thrower.getLookVec();
        this.shoot(vec.getX(), vec.getY(), vec.getZ(), velocity, 0.2F);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.1F;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onImpact(RayTraceResult impact) {
        World world = this.getEntityWorld();
        BlockPos pos = this.getBlockPosFromImpact(impact);
        this.applySmokeBuff(world, pos);
        this.createSmokeCloud(world, pos);
    }

    private BlockPos getBlockPosFromImpact(RayTraceResult impact) {
        if(impact instanceof EntityRayTraceResult) {
            EntityRayTraceResult entityImpact = (EntityRayTraceResult) impact;
            Entity hit = entityImpact.getEntity();
            if (hit != null) {
                return hit.getPosition();
            }
        } else if(impact instanceof BlockRayTraceResult) {
            BlockRayTraceResult blockImpact = (BlockRayTraceResult) impact;
            return blockImpact.getPos().offset(blockImpact.getFace());
        }
        return new BlockPos(impact.getHitVec());
    }

    private void applySmokeBuff(World world, BlockPos pos) {
        int r = NinjaGear.instance.getConfig().getSmokeRadius();
        world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.add(-r, -r, -r), pos.add(r, r, r))).stream()
                .filter(entity -> entity != null && (entity instanceof LivingEntity))
                .forEach(entity -> {
                    LivingEntity living = (LivingEntity) entity;
                    if (living.isPotionActive(EffectRegistry.getInstance().effectNinjaRevealed)) {
                        living.removePotionEffect(EffectRegistry.getInstance().effectNinjaRevealed);
                    }
                    int duration = NinjaGear.instance.getConfig().getSmokeBuffDuration();
                    if(duration > 0) {
                        living.addPotionEffect(new EffectInstance(EffectRegistry.getInstance().effectNinjaSmoked, duration));
                    }
                });
    }

    @SuppressWarnings("deprecation")
    private void createSmokeCloud(World world, BlockPos pos) {
        int r = NinjaGear.instance.getConfig().getSmokeRadius();
        for(int x = -r; x <= r; x++) {
            for(int y = -r; y <= r; y++) {
                for(int z = -r; z <= r; z++) {
                    int radius = x*x + y*y + z*z;
                    if(radius > r*r) {
                        continue;
                    }
                    BlockPos posAt = pos.add(x, y, z);
                    BlockState state = world.getBlockState(posAt);
                    if(state.getMaterial() == Material.AIR) {
                        if(world.isRemote) {
                            this.spawnSmokeParticle(world, posAt);
                        } else if(NinjaGear.instance.getConfig().placeSmokeBlocks()){
                            world.setBlockState(posAt, BlockSmoke.getBlockStateForDarkness(this.getDarknessValue(radius, world.rand)), 3);
                        }
                    }
                }
            }
        }
    }

    private int getDarknessValue(int radius, Random rand) {
        if(radius <= 5) {
            return rand.nextInt(2);
        }
        if(radius <= 10) {
            return 1 + rand.nextInt(2);
        }
        if(radius <= 15) {
            return 2 + rand.nextInt(2);
        }
        if(radius <= 20) {
            return 2 + rand.nextInt(3);
        }
        return 3 + rand.nextInt(2);
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnSmokeParticle(World world, BlockPos pos) {
        if (NinjaGear.instance.getConfig().disableSmoke()) {
            return;
        }
        world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5D, 0, 0, 0);
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

    public static class SpawnFactory implements EntityType.IFactory<EntitySmokeBomb> {
        private static final EntitySmokeBomb.SpawnFactory INSTANCE = new EntitySmokeBomb.SpawnFactory();

        public static EntitySmokeBomb.SpawnFactory getInstance() {
            return INSTANCE;
        }

        private SpawnFactory() {}

        @Override
        public EntitySmokeBomb create(EntityType<EntitySmokeBomb> type, World world) {
            return new EntitySmokeBomb(type, world);
        }
    }

    public static class RenderFactory implements IRenderFactory<EntitySmokeBomb> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @OnlyIn(Dist.CLIENT)
        public EntityRenderer<? super EntitySmokeBomb> createRenderFor(EntityRendererManager manager) {
            return new RenderEntitySmokeBomb(manager);
        }
    }
}
