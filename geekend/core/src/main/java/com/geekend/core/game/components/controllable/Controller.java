package com.geekend.core.game.components.controllable;

public interface Controller<T> {

	public void initializeData(Data<T> data);

	public void updateData(final Data<T> data, final float delta);
}
