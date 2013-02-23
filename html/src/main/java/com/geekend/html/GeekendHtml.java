package com.geekend.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.geekend.core.Geekend;

public class GeekendHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("geekend/");
    PlayN.run(new Geekend());
  }
}
