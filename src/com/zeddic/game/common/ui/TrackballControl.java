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

package com.zeddic.game.common.ui;

import android.view.KeyEvent;
import android.view.MotionEvent;

public class TrackballControl {
  
  private boolean fireButtonDown = false;
  
  public boolean shouldFire() {
    return fireButtonDown;
  }
  
  /**
   * Processes a trackball event. Returns true if the event was handled. 
   */
  public boolean onTrackballEvent(MotionEvent e) {
    
    // There are several quirks in the trackball api that cause it to
    // report key up / down events at inappropriate times. Here we detect
    // them and suppress the events. 
    
    float eX = e.getX();
    float eY = e.getY();
    
    // A move event with no x or y is equivalent to the trackball being
    // released after it was moved in the depressed state. Due to API quirks, 
    // this isn't normally registered as a key up. 
    if (e.getAction() == MotionEvent.ACTION_MOVE && eX == 0 && eY == 0 ) {
      fireButtonDown = false;
      return true;
    } 
    // A trackball released event with any x/y change doesn't actually
    // correspond to the trackball being released - just moving to another 
    // directional state. It should be suppressed so we don't stop shotting.
    else if (e.getAction() == MotionEvent.ACTION_UP && (eX != 0 || eY != 0)) {
      return true;
    }
    return false;
  }
  
  /**
   * Processes a keydown event. Returns true if handled.
   */
  public boolean onKeyDown(int key, KeyEvent e) {
    if (key == 23) {
      fireButtonDown = true;
      return true;
    }
    return false;
  }
  
  /**
   * Processes a up event. Returns true if handled.
   */
  public boolean onKeyUp(int key, KeyEvent e) {
    if (key == 23) {
      fireButtonDown = false;
    }
    return false;
  }
}
