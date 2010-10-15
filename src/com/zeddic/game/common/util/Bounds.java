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

/**
 * Represents a bounding box surrounding some object. The bounds of
 * an object are represented by some shape, such as a Polygon or 
 * a Circle.
 */
public class Bounds {
  
  public static final int POLYGON = 1;
  public static final int CIRCLE = 1;
  
  /** The shape of the bounds. */
  public int type;
  
  /**
   * The original, underlying shape of the bounds. This is
   * the bounds before being translated or scaled.
   */
  public Shape raw;
  
  /**
   * The bounds after being scaled and rotated. Any collision detection should
   * be done against this object, as it represents the latest state 
   * of the parent object in the world.
   */
  public Shape shape;
  
  public Bounds(Shape shape) {
    if (shape instanceof Circle) {
      type = CIRCLE;
    } else {
      type = POLYGON;
    }
    this.raw = shape;
    this.shape = shape.copy();
  }
  
  /**
   * Transforms a bounds by rotating it ands scaling it. These transformations
   * are applied to the _original_ shape, not the already transformed
   * shape.
   */
  public void transform(float rotation, float scale) {
    raw.transform(rotation, scale, shape);
  }
}
