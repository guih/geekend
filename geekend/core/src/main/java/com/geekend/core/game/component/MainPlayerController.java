package com.geekend.core.game.component;

import playn.core.Key;

import com.geekend.core.game.data.PlayerData;
import com.geekend.core.game.input.InputOracle;

public final class MainPlayerController implements PlayerController {

	private static final float SPEED = 0.13f;
	private static final float TURNING_SPEED = 1f / 200f;

	@Override
	public void updatePlayerData(final PlayerData data, final float delta) {
		final float distance = InputOracle.isKeyPressed(Key.UP) || InputOracle.isKeyPressed(Key.DOWN) ? SPEED * delta : 0;
		final float direction = InputOracle.isKeyPressed(Key.DOWN) ? -1 : 1;
		if (InputOracle.isKeyPressed(Key.LEFT)) data.angle -= delta * TURNING_SPEED;
		if (InputOracle.isKeyPressed(Key.RIGHT)) data.angle += delta * TURNING_SPEED;
		data.x += Math.cos(data.angle) * distance * direction;
		data.y += Math.sin(data.angle) * distance * direction;
		data.state = PlayerStates.determinePlayerState();
	}
}
