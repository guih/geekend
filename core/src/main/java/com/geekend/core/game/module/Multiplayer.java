package com.geekend.core.game.module;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Key;

import com.geekend.core.game.GameModule;
import com.geekend.core.game.component.Console;
import com.geekend.core.game.component.Player;
import com.geekend.core.game.input.InputOracle;

public class Multiplayer implements GameModule {

	private GroupLayer layer;
	private final Player mainPlayer = new Player();
	private Console console;

	@Override
	public void init() {
		layer = graphics().createGroupLayer();
		graphics().rootLayer().add(layer);

		final Image bgImage = assets().getImage("images/bg.jpg");
		final ImageLayer bgLayer = graphics().createImageLayer(bgImage);
		layer.add(bgLayer);
		bgLayer.setTranslation(-200, bgImage.height() / 2);

		mainPlayer.init(layer, 0, 0);
		console = new Console();
		console.init(graphics().rootLayer());
	}

	@Override
	public void destroy() {
	}

	@Override
	public void update(final float delta) {
		mainPlayer.update(delta);

		if (InputOracle.isKeyPressed(Key.SPACE))
			console.log("Coordenada: " + mainPlayer.getX() + " - " + mainPlayer.getY());
	}

	@Override
	public void paint(final float alpha) {
		layer.setTranslation(graphics().width() / 2, graphics().height() / 2);
		layer.setOrigin(mainPlayer.getX(), mainPlayer.getY());
		mainPlayer.paint(alpha);
	}
}
