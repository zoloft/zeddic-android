package com.zeddic.game.common.collision;

import junit.framework.TestCase;

import com.zeddic.game.common.PhysicalObject;
import com.zeddic.game.common.collision.CollisionComponent;
import com.zeddic.game.common.collision.CollisionManager;
import com.zeddic.game.common.util.Bounds;
import com.zeddic.game.common.util.Circle;
import com.zeddic.game.common.util.Vector2d;
import com.zeddic.game.common.util.Polygon.PolygonBuilder;

public class CollisionManagerTest extends TestCase {

  private static final int WORLD_WIDTH = 1000;
  private static final int WORLD_HEIGHT = 1000;
  private MockPhysicalObject square;
  private CollisionComponent squareComponent;
  private MockPhysicalObject circle;
  private CollisionComponent circleComponent;
  private MockPhysicalObject circle2;
  private CollisionComponent circle2Component;
  private MockPhysicalObject triangle;
  private CollisionComponent triangleComponent;
  private CollisionManager manager;
  
  @Override
  public void setUp() {
    CollisionManager.setup(WORLD_WIDTH, WORLD_HEIGHT);
    manager = CollisionManager.get();
    
    square = new MockPhysicalObject(0, 0);
    squareComponent = new CollisionComponent(square, CollisionManager.TYPE_HIT_RECEIVE);
    square.bounds = new Bounds(new PolygonBuilder()
        .add(-5, -5)
        .add(5, -5)
        .add(5, 5)
        .add(-5, 5)
        .build());
    
    triangle = new MockPhysicalObject(15, 0);
    triangleComponent = new CollisionComponent(triangle, CollisionManager.TYPE_HIT_RECEIVE);
    triangle.bounds = new Bounds(new PolygonBuilder()
        .add(-4, 4)
        .add(0, -4)
        .add(4, 4)
        .build());

    circle = new MockPhysicalObject(10, 5);
    circleComponent = new CollisionComponent(circle, CollisionManager.TYPE_HIT_RECEIVE);
    circle.bounds = new Bounds(new Circle(4));
    
    circle2 = new MockPhysicalObject(10, 8);
    circle2Component = new CollisionComponent(circle2, CollisionManager.TYPE_HIT_RECEIVE);
    circle2.bounds = new Bounds(new Circle(4));
    
    manager.addObject(squareComponent);
    manager.addObject(triangleComponent);
    manager.addObject(circleComponent);
    manager.addObject(circle2Component);
  }
  
  public void testGetNumAxis_withPolygon() {
    assertEquals(4, manager.getNumAxis(square, triangle));
    assertEquals(4, manager.getNumAxis(square, circle));
    assertEquals(3, manager.getNumAxis(triangle, circle));
  }
  
  public void testGetNumAxis_circleWithPolygon() {
    assertEquals(4, manager.getNumAxis(circle, square));
    assertEquals(3, manager.getNumAxis(circle, triangle));
  }
  
  public void testGetNumAxis_circleWithCircle() {
    assertEquals(1, manager.getNumAxis(circle, circle2));
  }
  
  public void testGetAxis_twoPolygons() {
    Vector2d axis = new Vector2d(0, 0);
    Vector2d expected;
    
    expected = new Vector2d(0, 10);
    expected.normalize();
    manager.getAxis(square, triangle, 0, axis);
    assertTrue(expected + "!=" + axis, expected.equals(axis));
    
    expected = new Vector2d(-10, 0);
    expected.normalize();
    manager.getAxis(square, triangle, 1, axis);
    assertTrue(expected + "!=" + axis, expected.equals(axis));
  }
  
  public void testGetAxis_circleAndPolygon() {
    Vector2d axis = new Vector2d(0, 0);
    Vector2d expected;
    
    expected = new Vector2d(-15, -10);
    expected.normalize();
    manager.getAxis(circle, square, 0, axis);
    assertTrue(expected + "!=" + axis, expected.equals(axis));
    
    expected = new Vector2d(-5, -10);
    expected.normalize();
    manager.getAxis(circle, square, 1, axis);
    assertTrue(expected + "!=" + axis, expected.equals(axis)); 
  }
  
  public void testGetAxis_circleAndCircle() {
    Vector2d axis = new Vector2d(0, 0);
    Vector2d expected;
    
    expected = new Vector2d(0, 3);
    expected.normalize();
    manager.getAxis(circle, circle2, 0, axis);
    assertTrue(expected + "!=" + axis, expected.equals(axis));
    
    expected = new Vector2d(0, -3);
    expected.normalize();
    manager.getAxis(circle2, circle, 0, axis);
    assertTrue(expected + "!=" + axis, expected.equals(axis)); 
  }
  
  public void testCheckForCollision() {
    manager.checkForCollision(squareComponent, 1000l);
    assertFalse(square.collided);
    assertFalse(triangle.collided);
    
    square.x = 8;
    manager.checkForCollision(squareComponent, 1000l);
    assertTrue(square.collided);
    assertTrue(triangle.collided);
    
    manager.checkForCollision(circleComponent, 1000l);
    assertTrue(circle.collided);
    assertTrue(circle2.collided);
    
    square.collided = false;
    circle.collided = false;
    
    circle.x = -7;
    square.x = 0;
    manager.checkForCollision(circleComponent, 1000l);
    assertTrue(square.collided);
    assertTrue(circle.collided);
  }
  
  class MockPhysicalObject extends PhysicalObject {
    public boolean collided = false;
    public MockPhysicalObject(float x, float y) {
      super(x, y);
    }
    
    public void collide(PhysicalObject object, Vector2d avoidVector) {
      collided = true;
    }
  }
}
