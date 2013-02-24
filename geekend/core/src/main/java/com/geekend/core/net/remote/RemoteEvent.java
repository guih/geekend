package com.geekend.core.net.remote;

import java.io.Serializable;

public interface RemoteEvent extends Serializable {

	<T extends RemoteEvent> RemoteEventType<T> getEventType();

}
