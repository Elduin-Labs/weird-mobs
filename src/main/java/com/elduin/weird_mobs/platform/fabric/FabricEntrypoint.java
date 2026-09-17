package com.elduin.weird_mobs.platform.fabric;

//? fabric {

import com.elduin.weird_mobs.WeirdMobs;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		WeirdMobs.onInitialize();
		FabricEventSubscriber.registerEvents();
	}
}
//?}
