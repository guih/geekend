package com.geekend.core.game.input;

import java.util.HashSet;
import java.util.Set;

import playn.core.Key;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.Listener;
import playn.core.Keyboard.TypedEvent;
import playn.core.PlayN;

public class InputOracle {

	private final Set<Key> pressedKeys;

	public InputOracle() {
		pressedKeys = new HashSet<Key>();

		PlayN.keyboard().setListener(new Listener() {
			@Override
			public void onKeyUp(final Event event) {
				pressedKeys.remove(event.key());
			}

			@Override
			public void onKeyTyped(final TypedEvent event) {
			}

			@Override
			public void onKeyDown(final Event event) {
				pressedKeys.add(event.key());
			}
		});
	}

	public boolean isKeyPressed(final Key key) {
		return pressedKeys.contains(key);
	}
}
