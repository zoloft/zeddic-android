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

import android.graphics.Canvas;
import android.view.MotionEvent;

public class Container extends UiElement {
  
  protected List<UiElement> children = new ArrayList<UiElement>();
  
  public Container() {
    super(0, 0, 0, 0);
  }
  
  public void add(UiElement child) {
    children.add(child);
  }
  
  public void remove(UiElement child) {
    children.remove(child);
  }
  
  public void draw(Canvas canvas) {
    
    canvas.save();
    canvas.translate(x, y);
    
    for (UiElement element : children) {
      element.draw(canvas);
    }
    
    canvas.restore();
  }
  
  public void update(long time) {
    for (UiElement element : children) {
      element.update(time);
    }
  }
  
  public boolean onTouchEvent(MotionEvent e) {
    for (UiElement element : children) {
      element.onTouchEvent(e);
    }
    return true;
  }
}
