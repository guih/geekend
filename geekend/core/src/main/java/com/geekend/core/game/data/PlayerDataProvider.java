package com.geekend.core.game.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.geekend.core.net.CommunicationService;
import com.geekend.core.net.event.PlayerUpdatePositionEvent;
import com.geekend.core.net.event.PlayerUpdatePositionHandler;
import com.geekend.core.net.remote.WebSocketRemoteConnection;

public class PlayerDataProvider {

	private final CommunicationService service;
	private final Map<String, PlayerData> otherPlayers = new HashMap<String, PlayerData>();
	private final PlayerData playerData;

	public PlayerDataProvider() {
		playerData = new PlayerData(generateRandomId(), 310f, 357f);

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

	public PlayerData getMainPlayerData() {
		return playerData;
	}

	public Collection<PlayerData> getOtherPlayers() {
		return otherPlayers.values();
	}

	public void multicastPlayerData() {
		service.send(new PlayerUpdatePositionEvent(playerData));
	}

	private void updatePlayerData(final PlayerUpdatePositionEvent event) {
		final PlayerData data = getPlayerData(event.getPlayerId());
		event.updateRealInstanceFromEvent(data);
	}

	private PlayerData getPlayerData(final String id) {
		if (!otherPlayers.containsKey(id)) otherPlayers.put(id, new PlayerData(id));
		return otherPlayers.get(id);
	}

	private String generateRandomId() {
		return "player" + Math.random() * 1000;
	}
}
