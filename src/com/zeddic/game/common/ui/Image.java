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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Image extends UiElement {

  private Bitmap bitmap;
  private Rect bitmapSrc;
  private Rect bitmapDest;
  private Paint paint;
  
  public Image(Bitmap bitmap, float x, float y, float width, float height)  {
    super(x, y, width, height);
    this.bitmap = bitmap;
    
    bitmapSrc = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    bitmapDest = new Rect(0, 0, (int) width, (int) height);
    
    paint = new Paint();
  }
  
  @Override
  public void draw(Canvas canvas) {   
   
    canvas.drawBitmap(bitmap, bitmapSrc, bitmapDest, paint);
   
  }
}
