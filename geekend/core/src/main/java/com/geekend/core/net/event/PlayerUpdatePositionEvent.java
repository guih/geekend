package com.geekend.core.net.event;

import playn.core.Json.Object;
import playn.core.json.JsonImpl;

import com.geekend.core.net.remote.RemoteEvent;
import com.geekend.core.net.remote.RemoteEventFactory;
import com.geekend.core.net.remote.RemoteEventType;

public class PlayerUpdatePositionEvent implements RemoteEvent {

	private static final long serialVersionUID = 1L;

	private static RemoteEventType<PlayerUpdatePositionEvent> TYPE = null;

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

	public static RemoteEventType<PlayerUpdatePositionEvent> getType() {
		return TYPE == null ? TYPE = new RemoteEventType<PlayerUpdatePositionEvent>(
				new RemoteEventFactory<PlayerUpdatePositionEvent>() {
					@Override
					public boolean supports(String className) {
						return PlayerUpdatePositionEvent.class.getName().equals(className);
					}

					@Override
					public PlayerUpdatePositionEvent fromJson(Object object) {
						PlayerUpdatePositionEvent event = new PlayerUpdatePositionEvent();
						event.playerId = object.getString("playerId", "");
						event.playerAngle = object.getNumber("playerAngle", 0);
						event.playerX = object.getNumber("playerX", 0);
						event.playerY = object.getNumber("playerY", 0);
						return event;
					}

					@Override
					public String toJson(PlayerUpdatePositionEvent event) {
						return new JsonImpl().newWriter().object().value("playerId", event.playerId)
								.value("playerAngle", event.playerAngle).value("playerX", event.playerX)
								.value("playerY", event.playerY).end().write();
					}
				}) : TYPE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RemoteEventType<PlayerUpdatePositionEvent> getEventType() {
		return getType();
	}

}
