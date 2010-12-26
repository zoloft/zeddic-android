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

package com.zeddic.game.common.util;

public class Vector2d {
  public float x;
  public float y;
  
  public Vector2d() {
    this.x = 0;
    this.y = 0;
  }
  
  public Vector2d(float x, float y) {
    this.x = x;
    this.y = y;
  }
  
  public void normalize() {
    float length = (float) Math.sqrt(x * x + y * y);
    this.x = x / length;
    this.y = y / length;
  }
  
  public float dotProduct(float oX, float oY) {
    return this.x * oX + this.y * oY;
  }
  
  public float dotProduct(Vector2d other) {
    return this.x * other.x + this.y * other.y;
  }
  
  public void scale(float scale) {
    this.x = x * scale;
    this.y = y * scale;
  }
  
  public Vector2d copy() {
    return new Vector2d(x, y);
  }
  
  public boolean equals(Vector2d other) {
    return this.x == other.x && this.y == other.y;
  }
  
  public float getAngle() {
    float angle = (float) Math.toDegrees(Math.atan( (y) / (x)));
    if (angle < 0) {
      angle += 360;
    }
    
    if (x < 0) {
      angle += 180;
    }
 
    return angle;
  }
  
  @Override
  public String toString() {
    return "<" + x + "," + y + ">";
  }
}
