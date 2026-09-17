package com.elduin.weird_mobs.client;

import com.elduin.weird_mobs.entity.ModEntities;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

/** Everything the OKIE needs that only exists on the client. */
public final class ModClient {

	private ModClient() {
	}

	public static void register() {
		EntityModelLayerRegistry.registerModelLayer(OkieModel.LAYER, OkieModel::createBodyLayer);
		EntityRendererRegistry.register(ModEntities.OKIE, OkieRenderer::new);
	}
}
