package com.infinityraider.ninjagear.registry;

import com.infinityraider.ninjagear.entity.EntityRopeCoil;
import com.infinityraider.ninjagear.entity.EntityShuriken;
import com.infinityraider.ninjagear.entity.EntitySmokeBomb;
import com.infinityraider.infinitylib.entity.EntityRegistryEntry;

public class EntityRegistry {
    private static final EntityRegistry INSTANCE = new EntityRegistry();

    public static EntityRegistry getInstance() {
        return INSTANCE;
    }

    private EntityRegistry() {
        this.entitySmokeBomb = new EntityRegistryEntry<>(EntitySmokeBomb.class, "entity.smokeBomb")
                .setTrackingDistance(32)
                .setUpdateFrequency(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntitySmokeBomb.RenderFactory.getInstance());

        this.entityShuriken = new EntityRegistryEntry<>(EntityShuriken.class, "entity.shuriken")
                .setTrackingDistance(32)
                .setUpdateFrequency(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityShuriken.RenderFactory.getInstance());

        this.entityRopeCoil = new EntityRegistryEntry<>(EntityRopeCoil.class, "entity.shuriken")
                .setTrackingDistance(32)
                .setUpdateFrequency(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityRopeCoil.RenderFactory.getInstance());
    }

    public final EntityRegistryEntry<EntitySmokeBomb> entitySmokeBomb;
    public final EntityRegistryEntry<EntityShuriken> entityShuriken;
    public final EntityRegistryEntry<EntityRopeCoil> entityRopeCoil;
}
