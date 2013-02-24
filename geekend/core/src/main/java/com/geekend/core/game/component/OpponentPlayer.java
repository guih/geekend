package com.geekend.core.game.component;

import playn.core.GroupLayer;
import playn.core.PlayN;
import playn.core.ResourceCallback;

import com.geekend.core.game.GamePlayer;
import com.geekend.core.game.data.PlayerData;
import com.geekend.core.game.sprite.Sprite;
import com.geekend.core.game.sprite.SpriteLoader;

@SuppressWarnings("deprecation")
public class OpponentPlayer implements GamePlayer {

	private static final String IMAGE = "sprites/player.png";
	private static final String JSON = "sprites/player.json";

	private static final int SPRITE_HEIGHT = 16;
	private static final int SPRITE_WIDTH = 16;

	private PlayerData data;

	private GroupLayer mainLayer;
	private Sprite playerSprite;
	private float spriteCountdown;
	private int spriteIndex;
	private PlayerStates currentPlayerState;

	public OpponentPlayer(PlayerData playerData) {
		this.data = playerData;
	}

	@Override
	public void init(final GroupLayer layer) {
		mainLayer = layer;

		playerSprite = SpriteLoader.getSprite(IMAGE, JSON);
		playerSprite.addCallback(new ResourceCallback<Sprite>() {

			@Override
			public void done(final Sprite resource) {
				playerSprite.layer().setOrigin(SPRITE_WIDTH, SPRITE_HEIGHT);
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

	@Override
	public void update(final float delta) {
		if (!playerSprite.isReady()) return;

		updatePlayerState();
		if (spriteCountdown < 0) updateSprite();
		else
			spriteCountdown -= delta;
	}

	private void updatePlayerState() {
		if (data.state == currentPlayerState) return;
		currentPlayerState = data.state;

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
