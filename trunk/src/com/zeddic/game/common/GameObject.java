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

/**
 * A generic object in the game render/update tree.
 * 
 * @author scott@zeddic.com
 */
public class GameObject {
  
  /** Whether an object should be actively drawn and updated. */
  public boolean active = true;
  
  /** Whether an object can be restored to an object pool. */
  public boolean canRecycle = false;
  
  /** Whether an object was obtained from an object pool. */
  public boolean taken = false;
  
  public GameObject() {

  }
  
  /**
   * Draws the object on the canvas.
   * @param canvas The default drawing canvas.
   */
  public void draw(Canvas canvas) {
   
  }
  
  /**
   * Updates the game object.
   * @param time The passed time in milliseconds.
   */
  public void update(long time) {
    
  }
  
  /**
   * Enables an object. Only enabled objects will be draw or updated by
   * object pools.
   */
  public void enable() {
    active = true;
    canRecycle = false;
  }
  
  /**
   * Resets an object to its basic, initialized state. Optional.
   */
  public void reset() {

  }
  
  /**
   * Disables an object and clears it to be restored to an object pool.
   */
  public void kill() {
    active = false;
    canRecycle = true;
  }
}
