package com.geekend.core.game.services.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.geekend.core.game.components.controllable.Data;
import com.geekend.core.game.components.controllable.player.Player;
import com.geekend.core.game.components.controllable.player.PlayerUpdatePositionEvent;
import com.geekend.core.game.components.controllable.player.PlayerUpdatePositionHandler;
import com.geekend.core.game.services.network.CommunicationService;
import com.geekend.core.game.services.network.remote.WebSocketRemoteConnection;

public class DataProvider {

	private final CommunicationService service;
	private final Map<String, Data<Player>> otherPlayers = new HashMap<String, Data<Player>>();
	private final Data<Player> playerData;

	public DataProvider() {
		playerData = new Data<Player>(generateRandomId(), 310f, 357f);

		final WebSocketRemoteConnection connection = new WebSocketRemoteConnection();
		service = new CommunicationService(connection);
		service.register(PlayerUpdatePositionEvent.getType(), new PlayerUpdatePositionHandler() {
			@Override
			public void onEvent(final PlayerUpdatePositionEvent event) {
				updatePlayerData(event);
			}
		});
		connection.connect();
	}

	public Data<Player> getMainPlayerData() {
		return playerData;
	}

	public Collection<Data<Player>> getControllableComponents() {
		return otherPlayers.values();
	}

	public void multicastPlayerData() {
		service.send(new PlayerUpdatePositionEvent(playerData));
	}

	private void updatePlayerData(final PlayerUpdatePositionEvent event) {
		final Data<Player> data = getPlayerData(event.getPlayerId());
		event.updateRealInstanceFromEvent(data);
	}

	private Data<Player> getPlayerData(final String id) {
		if (!otherPlayers.containsKey(id)) otherPlayers.put(id, new Data<Player>(id));
		return otherPlayers.get(id);
	}

	private String generateRandomId() {
		return "player" + Math.random() * 1000;
	}
}
