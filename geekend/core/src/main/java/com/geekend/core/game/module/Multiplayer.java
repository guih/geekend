package com.geekend.core.game.module;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import java.util.HashMap;
import java.util.Map;

import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;

import com.geekend.core.game.GameModule;
import com.geekend.core.game.component.MainPlayerController;
import com.geekend.core.game.component.OpponentPlayerController;
import com.geekend.core.game.component.Player;
import com.geekend.core.game.data.PlayerData;
import com.geekend.core.game.data.PlayerDataProvider;

public class Multiplayer implements GameModule {

	private final Player mainPlayer;

	private final Map<PlayerData, Player> otherPlayers = new HashMap<PlayerData, Player>();

	private GroupLayer gameLayer;

	private GroupLayer playerLayer;

	private Image bgImage;

	private final PlayerDataProvider dataProvider;

	public Multiplayer(final PlayerDataProvider dataProvider) {
		this.dataProvider = dataProvider;
		mainPlayer = new Player(new MainPlayerController(), dataProvider.getMainPlayerData());
	}

	@Override
	public void init(final GroupLayer rootLayer) {
		gameLayer = graphics().createGroupLayer();
		rootLayer.add(gameLayer);

		bgImage = assets().getImage("images/bg.jpg");
		final ImageLayer bgLayer = graphics().createImageLayer(bgImage);
		gameLayer.add(bgLayer);

		playerLayer = graphics().createGroupLayer();
		gameLayer.add(playerLayer);

		mainPlayer.init(playerLayer);
	}

	@Override
	public void destroy() {
		playerLayer.destroy();
		gameLayer.destroy();
	}

	@Override
	public void update(final float delta) {
		mainPlayer.update(delta);
		for (final PlayerData playerData : dataProvider.getOtherPlayers()) {
			if (otherPlayers.containsKey(playerData)) continue;
			final Player player = new Player(new OpponentPlayerController(), playerData);
			otherPlayers.put(playerData, player);
			player.init(playerLayer);
		}
		for (final Player player : otherPlayers.values())
			player.update(delta);
		dataProvider.multicastPlayerData();
	}

	@Override
	public void paint(final float alpha) {
		final PlayerData mainPlayerData = dataProvider.getMainPlayerData();
		gameLayer.setOrigin(mainPlayerData.x, mainPlayerData.y);
		gameLayer.setTranslation(graphics().width() / 2, graphics().height() / 2);
		mainPlayer.paint(alpha);
		for (final Player player : otherPlayers.values())
			player.paint(alpha);
	}
}
