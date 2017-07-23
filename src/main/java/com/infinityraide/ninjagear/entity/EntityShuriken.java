package com.infinityraider.ninjagear.entity;

import com.infinityraider.ninjagear.handler.ConfigurationHandler;
import com.infinityraider.ninjagear.reference.Names;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.infinityraider.ninjagear.render.entity.RenderEntityShuriken;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityShuriken extends EntityThrowable implements IEntityAdditionalSpawnData {
    private float crit;
    private int timer;
    private Vec3d direction;

    @SuppressWarnings("unused")
    public EntityShuriken(World world) {
        super(world);
    }

    public EntityShuriken(World world, EntityLivingBase thrower, boolean crit) {
        super(world, thrower);
        this.direction = thrower.getLookVec();
        this.setThrowableHeading(direction.xCoord, direction.yCoord, direction.zCoord, 4F, 0.2F);
        this.crit = crit ? ConfigurationHandler.getInstance().critMultiplier : 1;
        this.timer = 0;
    }

    public Vec3d getDirection() {
        return direction;
    }

    @Override
     protected float getGravityVelocity() {
        return 0.3F;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!this.getEntityWorld().isRemote) {
            timer = timer + 1;
            if(timer > 5000) {
                this.dropAsItem(this.posX, this.posY, this.posZ);
            }
        }
    }

    @Override
    protected void onImpact(RayTraceResult impact) {
        Entity hit = impact.entityHit;
        if(hit == this.getThrower()) {
            return;
        }
        if(hit != null) {
            DamageSource damage = DamageSource.causeThrownDamage(this.getThrower(), this);
            if(!hit.isEntityInvulnerable(damage)) {
                float crit = this.crit;
                hit.attackEntityFrom(damage, 3.0F + ConfigurationHandler.getInstance().shurikenDamage*crit);
            }
        }
        World world = this.getEntityWorld();
        if(!world.isRemote && impact.hitVec != null) {
            this.dropAsItem(impact.hitVec.xCoord, impact.hitVec.yCoord, impact.hitVec.zCoord);
        }
    }

    public void dropAsItem(double x, double y, double z) {
        EntityItem item = new EntityItem(getEntityWorld(), x, y, z,
                new ItemStack(ItemRegistry.getInstance().itemShuriken));
        getEntityWorld().spawnEntityInWorld(item);
        this.setDead();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setFloat(Names.NBT.CRIT, this.crit);
        tag.setDouble(Names.NBT.X, this.direction.xCoord);
        tag.setDouble(Names.NBT.Y, this.direction.yCoord);
        tag.setDouble(Names.NBT.Z, this.direction.zCoord);
    }

    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.crit = tag.getFloat(Names.NBT.CRIT);
        this.direction = new Vec3d(tag.getDouble(Names.NBT.X), tag.getDouble(Names.NBT.Y), tag.getDouble(Names.NBT.Z));
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeFloat(this.crit);
        buffer.writeDouble(this.direction.xCoord);
        buffer.writeDouble(this.direction.yCoord);
        buffer.writeDouble(this.direction.zCoord);
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        this.crit = buffer.readFloat();
        this.direction =new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }


    public static class RenderFactory implements IRenderFactory<EntityShuriken> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public Render<? super EntityShuriken> createRenderFor(RenderManager manager) {
            return new RenderEntityShuriken(manager);
        }
    }
}
