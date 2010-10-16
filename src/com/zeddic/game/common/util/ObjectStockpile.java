package com.zeddic.game.common.util;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Canvas;

import com.zeddic.game.common.GameObject;

/**
 * Creates and manages multiple object pools of different classes. A pool
 * represents some reusable collection of objects that are instantiated once
 * then recycled through the game, eliminating the need for allocating new
 * memory at run time. Objects may be taken using the take() method. Once
 * kill()ed, an object will automatically be re-added to a pool for subsequent
 * takes(). If a pool is empty (all pool items have been taken already), take()
 * will return null.
 * 
 * <p>Note that in order to function, the stock pile must have its update() and
 * draw() methods called on every update cycle. These calls will draw all
 * currently active objects in the pool.
 * 
 * <p>Example uses:
 * <code>
 * ObjectStockpile stockpile = new ObjectStockpile();
 * stockpile.createSupply(EnemyShip.class, 50);
 * 
 * EnemyShip ship = stockpile.take(EnemyShip.class);
 * ship.enable();
 * </code>
 * 
 * @author scott@zeddic.com
 */
public class ObjectStockpile extends GameObject {
  
  public Map<Class<? extends GameObject>, ObjectPoolManager<? extends GameObject>> supply;
    
  public ObjectStockpile() {
    supply = new HashMap<Class<? extends GameObject>, ObjectPoolManager<? extends GameObject>>();
  }
 
  /**
   * Creates a new supply of the specified class.
   */
  public <T extends GameObject> ObjectPoolManager<T> createSupply(
      Class<T> shipType, int maxShips) {
    
    ObjectPoolManager<T> pool = new ObjectPoolManager<T>(shipType, maxShips);
    supply.put(shipType, pool);
    return pool;
  }
  
  /**
   * Causes all pools to be reset, automatically recycling all taken objects
   * and restoring them to their respective pools.
   */
  public void reset() {
    for (ObjectPoolManager<? extends GameObject> pool : supply.values()) {
      pool.reclaimPool();
    }
  }
  
  /**
   * Calls the update method on all currently enabled objects in all pools.
   */
  public void update(long time) {
    for (ObjectPoolManager<? extends GameObject> pool : supply.values()) {
      pool.update(time);
    }
  }
  
  /**
   * Calls the draw method on all currently enabled objects in all pools.
   */
  public void draw(Canvas canvas) {
    for (ObjectPoolManager<? extends GameObject> pool : supply.values()) {
      pool.draw(canvas);
    }
  }
  
  /**
   * Obtains a single object pool for the specified class.
   */
  @SuppressWarnings("unchecked")
  public <T extends GameObject> ObjectPoolManager<T> getSupply(Class<T> shipType) {
    return (ObjectPoolManager<T>) supply.get(shipType);
  }
  
  /**
   * Obtains an instance of the specified class time. Returns null if
   * the pool has run out of instances.
   */
  @SuppressWarnings("unchecked")
  public <T extends GameObject> T take(Class<T> shipType) {
    return (T) supply.get(shipType).take();
  }
}
