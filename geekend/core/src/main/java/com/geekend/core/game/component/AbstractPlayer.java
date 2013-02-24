package com.geekend.core.game.component;

import playn.core.GroupLayer;
import playn.core.PlayN;
import playn.core.ResourceCallback;

import com.geekend.core.game.GamePlayer;
import com.geekend.core.game.data.PlayerData;
import com.geekend.core.game.sprite.Sprite;
import com.geekend.core.game.sprite.SpriteLoader;

@SuppressWarnings("deprecation")
public abstract class AbstractPlayer implements GamePlayer {

	private static final String IMAGE = "sprites/player.png";
	private static final String JSON = "sprites/player.json";

	private static final int SPRITE_HEIGHT = 16;
	private static final int SPRITE_WIDTH = 16;

	protected PlayerData data;

	private GroupLayer mainLayer;
	private Sprite playerSprite;
	private float spriteCountdown;
	private int spriteIndex;

	private PlayerStates currentPlayerState;

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

	@Override
	public void paint(final float alpha) {
		playerSprite.layer().setTranslation(data.x, data.y);
		playerSprite.layer().setRotation(data.angle);
	}

	@Override
	public void update(final float delta) {
		updatePlayerData(delta);
		if (playerSprite.isReady()) updateSprite(delta);
	}
	
	public abstract void updatePlayerData(final float delta);

	private void updateSprite(final float delta) {
		if (data.state != currentPlayerState) {
			currentPlayerState = data.state;	
			spriteIndex = -1;
			spriteCountdown = data.state.getCountdownDelay();
			updateSpriteFrame();
		}

		if (spriteCountdown < 0) updateSpriteFrame();
		else
			spriteCountdown -= delta;
	}

	private void updateSpriteFrame() {
		spriteIndex = (spriteIndex + 1) % data.state.numSprites();
		playerSprite.setSprite(data.state.getSpriteIndex(spriteIndex));
		spriteCountdown = data.state.getCountdownDelay();
	}
}
