package com.geekend.core.game.components.controllable;

public interface State<T> {

	public abstract int ordinalRepresentation();

	public abstract float getCountdownDelay();

	public abstract int numSprites();

	public abstract int getSpriteIndex(int index);
}
