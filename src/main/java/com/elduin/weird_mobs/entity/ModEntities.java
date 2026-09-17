package com.elduin.weird_mobs.entity;

import com.elduin.weird_mobs.WeirdMobs;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;

public final class ModEntities {

	private ModEntities() {
	}

	private static final ResourceKey<EntityType<?>> OKIE_KEY =
		ResourceKey.create(Registries.ENTITY_TYPE, WeirdMobs.id("okie"));

	/**
	 * The OKIE is a big flat panel: 19 wide and 35 tall in model pixels, which
	 * is about 1.2 by 2.2 blocks.
	 */
	public static final EntityType<OkieEntity> OKIE = Registry.register(
		BuiltInRegistries.ENTITY_TYPE,
		OKIE_KEY,
		EntityType.Builder.of(OkieEntity::new, MobCategory.CREATURE)
			.sized(1.2F, 2.2F)
			.eyeHeight(1.8F)
			.clientTrackingRange(10)
			.build(OKIE_KEY)
	);

	public static void register() {
		FabricDefaultAttributeRegistry.register(OKIE, OkieEntity.createAttributes());

		// The Deep Dark is pitch black, so the usual "needs light" check would
		// stop it ever spawning. It only needs somewhere solid to stand.
		SpawnPlacements.register(
			OKIE,
			SpawnPlacementTypes.ON_GROUND,
			Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
			(type, level, reason, pos, random) -> level.getBlockState(pos.below()).isSolid()
		);

		BiomeModifications.addSpawn(
			BiomeSelectors.includeByKey(Biomes.DEEP_DARK),
			MobCategory.CREATURE,
			OKIE,
			30,
			1,
			2
		);
	}
}
