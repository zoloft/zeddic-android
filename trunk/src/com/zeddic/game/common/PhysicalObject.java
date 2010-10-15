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
import android.graphics.Color;
import android.graphics.Paint;

import com.zeddic.game.common.util.Bounds;
import com.zeddic.game.common.util.Polygon;
import com.zeddic.game.common.util.Vector2d;
import com.zeddic.game.common.util.Polygon.PolygonBuilder;

public class PhysicalObject extends GameObject {

  public static float TIME_SCALER = 200;
  
  //// SETUP OBJECT SHAPE AND PAINT
  protected static Paint defaultPaint;
  protected static Polygon defaultPolygon;
  protected static float ANGLE_OFFSET = 0;
  static {
    defaultPaint = new Paint();
    defaultPaint.setColor(Color.RED);
    defaultPaint.setStyle(Paint.Style.STROKE);
    defaultPaint.setStrokeWidth(1);
    
    defaultPolygon = new PolygonBuilder()
        .add(-5, -5)
        .add(5, -5)
        .add(5, 5)
        .add(-5, 5)
        .build();
  }
  
  public float x;
  public float y;
  public float angle;
  public Bounds bounds;
  public Vector2d velocity;
  public float scale;
  public float rotation;
  public Paint paint;
  
  public PhysicalObject() {
    this(0, 0);
  }
  
  public PhysicalObject(float x, float y) {
    this.x = x;
    this.y = y;
    this.bounds = new Bounds(defaultPolygon);
    this.paint = defaultPaint;
    this.velocity = new Vector2d(0, 0);
    this.scale = 1;
    this.setAngle(0);
    this.rotation = 0;
  }
  
  public void setAngle(float angle) {
    if (angle == this.angle) {
      return;
    }
    this.angle = angle;
    if (bounds != null) {
      bounds.transform(angle + getAngleOffset(), scale);
    }
  }
  
  public void setScale(float scale) {
    if (scale == this.scale) {
      return;
    }
    this.scale = scale;
    if (bounds != null) {
      bounds.transform(angle + getAngleOffset(), scale);
    }
  }
  
  protected void setBounds(Bounds bounds) {
    this.bounds = bounds;
    bounds.transform(angle + getAngleOffset(), scale);
  }

  public void setVelocity(float x, float y) {
    velocity.x = x;
    velocity.y = y;
  }
  
  public void setVelocityBySpeed(float angle, float speed) {
    double radians = Math.toRadians(angle);
    velocity.x = speed * (float) Math.cos(radians);
    velocity.y = speed * (float) Math.sin(radians);
  }
  
  public void setVelocityBySpeed(float speed) {
    double radians = Math.toRadians(angle);
    velocity.x = speed * (float) Math.cos(radians);
    velocity.y = speed * (float) Math.sin(radians);
  }
  
  public float getSpeed() {
    return (float) Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y);
  }
  
  public void matchAngleWithVelocity() {
    this.angle = velocity.getAngle();
  }
  
  private float timeFraction;
  public void update(long time) {
    super.update(time);    

    timeFraction = (float) time / TIME_SCALER;
    x += velocity.x * timeFraction;
    y += velocity.y * timeFraction;
    setAngle(angle + rotation * timeFraction);
  }

  protected float getAngleOffset() {
    return 0;
  }
  
  public void drawBounds(Canvas canvas) {
    canvas.save();

    canvas.translate(x, y);
    canvas.rotate(getAngleOffset() + angle);
    canvas.scale(scale, scale);
    
    canvas.drawPath(bounds.raw.path, paint);
    canvas.restore();
  }
  
  public void collide(PhysicalObject object, Vector2d avoidVector) {
    if (avoidVector != null) {
      x += avoidVector.x;
      y += avoidVector.y;
    }
  }
}
