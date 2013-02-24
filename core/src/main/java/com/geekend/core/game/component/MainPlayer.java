package com.geekend.core.game.component;

import playn.core.GroupLayer;
import playn.core.Key;
import playn.core.PlayN;
import playn.core.ResourceCallback;

import com.geekend.core.game.GamePlayer;
import com.geekend.core.game.input.InputOracle;
import com.geekend.core.game.sprite.Sprite;
import com.geekend.core.game.sprite.SpriteLoader;

@SuppressWarnings("deprecation")
public class MainPlayer implements GamePlayer {

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

	public void init(final GroupLayer layer, final int x, final int y) {
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

	@Override
	public void destroy() {}

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

	@Override
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

	@Override
	public void paint(final float alpha) {
		playerSprite.layer().setTranslation(x, y);
		playerSprite.layer().setRotation(angle);
	}
}
