package com.infinityraider.ninjagear.handler;

import com.infinityraider.ninjagear.NinjaGear;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ConfigurationHandler {
    private static final ConfigurationHandler INSTANCE = new ConfigurationHandler();

    private ConfigurationHandler() {}

    public static ConfigurationHandler getInstance() {
        return INSTANCE;
    }

    private Configuration config;

    //combat
    public float katanaDamage;
    public float saiDamage;
    public float shurikenDamage;
    public float critMultiplier;

    //invisibility
    public int brightnessLimit;
    public int hidingCoolDown;

    //smoke bomb
    public int smokeCloudRadius;
    public int smokeCloudDispersionFactor;

    //rope
    public int ropeCoilLength;

    //debug
    public boolean debug;

    //client
    @SideOnly(Side.CLIENT)
    public boolean disableSmokeParticles;
    @SideOnly(Side.CLIENT)
    public boolean renderGadgets;

    public Configuration getConfiguration() {
        return config;
    }

    public void init(FMLPreInitializationEvent event) {
        if(config == null) {
            config = new Configuration(event.getSuggestedConfigurationFile());
        }
        loadConfiguration();
        if(config.hasChanged()) {
            config.save();
        }
        NinjaGear.instance.getLogger().debug("Configuration Loaded");
    }

    @SideOnly(Side.CLIENT)
    public void initClientConfigs(FMLPreInitializationEvent event) {
        if(config == null) {
            config = new Configuration(event.getSuggestedConfigurationFile());
        }
        loadClientConfiguration();
        if(config.hasChanged()) {
            config.save();
        }
        NinjaGear.instance.getLogger().debug("Client configuration Loaded");
    }

    private void loadConfiguration() {
        //combat
        katanaDamage = config.getFloat("katana damage", Categories.COMBAT.getName(), 5.0F, 1.0F, 20.0F, "The damage output of the katana.");
        saiDamage = config.getFloat("sai damage", Categories.COMBAT.getName(), 3.0F, 1.0F, 20.0F, "The damage output of the sai.");
        shurikenDamage = config.getFloat("shuriken damage", Categories.COMBAT.getName(), 4.0F, 1.0F, 20.0F, "The damage output of the shuriken.");
        critMultiplier = config.getFloat("critical hit multiplier", Categories.COMBAT.getName(), 3.5F, 1.0F, 10.0F, "The multiplier for critical hits");

        //invisibility
        brightnessLimit = config.getInt("brightness limit", Categories.INVISIBILITY.getName(), 8, 0, 16,
                "When the brightness is below this value it is possible to hide (0 means it is never possible to hide, 16 means it is always possible to hide).");
        hidingCoolDown = config.getInt("hiding cool down", Categories.INVISIBILITY.getName(), 60, 0, 600,
                "The cool down time in ticks before a ninja can hide again after being revealed.");

        //smoke bomb
        smokeCloudRadius = config.getInt("smoke cloud radius", Categories.SMOKE_BOMB.getName(), 4, 2, 5,
                "The radius of the smoke cloud created by smoke bombs");
        smokeCloudDispersionFactor = config.getInt("smoke cloud dispersion", Categories.SMOKE_BOMB.getName(), 5, 1, 20,
                "Smoke cloud dispersion factor, the lower this is, the faster smoke clouds will disperse and disappear");

        //rope
        ropeCoilLength = config.getInt("rope coil length", Categories.ROPE.getName(), 10, 1, 16,
                "The length of a rope coil");

        //debug
        debug = config.getBoolean("debug", Categories.DEBUG.getName(), false, "Set to true if you wish to enable debug mode.");
    }

    public int getPotionEffectId(String name, int defaultId) {
        int id = config.getInt(name, Categories.POTION.getName(), defaultId, 0, 255,
                "Id for the " + name + " potion effect, this potion effect is generated on the first run by detecting a free potion id.");
        if(config.hasChanged()) {
            config.save();
        }
        return id;
    }

    @SideOnly(Side.CLIENT)
    private void loadClientConfiguration() {
        disableSmokeParticles = config.getBoolean("Disable smoke bomb particles", Categories.CLIENT.getName(), false,
                "Set this to false to disable the spawning of smoke bomb particles");
        renderGadgets = config.getBoolean("Render gadgets", Categories.CLIENT.getName(), true,
                "Set to false to disable the rendering of ninja gadgets onto player models");
    }

    public enum Categories {
        COMBAT("combat"),
        SMOKE_BOMB("smoke bomb"),
        INVISIBILITY("invisibility"),
        POTION("potion"),
        ROPE("rope"),
        DEBUG("debug"),
        CLIENT("client");

        private final String name;

        Categories(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
