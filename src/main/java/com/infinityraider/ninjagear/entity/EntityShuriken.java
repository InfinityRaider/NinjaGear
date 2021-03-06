package com.infinityraider.ninjagear.entity;

import com.infinityraider.infinitylib.entity.EntityThrowableBase;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.reference.Names;
import com.infinityraider.ninjagear.registry.EntityRegistry;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.infinityraider.ninjagear.render.entity.RenderEntityShuriken;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nonnull;

public class EntityShuriken extends EntityThrowableBase {
    private float crit;
    private int timer;
    private Vector3d direction;

    //For client side spawning
    private EntityShuriken(EntityType<? extends EntityShuriken> type, World world) {
        super(type, world);
    }

    public EntityShuriken(World world, LivingEntity thrower, boolean crit) {
        super(EntityRegistry.getInstance().entityShuriken, thrower, world);
        this.direction = thrower.getLookVec();
        this.shoot(direction.getX(), direction.getY(), direction.getZ(), 4F, 0.2F);
        this.crit = crit ? NinjaGear.instance.getConfig().getCitMultiplier() : 1;
        this.timer = 0;
    }

    public Vector3d getDirection() {
        return direction;
    }

    @Override
     protected float getGravityVelocity() {
        return 0.3F;
    }

    @Override
    protected void registerData() {

    }

    @Override
    public void tick() {
        super.tick();
        if(!this.getEntityWorld().isRemote) {
            timer = timer + 1;
            if(timer > 5000) {
                this.dropAsItem(this.getPosX(), this.getPosY(), this.getPosZ());
            }
        }
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult impact) {
        if (impact instanceof EntityRayTraceResult) {
            Entity hit = ((EntityRayTraceResult) impact).getEntity();
            if (hit == this.getShooter()) {
                return;
            }
            if (hit != null) {
                DamageSource damage = DamageSource.causeThrownDamage(hit, this);
                if (!hit.isInvulnerableTo(damage)) {
                    float crit = this.crit;
                    hit.attackEntityFrom(damage, 3.0F + NinjaGear.instance.getConfig().getShurikenDamage() * crit);
                }
            }
        }
        World world = this.getEntityWorld();
        Vector3d hitVec = impact.getHitVec();
        if(!world.isRemote && hitVec != null) {
            this.dropAsItem(hitVec.getX(), hitVec.getY(), hitVec.getZ());
        }
    }

    public void dropAsItem(double x, double y, double z) {
        ItemEntity item = new ItemEntity(getEntityWorld(), x, y, z,
                new ItemStack(ItemRegistry.getInstance().itemShuriken));
        this.getEntityWorld().addEntity(item);
        this.setDead();
    }

    @Override
    public void writeCustomEntityData(CompoundNBT tag) {
        tag.putFloat(Names.NBT.CRIT, this.crit);
        tag.putDouble(Names.NBT.X, this.direction.getX());
        tag.putDouble(Names.NBT.Y, this.direction.getY());
        tag.putDouble(Names.NBT.Z, this.direction.getZ());
    }

    @Override
    public void readCustomEntityData(CompoundNBT tag) {
        this.crit = tag.getFloat(Names.NBT.CRIT);
        this.direction = new Vector3d(tag.getDouble(Names.NBT.X), tag.getDouble(Names.NBT.Y), tag.getDouble(Names.NBT.Z));
    }

    public static class SpawnFactory implements EntityType.IFactory<EntityShuriken> {
        private static final SpawnFactory INSTANCE = new SpawnFactory();

        public static SpawnFactory getInstance() {
            return INSTANCE;
        }

        private SpawnFactory() {}

        @Override
        public EntityShuriken create(EntityType<EntityShuriken> type, World world) {
            return new EntityShuriken(type, world);
        }
    }

    public static class RenderFactory implements IRenderFactory<EntityShuriken> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @OnlyIn(Dist.CLIENT)
        public EntityRenderer<? super EntityShuriken> createRenderFor(EntityRendererManager manager) {
            return new RenderEntityShuriken(manager);
        }
    }
}
