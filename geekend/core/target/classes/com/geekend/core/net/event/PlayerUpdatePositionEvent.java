package com.geekend.core.net.event;

import com.geekend.core.net.remote.RemoteEvent;
import com.geekend.core.net.remote.RemoteEventType;

public class PlayerUpdatePositionEvent implements RemoteEvent {

	private static final long serialVersionUID = 1L;

	private static RemoteEventType<PlayerUpdatePositionHandler> TYPE = null;

	private String playerId;

	private float playerX;

	private float playerY;

	private float playerAngle;

	PlayerUpdatePositionEvent() {}

	public PlayerUpdatePositionEvent(String playerId, float playerX, float playerY, float playerAngle) {
		this.playerId = playerId;
		this.playerX = playerX;
		this.playerY = playerY;
		this.playerAngle = playerAngle;
	}

	public String getPlayerId() {
		return playerId;
	}

	public float getPlayerX() {
		return playerX;
	}

	public float getPlayerY() {
		return playerY;
	}

	public float getAngle() {
		return playerAngle;
	}

	public static RemoteEventType<PlayerUpdatePositionHandler> getType() {
		return TYPE == null ? TYPE = new RemoteEventType<PlayerUpdatePositionHandler>(PlayerUpdatePositionEvent.class)
				: TYPE;
	}

}
