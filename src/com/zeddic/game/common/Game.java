/*
 * Copyright (C) 2010 Geo Siege Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zeddic.game.common;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Game {
  
  public Updater updater;
  public boolean initialized = false;
  
  public void init(int screenWidth, int screenHeight) {
    initialized = true;
  }
  
  public void draw(Canvas canvas) {
    
  }
  
  public void update(long time) {

  }
  
  public boolean onTouchEvent(MotionEvent e) {
    return false;
  }

  public boolean onTrackballEvent(MotionEvent e) {
    return false;
  }
  
  public boolean onKeyDown(int key, KeyEvent e) {
    return false;
  }
  
  public boolean onKeyUp(int key, KeyEvent e) {
    return false;
  }
  
  public void onFocusChangedEvent(boolean hasFocus) {

  }
  
  public void onLayoutChangedEvent(boolean changed, int left, int top, int right, int bottom) {

  }
  
  public void onSurfaceReady() {
    
  }
  
  protected void sleep() {
    updater.stop();
  }
  
  protected void wakeup() {
    updater.start();
  }
  
  protected void triggerEvent(int code) {
    updater.triggerEventHandler(code);
  }

}
