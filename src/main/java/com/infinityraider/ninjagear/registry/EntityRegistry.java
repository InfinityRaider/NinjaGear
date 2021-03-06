package com.infinityraider.ninjagear.registry;

import com.infinityraider.infinitylib.entity.EntityTypeBase;
import com.infinityraider.ninjagear.entity.EntityRopeCoil;
import com.infinityraider.ninjagear.entity.EntityShuriken;
import com.infinityraider.ninjagear.entity.EntitySmokeBomb;
import com.infinityraider.ninjagear.reference.Constants;
import com.infinityraider.ninjagear.reference.Names;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;

public class EntityRegistry {
    private static final EntityRegistry INSTANCE = new EntityRegistry();

    public static EntityRegistry getInstance() {
        return INSTANCE;
    }

    private EntityRegistry() {
        this.entitySmokeBomb = EntityTypeBase.entityTypeBuilder(Names.Items.SMOKE_BOMB, EntitySmokeBomb.class, EntitySmokeBomb.SpawnFactory.getInstance(),
                EntityClassification.MISC, EntitySize.fixed(Constants.UNIT, Constants.UNIT))

                .setTrackingRange(32)
                .setUpdateInterval(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntitySmokeBomb.RenderFactory.getInstance())
                .build();

        this.entityShuriken = EntityTypeBase.entityTypeBuilder(Names.Items.SHURIKEN, EntityShuriken.class, EntityShuriken.SpawnFactory.getInstance(),
                EntityClassification.MISC, EntitySize.fixed(Constants.UNIT, Constants.UNIT))

                .setTrackingRange(32)
                .setUpdateInterval(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityShuriken.RenderFactory.getInstance())
                .build();

        this.entityRopeCoil = EntityTypeBase.entityTypeBuilder(Names.Items.ROPE_COIL, EntityRopeCoil.class, EntityRopeCoil.SpawnFactory.getInstance(),
                EntityClassification.MISC, EntitySize.fixed(Constants.UNIT, Constants.UNIT))

                .setTrackingRange(32)
                .setUpdateInterval(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityRopeCoil.RenderFactory.getInstance())
                .build();
    }

    public final EntityType<EntitySmokeBomb> entitySmokeBomb;
    public final EntityType<EntityShuriken> entityShuriken;
    public final EntityType<EntityRopeCoil> entityRopeCoil;
}
