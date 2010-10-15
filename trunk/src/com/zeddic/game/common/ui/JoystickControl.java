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
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.zeddic.game.common.GameObject;

/**
 * A game object that draws a joystick. The object gets user input
 * and updates relevant state.
 */
public class JoystickControl extends GameObject {

  /** How large the joystick input circle should be. */
  public static final float BORDER_RADIUS = 100;
  
  /** How large the joystick pointer should be. */
  private static final float JOYSTICK_RADIUS = 15;
  
  /** The radius within which to pay attention to user input. */
  private static final float INPUT_RADIUS = 220;

  private static final float STATIC_ARROW_OFFSET = 40;
  private static final float ARROW_OFFSET = 80;
  private static final float MIN_ARROW_SCALE = 3;
  private static final float MAX_ARROW_SCALE = 3;
  
  /** X and Y position to draw the joystick at. */
  float x; 
  float y;
  
  /** The X and Y position where the pointer is at within the joystick. */
  float pointX;
  float pointY;
  
  /** The angle between the joystick pointer and the control center. */
  float angle = 0;
  
  /** Default paint to draw with. */
  Paint paint;
  
  Path rightArrow;
  
  boolean fingerDown = false;
  
  boolean drawArrows = false;
  
  public JoystickControl(float x, float y) {
    this(x, y, true, Color.argb(255, 214, 28, 28));
  }
  
  public JoystickControl(float x, float y, boolean drawArrows, int color) {
    setPosition(x, y);
    paint = new Paint();
    paint.setColor(color);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(2);
    paint.setAlpha(80);
    
    rightArrow = new Path();
    rightArrow.moveTo(0, -10);
    rightArrow.lineTo(5, 0);
    rightArrow.lineTo(0, 10);
    
    this.drawArrows = drawArrows;
  }
  
  public void draw(Canvas c) {
    
    float arrowX;
    float arrowY;
    float scale;
    float arrowOffset = fingerDown ? ARROW_OFFSET : STATIC_ARROW_OFFSET;
    
    //if (fingerDown) {
      paint.setStyle(Paint.Style.STROKE);
      
      if (fingerDown && drawArrows) {
        c.drawCircle(x, y, BORDER_RADIUS, paint);
      }
      paint.setStyle(Paint.Style.FILL);
      c.drawCircle(pointX, pointY, JOYSTICK_RADIUS, paint);
      
      
      if (!drawArrows) {
        return;
      }
      //paint.setStyle(Paint.Style.STROKE);
      
      // Draw right arrow
      arrowX = x + arrowOffset; //Math.max(x + arrowOffset, pointX + arrowOffset);
      arrowY = y;
      scale = getArrowScale(arrowX - x);
      drawArrow(c, arrowX, arrowY, scale, 0);
    
      // Draw left arrow
      arrowX = x - arrowOffset;//Math.min(x - arrowOffset, pointX - arrowOffset);
      arrowY = y;
      scale = getArrowScale(x - arrowX);
      drawArrow(c, arrowX, arrowY, scale, 180);
      
      // Draw up arrow
      arrowX = x;
      arrowY = y - arrowOffset;//Math.min(y - arrowOffset, pointY - arrowOffset);
      scale = getArrowScale(y - arrowY);
      drawArrow(c, arrowX, arrowY, scale, 270);
      
      // Draw down arrow
      arrowX = x;
      arrowY = y + arrowOffset;//Math.max(y + arrowOffset, pointY + arrowOffset);
      scale = getArrowScale(arrowY - y);
      drawArrow(c, arrowX, arrowY, scale, 90);
      
    //} else {
    //  
    //}
  }
  
  private void drawArrow(Canvas c, float arrowX, float arrowY, float scale, float degree) {
    c.save();
    c.translate(arrowX, arrowY);
    c.scale(scale, scale);
    c.rotate(degree);
    c.drawPath(rightArrow, paint);
    c.restore();
  }
  
  private float getArrowScale(float distanceOffset) {
    return MIN_ARROW_SCALE + distanceOffset * (MAX_ARROW_SCALE - MIN_ARROW_SCALE) / BORDER_RADIUS;
  }
  
  /**
   * Returns a value between 0 and 1 that represents the current input points
   * distance from the center of the joystick. 
   */
  public float getSpeed() {
    return (float) distanceToPoint(pointX, pointY) / 
        getMaximumJoystickDistance();
  }
  
  public float getXVelocity() {
    return (float) (pointX - x) / getMaximumJoystickDistance();
  }
  
  public float getYVelocity() {
    return (float) (pointY - y) / getMaximumJoystickDistance();
  }
  
  /**
   * Returns the angle that the joystick is at.  
   */
  public float getAngle() {
    return this.angle;
  }
  
  /**
   * Calculates the angle between the user input and the center of the controls.
   */
  private void calculateAngle() {
    angle = (float) Math.toDegrees(Math.atan( (pointY - y) / (pointX - x)));
    if (pointX > x) {
      angle += 180;
    }
    angle += 180;
  }
  
  /**
   * Handles user screen input to update the joystick input.
   */
  public boolean onTouchEvent(MotionEvent e) {
    
    /*if (scanForPossibleInput(e)) {
      return true;
    }*/
    
    // Perform a check on all pointer inputs.
    for (int i = 0 ; i < e.getPointerCount() ; i++) {
      float eX = e.getX(i);
      float eY = e.getY(i);
      if (handleInput(e, eX, eY)) {
        return true;
      }
    }
    
    // If there is no input, reset the joystick position to the center.
    setJoystickPosition(x, y);
    fingerDown = false;
    return false;
  }
  
  private boolean handleInput(MotionEvent e, float eX, float eY) {
    // Only process mouse up/down events that are within range.
    if (inRange(eX, eY)) {
      if (e.getAction() == MotionEvent.ACTION_MOVE) {
        
        if (!fingerDown) {
          fingerDown = true;
          //recenter(eX, eY);
        }
        
        // Move the joystick pointer.
        setJoystickPosition(eX, eY);
      
        // Update the angle.
        calculateAngle();
        
        return true;
      }
    }
    return false;
  }
  
  
  
  private boolean scanForPossibleInput(MotionEvent e) {
    
    boolean foundX = false;
    boolean foundY = false;
    float inputX = 0;
    float inputY = 0;
    
    for (int i = 0 ; i < e.getPointerCount() ; i++) {
      float eX = e.getX(i);
      float eY = e.getY(i);
      if (!foundX && eX >= x - INPUT_RADIUS && eX <= x + INPUT_RADIUS) {
        inputX = eX;
        foundX = true;
      }
      if (!foundY && eY >= y - INPUT_RADIUS && eY <= y + INPUT_RADIUS) {
        inputY = eY;
        foundY = true;
      }
    }
    
    if (foundX && foundY) {
      return handleInput(e, inputX, inputY);
    }
    
    return false;
  }
  
  public void setPosition(float newX, float newY) {
    x = newX;
    y = newY;
    setJoystickPosition(x, y);
  }
  
  public boolean isPressed() {
    return fingerDown;
  }
  
  /**
   * Returns true if the given point is within user input range.
   */
  private boolean inRange(float testX, float testY) {
    float distance = distanceToPoint(testX, testY);
    return distance <= INPUT_RADIUS;
  }
  
  /**
   * Returns the distance between the given point and the center
   * of the controls.
   */
  private float distanceToPoint(float testX, float testY) {
    return PointF.length(testX - x, testY - y);
  }
  
  /**
   * Sets the joystick input to the given position. This will 
   * automatically reduce the x/y position to make sure it remains within
   * the joystick control.
   */
  private void setJoystickPosition(float pointX, float pointY) {
    float distance = distanceToPoint(pointX, pointY);
    float maxDistance = getMaximumJoystickDistance();
    if ( distance > maxDistance) {
      pointX = x + maxDistance * (pointX - x)  / distance;
      pointY = y + maxDistance * (pointY - y)  / distance;
    }
    this.pointX = pointX;
    this.pointY = pointY;
  }
  
  /**
   * Gets the maximum distance that a joystick input can appear from 
   * the center of the joystick.
   */
  private float getMaximumJoystickDistance() {
    return BORDER_RADIUS - 1 - JOYSTICK_RADIUS; 
  }
}
