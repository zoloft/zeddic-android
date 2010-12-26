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

/**
 * Base class for any arbitrary shape.
 */
public class Shape {

  public float width = 0;
  public float height = 0;
  public float radius = 0; 
  public Path path;
  
  /**
   * Transforms the shape by rotating it and scaling it. The scaled shape
   * is stored in the parameter "other". If other is null, a new copy is 
   * created and scaled. The original shape is not modified. 
   */
  public void transform(float rotation, float scale, Shape other) {
    
  }
  
  /**
   * Creates an exact copy of the shape.
   */
  public Shape copy() {
    return null;
  }
  
  /**
   * Builds a path representing the shape.
   */
  public void buildPath() {

  }
  
  /**
   * Projects a shape onto an arbitrary axis in 2D space. The projection
   * of the shape will take up a span on the axis, which will be contained
   * in the returned Span object. Span should be a pre-created Span object
   * to void new object allocation.
   */
  public void projectOnAxis(
      float xOffset,
      float yOffset,
      Vector2d axis,
      Span span) {
    
  }
}
