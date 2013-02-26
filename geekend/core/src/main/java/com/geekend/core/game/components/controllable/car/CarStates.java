package com.geekend.core.game.components.controllable.car;

import com.geekend.core.game.components.controllable.State;

public enum CarStates implements State<Car> {
	NORMAL(new int[] { 0 }, 50f);

	private final int[] spriteIndexes;
	private final float delay;

	private CarStates(final int[] spriteIndexes, final float delay) {
		this.spriteIndexes = spriteIndexes;
		this.delay = delay;
	}

	@Override
	public float getCountdownDelay() {
		return delay;
	}

	@Override
	public int numSprites() {
		return spriteIndexes.length;
	}

	@Override
	public int getSpriteIndex(final int index) {
		return spriteIndexes[index];
	}

	@Override
	public int ordinalRepresentation() {
		return ordinal();
	}
}
