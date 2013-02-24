package com.geekend.core.net.event;

import playn.core.Json.Object;
import playn.core.json.JsonImpl;

import com.geekend.core.game.component.PlayerStates;
import com.geekend.core.game.data.PlayerData;
import com.geekend.core.net.remote.RemoteEvent;
import com.geekend.core.net.remote.RemoteEventFactory;
import com.geekend.core.net.remote.RemoteEventType;

public class PlayerUpdatePositionEvent implements RemoteEvent<PlayerData> {

	private static final long serialVersionUID = 1L;

	private static RemoteEventType<PlayerUpdatePositionEvent> TYPE = null;

	public String id;
	public float x;
	public float y;
	public float angle;
	public int state;

	PlayerUpdatePositionEvent() {}

	public PlayerUpdatePositionEvent(final PlayerData data) {
		updateEventFromRealInstance(data);
	}

	public static RemoteEventType<PlayerUpdatePositionEvent> getType() {
		return TYPE == null ? TYPE = new RemoteEventType<PlayerUpdatePositionEvent>(new RemoteEventFactory<PlayerUpdatePositionEvent>() {
			@Override
			public boolean supports(final String className) {
				return PlayerUpdatePositionEvent.class.getName().equals(className);
			}

			@Override
			public PlayerUpdatePositionEvent fromJson(final Object object) {
				final PlayerUpdatePositionEvent event = new PlayerUpdatePositionEvent();
				event.id = object.getString("playerId", "");
				event.angle = object.getNumber("playerAngle", 0);
				event.x = object.getNumber("playerX", 0);
				event.y = object.getNumber("playerY", 0);
				return event;
			}

			@Override
			public String toJson(final PlayerUpdatePositionEvent event) {
				return new JsonImpl().newWriter().object().value("playerId", event.id).value("playerAngle", event.angle).value("playerX", event.x)
						.value("playerY", event.y).end().write();
			}
		}) : TYPE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RemoteEventType<PlayerUpdatePositionEvent> getEventType() {
		return getType();
	}

	public String getPlayerId() {
		return id;
	}

	@Override
	public void updateRealInstanceFromEvent(final PlayerData data) {
		data.id = id;
		data.x = x;
		data.y = y;
		data.angle = angle;
		data.state = PlayerStates.values()[state];
	}

	@Override
	public void updateEventFromRealInstance(final PlayerData data) {
		id = data.id;
		x = data.x;
		y = data.y;
		angle = data.angle;
		state = data.state.ordinal();
	}
}
