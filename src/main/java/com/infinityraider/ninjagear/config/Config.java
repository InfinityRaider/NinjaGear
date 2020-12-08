package com.infinityraider.ninjagear.config;

import com.infinityraider.infinitylib.config.ConfigurationHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public abstract class Config implements ConfigurationHandler.SidedModConfig {

    private Config() {}

    public abstract float getKatanaDamage();

    public abstract float getSaiDamage();

    public abstract float getShurikenDamage();

    public abstract float getCitMultiplier();

    public abstract int getBrightnessLimit();

    public abstract int getHidingCooldown();

    public abstract int getImbueCost();

    public abstract int getSmokeRadius();

    public abstract int getSmokeBuffDuration();

    public abstract int getSmokeDispersion();

    public abstract int getRopeCoilLength();

    public abstract boolean debug();

    @OnlyIn(Dist.CLIENT)
    public abstract boolean disableSmoke();

    @OnlyIn(Dist.CLIENT)
    public abstract boolean renderGadgets();

    public static class Common extends Config {
        // Combat
        public final ForgeConfigSpec.DoubleValue katanaDamage;
        public final ForgeConfigSpec.DoubleValue saiDamage;
        public final ForgeConfigSpec.DoubleValue shurikenDamage;
        public final ForgeConfigSpec.DoubleValue critMultiplier;

        //invisibility
        public final ForgeConfigSpec.IntValue brightnessLimit;
        public final ForgeConfigSpec.IntValue hidingCoolDown;
        public final ForgeConfigSpec.IntValue imbueCost;

        //smoke bomb
        public final ForgeConfigSpec.IntValue smokeCloudRadius;
        public final ForgeConfigSpec.IntValue smokeBuffDuration;
        public final ForgeConfigSpec.IntValue smokeCloudDispersionRate;

        //rope
        public final ForgeConfigSpec.IntValue ropeCoilLength;

        //debug
        public final ForgeConfigSpec.BooleanValue debug;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("Combat");
            this.katanaDamage = builder
                    .comment("The damage output of the katana. (Requires restart)")
                    .defineInRange("Katana damage", 5.0F, 1.0F, 20.0F);
            this.saiDamage = builder
                    .comment("The damage output of the sai.")
                    .defineInRange("Sai damage", 3.0F, 1.0F, 20.0F);
            this.shurikenDamage = builder
                    .comment("The damage output of the shuriken.")
                    .defineInRange("shuriken damage", 4.0F, 1.0F, 20.0F);
            this.critMultiplier = builder
                    .comment("The multiplier for critical hits")
                    .defineInRange("critical hit multiplier", 3.5F, 1.0F, 10.0F);
            builder.pop();

            builder.push("Invisibility");
            this.brightnessLimit = builder
                    .comment("When the brightness is below this value it is possible to hide (0 means it is never possible to hide, 16 means it is always possible to hide).")
                    .defineInRange("brightness limit",8, 0, 16);
            this.hidingCoolDown = builder
                    .comment("The cooldown time in ticks before a ninja can hide again after being revealed.")
                    .defineInRange("hiding cooldown", 60, 0, 600);
            this.imbueCost = builder
                    .comment("The number of levels it costs to imbue an armor piece with ninja armor")
                    .defineInRange("Imbue cost", 10, 0, 100);
            builder.pop();

            builder.push("Smoke Bomb");
            this.smokeCloudRadius = builder
                    .comment("The radius of the smoke cloud created by smoke bombs")
                    .defineInRange("smoke cloud radius", 4, 2, 5);
            this.smokeBuffDuration = builder
                    .comment("Defines how many ticks a ninja can stay hidden after using a smoke bomb")
                    .defineInRange("smoke buff duration", 400, 0, 60000);
            this.smokeCloudDispersionRate = builder
                    .comment("Smoke cloud dispersion rate, the higher this is, the faster smoke clouds will disperse and disappear")
                    .defineInRange("smoke cloud dispersion", 3, 1, 10);
            builder.pop();

            builder.push("Rope");
            this.ropeCoilLength = builder
                    .comment("The length of a rope coil")
                    .defineInRange("rope coil length", 10, 1, 16);
            builder.pop();

            builder.push("Debug");
            this.debug = builder.comment("Set to true if you wish to enable debug mode.")
                    .define("debug", false);
            builder.pop();
        }

        @Override
        public float getKatanaDamage() {
            return this.katanaDamage.get().floatValue();
        }

        @Override
        public float getSaiDamage() {
            return this.saiDamage.get().floatValue();
        }

        @Override
        public float getShurikenDamage() {
            return this.shurikenDamage.get().floatValue();
        }

        @Override
        public float getCitMultiplier() {
            return this.critMultiplier.get().floatValue();
        }

        @Override
        public int getBrightnessLimit() {
            return this.brightnessLimit.get();
        }

        @Override
        public int getHidingCooldown() {
            return this.hidingCoolDown.get();
        }

        @Override
        public int getImbueCost() {
            return this.imbueCost.get();
        }

        @Override
        public int getSmokeRadius() {
            return this.smokeCloudRadius.get();
        }

        @Override
        public int getSmokeBuffDuration() {
            return this.smokeBuffDuration.get();
        }

        @Override
        public int getSmokeDispersion() {
            return this.smokeCloudDispersionRate.get();
        }

        @Override
        public int getRopeCoilLength() {
            return this.ropeCoilLength.get();
        }

        @Override
        public boolean debug() {
            return this.debug.get();
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public boolean disableSmoke() {
            return false;
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public boolean renderGadgets() {
            return false;
        }

        @Override
        public ModConfig.Type getSide() {
            return ModConfig.Type.COMMON;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Client extends Common {
        //client
        public final ForgeConfigSpec.BooleanValue disableSmokeParticles;
        public final ForgeConfigSpec.BooleanValue renderGadgets;

        public Client(ForgeConfigSpec.Builder builder) {
            super(builder);
            builder.push("Client");
            this.disableSmokeParticles = builder
                    .comment( "Set this to false to disable the spawning of smoke bomb particles")
                    .define("Disable bomb particles", false);
            this.renderGadgets = builder
                    .comment( "Set to false to disable the rendering of ninja gadgets onto player models")
                    .define("Render gadgets", true);
            builder.pop();
        }

        @Override
        public boolean disableSmoke() {
            return this.disableSmokeParticles.get();
        }

        @Override
        public boolean renderGadgets() {
            return this.renderGadgets.get();
        }

        @Override
        public ModConfig.Type getSide() {
            return ModConfig.Type.CLIENT;
        }

    }
}
