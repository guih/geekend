package com.geekend.core.game.components.modules;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import java.util.HashMap;
import java.util.Map;

import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;

import com.geekend.core.game.components.ControllableComponentFactory;
import com.geekend.core.game.components.controllable.ControllableComponent;
import com.geekend.core.game.components.controllable.Data;
import com.geekend.core.game.components.controllable.player.Player;
import com.geekend.core.game.services.data.DataProvider;

public class Multiplayer implements GameModule {

	private final DataProvider dataProvider;

	private final ControllableComponentFactory componentFactory;

	private final Map<Data<?>, ControllableComponent<?>> otherControllableComponents = new HashMap<Data<?>, ControllableComponent<?>>();

	private final ControllableComponent<Player> player;

	private Image bgImage;

	private GroupLayer gameLayer;

	private GroupLayer componentLayer;

	public Multiplayer(final DataProvider dataProvider, final ControllableComponentFactory componentFactory) {
		this.dataProvider = dataProvider;
		this.componentFactory = componentFactory;
		player = componentFactory.createMain(dataProvider.getMainPlayerData());
	}

	@Override
	public void init(final GroupLayer rootLayer) {
		gameLayer = graphics().createGroupLayer();
		rootLayer.add(gameLayer);

		bgImage = assets().getImage("images/bg.jpg");
		final ImageLayer bgLayer = graphics().createImageLayer(bgImage);
		gameLayer.add(bgLayer);

		componentLayer = graphics().createGroupLayer();
		gameLayer.add(componentLayer);

		player.init(componentLayer);
	}

	@Override
	public void destroy() {
		componentLayer.destroy();
		gameLayer.destroy();
	}

	@Override
	public void update(final float delta) {
		player.update(delta);
		for (final Data<?> data : dataProvider.getControllableComponents()) {
			if (otherControllableComponents.containsKey(data)) continue;
			final ControllableComponent<?> controllableComponent = componentFactory.create(data);
			otherControllableComponents.put(data, controllableComponent);
			controllableComponent.init(componentLayer);
		}
		for (final ControllableComponent<?> controllableComponent : otherControllableComponents.values())
			controllableComponent.update(delta);
		dataProvider.multicastPlayerData();
	}

	@Override
	public void paint(final float alpha) {
		final Data<Player> mainPlayerData = dataProvider.getMainPlayerData();
		gameLayer.setOrigin(mainPlayerData.x, mainPlayerData.y);
		gameLayer.setTranslation(graphics().width() / 2, graphics().height() / 2);
		player.paint(alpha);
		for (final ControllableComponent<?> controllableComponent : otherControllableComponents.values())
			controllableComponent.paint(alpha);
	}
}
