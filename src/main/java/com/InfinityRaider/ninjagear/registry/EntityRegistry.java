package com.InfinityRaider.ninjagear.registry;

import com.InfinityRaider.ninjagear.NinjaGear;
import com.InfinityRaider.ninjagear.entity.EntityRopeCoil;
import com.InfinityRaider.ninjagear.entity.EntityShuriken;
import com.InfinityRaider.ninjagear.entity.EntitySmokeBomb;
import com.InfinityRaider.ninjagear.render.entity.RenderEntityRopeCoil;
import com.InfinityRaider.ninjagear.render.entity.RenderEntityShuriken;
import com.InfinityRaider.ninjagear.render.entity.RenderEntitySmokeBomb;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityRegistry {
    private static final EntityRegistry INSTANCE = new EntityRegistry();

    public static EntityRegistry getInstance() {
        return INSTANCE;
    }

    private EntityRegistry() {}

    public final String SMOKE_BOMB = "entity.smokeBomb";
    public final String SHURIKEN = "entity.shuriken";
    public final String ROPE_COIL = "entity.ropeCoil";

    public final int ID_SMOKE_BOMB = 0;
    public final int ID_SHURIKEN = 1;
    public final int ID_ROPE_COIL = 2;

    public void init() {
        net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(
                EntitySmokeBomb.class,
                SMOKE_BOMB,
                ID_SMOKE_BOMB,
                NinjaGear.instance,
                32,
                1,
                true
        );

        net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(
                EntityShuriken.class,
                SHURIKEN,
                ID_SHURIKEN,
                NinjaGear.instance,
                32,
                1,
                true
        );

        net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(
                EntityRopeCoil.class,
                ROPE_COIL,
                ID_ROPE_COIL,
                NinjaGear.instance,
                32,
                1,
                true
        );
    }

    @SideOnly(Side.CLIENT)
    public void initRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntitySmokeBomb.class, RenderEntitySmokeBomb.getFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntityShuriken.class, RenderEntityShuriken.getFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntityRopeCoil.class, RenderEntityRopeCoil.getFactory());
    }
}
