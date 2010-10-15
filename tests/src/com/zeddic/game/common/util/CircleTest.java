package com.zeddic.game.common.util;

import junit.framework.TestCase;

import com.zeddic.game.common.util.Circle;
import com.zeddic.game.common.util.Span;
import com.zeddic.game.common.util.Vector2d;

public class CircleTest extends TestCase {

  public void testCircle() {
    Circle circle = new Circle(5);
    assertEquals(5f, circle.radius);
    assertEquals(10f, circle.diameter);
    assertEquals(10f, circle.width);
    assertEquals(10f, circle.height);
    assertNotNull(circle.path);
  }
  
  public void testCopy() {
    Circle circle = new Circle(5);
    Circle copy = (Circle) circle.copy();
    
    assertEquals(circle.radius, copy.radius);
    assertEquals(circle.diameter, copy.diameter);
    assertEquals(circle.width, copy.width);
    assertEquals(circle.height, copy.height);
  }
  
  public void testTransform() {
    Circle circle = new Circle(5);
    Circle other = (Circle) circle.copy();
    
    circle.transform(50, 2, other);
    assertEquals(5f, circle.radius);
    assertEquals(10f, other.radius);
    assertEquals(20f, other.diameter);
  }
  
  public void testProjectOnAxis() {
    Circle circle = new Circle(5);
    Span span = new Span();
    Vector2d axis;
    
    // Horizontal Axis
    axis = new Vector2d(1, 0);
    circle.projectOnAxis(0, 0, axis, span);
    assertEquals(-5f, span.min);
    assertEquals(5f, span.max);
    
    // Diagonal Axis
    axis = new Vector2d(1, 1);
    axis.normalize();
    circle.projectOnAxis(0, 0, axis, span);
    assertEquals(-5f, span.min);
    assertEquals(5f, span.max);
    
    // Translate
    axis = new Vector2d(1, 0);
    circle.projectOnAxis(5, 5, axis, span);
    assertEquals(0f, span.min);
    assertEquals(10f, span.max);
  }
}
