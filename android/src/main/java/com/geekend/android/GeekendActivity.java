package com.geekend.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.geekend.core.Geekend;

public class GeekendActivity extends GameActivity {

  @Override
  public void main(){
    platform().assets().setPathPrefix("com/geekend/resources");
    PlayN.run(new Geekend());
  }
}
