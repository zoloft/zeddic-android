package com.zeddic.game.common;

import android.view.KeyEvent;
import android.view.MotionEvent;

public interface GameSurfaceEventListener {
  void onSurfaceCreated();
  
  void onSurfaceDestroyed();
  
  boolean onTouchEvent(MotionEvent e);
  
  boolean onKeyDown(int key, KeyEvent e);
  
  boolean onKeyUp(int key, KeyEvent e);
  
  boolean onTrackballEvent(MotionEvent e);
  
  void onFocusChangedEvent(boolean hasFocus);
  
  void onLayoutChangedEvent(boolean changed, int left, int top, int right, int bottom);
}
