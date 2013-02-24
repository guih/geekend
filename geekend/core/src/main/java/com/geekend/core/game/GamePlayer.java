package com.geekend.core.game;

import playn.core.GroupLayer;


/**
 * A module represents a player.
 */
public interface GamePlayer extends GameComponent {

	void init(GroupLayer layer, float x, float y);
}
