package com.geekend.core.game.components.controllable.car;

import com.geekend.core.game.components.controllable.Controller;
import com.geekend.core.game.components.controllable.Data;

public class NoDriverCarController implements Controller<Car> {

	@Override
	public void initializeData(final Data<Car> data) {
		if (data.state == null) data.state = CarStates.NORMAL;
	}

	@Override
	public void updateData(final Data<Car> data, final float delta) {}
}
