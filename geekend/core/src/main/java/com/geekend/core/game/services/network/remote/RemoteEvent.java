package com.geekend.core.game.services.network.remote;

import java.io.Serializable;

public interface RemoteEvent<U> extends Serializable {

	public static final String EVENTCLASS_ATTRIB_NAME = "class";

	<T extends RemoteEvent<U>> RemoteEventType<T> getEventType();

	public void updateRealInstanceFromEvent(final U data);

	public void updateEventFromRealInstance(final U data);

}
