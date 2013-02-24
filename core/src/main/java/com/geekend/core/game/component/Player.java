package com.geekend.core.game.component;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Key;

import com.geekend.core.game.input.InputOracle;

public class Player {

	private float x = 0;
	private float y = 0;

	private GroupLayer layer;
	private ImageLayer imageLayer;

	public void init(final GroupLayer layer, final int i, final int j) {
		this.layer = layer;
		final Image pea = assets().getImage("images/player.gif");
		imageLayer = graphics().createImageLayer(pea);
		layer.add(imageLayer);
	}

	public float getSpeed() {
		return 0.1f;
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public void updateY(final float delta) {
		y += delta;
	}

	public void updateX(final float delta) {
		x += delta;
	}

	public void update(final float delta) {
		final float distance = getSpeed() * delta;
		if (InputOracle.isKeyPressed(Key.UP))
			updateY(-1 * distance);
		if (InputOracle.isKeyPressed(Key.DOWN))
			updateY(distance);
		if (InputOracle.isKeyPressed(Key.LEFT))
			updateX(-1 * distance);
		if (InputOracle.isKeyPressed(Key.RIGHT))
			updateX(distance);
	}

	public void paint(final float alpha) {
		imageLayer.setTranslation(x, y);
	}
}
