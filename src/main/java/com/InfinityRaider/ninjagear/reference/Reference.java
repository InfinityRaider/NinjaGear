package com.InfinityRaider.ninjagear.reference;

public interface Reference {
    String MOD_ID = "ninjagear";
    String MOD_NAME = "${mod.name}";

    String VER_MAJOR = "${mod.version_major}";
    String VER_MINOR = "${mod.version_minor}";
    String VER_REVIS = "${mod.version_patch}";
    String MOD_VERSION = "${mod.version}";
    String VERSION = "${mod.version_minecraft}-${mod.version}";

    String AUTHOR = "${mod.author}";
    String CLIENT_PROXY_CLASS = "com.InfinityRaider.ninjagear.proxy.ClientProxy";
    String SERVER_PROXY_CLASS = "com.InfinityRaider.ninjagear.proxy.ServerProxy";
    String GUI_FACTORY_CLASS = "com.InfinityRaider.ninjagear.gui.GuiFactory";
}