package com.elduin.weird_mobs.event;

import com.elduin.weird_mobs.WeirdMobs;
import net.minecraft.server.level.ServerPlayer;

public class ExampleEventHandler {

	public static void onPlayerHurt(ServerPlayer player) {
		WeirdMobs.LOGGER.info("{} took damage.", player.getDisplayName());
	}
}
