package com.infinityraider.ninjagear.registry;

import com.infinityraider.infinitylib.entity.EntityTypeBase;
import com.infinityraider.infinitylib.utility.registration.ModContentRegistry;
import com.infinityraider.infinitylib.utility.registration.RegistryInitializer;
import com.infinityraider.ninjagear.entity.EntityRopeCoil;
import com.infinityraider.ninjagear.entity.EntityShuriken;
import com.infinityraider.ninjagear.entity.EntitySmokeBomb;
import com.infinityraider.ninjagear.reference.Constants;
import com.infinityraider.ninjagear.reference.Names;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EntityRegistry extends ModContentRegistry {
    private static final EntityRegistry INSTANCE = new EntityRegistry();

    public static EntityRegistry getInstance() {
        return INSTANCE;
    }

    private final RegistryInitializer<EntityTypeBase<EntitySmokeBomb>> smokeBomb;
    private final RegistryInitializer<EntityTypeBase<EntityShuriken>> shuriken;
    private final RegistryInitializer<EntityTypeBase<EntityRopeCoil>> ropeCoil;

    private EntityRegistry() {
        this.smokeBomb = this.entity(() -> EntityTypeBase.entityTypeBuilder(
                Names.Items.SMOKE_BOMB, EntitySmokeBomb.class, EntitySmokeBomb.SpawnFactory.getInstance(),
                MobCategory.MISC, EntityDimensions.fixed(Constants.UNIT, Constants.UNIT))
                .setTrackingRange(32)
                .setUpdateInterval(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntitySmokeBomb.RenderFactory.getInstance())
                .build());

        this.shuriken = this.entity(() -> EntityTypeBase.entityTypeBuilder(
                Names.Items.SHURIKEN, EntityShuriken.class, EntityShuriken.SpawnFactory.getInstance(),
                MobCategory.MISC, EntityDimensions.fixed(Constants.UNIT, Constants.UNIT))
                .setTrackingRange(32)
                .setUpdateInterval(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityShuriken.RenderFactory.getInstance())
                .build());

        this.ropeCoil = this.entity(() -> EntityTypeBase.entityTypeBuilder(
                Names.Items.ROPE_COIL, EntityRopeCoil.class, EntityRopeCoil.SpawnFactory.getInstance(),
                MobCategory.MISC, EntityDimensions.fixed(Constants.UNIT, Constants.UNIT))
                .setTrackingRange(32)
                .setUpdateInterval(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityRopeCoil.RenderFactory.getInstance())
                .build());
    }

    public EntityType<EntitySmokeBomb> getSmokeBombEntityType() {
        return this.smokeBomb.get();
    }

    public EntityType<EntityShuriken> getShurikenEntityType() {
        return this.shuriken.get();
    }

    public EntityType<EntityRopeCoil> getRopeCoilEntityType() {
        return this.ropeCoil.get();
    }
}
