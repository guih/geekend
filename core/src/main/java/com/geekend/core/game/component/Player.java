package com.geekend.core.game.component;

import playn.core.GroupLayer;
import playn.core.Key;
import playn.core.PlayN;
import playn.core.ResourceCallback;

import com.geekend.core.game.input.InputOracle;
import com.geekend.core.game.sprite.Sprite;
import com.geekend.core.game.sprite.SpriteLoader;

@SuppressWarnings("deprecation")
public class Player {

	private enum PlayerStates {
		WAITING(new int[] { 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 2, 2, 0, 1, 0, 1, 0, 56, 56 }, 800f), SHOOTING(new int[] { 3, 4, 5, 6, 7 }, 40f), RUNNING(
				new int[] { 8, 9, 10, 11, 12, 13, 14, 15 },
				120f), SKATING(new int[] { 16, 17 }, 300f), SKATING_HIT(new int[] { 22 }, 50f), HIT(new int[] { 53 }, 50f), DEAD(new int[] { 55 }, 50f);

		private final int[] spriteIndexes;
		private final float delay;

		private PlayerStates(final int[] spriteIndexes, final float delay) {
			this.spriteIndexes = spriteIndexes;
			this.delay = delay;
		}

		public static PlayerStates determinePlayerState() {
			if (InputOracle.isKeyPressed(Key.F)) return PlayerStates.SHOOTING;
			else if (InputOracle.isKeyPressed(Key.UP) || InputOracle.isKeyPressed(Key.DOWN)) return InputOracle.isKeyPressed(Key.TAB) ? PlayerStates.SKATING
					: PlayerStates.RUNNING;
			else
				return PlayerStates.WAITING;
		}

		public float getCountdownDelay() {
			return delay;
		}

		public int numSprites() {
			return spriteIndexes.length;
		}

		public int getSpriteIndex(final int index) {
			return spriteIndexes[index];
		}
	}

	private static final String IMAGE = "sprites/player.png";
	private static final String JSON = "sprites/player.json";

	private static final int SPRITE_HEIGHT = 16;
	private static final int SPRITE_WIDTH = 16;
	private static final float SPEED = 0.13f;
	private static final float TURNING_SPEED = 1f / 200f;

	private float x = 0;
	private float y = 0;
	private float angle = 0;
	private PlayerStates state = PlayerStates.WAITING;

	private GroupLayer mainLayer;
	private Sprite playerSprite;
	private float spriteCountdown;
	private int spriteIndex;

	public void init(final GroupLayer layer, final int i, final int j) {
		mainLayer = layer;
		playerSprite = SpriteLoader.getSprite(IMAGE, JSON);
		playerSprite.addCallback(new ResourceCallback<Sprite>() {

			@Override
			public void done(final Sprite resource) {
				playerSprite.layer().setOrigin(SPRITE_WIDTH, SPRITE_HEIGHT);
			}

			@Override
			public void error(final Throwable err) {
				final Console console = new Console();
				console.init(PlayN.graphics().rootLayer());
				console.log("erro!!!!!!!!");
				console.log(err.getMessage());
				console.log(err.toString());
			}
		});
		mainLayer.add(playerSprite.layer());
	}

	public float getSpeed() {
		return SPEED;
	}

	private float getTurningSpeed() {
		return TURNING_SPEED;
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public void update(final float delta) {
		final float distance = InputOracle.isKeyPressed(Key.UP) || InputOracle.isKeyPressed(Key.DOWN) ? getSpeed() * delta : 0;
		final float direction = InputOracle.isKeyPressed(Key.DOWN) ? +1 : 1;
		if (InputOracle.isKeyPressed(Key.LEFT)) angle -= delta * getTurningSpeed();
		if (InputOracle.isKeyPressed(Key.RIGHT)) angle += delta * getTurningSpeed();
		x += Math.cos(angle) * distance * direction;
		y += Math.sin(angle) * distance * direction;

		if (!playerSprite.isReady()) return;

		updatePlayerState();
		if (spriteCountdown < 0) updateSprite();
		else
			spriteCountdown -= delta;

	}

	private void updatePlayerState() {
		final PlayerStates current = state;
		state = PlayerStates.determinePlayerState();

		if (state == current) return;

		spriteIndex = -1;
		spriteCountdown = state.getCountdownDelay();
		updateSprite();
	}

	private void updateSprite() {
		spriteIndex = (spriteIndex + 1) % state.numSprites();
		playerSprite.setSprite(state.getSpriteIndex(spriteIndex));
		spriteCountdown = state.getCountdownDelay();
	}

	public void paint(final float alpha) {
		playerSprite.layer().setTranslation(x, y);
		playerSprite.layer().setRotation(angle);
	}
}
