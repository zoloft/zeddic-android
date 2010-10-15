package com.zeddic.game.common.util;

import android.test.AndroidTestCase;

import com.zeddic.game.common.util.ObjectPool;
import com.zeddic.game.common.util.Vector2d;
import com.zeddic.game.common.util.ObjectPool.ObjectBuilder;

public class ObjectPoolTest extends AndroidTestCase {
  
  private static int POOL_SIZE = 10;
  
  Vector2d[] raw;
  ObjectPool<Vector2d> pool;
  
  @Override
  public void setUp() {
    raw = new Vector2d[POOL_SIZE];
    for (int i = 0 ; i < POOL_SIZE ; i++) {  
      raw[i] = new Vector2d(i, i);
    }
  }
  
  public void testTakeAndRestore() {
   
    pool = new ObjectPool<Vector2d>(Vector2d.class, raw);
    
    assertEquals("Pool did not start with right size.", POOL_SIZE, pool.numLeft());
    
    for (int i = 0 ; i < POOL_SIZE ; i++) {
      assertEquals("Pool take didn't fetch in the right order.", raw[i], pool.take());
      assertEquals(raw.length - i - 1, pool.numLeft());
    }
    
    assertEquals("Pool should be empty.", 0, pool.numLeft());
    assertNull("Pool should return null when empty.", pool.take());
    
    pool.restore(raw[0]);
    assertEquals("Pool didn't restore item.", 1, pool.numLeft());
    assertNotNull("Restored item not available.", pool.take());
  }
  
  public void testBuilder() {
    pool = new ObjectPool<Vector2d>(Vector2d.class, POOL_SIZE, new ObjectBuilder<Vector2d>() {
      @Override
      public Vector2d get(int count) {
        return new Vector2d(count, count);
      } 
    });
    
    assertEquals(POOL_SIZE, pool.numLeft());
    
    for (int i = 0 ; i < POOL_SIZE ; i++) {
      assertEquals((float) i, pool.take().x);
    }
  }
  
  public void testValidClassCasting() {
    pool = new ObjectPool<Vector2d>(Vector2d.class, POOL_SIZE, new ObjectBuilder<Vector2d>() {
      @Override
      public Vector2d get(int count) {
        return new Vector2d(count, count);
      } 
    });
    
    Vector2d test = pool.items[0];
    assertNotNull(test);
    assertEquals(0f, test.x);
  }
}
