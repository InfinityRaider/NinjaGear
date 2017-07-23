package com.infinityraider.ninjagear.entity;

import com.infinityraider.ninjagear.block.BlockRope;
import com.infinityraider.ninjagear.handler.ConfigurationHandler;
import com.infinityraider.ninjagear.registry.BlockRegistry;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.infinityraider.ninjagear.render.entity.RenderEntityRopeCoil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityRopeCoil extends EntityThrowable {
    @SuppressWarnings("unused")
    public EntityRopeCoil(World world) {
        super(world);
    }

    public EntityRopeCoil(World world, EntityPlayer thrower) {
        super(world, thrower);
        Vec3d vec = thrower.getLookVec();
        this.setThrowableHeading(vec.xCoord, vec.yCoord, vec.zCoord, 2F, 0.2F);
    }

    @Override
    public EntityPlayer getThrower() {
        return (EntityPlayer) super.getThrower();
    }

    @Override
    protected float getGravityVelocity() {
        return 0.1F;
    }

    @Override
    protected void onImpact(RayTraceResult impact) {
        if (impact.entityHit != null) {
            return;
        }
        World world = this.getEntityWorld();
        if (!world.isRemote) {
            BlockPos pos = this.getBlockPosFromImpact(impact);
            IBlockState state = this.getEntityWorld().getBlockState(pos);
            BlockRope rope = (BlockRope) BlockRegistry.getInstance().blockRope;
            if(state.getBlock() instanceof BlockRope) {
                this.addRemainingToInventory(this.extendRope(world, pos, ConfigurationHandler.getInstance().ropeCoilLength));
            } else if (rope.canPlaceBlockAt(world, pos)) {
                this.addRemainingToInventory(this.placeRope(world, pos, ConfigurationHandler.getInstance().ropeCoilLength));
            } else {
                this.dropAsItem(impact.hitVec.xCoord, impact.hitVec.yCoord, impact.hitVec.zCoord);
            }
        }
    }

    public void dropAsItem(double x, double y, double z) {
        EntityItem item = new EntityItem(getEntityWorld(), x, y, z,
                new ItemStack(ItemRegistry.getInstance().itemRopeCoil));
        getEntityWorld().spawnEntity(item);
        this.setDead();
    }

    private BlockPos getBlockPosFromImpact(RayTraceResult impact) {
        if(impact.entityHit != null) {
            return impact.entityHit.getPosition();
        } else {
            IBlockState state = this.getEntityWorld().getBlockState(impact.getBlockPos());
            if(state.getBlock() instanceof BlockRope) {
                return impact.getBlockPos();
            }
            return impact.getBlockPos().offset(impact.sideHit);
        }
    }

    private int placeRope(World world, BlockPos pos, int ropeCount) {
        BlockRope rope = (BlockRope) BlockRegistry.getInstance().blockRope;
        world.setBlockState(pos, rope.getDefaultState(), 3);
        ropeCount = ropeCount - 1;
        if(ropeCount > 0) {
            BlockPos down = pos.down();
            if(rope.canPlaceBlockAt(world, down)) {
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
            if(getThrower() != null && !getThrower().inventory.addItemStackToInventory(stack)) {
                EntityItem item = new EntityItem(this.getEntityWorld(), this.posX, this.posY, this.posZ, stack);
                this.getEntityWorld().spawnEntity(item);
            }
        }
        this.setDead();
    }

    public static class RenderFactory implements IRenderFactory<EntityRopeCoil> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @SideOnly(Side.CLIENT)
        public Render<? super EntityRopeCoil> createRenderFor(RenderManager manager) {
            return new RenderEntityRopeCoil(manager);
        }
    }
}
