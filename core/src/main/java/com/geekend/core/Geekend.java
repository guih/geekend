package com.geekend.core;

import playn.core.Game;
import playn.core.PlayN;
import playn.core.Touch;

import com.geekend.core.game.GameModule;
import com.geekend.core.game.module.Multiplayer;

public class Geekend implements Game {

	private GameModule currentModule = null;
	private final GameModule multiplayer = new Multiplayer();

	@Override
	public void init() {
		try {
			PlayN.touch().setListener(new Touch.Adapter() {
				@Override
				public void onTouchStart(final Touch.Event[] touches) {
					if (touches.length > 1)
						switchModule(multiplayer);
				}
			});
		} catch (final UnsupportedOperationException e) {
			// no support for touch; no problem
		}

		switchModule(multiplayer);
	}

	@Override
	public void paint(final float alpha) {
		currentModule.paint(alpha);
	}

	@Override
	public void update(final float delta) {
		currentModule.update(delta);
	}

	@Override
	public int updateRate() {
		return 25;
	}

	public void switchModule(final GameModule module) {
		if (currentModule != null)
			currentModule.destroy();
		currentModule = module;
		currentModule.init();
	}

}
