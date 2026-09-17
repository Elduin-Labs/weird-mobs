package com.elduin.weird_mobs.client;

import com.elduin.weird_mobs.WeirdMobs;
import com.elduin.weird_mobs.entity.OkieEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class OkieRenderer extends MobRenderer<OkieEntity, OkieRenderState, OkieModel> {

	private static final ResourceLocation TEXTURE = WeirdMobs.id("textures/entity/okie.png");

	public OkieRenderer(EntityRendererProvider.Context context) {
		super(context, new OkieModel(context.bakeLayer(OkieModel.LAYER)), 0.7F);
	}

	@Override
	public OkieRenderState createRenderState() {
		return new OkieRenderState();
	}

	@Override
	public void extractRenderState(OkieEntity entity, OkieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.blinking = entity.isBlinking();
	}

	@Override
	public ResourceLocation getTextureLocation(OkieRenderState state) {
		return TEXTURE;
	}
}
