package com.geekend.core.game.module;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.Image;
import playn.core.ImageLayer;

import com.geekend.core.game.GameModule;

public class Multiplayer implements GameModule {

	@Override
	public void init() {
		final Image pea = assets().getImage("images/player.gif");
		final ImageLayer peaLayer = graphics().createImageLayer(pea);
		graphics().rootLayer().add(peaLayer);
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void update(final float delta) {
	}

	@Override
	public void paint(final float alpha) {
	}
}
