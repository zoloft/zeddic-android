package com.zeddic.game.common.collision;

import android.test.AndroidTestCase;

import com.zeddic.game.common.PhysicalObject;
import com.zeddic.game.common.collision.GridSpot;

public class GridSpotTest extends AndroidTestCase {

  GridSpot spot;
  PhysicalObject dummy;
  PhysicalObject dummy2;
  
  @Override
  public void setUp() {
    spot = new GridSpot();
    dummy = new PhysicalObject(0, 0);
    dummy2 = new PhysicalObject(2, 2);
  }
  
  public void testAdd() {
    assertEquals(0, spot.items.size);
    spot.add(dummy);
   
    assertEquals(1, spot.items.size);
    assertEquals(dummy, spot.items.items[0]);
  }
  
  public void testAdd_cantAddSameItemTwice() {
    spot.add(dummy);
    spot.add(dummy);
    assertEquals(1, spot.items.size);
  }
  
  public void testRemove() {
    spot.add(dummy);
    spot.remove(dummy);
    assertEquals(0, spot.items.size);
    
    spot.add(dummy);
    spot.add(dummy2);
    assertEquals(2, spot.items.size);
    spot.remove(dummy);
    spot.remove(dummy2);
    assertEquals(0, spot.items.size);
  }
  
  public void testGrow() {
    
    // Insert enough objects to almost, but not quite, force it to grow.
    int initialSize = spot.items.size;
    for (int i = 0; i < initialSize; i++) {
      spot.add(new PhysicalObject(i, i));
    }
    
    assertEquals(initialSize, spot.items.size);
    assertEquals(initialSize, spot.items.size);
    
    spot.add(dummy);
    
    // Verify that it grew.
    assertNotSame(initialSize, spot.items.size);
    
    assertEquals(dummy, spot.items.items[initialSize]);
  }
}
