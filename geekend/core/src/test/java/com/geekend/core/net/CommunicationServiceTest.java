package com.geekend.core.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import playn.core.json.JsonImpl;

import com.geekend.core.game.components.controllable.Data;
import com.geekend.core.game.components.controllable.player.Player;
import com.geekend.core.game.components.controllable.player.PlayerStates;
import com.geekend.core.game.components.controllable.player.PlayerUpdatePositionEvent;
import com.geekend.core.game.components.controllable.player.PlayerUpdatePositionHandler;
import com.geekend.core.game.services.network.CommunicationService;
import com.geekend.core.game.services.network.remote.RemoteConnection;
import com.geekend.core.game.services.network.remote.RemoteMessageListener;

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
		final ArgumentCaptor<RemoteMessageListener> captor = ArgumentCaptor.forClass(RemoteMessageListener.class);
		service = new CommunicationService(webSocket);
		verify(webSocket).registerMessageListener(captor.capture());
		messageListener = captor.getValue();
		condition = false;
		final Data<Player> data = new Data<Player>(playerId);
		data.id = playerId = "2";
		data.x = playerX = 15F;
		data.y = playerY = 10F;
		data.angle = playerAngle = 45F;
		data.state = PlayerStates.RUNNING;
		event = new PlayerUpdatePositionEvent(data);
		eventJsonString = new JsonImpl().newWriter().object()
				.value(PlayerUpdatePositionEvent.EVENTCLASS_ATTRIB_NAME, PlayerUpdatePositionEvent.class.getName())
				.value(PlayerUpdatePositionEvent.ID_ATTRIB_NAME, playerId).value(PlayerUpdatePositionEvent.ANGLE_ATTRIB_NAME, playerAngle)
				.value(PlayerUpdatePositionEvent.XPOS_ATTRIB_NAME, playerX).value(PlayerUpdatePositionEvent.YPOS_ATTRIB_NAME, playerY)
				.value(PlayerUpdatePositionEvent.STATE_ATTRIB_NAME, 2).end().write();
	}

	@Test
	public void whenAnEventHappensRegisteredListenersShouldBeNotifiedWithTheEvent() throws Exception {
		service.register(PlayerUpdatePositionEvent.getType(), new PlayerUpdatePositionHandler() {
			@Override
			public void onEvent(final PlayerUpdatePositionEvent event) {
				assertEquals(playerId, event.getPlayerId());
				assertEquals(playerX, event.x, 0);
				assertEquals(playerY, event.y, 0);
				assertEquals(playerAngle, event.angle, 0);
				condition = true;
			}
		});
		assertFalse(condition);
		messageListener.onMessage(eventJsonString);
		assertTrue(condition);
	}

	@Test
	public void shouldBeAbleToSendAnEvent() throws Exception {
		when(webSocket.isConnected()).thenReturn(true);
		service.send(event);
		verify(webSocket).send(eventJsonString);
	}
}
