package com.zeddic.game.common.util;

import junit.framework.TestCase;

public class Vector2dTest extends TestCase {

  private static final int ROUND_PRECISION = 100;
  
  public void testGetAngle_top() {
    Vector2d vector = new Vector2d(0, -1);
    assertEquals(270.0f, round(vector.getAngle()));
  }
  
  public void testGetAngle_bottom() {
    Vector2d vector = new Vector2d(0, 1);
    assertEquals(90.0f, round(vector.getAngle()));
  }

  public void testGetAngle_left() {
    Vector2d vector = new Vector2d(-1, 0);
    assertEquals(180.0f, round(vector.getAngle()));
  }
  
  public void testGetAngle_right() {
    Vector2d vector = new Vector2d(1, 0);
    assertEquals(0.0f, round(vector.getAngle()));
  }
  
  private float round(float value) {
    return Math.round(value * ROUND_PRECISION) / ROUND_PRECISION;
  }
}
