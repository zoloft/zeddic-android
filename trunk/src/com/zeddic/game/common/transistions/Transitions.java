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

package com.zeddic.game.common.transistions;

public class Transitions {
  private static final double PI = 3.14159265358;
  private static final double EULER = 2.718281828459045;
  
  /**
   * The value changes directly proportional to the time.
   * This transition has the danger of appearing jerky.
   */
  public static final int LINEAR = 1;
  
  /**
   * This starts out quickly then slows down as its finishing.
   */
  public static final int LOG = 2;
  
  /**
   * This starts slowly and near the end is very quick.
   */
  public static final int EXPONENTIAL = 3;
  
  /**
   * This transition starts out slowly, until it hits
   * its max speed, then slows down again before finishing.
   * Think of this as as the way you drive your car.
   */
  public static final int EASE_IN_OUT = 4;
  
  /**
   * Transition starts out quickly, overshoots its final value,
   * then wobbles back and forth before settling on the
   * final value.
   */
  public static final int SPRING = 5;

  public static double getProgress(int transitionType, double timeProgress) {
 
    switch (transitionType) {
      case LINEAR: return getLinearProgress(timeProgress);
      case LOG: return getLogProgress(timeProgress);
      case EXPONENTIAL: return getExponentialProgress(timeProgress);
      case EASE_IN_OUT: return getEaseInOutProgress(timeProgress);
      case SPRING: return getSpringProgress(timeProgress);
      default: return getLinearProgress(timeProgress);
    }
  }

  private static double getEaseInOutProgress(double x) {
    return Math.cos( x * PI + PI ) / 2 + 0.5;
  }
  
  private static double getLinearProgress(double x) {
    return x;
  }
  
  private static double getLogProgress(double x) {
    return Math.pow(x, 0.5);
  }
  
  private static double getExponentialProgress(double x) {
    return Math.pow(x, 3);
  }

  private static double getSpringProgress(double x) {
    return 1-Math.cos(x* 4.5 * PI) * Math.pow(EULER,(-x * 6));
  }
}
