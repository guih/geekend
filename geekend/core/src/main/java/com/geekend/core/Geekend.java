package com.geekend.core;

import static playn.core.PlayN.graphics;
import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.PlayN;
import playn.core.Touch;

import com.geekend.core.game.components.ControllableComponentFactory;
import com.geekend.core.game.components.controllable.ControllableComponent;
import com.geekend.core.game.components.controllable.Data;
import com.geekend.core.game.components.controllable.player.MainPlayerController;
import com.geekend.core.game.components.controllable.player.OpponentPlayerController;
import com.geekend.core.game.components.controllable.player.Player;
import com.geekend.core.game.components.modules.GameModule;
import com.geekend.core.game.components.modules.Multiplayer;
import com.geekend.core.game.services.data.DataProvider;
import com.geekend.core.game.utils.sprite.SpriteLoader;

public class Geekend implements Game {

	private GameModule currentModule = null;
	private final GroupLayer rootLayer = graphics().rootLayer();
	private final GameModule multiplayer;

	public Geekend() {
		multiplayer = new Multiplayer(new DataProvider(), new ControllableComponentFactory() {

			private static final String IMAGE = "sprites/player.png";
			private static final String JSON = "sprites/player.json";

			@Override
			public ControllableComponent<Player> createMain(final Data<Player> data) {
				return new ControllableComponent<Player>(new MainPlayerController(), data, SpriteLoader.getSprite(IMAGE, JSON));
			}

			@Override
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public ControllableComponent create(final Data data) {
				return new ControllableComponent(new OpponentPlayerController(), data, SpriteLoader.getSprite(IMAGE, JSON));
			}
		});
	}

	@Override
	public void init() {
		try {
			PlayN.touch().setListener(new Touch.Adapter() {
				@Override
				public void onTouchStart(final Touch.Event[] touches) {
					if (touches.length > 1) switchModule(multiplayer);
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
		if (currentModule != null) currentModule.destroy();
		rootLayer.clear();
		currentModule = module;
		currentModule.init(rootLayer);
	}

}
