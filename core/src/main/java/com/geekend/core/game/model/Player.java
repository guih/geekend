package com.geekend.core.game.model;

public class Player {

	private float x = 0;
	private float y = 0;

	public Player() {
	}

	public float getSpeed() {
		return 0.1f;
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public void updateY(final float delta) {
		y += delta;
	}

	public void updateX(final float delta) {
		x += delta;
	}
}
