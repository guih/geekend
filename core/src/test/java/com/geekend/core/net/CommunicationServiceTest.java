package com.geekend.core.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import playn.core.json.JsonImpl;

import com.geekend.core.net.event.PlayerUpdatePositionEvent;
import com.geekend.core.net.event.PlayerUpdatePositionHandler;
import com.geekend.core.net.remote.RemoteConnection;
import com.geekend.core.net.remote.RemoteMessageListener;

public class CommunicationServiceTest {

	private CommunicationService service;

	@Mock
	private RemoteConnection webSocket;

	private RemoteMessageListener messageListener;

	private boolean condition;

	private String playerId;

	private float playerX;

	private float playerY;

	private float playerAngle;

	private PlayerUpdatePositionEvent event;

	private String eventJsonString;

	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		ArgumentCaptor<RemoteMessageListener> captor = ArgumentCaptor.forClass(RemoteMessageListener.class);
		service = new CommunicationService(webSocket);
		verify(webSocket).registerMessageListener(captor.capture());
		messageListener = captor.getValue();
		condition = false;
		playerId = "2";
		playerX = 15F;
		playerY = 10F;
		playerAngle = 45F;
		event = new PlayerUpdatePositionEvent(playerId, playerX, playerY, playerAngle);
		eventJsonString = new JsonImpl().newWriter().object().value("class", PlayerUpdatePositionEvent.class.getName())
				.value("playerId", playerId).value("playerX", playerX).value("playerY", playerY)
				.value("playerAngle", playerAngle).end().write();
	}

	@Test
	public void whenAnEventHappensRegisteredListenersShouldBeNotifiedWithTheEvent() throws Exception {
		service.register(PlayerUpdatePositionEvent.getType(), new PlayerUpdatePositionHandler() {
			@Override
			public void onEvent(final PlayerUpdatePositionEvent event) {
				assertEquals(playerId, event.getPlayerId());
				assertEquals(playerX, event.getPlayerX(), 0);
				assertEquals(playerY, event.getPlayerY(), 0);
				assertEquals(playerAngle, event.getAngle(), 0);
				condition = true;
			}
		});
		assertFalse(condition);
		messageListener.onMessage(eventJsonString);
		assertTrue(condition);
	}

	@Test
	public void shouldBeAbleToSendAnEvent() throws Exception {
		service.send(event);
		verify(webSocket).send(eventJsonString);
	}
}
