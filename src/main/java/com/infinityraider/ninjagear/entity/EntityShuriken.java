package com.infinityraider.ninjagear.entity;

import com.infinityraider.infinitylib.entity.EntityThrowableBase;
import com.infinityraider.infinitylib.entity.IEntityRenderSupplier;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.reference.Names;
import com.infinityraider.ninjagear.registry.EntityRegistry;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.infinityraider.ninjagear.render.entity.RenderEntityShuriken;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class EntityShuriken extends EntityThrowableBase {
    private float crit;
    private int timer;
    private Vec3 direction;

    //For client side spawning
    private EntityShuriken(EntityType<? extends EntityShuriken> type, Level world) {
        super(type, world);
    }

    public EntityShuriken(LivingEntity thrower, boolean crit) {
        super(EntityRegistry.entityShuriken, thrower);
        this.direction = thrower.getLookAngle();
        this.shoot(direction.x(), direction.y(), direction.z(), 4F, 0.2F);
        this.crit = crit ? NinjaGear.instance.getConfig().getCitMultiplier() : 1;
        this.timer = 0;
    }

    public Vec3 direction() {
        return direction;
    }

    @Override
     protected float getGravity() {
        return 0.3F;
    }

    @Override
    public void tick() {
        super.tick();
        if(!this.getLevel().isClientSide()) {
            timer = timer + 1;
            if(timer > 5000) {
                this.dropAsItem(this.getX(), this.getY(), this.getZ());
            }
        }
    }

    @Override
    protected void onHit(@Nonnull HitResult impact) {
        if (impact instanceof EntityHitResult) {
            Entity hit = ((EntityHitResult) impact).getEntity();
            if (hit == this.getOwner()) {
                return;
            }
            if (hit != null) {
                DamageSource damage = DamageSource.thrown(hit, this);
                if (!hit.isInvulnerableTo(damage)) {
                    float crit = this.crit;
                    hit.hurt(damage, 3.0F + NinjaGear.instance.getConfig().getShurikenDamage() * crit);
                }
            }
        }
        Level world = this.getLevel();
        Vec3 hitVec = impact.getLocation();
        if(!world.isClientSide() && hitVec != null) {
            this.dropAsItem(hitVec.x(), hitVec.y(), hitVec.z());
        }
    }

    public void dropAsItem(double x, double y, double z) {
        ItemEntity item = new ItemEntity(getLevel(), x, y, z,
                new ItemStack(ItemRegistry.itemShuriken));
        this.getLevel().addFreshEntity(item);
        this.kill();
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void writeCustomEntityData(CompoundTag tag) {
        tag.putFloat(Names.NBT.CRIT, this.crit);
        tag.putDouble(Names.NBT.X, this.direction.x());
        tag.putDouble(Names.NBT.Y, this.direction.y());
        tag.putDouble(Names.NBT.Z, this.direction.z());
    }

    @Override
    public void readCustomEntityData(CompoundTag tag) {
        this.crit = tag.getFloat(Names.NBT.CRIT);
        this.direction = new Vec3(tag.getDouble(Names.NBT.X), tag.getDouble(Names.NBT.Y), tag.getDouble(Names.NBT.Z));
    }

    public static class SpawnFactory implements EntityType.EntityFactory<EntityShuriken> {
        private static final SpawnFactory INSTANCE = new SpawnFactory();

        public static SpawnFactory getInstance() {
            return INSTANCE;
        }

        private SpawnFactory() {}

        @Override
        public EntityShuriken create(EntityType<EntityShuriken> type, Level world) {
            return new EntityShuriken(type, world);
        }
    }

    public static class RenderFactory implements IEntityRenderSupplier<EntityShuriken> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        public Supplier<EntityRendererProvider<EntityShuriken>> supplyRenderer() {
            return () -> RenderEntityShuriken::new;
        }
    }
}
