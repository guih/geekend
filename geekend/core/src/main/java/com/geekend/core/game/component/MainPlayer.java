package com.geekend.core.game.component;

import playn.core.GroupLayer;
import playn.core.Key;
import playn.core.PlayN;
import playn.core.ResourceCallback;

import com.geekend.core.game.GamePlayer;
import com.geekend.core.game.data.PlayerData;
import com.geekend.core.game.input.InputOracle;
import com.geekend.core.game.sprite.Sprite;
import com.geekend.core.game.sprite.SpriteLoader;

@SuppressWarnings("deprecation")
public final class MainPlayer implements GamePlayer {

	private static final String IMAGE = "sprites/player.png";
	private static final String JSON = "sprites/player.json";

	private static final int SPRITE_HEIGHT = 16;
	private static final int SPRITE_WIDTH = 16;
	private static final float SPEED = 0.13f;
	private static final float TURNING_SPEED = 1f / 200f;

	private PlayerData data;

	private GroupLayer mainLayer;
	private Sprite playerSprite;
	private float spriteCountdown;
	private int spriteIndex;

	public MainPlayer(PlayerData data) {
		this.data = data;
	}

	@Override
	public void init(final GroupLayer layer) {
		mainLayer = layer;

		playerSprite = SpriteLoader.getSprite(IMAGE, JSON);
		playerSprite.addCallback(new ResourceCallback<Sprite>() {

			@Override
			public void done(final Sprite resource) {
				playerSprite.layer().setOrigin(SPRITE_WIDTH/2, SPRITE_HEIGHT/2);
			}

			@Override
			public void error(final Throwable err) {
				// TODO Unify error threatment
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

	protected float getSpeed() {
		return SPEED;
	}

	protected float getTurningSpeed() {
		return TURNING_SPEED;
	}

	@Override
	public void update(final float delta) {
		final float distance = InputOracle.isKeyPressed(Key.UP) || InputOracle.isKeyPressed(Key.DOWN) ? getSpeed() * delta : 0;
		final float direction = InputOracle.isKeyPressed(Key.DOWN) ? -1 : 1;
		if (InputOracle.isKeyPressed(Key.LEFT)) data.angle -= delta * getTurningSpeed();
		if (InputOracle.isKeyPressed(Key.RIGHT)) data.angle += delta * getTurningSpeed();
		data.x += Math.cos(data.angle) * distance * direction;
		data.y += Math.sin(data.angle) * distance * direction;

		if (!playerSprite.isReady()) return;

		updatePlayerState();
		if (spriteCountdown < 0) updateSprite();
		else
			spriteCountdown -= delta;
	}

	private void updatePlayerState() {
		final PlayerStates current = data.state;
		data.state = PlayerStates.determinePlayerState();

		if (data.state == current) return;

		spriteIndex = -1;
		spriteCountdown = data.state.getCountdownDelay();
		updateSprite();
	}

	private void updateSprite() {
		spriteIndex = (spriteIndex + 1) % data.state.numSprites();
		playerSprite.setSprite(data.state.getSpriteIndex(spriteIndex));
		spriteCountdown = data.state.getCountdownDelay();
	}

	@Override
	public void paint(final float alpha) {
		playerSprite.layer().setTranslation(data.x, data.y);
		playerSprite.layer().setRotation(data.angle);
	}
}
