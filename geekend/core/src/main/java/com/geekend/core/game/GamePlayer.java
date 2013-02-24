package com.geekend.core.game;

import playn.core.GroupLayer;

/**
 * A module represents a player.
 */
public interface GamePlayer extends GameComponent {
	public void init(final GroupLayer layer, final int x, final int y);
}
