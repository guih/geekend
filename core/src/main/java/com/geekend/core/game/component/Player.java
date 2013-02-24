package com.geekend.core.game.component;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Key;
import playn.core.util.Callback;

import com.geekend.core.game.input.InputOracle;

public class Player {

	private static final String IMAGE_PLAYER_GIF = "images/player.gif";

	private static final float SPEED = 0.13f;
	private static final float TURNING_SPEED = 1f / 200f;
	private float x = 0;
	private float y = 0;
	private float angle = 0;

	private GroupLayer mainLayer;
	private ImageLayer imageLayer;

	public void init(final GroupLayer layer, final int i, final int j) {
		mainLayer = layer;
		final Image image = assets().getImage(IMAGE_PLAYER_GIF);
		image.addCallback(new Callback<Image>() {

			@Override
			public void onSuccess(final Image result) {
				imageLayer.setOrigin(imageLayer.width() / 2, imageLayer.height() / 2);
			}

			@Override
			public void onFailure(final Throwable cause) {}
		});
		imageLayer = graphics().createImageLayer(image);
		mainLayer.add(imageLayer);
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
	}

	public void paint(final float alpha) {
		imageLayer.setTranslation(x, y);
		imageLayer.setRotation(angle);
	}
}
