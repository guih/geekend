package com.geekend.core.game.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geekend.core.net.CommunicationService;
import com.geekend.core.net.event.PlayerUpdatePositionEvent;
import com.geekend.core.net.event.PlayerUpdatePositionHandler;
import com.geekend.core.net.remote.WebSocketRemoteConnection;

public class PlayerDataProvider {

	private CommunicationService service;
	private final Map<String, PlayerData> otherPlayers = new HashMap<String, PlayerData>();
	private PlayerData playerData;
	
	public PlayerDataProvider() {
		playerData = new PlayerData(generateRandomId(), 310f, 357f);

		WebSocketRemoteConnection connection = new WebSocketRemoteConnection();
		service = new CommunicationService(connection);
		service.register(PlayerUpdatePositionEvent.getType(), new PlayerUpdatePositionHandler() {
			@Override
			public void onEvent(PlayerUpdatePositionEvent event) {
				updatePlayerData(event);
			}
		});
		connection.connect();
	}
	
	public PlayerData getMainPlayerData() {
		return playerData;
	}
	
	public List<PlayerData> getOtherPlayers() {
		return new ArrayList<PlayerData>();
	}
	
	private void updatePlayerData(PlayerUpdatePositionEvent event) {
		PlayerData data = getPlayerData(event.getPlayerId());
		data.x = event.getPlayerX();
		data.y = event.getPlayerY();
		data.angle = event.getAngle();
	}

	private PlayerData getPlayerData(String id) {
		if (!otherPlayers.containsKey(id)) otherPlayers.put(id, new PlayerData(id));
		return otherPlayers.get(id);
	}

	private String generateRandomId() {
		return "player" + Math.random() * 1000;
	}

	public void multicastPlayerData() {
		service.send(new PlayerUpdatePositionEvent(playerData.id, playerData.x, playerData.y, playerData.angle));
	}
}
