package com.InfinityRaider.ninjagear.gui;

import com.InfinityRaider.ninjagear.handler.ConfigurationHandler;
import com.InfinityRaider.ninjagear.reference.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiConfig  extends net.minecraftforge.fml.client.config.GuiConfig {
    public GuiConfig(GuiScreen guiScreen) {
        super(guiScreen, getConfigElements(), Reference.MOD_ID, false, false,
                net.minecraftforge.fml.client.config.GuiConfig.getAbridgedConfigPath(ConfigurationHandler.getInstance().toString()));
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> configElements = new ArrayList<>();
        for (ConfigurationHandler.Categories category : ConfigurationHandler.Categories.values()) {
            String description = Reference.MOD_NAME + " " + category.getName() + " Settings";
            String name = "agricraft.configgui.ctgy." + category.getName();
            configElements.add(new DummyConfigElement.DummyCategoryElement(
                    description,
                    name,
                    new ConfigElement(ConfigurationHandler.getInstance().getConfiguration().getCategory(category.getName())).getChildElements()));
        }
        return configElements;
    }
}