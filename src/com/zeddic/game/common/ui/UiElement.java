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

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;

import com.zeddic.game.common.GameObject;

public class UiElement extends GameObject {
  public float width;
  public float height;
  public float x;
  public float y;
  public boolean pressed = false;
  
  public List<ClickHandler> listeners = new ArrayList<ClickHandler>();
  
  public UiElement(float x, float y, float width, float height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
  
  public void addClickHandler(ClickHandler handler) {
    listeners.add(handler);
  }
  
  public void removeClickHandler(ClickHandler handler) {
    listeners.remove(handler);
  }
  
  public boolean onTouchEvent(MotionEvent e) {
    
    boolean inRange = inRange(e);
    
    if (e.getAction() == MotionEvent.ACTION_UP && pressed) {
      pressed = false;
      onRelease(inRange);
      if (inRange) {
        notifyClick();
      }
      return true;
    }
    
    if (!inRange) {
      return false;
    }
    
    if (e.getAction() == MotionEvent.ACTION_DOWN && inRange && !pressed) {
      pressed = true;
      onClick();
      
      return true;
    } else {
      return false;
    }
  }
  
  protected void onClick() {
    
  }
  
  protected void onRelease(boolean inRange) {
    
  }
  
  private void notifyClick() {
    for (ClickHandler listener : listeners) {
      listener.onClick();
    }
  }
  
  public boolean inRange(MotionEvent e) {
    float eX = e.getX();
    float eY = e.getY();
    return !(eX < x || eX > x + width ||
             eY < y || eY > y + height);
  }  
}
