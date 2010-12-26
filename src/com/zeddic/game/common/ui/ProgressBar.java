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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.zeddic.game.common.GameObject;

public class ProgressBar extends GameObject {

  public static float WIDTH = 200;
  public static float HEIGHT = 20;
  
  public float x;
  public float y;
  public float value;
  public float max;
  Paint paint;
  
  public ProgressBar(float x, float y, float value, float max) {
    this.x = x - WIDTH;
    this.y = y;
    this.value = value;
    this.max = max;
    paint = new Paint();
    paint.setColor(Color.RED);
    paint.setStrokeWidth(3);
    paint.setStyle(Style.STROKE);
    paint.setColor(Color.RED);
  }
  
  public void update(long time) {
    // super.update(time);
  }
  
  public void draw(Canvas c) {
    // super.draw(c);
    
    paint.setStyle(Style.FILL);
    paint.setAlpha(80);
    c.drawRect(x, y, x + WIDTH, y + HEIGHT, paint);
    
    paint.setAlpha(255);
    paint.setStyle(Style.STROKE);
    c.drawRect(x, y, x + WIDTH, y + HEIGHT, paint);
    
    float progress = value * WIDTH / max;
    paint.setStyle(Style.FILL);
    c.drawRect(x, y, x + progress, y + HEIGHT, paint);
  }
}
