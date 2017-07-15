package com.infinityraider.ninjagear.item;

import com.infinityraider.infinitylib.item.IItemWithModel;
import com.infinityraider.infinitylib.item.ItemBase;
import com.infinityraider.infinitylib.utility.IRecipeRegister;
import com.infinityraider.ninjagear.api.v1.IHiddenItem;
import com.infinityraider.ninjagear.entity.EntityShuriken;
import com.infinityraider.ninjagear.handler.ConfigurationHandler;
import com.infinityraider.ninjagear.handler.NinjaAuraHandler;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.infinityraider.ninjagear.registry.PotionRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@MethodsReturnNonnullByDefault
public class ItemShuriken extends ItemBase implements IHiddenItem, IRecipeRegister, IItemWithModel {
    public ItemShuriken() {
        super("shuriken");
        this.setCreativeTab(ItemRegistry.CREATIVE_TAB);
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if(!world.isRemote) {
            boolean crit = player.isPotionActive(PotionRegistry.getInstance().potionNinjaHidden);
            EntityShuriken shuriken = new EntityShuriken(world, player, crit);
            world.spawnEntityInWorld(shuriken);
            if (!player.capabilities.isCreativeMode) {
                player.inventory.decrStackSize(player.inventory.currentItem, 1);
            }
            NinjaAuraHandler.getInstance().revealEntity(player, ConfigurationHandler.getInstance().hidingCoolDown);
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("deprecation")
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L1"));
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L2"));
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L3"));
    }

    @Override
    public boolean shouldRevealPlayerWhenEquipped(EntityPlayer entity, ItemStack stack) {
        return false;
    }

    @Override
    public void registerRecipes() {
        this.getRecipes().forEach(GameRegistry::addRecipe);
    }

    public List<IRecipe> getRecipes() {
        List<IRecipe> list = new ArrayList<>();
        list.add(new ShapedOreRecipe(new ItemStack(this, 16)," b ", "bib", " b ",
                'b', Blocks.IRON_BARS,
                'i', "ingotIron"));
        return list;
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        List<Tuple<Integer, ModelResourceLocation>> list = new ArrayList<>();
        list.add(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + this.getInternalName(), "inventory")));
        return list;
    }
}
