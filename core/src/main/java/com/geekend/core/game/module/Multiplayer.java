package com.geekend.core.game.module;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Key;

import com.geekend.core.game.GameModule;
import com.geekend.core.game.input.InputOracle;
import com.geekend.core.game.model.Player;

public class Multiplayer implements GameModule {

	private InputOracle inputOracle;
	private final Player mainPlayer = new Player();
	private ImageLayer peaLayer;

	@Override
	public void init(final InputOracle inputOracle) {
		this.inputOracle = inputOracle;
		final Image pea = assets().getImage("images/player.gif");
		peaLayer = graphics().createImageLayer(pea);
		graphics().rootLayer().add(peaLayer);
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void update(final float delta) {
		final float distance = mainPlayer.getSpeed() * delta;
		if (inputOracle.isKeyPressed(Key.UP))
			mainPlayer.updateY(-1 * distance);
		if (inputOracle.isKeyPressed(Key.DOWN))
			mainPlayer.updateY(distance);
		if (inputOracle.isKeyPressed(Key.LEFT))
			mainPlayer.updateX(-1 * distance);
		if (inputOracle.isKeyPressed(Key.RIGHT))
			mainPlayer.updateX(distance);
	}

	@Override
	public void paint(final float alpha) {
		peaLayer.setTranslation(mainPlayer.getX(), mainPlayer.getY());
	}
}
