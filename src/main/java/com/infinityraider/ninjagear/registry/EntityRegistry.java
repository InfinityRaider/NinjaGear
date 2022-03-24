package com.infinityraider.ninjagear.registry;

import com.infinityraider.infinitylib.entity.EntityTypeBase;
import com.infinityraider.ninjagear.entity.EntityRopeCoil;
import com.infinityraider.ninjagear.entity.EntityShuriken;
import com.infinityraider.ninjagear.entity.EntitySmokeBomb;
import com.infinityraider.ninjagear.reference.Constants;
import com.infinityraider.ninjagear.reference.Names;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EntityRegistry {
    public static final EntityType<EntitySmokeBomb> entitySmokeBomb = EntityTypeBase.entityTypeBuilder(
            Names.Items.SMOKE_BOMB, EntitySmokeBomb.class, EntitySmokeBomb.SpawnFactory.getInstance(),
            MobCategory.MISC, EntityDimensions.fixed(Constants.UNIT, Constants.UNIT))
                .setTrackingRange(32)
                .setUpdateInterval(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntitySmokeBomb.RenderFactory.getInstance())
                .build();

    public static final EntityType<EntityShuriken> entityShuriken = EntityTypeBase.entityTypeBuilder(
            Names.Items.SHURIKEN, EntityShuriken.class, EntityShuriken.SpawnFactory.getInstance(),
            MobCategory.MISC, EntityDimensions.fixed(Constants.UNIT, Constants.UNIT))
                .setTrackingRange(32)
                .setUpdateInterval(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityShuriken.RenderFactory.getInstance())
                .build();

    public static final EntityType<EntityRopeCoil> entityRopeCoil = EntityTypeBase.entityTypeBuilder(
            Names.Items.ROPE_COIL, EntityRopeCoil.class, EntityRopeCoil.SpawnFactory.getInstance(),
            MobCategory.MISC, EntityDimensions.fixed(Constants.UNIT, Constants.UNIT))
                .setTrackingRange(32)
                .setUpdateInterval(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityRopeCoil.RenderFactory.getInstance())
                .build();
}
