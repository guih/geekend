package com.geekend.core.game.components.controllable.player;

import playn.core.Json.Object;
import playn.core.json.JsonImpl;

import com.geekend.core.game.components.controllable.Data;
import com.geekend.core.game.services.network.remote.RemoteEvent;
import com.geekend.core.game.services.network.remote.RemoteEventFactory;
import com.geekend.core.game.services.network.remote.RemoteEventType;

public class PlayerUpdatePositionEvent implements RemoteEvent<Data<Player>> {

	public static final String ID_ATTRIB_NAME = "id";
	public static final String ANGLE_ATTRIB_NAME = "angle";
	public static final String XPOS_ATTRIB_NAME = "x";
	public static final String YPOS_ATTRIB_NAME = "y";
	public static final String STATE_ATTRIB_NAME = "state";

	private static final long serialVersionUID = 1L;

	private static RemoteEventType<PlayerUpdatePositionEvent> TYPE = null;

	public String id;
	public float x;
	public float y;
	public float angle;
	public int state;

	PlayerUpdatePositionEvent() {}

	public PlayerUpdatePositionEvent(final Data<Player> data) {
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
				event.id = object.getString(ID_ATTRIB_NAME, "");
				event.angle = object.getNumber(ANGLE_ATTRIB_NAME, 0);
				event.x = object.getNumber(XPOS_ATTRIB_NAME, 0);
				event.y = object.getNumber(YPOS_ATTRIB_NAME, 0);
				event.state = (int) object.getNumber(STATE_ATTRIB_NAME, 0);
				return event;
			}

			@Override
			public String toJson(final PlayerUpdatePositionEvent event) {
				return new JsonImpl().newWriter().object().value(EVENTCLASS_ATTRIB_NAME, PlayerUpdatePositionEvent.class.getName())
						.value(ID_ATTRIB_NAME, event.id).value(ANGLE_ATTRIB_NAME, event.angle).value(XPOS_ATTRIB_NAME, event.x)
						.value(YPOS_ATTRIB_NAME, event.y).value(STATE_ATTRIB_NAME, event.state).end().write();
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
	public void updateRealInstanceFromEvent(final Data<Player> data) {
		data.id = id;
		data.x = x;
		data.y = y;
		data.angle = angle;
		data.state = PlayerStates.values()[state];
	}

	@Override
	public void updateEventFromRealInstance(final Data<Player> data) {
		id = data.id;
		x = data.x;
		y = data.y;
		angle = data.angle;
		state = data.state.ordinalRepresentation();
	}
}
