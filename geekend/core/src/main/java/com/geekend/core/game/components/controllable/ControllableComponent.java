package com.geekend.core.game.components.controllable;

import playn.core.GroupLayer;
import playn.core.PlayN;
import playn.core.ResourceCallback;

import com.geekend.core.game.components.Console;
import com.geekend.core.game.components.Updatable;
import com.geekend.core.game.utils.sprite.Sprite;

@SuppressWarnings("deprecation")
public final class ControllableComponent<T> implements Updatable {

	protected Data<T> data;
	private final Controller<T> controller;
	private State<T> currentState;

	private final Sprite sprite;
	private float spriteCountdown;
	private int spriteIndex;

	public ControllableComponent(final Controller<T> controller, final Data<T> data, final Sprite sprite) {
		this.controller = controller;
		this.sprite = sprite;
		this.data = data;
	}

	@Override
	public void init(final GroupLayer layer) {
		controller.initializeData(data);
		sprite.addCallback(new ResourceCallback<Sprite>() {

			@Override
			public void done(final Sprite resource) {
				sprite.layer().setOrigin(sprite.width() / 2, sprite.height() / 2);
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

		layer.add(sprite.layer());
	}

	@Override
	public void destroy() {}

	@Override
	public void paint(final float alpha) {
		sprite.layer().setTranslation(data.x, data.y);
		sprite.layer().setRotation(data.angle);
	}

	@Override
	public void update(final float delta) {
		controller.updateData(data, delta);
		if (sprite.isReady()) updateSprite(delta);
	}

	private void updateSprite(final float delta) {
		if (data.state != currentState) {
			currentState = data.state;
			spriteIndex = -1;
			spriteCountdown = data.state.getCountdownDelay();
			updateSpriteFrame();
		}

		if (spriteCountdown < 0) updateSpriteFrame();
		else
			spriteCountdown -= delta;
	}

	private void updateSpriteFrame() {
		final int numSprites = data.state.numSprites();
		if (numSprites <= 1) return;
		spriteIndex = (spriteIndex + 1) % numSprites;
		sprite.setSprite(data.state.getSpriteIndex(spriteIndex));
		spriteCountdown = data.state.getCountdownDelay();
	}
}
