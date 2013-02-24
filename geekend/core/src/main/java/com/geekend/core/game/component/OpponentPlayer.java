package com.geekend.core.game.component;

import com.geekend.core.game.data.PlayerData;

public class OpponentPlayer extends AbstractPlayer {

	public OpponentPlayer(PlayerData playerData) {
		this.data = playerData;
	}

	@Override
	public void updatePlayerData(final float delta) {}
}
