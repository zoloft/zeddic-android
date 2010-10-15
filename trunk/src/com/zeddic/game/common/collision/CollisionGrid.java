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

package com.zeddic.game.common.collision;

import com.zeddic.game.common.PhysicalObject;

/**
 * A matrix of regions that keeps track of objects that share similar space
 * in the world. This divides the world into square regions, much like the
 * checkerboard pattern on a chess board. As objects move in the world they
 * update their position in the grid.
 * 
 * This is used for the 'broad' phase of collision detection. An object can
 * query the grid to find other objects that are 'nearby' (in its grid spots
 * or in nearby spots). It can then perform specific, 'narrow' phase
 * collision detection on the specific nearby objects. This can dramatically
 * boost performance.
 * 
 * This grid keeps track of objects based on their center x/y position. 
 * PhysicalObjects are assumed not to be bigger than 2 grid positions.
 * Objects that are 'stationary', and do not move may, will be placed in
 * multiple grid positions.
 * 
 * @author baileys (Scott Bailey)
 */
public class CollisionGrid {

  /** 
   * The size, in world dimensions, that a single grid region should contain.
   */
  public float gridSize;
  
  /**
   * How many grid spots in the x direction.
   */
  public int width; 
  
  /**
   * How many grid spots in the y direction.
   */
  public int height;
  
  /**
   * The grid that contains the various objects.
   */
  public GridSpot[][] grid; 
  
  /**
   * Creates a new grid that covers the given world size. The world
   * is chopped up into regions based on the size of gridSize.
   */
  public CollisionGrid(float mapWidth, float mapHeight, float gridSize) {
    this.gridSize = gridSize;
    int width = (int) Math.ceil(mapWidth / gridSize);
    int height = (int) Math.ceil(mapHeight / gridSize);
    createGrid(width, height);
  }
  
  /**
   * Creates the grid.
   */
  private void createGrid(int width, int height) {
    this.width = width;
    this.height = height;
    
    grid = new GridSpot[width][height];
    
    for (int x = 0 ; x < width ; x++) {
      for (int y = 0 ; y < height ; y++) {
        grid[x][y] = new GridSpot();
      }
    }
  }
  
  /**
   * Adds a stationary object to the grid. A stationary object, unlike
   * regular objects, will have a reference added to every grid spot
   * that its width and height touches.
   */
  public void addStationaryObject(CollisionComponent component) {
    PhysicalObject object = component.object;
    int minX = convertMapToGridValue(object.x - object.bounds.shape.width / 2);
    int maxX = convertMapToGridValue(object.x + object.bounds.shape.width / 2);
    int minY = convertMapToGridValue(object.y - object.bounds.shape.height / 2);
    int maxY = convertMapToGridValue(object.y + object.bounds.shape.height / 2);
    for ( int x = minX ; x <= maxX ; x++) {
      for ( int y = minY ; y <= maxY ; y++) {
        add(object, x, y);
      }
    }
    component.gridSpot = null;
  }
  
  /**
   * Adds a movable object to the grid. Only its center point will
   * be used to place it in the grid.
   */
  public void addObject(CollisionComponent component) {
    PhysicalObject object = component.object;
    int x = convertMapToGridValue(object.x);
    int y = convertMapToGridValue(object.y);
    component.gridSpot = add(object, x, y);
  }
  
  /**
   * Returns an array of Grid positions of all the spots around the given
   * object. Note that some grid spots may be null (if it is out of bounds).
   * Returned spots will be placed in the provided nearby[] array.
   */
  public void getNearbyGridSpots(CollisionComponent component, GridSpot[] nearby) {
    int x = (int) (component.object.x / gridSize);
    int y = (int) (component.object.y / gridSize);
    
    if (x < 0 || x >= width || y < 0 || y >= height ) {
      nearby[1] = null;
      nearby[2] = null;
      nearby[3] = null;
      nearby[4] = null;
      return;
    }
 
    GridSpot here = grid[x][y];
    if (component.type == CollisionManager.TYPE_HIT_RECEIVE ||
        component.type == CollisionManager.TYPE_RECEIVE_ONLY) {
      updatePosition(component, here);
    }
    
    nearby[0] = here;
    /*nearby[1] = null;
    nearby[2] = null;
    nearby[3] = null;
    nearby[4] = null;*/
    nearby[1] = x - 1 >= 0 ? grid[x - 1][y] : null;
    nearby[2] = y - 1>= 0 ? grid[x][y - 1] : null;
    nearby[3] = x + 1 < width ? grid[x + 1][y] : null;
    nearby[4] = y + 1< height ? grid[x][y + 1] : null;
  }
  
  /**
   * Returns a list of nearby grid spots that are within a given distance of
   * the starting world position. Returns the number of non-empty gridspots
   * that were found. Populated gridspots are put into nearby.
   */
  public int getNearbyGridSpots(
      float worldX,
      float worldY,
      float distance,
      GridSpot[] nearby) {
    
    int centerX = (int) (worldX / gridSize);
    int centerY = (int) (worldY / gridSize);

    int range = (int) (distance / gridSize);
    int minX = centerX - range;
    int maxX = centerX + range;
    int minY = centerY - range;
    int maxY = centerY + range;
    
    GridSpot spot;
    int hits = 0;
    for (int x = minX; x <= maxX; x++) {
      for (int y = minY; y <= maxY; y++) {
        spot = get(x, y);
        if (spot != null) {
          nearby[hits] = spot;
          hits++;
        }
      }
    }
    
    return hits;
  }
  
  /**
   * Removes an object from the grid entirely.
   */
  public void removeObject(CollisionComponent component) {
    int x = (int) (component.object.x / gridSize);
    int y = (int) (component.object.y / gridSize);
 
    GridSpot newSpot = get(x, y);
    newSpot.remove(component.object);
  }
  
  /**
   * Updates an objects position in the grid.
   */
  public void updatePosition(CollisionComponent component) {
    int x = (int) (component.object.x / gridSize);
    int y = (int) (component.object.y / gridSize);
 
    GridSpot newSpot = get(x, y);
    updatePosition(component, newSpot);
  }
  
  /**
   * Updates an objects position in the grid.
   */
  private void updatePosition(CollisionComponent component, GridSpot newSpot) {
    if (component.type == CollisionManager.TYPE_HIT_RECEIVE ||
        component.type == CollisionManager.TYPE_RECEIVE_ONLY) {
      if (newSpot != component.gridSpot) {
        if (component.gridSpot != null) {
          component.gridSpot.remove(component.object);
        }
        if (newSpot != null) {
          newSpot.add(component.object);
        }
        component.gridSpot = newSpot;
      } 
    }
  }
  
  /**
   * Returns the grid represented by the world position.
   */
  public GridSpot getObjectsAtWorldPosition(float mapX, float mapY) {
    int x = (int) Math.floor(mapX / gridSize);
    int y = (int) Math.floor(mapY / gridSize);
    return get(x, y);
  }
  
  private int convertMapToGridValue(float rawValue) {
    return (int) Math.floor(rawValue / gridSize);
  }
  
  private GridSpot get(int x, int y) {
    if ( x < 0 || x >= width || y < 0 || y >= height)
      return null;
    return grid[x][y];
  }
  
  private GridSpot add(PhysicalObject object, int x, int y) {
    if ( x < 0 || x > width || y < 0 || y > height)
      return null;
    grid[x][y].add(object);
    return grid[x][y];
  }
}
