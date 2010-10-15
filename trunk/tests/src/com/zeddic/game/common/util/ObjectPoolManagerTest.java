package com.zeddic.game.common.util;

import android.test.AndroidTestCase;

import com.zeddic.game.common.util.ObjectPoolManager;
import com.zeddic.game.common.util.ObjectPool.ObjectBuilder;

/**
 * Tests for the {@link ObjectPoolManager}.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public class ObjectPoolManagerTest extends AndroidTestCase {

  private static final int POOL_SIZE = 10;
  
  ObjectPoolManager<MockGameObject> manager;
  
  @Override
  public void setUp() {
    manager = new ObjectPoolManager<MockGameObject>(MockGameObject.class);
  }
  
  public void testSimplePoolCreation() {
    assertNotNull(manager.pool);
  }
  
  /**
   * Tests using a customer pool size
   */
  public void testCorrectPoolSize() {
    manager = new ObjectPoolManager<MockGameObject>(
        MockGameObject.class, POOL_SIZE);
  
    assertNotNull(manager.pool);
    assertEquals(POOL_SIZE, manager.pool.items.length);
  }
  
  /**
   * Tests using custom builders.
   */
  public void testCustomBuilderCalled() {
    MockObjectBuilder builder = new MockObjectBuilder();
    
    manager = new ObjectPoolManager<MockGameObject>(
        MockGameObject.class, POOL_SIZE, builder);

    assertNotNull(manager.pool);
    assertEquals(POOL_SIZE, manager.pool.items.length);
    assertTrue(builder.called);
  }
  
  /**
   * Tests that objects from the pool can be accessed.
   */
  public void testTake() {
    MockGameObject temp = manager.take();
    assertNotNull(temp);
  }
  
  /**
   * Tests that pool objects are being updated.
   */
  public void testUpdate() {
    manager.activateAllItemsInPool();
    manager.update(10);
    for (MockGameObject gameObject : manager.pool.items) {
      assertTrue(gameObject.updateCalled);
    }
  }
  
  public void testUpdate_withObjectToReturnToPool() {
    manager = new ObjectPoolManager<MockGameObject>(
        MockGameObject.class, POOL_SIZE);
    
    manager.activateAllItemsInPool();
    
    // Take an object from the pool and verify pool shrunk.
    MockGameObject gameObject = manager.take();
    gameObject.active = true;
    assertEquals(POOL_SIZE - 1, manager.pool.numLeft());
    
    // "kill" the object.
    gameObject.dieOnUpdate = true;
    
    // Verify that the manager reclaims the object back into the
    // available pool when updated.
    manager.update(0);
    
    assertEquals(POOL_SIZE, manager.pool.numLeft());
  }
  
  /**
   * Tests that pool objects are being drawn.
   */
  public void testDraw() {
    manager.activateAllItemsInPool();
    manager.draw(null);
    for (MockGameObject gameObject : manager.pool.items) {
      assertTrue(gameObject.drawCalled);
    }
  }
  
  public void testDraw_skipsInactiveItems() {
    manager.activateAllItemsInPool();
    
    MockGameObject gameObject = manager.take();
    gameObject.active = false;
    manager.draw(null);
    
    assertFalse(gameObject.drawCalled);
  }
  
  public void testReclaimPool() {
    manager = new ObjectPoolManager<MockGameObject>(
        MockGameObject.class, POOL_SIZE);
    
    manager.take();
    manager.take();
    manager.take();
    
    assertEquals(POOL_SIZE - 3, manager.pool.numLeft());
    
    manager.reclaimPool();
    
    assertEquals(POOL_SIZE, manager.pool.numLeft());
  }
  
  /**
   * Simple mock builder to make sure its being used.
   */
  public class MockObjectBuilder extends ObjectBuilder<MockGameObject> {
    
    public boolean called = false;
    
    @Override
    public MockGameObject get(int count) {
      called = true;
      return new MockGameObject();
    }
  }
}
