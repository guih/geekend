package com.geekend.core.game.component;

import static playn.core.PlayN.graphics;
import playn.core.CanvasImage;
import playn.core.Font;
import playn.core.GroupLayer;
import playn.core.Layer;
import playn.core.TextFormat;
import playn.core.TextLayout;

public class Console {

	private GroupLayer mainLayer;
	private int logs;

	public void init(final GroupLayer rootLayer) {
		mainLayer = graphics().createGroupLayer();
		rootLayer.add(mainLayer);
	}

	public void log(final String message) {
		final Font font = graphics().createFont("Helvetica", Font.Style.PLAIN, 12f);
		final TextFormat format = new TextFormat().withFont(font);
		final TextLayout layout = graphics().layoutText(message, format);
		final Layer textLayer = createTextLayer(layout, 0xFFFFFFFF);
		textLayer.setTranslation(10, graphics().height() - 30 + 15 * ++logs);
		mainLayer.setTranslation(0, -15 * logs);
		mainLayer.add(textLayer);
	}

	private Layer createTextLayer(final TextLayout layout, final int color) {
		final CanvasImage image = graphics().createImage((int) Math.ceil(layout.width()), (int) Math.ceil(layout.height()));
		image.canvas().setFillColor(color);
		image.canvas().fillText(layout, 0, 0);
		return graphics().createImageLayer(image);
	}
}
