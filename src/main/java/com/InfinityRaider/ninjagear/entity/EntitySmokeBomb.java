package com.InfinityRaider.ninjagear.entity;

import com.InfinityRaider.ninjagear.handler.ConfigurationHandler;
import com.InfinityRaider.ninjagear.registry.BlockRegistry;
import com.InfinityRaider.ninjagear.registry.PotionRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class EntitySmokeBomb extends EntityThrowable {
    @SuppressWarnings("unused")
    public EntitySmokeBomb(World world) {
        super(world);
    }

    public EntitySmokeBomb(World world, EntityLivingBase thrower, float velocity) {
        super(world, thrower);
        Vec3d vec = thrower.getLookVec();
        this.setThrowableHeading(vec.xCoord, vec.yCoord, vec.zCoord, velocity, 0.2F);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.1F;
    }

    @Override
    protected void onImpact(RayTraceResult impact) {
        World world = this.getEntityWorld();
        BlockPos pos = this.getBlockPosFromImpact(impact);
        this.clearRevealedStatus(world, pos);
        this.createSmokeCloud(world, pos);
        if(world.isRemote) {
            this.spawnSmokeParticles(world, pos);
        }
    }

    private BlockPos getBlockPosFromImpact(RayTraceResult impact) {
        if(impact.entityHit != null) {
            return impact.entityHit.getPosition();
        } else {
            return impact.getBlockPos().offset(impact.sideHit);
        }
    }

    private void clearRevealedStatus(World world, BlockPos pos) {
        world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.add(-4, -4, -4), pos.add(4, 4, 4))).stream().filter(entity -> entity != null && (entity instanceof EntityLivingBase)).forEach(entity -> {
            EntityLivingBase living = (EntityLivingBase) entity;
            if (living.isPotionActive(PotionRegistry.getInstance().potionNinjaRevealed)) {
                living.removePotionEffect(PotionRegistry.getInstance().potionNinjaRevealed);
            }
        });
    }

    private void createSmokeCloud(World world, BlockPos pos) {
        int r = ConfigurationHandler.getInstance().smokeCloudRadius;
        for(int x = -r; x <= r; x++) {
            for(int y = -r; y <= r; y++) {
                for(int z = -r; z <= r; z++) {
                    int radius = x*x + y*y + z*z;
                    if(radius > r*r) {
                        continue;
                    }
                    BlockPos posAt = pos.add(x, y, z);
                    IBlockState state = world.getBlockState(posAt);
                    if(state.getMaterial() == Material.air) {
                        world.setBlockState(posAt, BlockRegistry.getInstance().blockSmoke.getStateFromMeta(getDarknessValue(radius, world.rand)), 5);
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

    @SideOnly(Side.CLIENT)
    private void spawnSmokeParticles(World world, BlockPos pos) {
        if(ConfigurationHandler.getInstance().disableSmokeParticles) {
            return;
        }
        for(int phi = 0; phi < 360; phi = phi + 10) {
            for(int theta = -90; theta <= 90; theta = theta + 10) {
                float v = 0.1F + world.rand.nextFloat()*0.2F;
                double cosPhi = Math.cos(phi * Math.PI / 180);
                double sinPhi = Math.sin(phi*Math.PI/180);
                double cosTheta = phi == theta ? cosPhi : Math.cos(theta*Math.PI/180);
                double sinTheta = phi == theta ? sinPhi : Math.sin(phi*Math.PI/180);
                Minecraft.getMinecraft().renderGlobal.spawnParticle(
                        EnumParticleTypes.SMOKE_LARGE.getParticleID(), true,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5D,
                        v*cosTheta*cosPhi, v*sinTheta, v*cosTheta*sinPhi,
                        10
                );
            }
        }
    }
}
