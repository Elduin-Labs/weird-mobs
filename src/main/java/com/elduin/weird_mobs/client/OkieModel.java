package com.elduin.weird_mobs.client;

import com.elduin.weird_mobs.WeirdMobs;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * The OKIE: one big flat panel, and an eyelid that covers the eyes when it
 * blinks.
 *
 * <p>There are no legs on purpose — Elduin's OKIE "moves by low legs that you
 * can't see". So instead of a walk cycle the whole panel hovers very slightly,
 * which reads as gliding.
 *
 * <p>The eyelid hides by sliding back <em>inside</em> the panel, where the
 * solid body blocks it from view, and pops forward to just in front of the face
 * for the few ticks of a blink.
 *
 * <p>These numbers are a placeholder that matches the proportions of the model
 * Elduin built in Blockbench. When he exports that model properly, replace
 * {@link #createBodyLayer()} with the exported one and drop his texture in at
 * {@code assets/weird_mobs/textures/entity/okie.png}.
 */
public class OkieModel extends EntityModel<OkieRenderState> {

	public static final ModelLayerLocation LAYER =
		new ModelLayerLocation(WeirdMobs.id("okie"), "main");

	/** Body half-depth, so the front face sits at -23. */
	private static final float FRONT = -23.0F;
	/** Where the eyelid sits while it is hidden inside the panel. */
	private static final float LID_HIDDEN = FRONT + 4.0F;
	/** Where the eyelid sits while the eyes are covered. */
	private static final float LID_SHUT = FRONT - 0.6F;

	private final ModelPart body;
	private final ModelPart eyelid;

	public OkieModel(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
		this.eyelid = this.body.getChild("eyelid");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition body = root.addOrReplaceChild(
			"body",
			CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-9.5F, -35.0F, FRONT, 19.0F, 35.0F, 46.0F),
			PartPose.offset(0.0F, 24.0F, 0.0F)
		);

		body.addOrReplaceChild(
			"eyelid",
			CubeListBuilder.create()
				.texOffs(0, 90)
				.addBox(-4.0F, 0.0F, 0.0F, 8.0F, 5.0F, 1.0F),
			PartPose.offset(0.0F, -24.0F, LID_HIDDEN)
		);

		return LayerDefinition.create(mesh, 256, 256);
	}

	@Override
	public void setupAnim(OkieRenderState state) {
		super.setupAnim(state);

		// A slow hover, so it looks like it is gliding rather than standing still.
		this.body.y = 24.0F + Mth.sin(state.ageInTicks * 0.08F) * 0.6F;

		this.eyelid.z = state.blinking ? LID_SHUT : LID_HIDDEN;
	}
}
