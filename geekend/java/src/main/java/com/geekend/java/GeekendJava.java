package com.geekend.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.geekend.core.Geekend;

public class GeekendJava {

  public static void main(String[] args) {
    JavaPlatform platform = JavaPlatform.register();
    platform.assets().setPathPrefix("com/geekend/resources");
    PlayN.run(new Geekend());
  }
}
