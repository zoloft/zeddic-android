package com.zeddic.game.common.collision;

import junit.framework.TestCase;

import com.zeddic.game.common.PhysicalObject;
import com.zeddic.game.common.collision.CollisionComponent;
import com.zeddic.game.common.collision.CollisionGrid;
import com.zeddic.game.common.collision.CollisionManager;
import com.zeddic.game.common.collision.GridSpot;

public class CollisionGridTest extends TestCase {

  private static final int GRID_WIDTH = 500;
  private static final int GRID_HEIGHT = 500;
  
  CollisionComponent component;
  PhysicalObject object; 
  
  @Override
  public void setUp() {
    CollisionManager.setup(GRID_WIDTH, GRID_HEIGHT);
    object = new PhysicalObject(0, 0);
    component = new CollisionComponent(
        object, CollisionManager.TYPE_HIT_RECEIVE);
  }
  
  public void testCreateGrid() {
    CollisionGrid grid = new CollisionGrid(GRID_WIDTH, GRID_HEIGHT, 50);
    
    assertEquals(50f, grid.gridSize);
    assertEquals(10, grid.width);
    assertEquals(10, grid.height);
    
    assertEquals(10, grid.grid.length);
    for (int i = 0 ; i < grid.grid.length ; i++) {
      assertEquals(10, grid.grid[i].length);
    }
  }
  
  public void testAddStationaryObject() {
    CollisionGrid grid = new CollisionGrid(GRID_WIDTH, GRID_HEIGHT, 50);
    object.bounds.shape.width = 100;
    object.bounds.shape.height = 100;
    object.x = 25;
    object.y = 25;
    
    grid.addStationaryObject(component);
    
    assertEquals(object, grid.grid[0][0].items.items[0]);
    assertEquals(object, grid.grid[1][0].items.items[0]);
    assertEquals(object, grid.grid[1][1].items.items[0]);
    assertEquals(object, grid.grid[0][1].items.items[0]);
  }
  
  public void testAddObject() {
    CollisionGrid grid = new CollisionGrid(GRID_WIDTH, GRID_HEIGHT, 50);
    object.bounds.shape.width = 100;
    object.bounds.shape.height = 100;
    object.x = 25;
    object.y = 25;
    
    grid.addObject(component);
    
    assertEquals(object, grid.grid[0][0].items.items[0]);
    assertEquals(0, grid.grid[1][0].items.size);
    assertEquals(0, grid.grid[0][1].items.size);
    assertEquals(0, grid.grid[1][1].items.size);
  }
  
  public void testUpdatePosition() {
    CollisionGrid grid = new CollisionGrid(GRID_WIDTH, GRID_HEIGHT, 50);
    object.bounds.shape.width = 100;
    object.bounds.shape.height = 100;
    object.x = 25;
    object.y = 25;
    
    grid.addObject(component);
    assertEquals(object, grid.grid[0][0].items.items[0]);
    
    object.x = 75;
    object.y = 75;
    grid.updatePosition(component);
    
    assertEquals(0, grid.grid[0][0].items.size);
    assertEquals(object, grid.grid[1][1].items.items[0]);
    
    object.x = -50;
    object.y = -50;
    grid.updatePosition(component);
    
    assertEquals(0, grid.grid[1][1].items.size);
  }
  
  public void testGetObjectsAtWorldPosition() {
    CollisionGrid grid = new CollisionGrid(GRID_WIDTH, GRID_HEIGHT, 50);
    
    PhysicalObject object1 = new PhysicalObject(75, 75);
    CollisionComponent component1 = new CollisionComponent(
        object1, CollisionManager.TYPE_HIT_RECEIVE);
    
    PhysicalObject object2 = new PhysicalObject(80, 80);
    CollisionComponent component2 = new CollisionComponent(
        object2, CollisionManager.TYPE_HIT_RECEIVE);
    
    PhysicalObject object3 = new PhysicalObject(101, 101);
    CollisionComponent component3 = new CollisionComponent(
        object3, CollisionManager.TYPE_HIT_RECEIVE);
    
    grid.addObject(component1);
    grid.addObject(component2);
    grid.addObject(component3);
    
    GridSpot spot = grid.getObjectsAtWorldPosition(75, 75);
    assertEquals(2, spot.items.size);
    assertEquals(object1, spot.items.items[0]);
    assertEquals(object2, spot.items.items[1]);
    
    spot = grid.getObjectsAtWorldPosition(101, 101);
    assertEquals(1, spot.items.size);
    assertEquals(object3, spot.items.items[0]);
    
    spot = grid.getObjectsAtWorldPosition(-20, -20);
    assertNull(spot);
  }
  
  public void testGetNearbyObjects() {
    
    // Setup some objects in the grid.
    CollisionGrid grid = new CollisionGrid(GRID_WIDTH, GRID_HEIGHT, 50);
    
    PhysicalObject object1 = new PhysicalObject(75, 45);
    CollisionComponent component1 = new CollisionComponent(
        object1, CollisionManager.TYPE_HIT_RECEIVE);
    
    PhysicalObject object2 = new PhysicalObject(80, 80);
    CollisionComponent component2 = new CollisionComponent(
        object2, CollisionManager.TYPE_HIT_RECEIVE);
    
    PhysicalObject object3 = new PhysicalObject(101, 101);
    CollisionComponent component3 = new CollisionComponent(
        object3, CollisionManager.TYPE_HIT_RECEIVE);
    
    PhysicalObject object4 = new PhysicalObject(1, 1);
    CollisionComponent component4 = new CollisionComponent(
        object4, CollisionManager.TYPE_HIT_RECEIVE);
    
    grid.addObject(component1);
    grid.addObject(component2);
    grid.addObject(component3);
    grid.addObject(component4);
    
    // Get objects around component4
    GridSpot[] nearby = new GridSpot[9];
    grid.getNearbyGridSpots(component4, nearby);
    
    assertTrue(objectInGridSpots(nearby, object1));
    assertFalse(objectInGridSpots(nearby, object2));
    assertFalse(objectInGridSpots(nearby, object3));
    assertTrue(objectInGridSpots(nearby, object4));
    
    // Move the object and more nearby objects.
    object4.x = 77;
    object4.y = 77;
    grid.getNearbyGridSpots(component4, nearby);
    
    assertTrue(objectInGridSpots(nearby, object1));
    assertTrue(objectInGridSpots(nearby, object2));
    assertFalse(objectInGridSpots(nearby, object3));
    assertTrue(objectInGridSpots(nearby, object4));
    
    // Move the object and more nearby objects.
    object4.x = 160;
    object4.y = 160;
    grid.getNearbyGridSpots(component4, nearby);
    
    assertFalse(objectInGridSpots(nearby, object1));
    assertFalse(objectInGridSpots(nearby, object2));
    assertFalse(objectInGridSpots(nearby, object3));
    assertTrue(objectInGridSpots(nearby, object4));
  }
  
  private boolean objectInGridSpots(GridSpot[] nearby, PhysicalObject object) {
    boolean found = false;
    for ( int i = 0 ; i < nearby.length ; i++ ) {
      if (nearby[i] != null) {
        for (PhysicalObject temp : nearby[i].items.items) {
          if (temp == object) {
            found = true;
          }
        }
      }
    }
    return found;
  }
}
