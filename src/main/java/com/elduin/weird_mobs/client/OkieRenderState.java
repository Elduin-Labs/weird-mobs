package com.elduin.weird_mobs.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/** Everything the OKIE's model needs to know, copied over each frame. */
public class OkieRenderState extends LivingEntityRenderState {

	/** True while the eyelid should be down over the eyes. */
	public boolean blinking;
}
