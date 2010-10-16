/*
 * Copyright (C) 2010 Zeddic Game Library
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

/**
 * A simple game. This should be the top level of any game tree and should be
 * responsible for initializing game objects and calling draw/update on all
 * GameOjbects. The game will automatically have its update and draw commands
 * called by it's {@link Updater}.
 */
public class Game {
  
  public Updater updater;
  public boolean initialized = false;
  
  /**
   * Initializes the game. Extended instances should use this to allocate all
   * game objects.
   * 
   * @param screenWidth - The width of the canvas
   * @param screenHeight - The height of the canvas
   */
  public void init(int screenWidth, int screenHeight) {
    initialized = true;
  }
  
  /**
   * Renders the entire game scene from scratch.
   */
  public void draw(Canvas canvas) {
    
  }

  /**
   * Updates all game objects in the world. All updates should be scaled based
   * on the passed time.
   * 
   * @param time The number of milliseconds passed from the last update call.
   */
  public void update(long time) {

  }
  
  /**
   * Triggered when a user touches the screen.
   */
  public boolean onTouchEvent(MotionEvent e) {
    return false;
  }

  /**
   * Triggered when the user uses the touchpad or trackball on the device.
   */
  public boolean onTrackballEvent(MotionEvent e) {
    return false;
  }
  
  /**
   * Triggered when the user presses a key.
   */
  public boolean onKeyDown(int key, KeyEvent e) {
    return false;
  }
  
  /**
   * Triggered when the user releases a key.
   */
  public boolean onKeyUp(int key, KeyEvent e) {
    return false;
  }

  /**
   * Triggered when the application gains or loses focus, such as when answering
   * a call. Can be used to pause the updater so it no longer runs in the
   * background.
   */
  public void onFocusChangedEvent(boolean hasFocus) {
    if (initialized) {
      if (!hasFocus) {
        pause();
      } else {
        resume();
      }
    }
  }
  
  /**
   * Triggered when the screen size changes. Will be call initially on game
   * load. Will subsequently only be called if the resolution changes, such
   * as if the user rotates the screen.
   */
  public void onLayoutChangedEvent(boolean changed, int left, int top, int right, int bottom) {

  }
  
  /**
   * Triggered once the draw surface is ready for canvas draws.
   */
  public void onSurfaceReady() {
    
  }
  
  /**
   * Stops the updater, stopping all draw/update calls.
   */
  protected void pause() {
    updater.stop();
  }
  
  /**
   * Resumes the game after the sleep.
   */
  protected void resume() {
    updater.start();
  }
  
  /**
   * Triggers an event that a game activity registered to recieve notifcations
   * about.
   */
  protected void triggerEvent(int code) {
    updater.triggerEventHandler(code);
  }
}
