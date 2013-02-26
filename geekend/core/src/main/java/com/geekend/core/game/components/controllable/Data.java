package com.geekend.core.game.components.controllable;

public class Data<T> {

	public String id = "";
	public float x = 0;
	public float y = 0;
	public float angle = 0;
	public State<T> state;

	protected Data() {}

	public Data(final String id) {
		this();
		this.id = id;
	}

	public Data(final String id, final float x, final float y) {
		this(id);
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Data other = (Data) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}

}
