package com.zeddic.game.common.transitions;

import junit.framework.TestCase;

import com.zeddic.game.common.transistions.Range;

public class RangeTest extends TestCase {

  public void testGetProgress_simple() {
    
    Range range = new Range(0, 10);
    assertEquals(.5f, range.getProgress(5));
  }
  
  public void testGetProgress_offset() {
    Range range = new Range(10, 20);
    assertEquals(.5f, range.getProgress(15));
  }
  
  public void testGetProgress_offsetReversed() {
    Range range = new Range(20, 10);
    assertEquals(.9f, range.getProgress(11));
  }
  
  public void testGetProgress_outOfBounds() {
    Range range = new Range(10, 20);
    assertEquals(1f, range.getProgress(25));
  }
  
  public void testConvert() {
    Range range1 = new Range(0, 10);
    Range range2 = new Range(10, 20);
    assertEquals(15f, range1.convertValueToOtherRange(5, range2));
  }
}
