package com.infinityraider.ninjagear.item;

import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.api.v1.IHiddenItem;
import com.infinityraider.ninjagear.entity.EntityShuriken;
import com.infinityraider.ninjagear.handler.NinjaAuraHandler;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.registry.EffectRegistry;
import com.infinityraider.infinitylib.item.IItemWithModel;
import com.infinityraider.infinitylib.item.ItemBase;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@MethodsReturnNonnullByDefault
public class ItemShuriken extends ItemBase implements IHiddenItem, IItemWithModel {
    public ItemShuriken() {
        super("shuriken", new Properties().group(ItemRegistry.CREATIVE_TAB));
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if(!world.isRemote) {
            boolean crit = player.isPotionActive(EffectRegistry.getInstance().effectNinjaHidden);
            EntityShuriken shuriken = new EntityShuriken(world, player, crit);
            world.addEntity(shuriken);
            if (!player.isCreative()) {
                player.inventory.decrStackSize(player.inventory.currentItem, 1);
            }
            NinjaAuraHandler.getInstance().revealEntity(player, NinjaGear.instance.getConfig().getHidingCooldown());
        }
        return new ActionResult<>(ActionResultType.CONSUME, player.getHeldItem(hand));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L1"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L2"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L3"));
    }

    @Override
    public boolean shouldRevealPlayerWhenEquipped(PlayerEntity entity, ItemStack stack) {
        return false;
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        List<Tuple<Integer, ModelResourceLocation>> list = new ArrayList<>();
        list.add(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + this.getInternalName(), "inventory")));
        return list;
    }
}
