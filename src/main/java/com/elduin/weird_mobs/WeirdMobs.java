package com.elduin.weird_mobs;

import com.elduin.weird_mobs.entity.ModEntities;
import com.elduin.weird_mobs.platform.Platform;
import com.elduin.weird_mobs.platform.fabric.FabricPlatform;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeirdMobs {

	public static final String MOD_ID = /*$ mod_id*/ "weird_mobs";
	public static final String MOD_VERSION = /*$ mod_version*/ "1.0.0";
	public static final String MOD_FRIENDLY_NAME = /*$ mod_name*/ "Weird Mobs";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final Platform PLATFORM = createPlatformInstance();

	public static void onInitialize() {
		LOGGER.info("Initializing {} on {}", MOD_ID, WeirdMobs.xplat().loader());
		ModEntities.register();
		com.elduin.weird_mobs.item.ModItems.register();
	}

	public static void onInitializeClient() {
		LOGGER.info("Initializing {} Client on {}", MOD_ID, WeirdMobs.xplat().loader());
		com.elduin.weird_mobs.client.ModClient.register();
	}

	static Platform xplat() {
		return PLATFORM;
	}

	private static Platform createPlatformInstance() {
		return new FabricPlatform();
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
