package com.elduin.weird_mobs.item;

import com.elduin.weird_mobs.WeirdMobs;
import com.elduin.weird_mobs.entity.ModEntities;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public final class ModItems {

	private ModItems() {
	}

	private static final ResourceKey<Item> OKIE_SPAWN_EGG_KEY =
		ResourceKey.create(Registries.ITEM, WeirdMobs.id("okie_spawn_egg"));

	/**
	 * The egg's two colours are not set here — from 1.21.4 on they live in the
	 * item model definition at {@code assets/weird_mobs/items/okie_spawn_egg.json}.
	 * They are the OKIE's light blue panel and the magenta of its right eye.
	 */
	public static final Item OKIE_SPAWN_EGG = Registry.register(
		BuiltInRegistries.ITEM,
		OKIE_SPAWN_EGG_KEY,
		new SpawnEggItem(ModEntities.OKIE, new Item.Properties().setId(OKIE_SPAWN_EGG_KEY))
	);

	public static void register() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS)
			.register(entries -> entries.accept(OKIE_SPAWN_EGG));
	}
}
