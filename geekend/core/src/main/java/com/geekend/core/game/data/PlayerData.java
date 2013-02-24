package com.geekend.core.game.data;

import com.geekend.core.game.component.PlayerStates;

public final class PlayerData {

	public String id = "";
	public float x = 0;
	public float y = 0;
	public float angle = 0;
	public PlayerStates state = PlayerStates.WAITING;

	protected PlayerData() {}
	
	public PlayerData(String id) {
		this.id = id;
	}
	
	public PlayerData(String id, float x, float y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayerData other = (PlayerData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}