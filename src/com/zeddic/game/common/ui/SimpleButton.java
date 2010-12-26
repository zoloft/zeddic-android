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

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

import com.zeddic.game.common.transistions.Transition;
import com.zeddic.game.common.transistions.Transitions;

public class SimpleButton extends UiElement{
  
  private Paint paint;
  private BlurMaskFilter filter;
  private Paint font;
  
  Transition fadeTransition;
  Transition fillTransition;
  
  private String text;
  
  public SimpleButton(float x, float y, float width, float height) {
    super(x - width / 2, y - height / 2, width, height);
    
    fadeTransition = new Transition(90, 0, 500, Transitions.EXPONENTIAL);
    fillTransition = new Transition(90, 30, 500, Transitions.EXPONENTIAL);
    
    filter = new BlurMaskFilter(2, BlurMaskFilter.Blur.NORMAL);
    
    paint = new Paint();
    paint.setColor(Color.BLUE);
    paint.setStyle(Style.STROKE);
    paint.setStrokeWidth(2);
    paint.setPathEffect(new CornerPathEffect(10));
    //paint.setMaskFilter();
    // blurFilter = new BlurMaskFilter(18, BlurMaskFilter.Blur.NORMAL);
    
    
    
    font = new Paint();
    font.setTextSize(30);
    font.setStrikeThruText(false);
    font.setUnderlineText(false);
    font.setTextAlign(Align.CENTER);
    font.setColor(Color.BLUE);
    pressed = false;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
  public void setFontPaint(Paint font) {
    this.font = font;
  }
  
  @Override
  public void draw(Canvas canvas) {   
    
    paint.setStrokeWidth(0);
    paint.setColor(Color.BLUE);
    paint.setAlpha(pressed ? 90 : (int) fillTransition.get());
    paint.setMaskFilter(null);
    paint.setStyle(Style.FILL);
    canvas.drawRect(x, y, x + width, y + height, paint);
    
    paint.setStrokeWidth(20);
    paint.setColor(Color.BLUE);
    paint.setAlpha(pressed ? 90 : (int) fadeTransition.get());
    paint.setMaskFilter(filter);
    paint.setStyle(Style.STROKE);
    canvas.drawRect(x, y, x + width, y + height, paint);
    
    paint.setStrokeWidth(2);
    paint.setColor(Color.BLUE);
    paint.setAlpha(255);
    paint.setMaskFilter(null);
    paint.setStyle(Style.STROKE);
    canvas.drawRect(x, y, x + width, y + height, paint);
    
    canvas.drawText(text, x + width / 2, y + height / 2 + font.getTextSize() / 4, font);
  }
  
  @Override
  public void update(long time) {
    fadeTransition.update(time);
    fillTransition.update(time);
  }
  
  @Override
  protected void onRelease(boolean inRange) {
    fadeTransition.reset();
    fillTransition.reset();
  }
}
