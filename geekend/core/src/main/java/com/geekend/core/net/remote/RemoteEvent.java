package com.geekend.core.net.remote;

import java.io.Serializable;

public interface RemoteEvent<U> extends Serializable {

	<T extends RemoteEvent<U>> RemoteEventType<T> getEventType();

	public void updateRealInstanceFromEvent(final U data);

	public void updateEventFromRealInstance(final U data);

}
