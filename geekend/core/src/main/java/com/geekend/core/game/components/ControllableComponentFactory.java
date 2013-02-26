package com.geekend.core.game.components;

import com.geekend.core.game.components.controllable.ControllableComponent;
import com.geekend.core.game.components.controllable.Data;
import com.geekend.core.game.components.controllable.player.Player;

public interface ControllableComponentFactory {

	ControllableComponent<Player> createMain(Data<Player> mainPlayerData);

	ControllableComponent<?> create(Data<?> data);
}
