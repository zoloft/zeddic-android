package com.zeddic.game.common.util;

import android.test.AndroidTestCase;
import android.util.FloatMath;

import com.zeddic.game.common.util.Polygon;
import com.zeddic.game.common.util.Span;
import com.zeddic.game.common.util.Vector2d;
import com.zeddic.game.common.util.Polygon.PolygonBuilder;

public class PolygonTest extends AndroidTestCase {
  
  Polygon triangle;
  
  @Override
  public void setUp() {
    triangle = new PolygonBuilder()
      .add(0, 0)
      .add(10, 0)
      .add(5, 10)
      .build();
  }
  
  public void testPolygonBuilder() {
    Polygon polygon = new PolygonBuilder()
        .add(0, 0)
        .add(2, 5)
        .add(5, 2)
        .build();
      
    assertEquals(3, polygon.points.length);
    assertEquals(0f, polygon.points[0].x);
    assertEquals(0f, polygon.points[0].y);
    assertEquals(5f, polygon.points[2].x);
    assertEquals(2f, polygon.points[2].y);
  }
  
  public void testBuildEdges() {  
    assertEquals(3, triangle.edges.length);
    
    assertEquals(10f, triangle.edges[0].x);
    assertEquals(0f, triangle.edges[0].y);
    
    assertEquals(-5f, triangle.edges[1].x);
    assertEquals(10f, triangle.edges[1].y);
    
    assertEquals(-5f, triangle.edges[2].x);
    assertEquals(-10f, triangle.edges[2].y);
  }
  
  public void testBuildWidthHeight() {
    assertEquals(10f, triangle.width);
    assertEquals(10f, triangle.height);
  }
  
  public void testCopy() {
    Polygon copy = (Polygon) triangle.copy();
    
    assertEquals(triangle.width, copy.width);
    assertEquals(triangle.height, copy.height);
    assertEquals(triangle.radius, copy.radius);
    
    assertEquals(triangle.edges.length, copy.edges.length);
    for (int i = 0 ; i < triangle.edges.length ; i++) {
      assertEquals(triangle.edges[i].x, copy.edges[i].x);
    }
    
    assertEquals(triangle.points.length, copy.points.length);
    for (int i = 0 ; i < triangle.points.length ; i++) {
      assertEquals(triangle.points[i].x, copy.points[i].x);
    }
  }
  
  public void testTransform_scale() {
    Polygon polygon = new PolygonBuilder()
      .add(5, 5)
      .add(5, -5)
      .add(-5, -5)
      .add(-5, 5)
      .build();
    Polygon other = (Polygon) polygon.copy();
    polygon.transform(0, 2, other);
    
    float oldRadius = FloatMath.sqrt(5 * 5 + 5 * 5);
    float newRadius = FloatMath.sqrt(10 * 10 + 10 * 10);
    
    assertEquals(10f, other.points[0].x);
    assertEquals(10f, other.points[0].y);
    assertEquals(10f, other.points[1].x);
    assertEquals(-10f, other.points[1].y);
    assertEquals(-10f, other.points[2].x);
    assertEquals(-10f, other.points[2].y);
    assertEquals(-10f, other.points[3].x);
    assertEquals(10f, other.points[3].y);
    assertEquals(newRadius, other.radius);
    assertEquals(oldRadius, polygon.radius);
  }
  
  public void testTransform_rotate() {
    Polygon polygon = new PolygonBuilder()
      .add(5, 5)
      .add(5, -5)
      .add(-5, -5)
      .add(-5, 5)
      .build();
    Polygon other = (Polygon) polygon.copy();
    
    float d = FloatMath.sqrt(5f * 5f + 5f * 5f);
    
    polygon.transform(45, 1, other);
    
    assertEquals(0f, other.points[0].x);
    assertEquals(d, other.points[0].y);
    assertEquals(d, other.points[1].x);
    assertEquals(0f, other.points[1].y);
    assertEquals(0f, other.points[2].x);
    assertEquals(-d, other.points[2].y);
    assertEquals(-d, other.points[3].x);
    assertEquals(0f, other.points[3].y);
    
    assertEquals(10f, other.width);
    assertEquals(10f, other.height);
  }
  
  public void testBuildPath() {
    Polygon polygon = new PolygonBuilder()
      .add(0, 0)
      .add(10, 0)
      .add(5, 10)
      .build();
    assertNotNull(polygon.path);
  }
  
  public void testProjectOnAxis_withNoOffset() {
    Polygon polygon = new PolygonBuilder()
      .add(5, 5)
      .add(5, -5)
      .add(-5, -5)
      .add(-5, 5)
      .build();
    
    Span span = new Span();
    Vector2d axis;
    
    // Horizontal Axis
    axis = new Vector2d(1, 0);
    polygon.projectOnAxis(0, 0, axis, span);
    assertEquals(-5f, span.min);
    assertEquals(5f, span.max);
    
    // Vertical Axis
    axis = new Vector2d(0, 1);
    polygon.projectOnAxis(0, 0, axis, span);
    assertEquals(-5f, span.min);
    assertEquals(5f, span.max);
    
    // 45 degree Axis
    axis = new Vector2d(1, 1);
    axis.normalize();
    float d = FloatMath.sqrt(5 * 5 + 5 * 5);
    polygon.projectOnAxis(0, 0, axis, span);
    assertEquals(-d, span.min);
    assertEquals(d, span.max);
  }
  
  public void testProjectOnAxis_withOffset() {
    Polygon polygon = new PolygonBuilder()
      .add(5, 5)
      .add(5, -5)
      .add(-5, -5)
      .add(-5, 5)
      .build();
    
    Span span = new Span();
    Vector2d axis;
    
    // Horizontal Axis
    axis = new Vector2d(1, 0);
    polygon.projectOnAxis(5, 5, axis, span);
    assertEquals(0f, span.min);
    assertEquals(10f, span.max);
  }
}
