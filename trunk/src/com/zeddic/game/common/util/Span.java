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

public class Span {
  public float min;
  public float max;
  
  public Span(float min, float max) {
    this.min = min;
    this.max = max;
  }
  
  public Span() {
    min = Float.MAX_VALUE;
    max = -Float.MAX_VALUE;
  }

  public float distanceBetween(Span other) {
    if (min < other.min) {
      return other.min - max;
    } else {
      return min - other.max;
    }
  }
}
