package com.geekend.core.game.module;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.PlayN;

import com.geekend.core.game.GameModule;
import com.geekend.core.game.component.MainPlayer;
import com.geekend.core.net.CommunicationService;
import com.geekend.core.net.event.PlayerUpdatePositionEvent;
import com.geekend.core.net.event.PlayerUpdatePositionHandler;
import com.geekend.core.net.remote.WebSocketRemoteConnection;

public class Multiplayer implements GameModule {

	private final MainPlayer mainPlayer = new MainPlayer();

	private GroupLayer gameLayer;

	private GroupLayer playerLayer;

	private Image bgImage;

	private CommunicationService service;

	@Override
	public void init(final GroupLayer rootLayer) {
		gameLayer = graphics().createGroupLayer();
		rootLayer.add(gameLayer);

		bgImage = assets().getImage("images/bg.jpg");
		final ImageLayer bgLayer = graphics().createImageLayer(bgImage);
		gameLayer.add(bgLayer);

		playerLayer = graphics().createGroupLayer();
		gameLayer.add(playerLayer);

		mainPlayer.init(playerLayer, 310f, 357f);

		WebSocketRemoteConnection connection = new WebSocketRemoteConnection();
		connection.connect();
		service = new CommunicationService(connection);
		service.register(PlayerUpdatePositionEvent.getType(), new PlayerUpdatePositionHandler() {
			@Override
			public void onEvent(PlayerUpdatePositionEvent event) {
				PlayN.log().info("received + " + event.getPlayerX() + " : " + event.getPlayerY());
			}
		});
	}

	@Override
	public void destroy() {
		playerLayer.destroy();
		gameLayer.destroy();
	}

	@Override
	public void update(final float delta) {
		mainPlayer.update(delta);
		service.send(new PlayerUpdatePositionEvent("", mainPlayer.getX(), mainPlayer.getY(), mainPlayer.getSpeed()));
	}

	@Override
	public void paint(final float alpha) {
		gameLayer.setTranslation(graphics().width() / 2, graphics().height() / 2);
		gameLayer.setOrigin(mainPlayer.getX(), mainPlayer.getY());
		mainPlayer.paint(alpha);
	}
}
