package com.zeddic.game.common.stage;

import junit.framework.TestCase;

public class StageIncrementorTest extends TestCase {

  public void testUpdate_whenEmpty() {
    
    // With an empty incrementor, nothing should be done.
    StageIncrementor<MockStage> incrementor = new StageIncrementor<MockStage>();    
    incrementor.reset();
    incrementor.add(0);
    incrementor.add(20);    
  }
  
  public void testUpdate() {
    
    MockStage stage1 = new MockStage(20);
    MockStage stage2 = new MockStage(50);
    
    StageIncrementor<MockStage> inc = new StageIncrementor<MockStage>();    
    inc.add(stage1);
    inc.add(stage2);
    
    inc.add(10);
    assertFalse(stage1.triggered);
    assertFalse(stage2.triggered);
    
    inc.add(5);
    assertFalse(stage1.triggered);
    assertFalse(stage2.triggered);
    
    inc.add(5);
    assertTrue(stage1.triggered);
    assertFalse(stage2.triggered);

    // Make sure a stage can never be triggered twice.
    stage1.triggered = false;
    inc.add(5);
    assertFalse(stage1.triggered);
    assertFalse(stage2.triggered);
    
    
    inc.add(51);
    assertFalse(stage1.triggered);
    assertTrue(stage2.triggered);
  }
  
  public void testReset() {
    MockStage stage1 = new MockStage(20);
    MockStage stage2 = new MockStage(50);
    
    StageIncrementor<MockStage> inc = new StageIncrementor<MockStage>();    
    inc.add(stage1);
    inc.add(stage2);
    
    inc.add(70);
    assertTrue(stage1.triggered);
    assertTrue(stage2.triggered);
    
    stage1.triggered = false;
    stage2.triggered = false;
    inc.reset();
    
    inc.add(25);
    assertTrue(stage1.triggered);
    assertFalse(stage2.triggered);
    
    inc.add(25);
    assertTrue(stage2.triggered);
  }
  
  public void testBuilder() {
    
    MockStage stage1 = new MockStage(20);
    MockStage stage2 = new MockStage(50);
    
    StageIncrementor<MockStage> inc = new StageIncrementor.Builder<MockStage>()
      .add(stage1)
      .add(stage2)
      .build();
    
    assertNotNull(inc);
    inc.add(40);
    assertTrue(stage1.triggered);
    assertFalse(stage2.triggered);
    
    inc.add(55);
    assertTrue(stage2.triggered);
  }
  
 
  static class MockStage implements Stage {

    boolean triggered = false;
    int value;
    
    public MockStage(int value) {
      this.value = value;
    }
    
    public int getValue() {
      return value;
    }

    public void onTrigger() {
      triggered = true;
    }
    
  }
}
