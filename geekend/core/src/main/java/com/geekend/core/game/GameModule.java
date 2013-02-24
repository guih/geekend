package com.geekend.core.game;

import playn.core.GroupLayer;

/**
 * A module represents a screen / level.
 */
public interface GameModule extends GameComponent {

	/**
	 * Initializes this module. Here is where listeners should be wired up and
	 * resources loaded.
	 */
	void init(GroupLayer rootLayer);

}
