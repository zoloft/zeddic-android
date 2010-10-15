package com.zeddic.game.common.util;

import junit.framework.TestCase;

import com.zeddic.game.common.util.Bounds;
import com.zeddic.game.common.util.Circle;
import com.zeddic.game.common.util.Polygon;
import com.zeddic.game.common.util.Polygon.PolygonBuilder;

public class BoundsTest extends TestCase {

  public void testCircleBounds() {
    Circle circle = new Circle(5);
    Bounds bound = new Bounds(circle);
    
    assertEquals(circle, bound.raw);
    assertNotNull(bound.shape);
    assertEquals(5f, bound.shape.radius);
    assertEquals(Bounds.CIRCLE, bound.type);
  }
  
  public void testPolygonBounds() {
    Polygon polygon = new PolygonBuilder()
      .add(0, 0)
      .add(10, 0)
      .add(5, 10)
      .build();
    Bounds bound = new Bounds(polygon);
    
    assertEquals(polygon, bound.raw);
    assertNotNull(bound.shape);
    assertEquals(polygon.edges.length, ((Polygon) bound.shape).edges.length);
    assertEquals(Bounds.POLYGON, bound.type);
  }
  
  public void testTransform() {
    Circle circle = new Circle(5);
    Bounds bound = new Bounds(circle);
    bound.transform(90, 2);
    
    assertEquals(5f, bound.raw.radius);
    assertEquals(10f, bound.shape.radius);
  }
}
