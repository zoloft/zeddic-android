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

import android.graphics.Path;
import android.graphics.Path.Direction;

/**
 * A circle shape.
 */
public class Circle extends Shape {
  
  public float diameter;
  
  /**
   * Creates a new circle with a given radius.
   */
  public Circle(float radius) {
    this.radius = radius;
    this.diameter = radius * 2;
    this.width = diameter;
    this.height = diameter;
    buildPath();
  }

  @Override
  public void transform(float rotation, float scale, Shape otherShape) {
    
    if (otherShape == null)
      otherShape = copy();
    
    if (!(otherShape instanceof Circle))
      return;

    Circle other = (Circle) otherShape;
    other.radius = radius * scale;
    other.diameter = other.radius * 2;
  }
  
  @Override
  public Shape copy() {
    return new Circle(radius);
  }
  
  @Override
  public void buildPath() {
    path = new Path();
    path.addCircle(0, 0, radius, Direction.CCW);
  }
  
  @Override
  public void projectOnAxis(
      float xOffset,
      float yOffset,
      Vector2d axis,
      Span span) {
    
    // Dot product represents the midpoints position on the axis.
    float dotProduct = axis.x * xOffset + axis.y * yOffset;
    
    // Min and max range of the circle is always based on the radius.
    span.max = dotProduct + radius;
    span.min = dotProduct - radius;    
  }
}
