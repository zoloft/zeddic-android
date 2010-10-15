package com.zeddic.game.common.util;

import android.graphics.Canvas;

import com.zeddic.game.common.GameObject;

public class MockGameObject extends GameObject {
  public boolean drawCalled = false;
  public boolean updateCalled = false;
  public boolean dieOnUpdate = false;
  
  public void draw(Canvas canvas) {
    drawCalled = true;
  }
  
  public void update(long time) {
    updateCalled = true;
    if (dieOnUpdate) {
      kill();
    }
  }
}
