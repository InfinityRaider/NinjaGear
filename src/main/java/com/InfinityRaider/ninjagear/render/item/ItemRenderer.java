package com.infinityraider.ninjagear.render.item;

import com.infinityraider.ninjagear.render.RenderUtilBase;
import com.infinityraider.ninjagear.render.tessellation.ITessellator;
import com.infinityraider.ninjagear.render.tessellation.TessellatorBakedQuad;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ItemRenderer<I extends Item> extends RenderUtilBase implements IModel {
    private final IItemRenderingHandler<I> renderer;

    public ItemRenderer(IItemRenderingHandler<I> renderer) {
        this.renderer = renderer;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return renderer.getAllTextures();
    }

    @Override
    public BakedSuperModel<I> bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return new BakedSuperModel<>(format, renderer, bakedTextureGetter);
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

    public static class BakedSuperModel<I extends Item> implements IBakedModel {
        private final VertexFormat format;
        private final IItemRenderingHandler<I> renderer;
        private final Function<ResourceLocation, TextureAtlasSprite> textures;

        private BakedSuperModel(VertexFormat format, IItemRenderingHandler<I> renderer, Function<ResourceLocation, TextureAtlasSprite> textures) {
            this.format = format;
            this.renderer = renderer;
            this.textures = textures;
        }


        @Override
        public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
            return ImmutableList.of();
        }

        @Override
        public boolean isAmbientOcclusion() {
            return false;
        }

        @Override
        public boolean isGui3d() {
            return true;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return null;
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return ItemCameraTransforms.DEFAULT;
        }

        @Override
        public ItemOverride<I> getOverrides() {
            return new ItemOverride<>(format, renderer, textures);
        }
    }

    public static class ItemOverride<I extends Item> extends ItemOverrideList {
        private final VertexFormat format;
        private final IItemRenderingHandler<I> renderer;
        private final Function<ResourceLocation, TextureAtlasSprite> textures;

        private ItemOverride(VertexFormat format, IItemRenderingHandler<I> renderer, Function<ResourceLocation, TextureAtlasSprite> textures) {
            super((ImmutableList.of()));
            this.format = format;
            this.renderer = renderer;
            this.textures = textures;
        }

        @Override
        public BakedItemModel<I> handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
            return new BakedItemModel<>(format, textures, world, stack, entity, renderer);
        }
    }

    public static class BakedItemModel<I extends Item> implements IBakedModel, IPerspectiveAwareModel {
        private final VertexFormat format;
        private final Function<ResourceLocation, TextureAtlasSprite> textures;
        private final IItemRenderingHandler<I> renderer;
        private final ItemStack stack;
        private final World world;
        private final EntityLivingBase entity;
        private ItemCameraTransforms.TransformType transformType;

        private BakedItemModel(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> textures, World world, ItemStack stack,
                               EntityLivingBase entity, IItemRenderingHandler<I> renderer) {
            this.format = format;
            this.textures = textures;
            this.world = world;
            this.stack = stack;
            this.entity = entity;
            this.renderer = renderer;
            this.transformType = ItemCameraTransforms.TransformType.NONE;
        }

        private BakedItemModel setTransformType(ItemCameraTransforms.TransformType type) {
            this.transformType = type;
            return this;
        }

        @Override
        public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
            List<BakedQuad> list;
            if(side == null) {
                ITessellator tessellator = TessellatorBakedQuad.getInstance().setTextureFunction(textures);

                tessellator.startDrawingQuads(format);

                this.renderer.renderItem(tessellator, world, renderer.getItem(), stack, entity, transformType);

                list = tessellator.getQuads();
                tessellator.draw();
            } else {
                list = ImmutableList.of();
            }
            return list;
        }

        @Override
        public boolean isAmbientOcclusion() {
            return false;
        }

        @Override
        public boolean isGui3d() {
            return true;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return null;
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return ItemCameraTransforms.DEFAULT;
        }

        @Override
        public ItemOverrideList getOverrides() {
            return null;
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
            return new ImmutablePair<>(this.setTransformType(cameraTransformType), null);
        }
    }
}
