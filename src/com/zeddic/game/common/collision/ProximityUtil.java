package com.zeddic.game.common.collision;

import com.zeddic.game.common.PhysicalObject;

public class ProximityUtil {

  // Reuse data structures across calls. This prevents the class from being
  // thread safe, but saves having to create new memory allocations at run
  // time which would kill game performance, especially since these methods
  // may be call many many times.
  private static ProximityResult result = new ProximityResult();
  private static GridSpot[] spots = new GridSpot[1000];
  private static PhysicalObject[] nearbyObjects = new PhysicalObject[100];
  
  public static ProximityResult getNearbyObjects(
      Class<?> targetClass,
      float x,
      float y,
      float distance) {
    
    CollisionGrid grid = CollisionManager.get().getGrid();
    float maxDistance = distance * distance;
    int nearby = grid.getNearbyGridSpots(x, y, distance, spots);

    PhysicalObject object;
    GridSpot spot;
    int hits = 0;
    int numObjects;
    for ( int i = 0 ; i < nearby ; i++) {
      
      spot = spots[i];
      numObjects = spot.items.size;
      
      for (int j = 0; j < numObjects; j++) {
        
        object = spot.items.items[j];
        
        if (!object.active) {
          continue;
        }
        
        if (!targetClass.isInstance(object)) {
          continue;
        }

        float dX = x - object.x;
        float dY = y - object.y;
        float distanceSquared = dX * dX + dY * dY;
        
        if (distanceSquared > maxDistance) {
          continue;
        }
        
        if (hits < nearbyObjects.length) {
          nearbyObjects[hits] = object;
          hits++;
        }     
      }
    }
    
    // Store the results and return.
    result.hits = hits;
    result.objects = nearbyObjects;
    
    return result;
  }
  
  public static PhysicalObject getClosest(Class<?> targetClass, float x, float y, float distance) {
    return getClosest(targetClass, x, y, distance, null);
  }
  
  public static PhysicalObject getClosest(Class<?> targetClass, float x, float y, float distance, PhysicalObject exclude) {
    
    float minDistanceSquared = Float.MAX_VALUE;
    PhysicalObject target = null;
    PhysicalObject object;
    ProximityResult result = getNearbyObjects(targetClass, x, y, distance);
    for (int i = 0; i < result.hits; i++) {
      object = result.objects[i];
      
      if (object == exclude) {
        continue;
      }

      float dX = x - object.x;
      float dY = y - object.y;
      float distanceSquared = dX * dX + dY * dY;
      if (distanceSquared < minDistanceSquared) {
        target = object;
        minDistanceSquared = distanceSquared;
      }
    }
    
    return target;
  }
  
  public static class ProximityResult {
    public PhysicalObject[] objects;
    public int hits;
  }
}
