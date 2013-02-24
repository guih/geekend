package com.geekend.core.game.component;

import com.geekend.core.game.data.PlayerData;

public interface PlayerController {

	public abstract void updatePlayerData(PlayerData data, float delta);

}