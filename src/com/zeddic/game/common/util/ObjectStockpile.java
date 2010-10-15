package com.zeddic.game.common.util;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Canvas;

import com.zeddic.game.common.GameObject;

public class ObjectStockpile extends GameObject {
  
  public Map<Class<? extends GameObject>, ObjectPoolManager<? extends GameObject>> supply;
    
  public ObjectStockpile() {
    supply = new HashMap<Class<? extends GameObject>, ObjectPoolManager<? extends GameObject>>();
  }
 
  public <T extends GameObject> ObjectPoolManager<T> createSupply(
      Class<T> shipType, int maxShips) {
    
    ObjectPoolManager<T> pool = new ObjectPoolManager<T>(shipType, maxShips);
    supply.put(shipType, pool);
    return pool;
  }
  
  public void reset() {
    for (ObjectPoolManager<? extends GameObject> pool : supply.values()) {
      pool.reclaimPool();
    }
  }
  
  public void update(long time) {
    for (ObjectPoolManager<? extends GameObject> pool : supply.values()) {
      pool.update(time);
    }
  }
  
  public void draw(Canvas canvas) {
    for (ObjectPoolManager<? extends GameObject> pool : supply.values()) {
      pool.draw(canvas);
    }
  }
  
  @SuppressWarnings("unchecked")
  public <T extends GameObject> ObjectPoolManager<T> getSupply(Class<T> shipType) {
    return (ObjectPoolManager<T>) supply.get(shipType);
  }
  
  @SuppressWarnings("unchecked")
  public <T extends GameObject> T take(Class<T> shipType) {
    return (T) supply.get(shipType).take();
  }
}
