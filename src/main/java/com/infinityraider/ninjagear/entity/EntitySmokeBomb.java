package com.infinityraider.ninjagear.entity;

import com.infinityraider.infinitylib.entity.EntityThrowableBase;
import com.infinityraider.infinitylib.entity.IEntityRenderSupplier;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.block.BlockSmoke;
import com.infinityraider.ninjagear.registry.EffectRegistry;
import com.infinityraider.ninjagear.registry.EntityRegistry;
import com.infinityraider.ninjagear.render.entity.RenderEntitySmokeBomb;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.function.Supplier;

public class EntitySmokeBomb extends EntityThrowableBase {
    //For client side spawning
    private EntitySmokeBomb(EntityType<? extends EntitySmokeBomb> type, Level world) {
        super(type, world);
    }

    public EntitySmokeBomb(LivingEntity thrower, float velocity) {
        super(EntityRegistry.getInstance().getSmokeBombEntityType(), thrower);
        Vec3 vec = thrower.getLookAngle();
        this.shoot(vec.x(), vec.y(), vec.z(), velocity, 0.2F);
    }

    @Override
    protected float getGravity() {
        return 0.1F;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onHit(HitResult impact) {
        Level world = this.getLevel();
        BlockPos pos = this.getBlockPosFromImpact(impact);
        this.applySmokeBuff(world, pos);
        this.createSmokeCloud(world, pos);
    }

    private BlockPos getBlockPosFromImpact(HitResult impact) {
        if(impact instanceof EntityHitResult) {
            EntityHitResult entityImpact = (EntityHitResult) impact;
            Entity hit = entityImpact.getEntity();
            if (hit != null) {
                return hit.blockPosition();
            }
        } else if(impact instanceof BlockHitResult) {
            BlockHitResult blockImpact = (BlockHitResult) impact;
            return blockImpact.getBlockPos().relative(blockImpact.getDirection());
        }
        return new BlockPos(impact.getLocation());
    }

    private void applySmokeBuff(Level world, BlockPos pos) {
        int r = NinjaGear.instance.getConfig().getSmokeRadius();
        world.getEntities(null, new AABB(pos.offset(-r, -r, -r), pos.offset(r, r, r))).stream()
                .filter(entity -> entity != null && (entity instanceof LivingEntity))
                .forEach(entity -> {
                    LivingEntity living = (LivingEntity) entity;
                    if (living.hasEffect(EffectRegistry.getInstance().getNinjaRevealedEffect())) {
                        living.removeEffect(EffectRegistry.getInstance().getNinjaRevealedEffect());
                    }
                    int duration = NinjaGear.instance.getConfig().getSmokeBuffDuration();
                    if(duration > 0) {
                        living.addEffect(new MobEffectInstance(EffectRegistry.getInstance().getNinjaSmokedEffect(), duration));
                    }
                });
    }

    @SuppressWarnings("deprecation")
    private void createSmokeCloud(Level world, BlockPos pos) {
        int r = NinjaGear.instance.getConfig().getSmokeRadius();
        for(int x = -r; x <= r; x++) {
            for(int y = -r; y <= r; y++) {
                for(int z = -r; z <= r; z++) {
                    int radius = x*x + y*y + z*z;
                    if(radius > r*r) {
                        continue;
                    }
                    BlockPos posAt = pos.offset(x, y, z);
                    BlockState state = world.getBlockState(posAt);
                    if(state.getMaterial() == Material.AIR) {
                        if(world.isClientSide()) {
                            this.spawnSmokeParticle(world, posAt);
                        } else if(NinjaGear.instance.getConfig().placeSmokeBlocks()){
                            world.setBlock(posAt, BlockSmoke.getBlockStateForDarkness(this.getDarknessValue(radius, world.getRandom())), 3);
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
    private void spawnSmokeParticle(Level world, BlockPos pos) {
        if (NinjaGear.instance.getConfig().disableSmoke()) {
            return;
        }
        world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5D, 0, 0, 0);
    }

    @Override
    public void writeCustomEntityData(CompoundTag tag) {

    }

    @Override
    public void readCustomEntityData(CompoundTag tag) {

    }

    @Override
    protected void defineSynchedData() {

    }

    public static class SpawnFactory implements EntityType.EntityFactory<EntitySmokeBomb> {
        private static final EntitySmokeBomb.SpawnFactory INSTANCE = new EntitySmokeBomb.SpawnFactory();

        public static EntitySmokeBomb.SpawnFactory getInstance() {
            return INSTANCE;
        }

        private SpawnFactory() {}

        @Override
        public EntitySmokeBomb create(EntityType<EntitySmokeBomb> type, Level world) {
            return new EntitySmokeBomb(type, world);
        }
    }

    public static class RenderFactory implements IEntityRenderSupplier<EntitySmokeBomb> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        public Supplier<EntityRendererProvider<EntitySmokeBomb>> supplyRenderer() {
            return () -> RenderEntitySmokeBomb::new;
        }
    }
}
