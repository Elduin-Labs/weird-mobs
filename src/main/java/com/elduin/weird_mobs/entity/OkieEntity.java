package com.elduin.weird_mobs.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * The OKIE. A robot that teaches you how to spell.
 *
 * <p>It is friendly and has no attack goals at all — that is deliberate, and
 * Elduin was explicit about it. Do not give this mob a target selector.
 *
 * <p>It has no legs in the model. It is supposed to look like it glides along
 * on little legs you cannot see, so there is no walk cycle either — just a
 * slow bob, done in the model.
 */
public class OkieEntity extends PathfinderMob {

	/** How long one blink lasts, in ticks. Short — a blink is quick. */
	public static final int BLINK_TICKS = 3;

	/** Roughly how often it blinks, in ticks. */
	public static final int BLINK_EVERY = 70;

	private static final String[] WORDS = {
		"cat", "dog", "tree", "house", "water", "friend", "rocket", "dragon",
		"because", "school", "people", "little", "jump", "green", "diamond",
		"pickaxe", "creeper", "village", "enough", "beautiful"
	};

	/**
	 * The sizes a debug stick cycles it through. Poking it walks down the list
	 * and then wraps back to 1.0, so an OKIE can never be left stuck tiny.
	 */
	private static final double[] SIZES = {1.0D, 0.7D, 0.5D, 0.35D, 0.25D};

	/** How close you have to be for its bar to show up, in blocks. */
	private static final double BAR_RANGE = 24.0D;

	/**
	 * The bar across the top of the screen, like the Ender Dragon's. It says
	 * OKIE and fills up with how much health it has left.
	 */
	private final ServerBossEvent bar = new ServerBossEvent(
		Component.literal("OKIE"),
		BossEvent.BossBarColor.BLUE,
		BossEvent.BossBarOverlay.PROGRESS
	);

	/** Ticks left of the lesson currently being spelled out, 0 when idle. */
	private int lessonTicks;
	/** Which letter of the current word goes out next. */
	private int lessonLetter;
	private String lessonWord = "";
	private Player lessonStudent;

	public OkieEntity(EntityType<? extends PathfinderMob> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
			.add(Attributes.MAX_HEALTH, 20.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.22D)
			.add(Attributes.FOLLOW_RANGE, 16.0D);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 0.8D));
		this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
	}

	/**
	 * True when the eyelid should be down. Worked out from the tick count and
	 * the entity id, so every OKIE blinks on its own rhythm and nothing has to
	 * be sent over the network for it.
	 */
	public boolean isBlinking() {
		int phase = (this.tickCount + this.getId() * 17) % BLINK_EVERY;
		return phase < BLINK_TICKS;
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		boolean debugStick = player.getItemInHand(hand).is(Items.DEBUG_STICK);
		if (this.level().isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		if (debugStick) {
			this.cycleSize(player);
			return InteractionResult.SUCCESS;
		}
		if (this.lessonTicks > 0) {
			return InteractionResult.SUCCESS;
		}
		this.lessonWord = WORDS[this.random.nextInt(WORDS.length)];
		this.lessonLetter = 0;
		this.lessonTicks = 1;
		this.lessonStudent = player;
		this.say(player, "Let's spell " + this.lessonWord + ".");
		return InteractionResult.SUCCESS;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			return;
		}
		this.tickBar();
		if (this.lessonTicks > 0) {
			this.tickLesson();
		}
	}

	/**
	 * Keeps the bar filled to its health, and shows it to whoever is close
	 * enough while hiding it from everyone who has walked away.
	 */
	private void tickBar() {
		this.bar.setProgress(this.getHealth() / this.getMaxHealth());

		List<ServerPlayer> nearby = this.level().getEntitiesOfClass(
			ServerPlayer.class, this.getBoundingBox().inflate(BAR_RANGE));

		for (ServerPlayer watching : new ArrayList<>(this.bar.getPlayers())) {
			if (!nearby.contains(watching)) {
				this.bar.removePlayer(watching);
			}
		}
		for (ServerPlayer player : nearby) {
			this.bar.addPlayer(player);
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		this.bar.removeAllPlayers();
		super.remove(reason);
	}

	/** Spells the current word out one letter at a time, about twice a second. */
	private void tickLesson() {
		this.lessonTicks--;
		if (this.lessonTicks > 0) {
			return;
		}
		if (this.lessonStudent == null || !this.lessonStudent.isAlive()) {
			this.lessonWord = "";
			return;
		}
		if (this.lessonLetter < this.lessonWord.length()) {
			String letter = String.valueOf(this.lessonWord.charAt(this.lessonLetter)).toUpperCase();
			this.say(this.lessonStudent, letter);
			this.lessonLetter++;
			this.lessonTicks = 10;
		} else {
			this.say(this.lessonStudent, this.lessonWord + "!");
			this.lessonWord = "";
			this.lessonStudent = null;
		}
	}

	/**
	 * Steps it down to the next size, wrapping back to full size at the end.
	 * The scale attribute takes the hitbox with it, so a tiny OKIE really is
	 * tiny, not just drawn small.
	 */
	private void cycleSize(Player player) {
		AttributeInstance scale = this.getAttribute(Attributes.SCALE);
		if (scale == null) {
			return;
		}
		int next = 0;
		for (int i = 0; i < SIZES.length; i++) {
			if (Math.abs(scale.getBaseValue() - SIZES[i]) < 0.01D) {
				next = (i + 1) % SIZES.length;
				break;
			}
		}
		scale.setBaseValue(SIZES[next]);
		this.say(player, next == 0 ? "big again!" : "eep! I am small!");
	}

	private void say(Player player, String message) {
		player.displayClientMessage(Component.literal("OKIE: " + message), false);
	}

	@Override
	public boolean removeWhenFarAway(double distance) {
		return false;
	}
}
