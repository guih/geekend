package com.geekend.core.game.components.controllable.player;

import playn.core.Key;

import com.geekend.core.game.components.controllable.Controller;
import com.geekend.core.game.components.controllable.Data;
import com.geekend.core.game.components.controllable.State;
import com.geekend.core.game.services.input.InputOracle;

public final class MainPlayerController implements Controller<Player> {

	private static final float SPEED = 0.13f;
	private static final float TURNING_SPEED = 1f / 200f;

	@Override
	public void initializeData(final Data<Player> data) {
		data.state = PlayerStates.WAITING;
	}

	@Override
	public void updateData(final Data<Player> data, final float delta) {
		final float distance = InputOracle.isKeyPressed(Key.UP) || InputOracle.isKeyPressed(Key.DOWN) ? SPEED * delta : 0;
		final float direction = InputOracle.isKeyPressed(Key.DOWN) ? -1 : 1;
		if (InputOracle.isKeyPressed(Key.LEFT)) data.angle -= delta * TURNING_SPEED;
		if (InputOracle.isKeyPressed(Key.RIGHT)) data.angle += delta * TURNING_SPEED;
		data.x += Math.cos(data.angle) * distance * direction;
		data.y += Math.sin(data.angle) * distance * direction;
		data.state = getState(data);
	}

	public State<Player> getState(final Data<Player> data) {
		if (InputOracle.isKeyPressed(Key.F)) return PlayerStates.SHOOTING;
		else if (InputOracle.isKeyPressed(Key.UP) || InputOracle.isKeyPressed(Key.DOWN)) return InputOracle.isKeyPressed(Key.TAB) ? PlayerStates.SKATING
				: PlayerStates.RUNNING;
		else
			return PlayerStates.WAITING;
	}
}
