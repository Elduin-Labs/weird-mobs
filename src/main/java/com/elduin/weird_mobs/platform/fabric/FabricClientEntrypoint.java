package com.elduin.weird_mobs.platform.fabric;

//? fabric {

import com.elduin.weird_mobs.WeirdMobs;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ClientModInitializer;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		WeirdMobs.onInitializeClient();
	}

}
//?}
