package com.geekend.core.game.components.controllable.player;

import com.geekend.core.game.components.controllable.Controller;
import com.geekend.core.game.components.controllable.Data;

public final class OpponentPlayerController implements Controller<Player> {

	@Override
	public void initializeData(final Data<Player> data) {
		if (data.state == null) data.state = PlayerStates.WAITING;
	}

	@Override
	public void updateData(final Data<Player> data, final float delta) {}
}
