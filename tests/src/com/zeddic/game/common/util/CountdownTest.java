package com.zeddic.game.common.util;

import junit.framework.TestCase;

import com.zeddic.game.common.util.Countdown;

public class CountdownTest extends TestCase {

  public void testUpdate() {
    Countdown countdown = new Countdown(100);
    assertFalse(countdown.isDone());
    assertEquals(100, countdown.duration);
    
    // Make sure it is off by default
    countdown.update(200);
    assertFalse(countdown.isDone());
    
    countdown.start();
    assertTrue(countdown.isCounting());
    assertTrue(countdown.update(100));
    assertTrue(countdown.isDone());
    assertFalse(countdown.isCounting());
  }
  
  public void testIsDone() {
    Countdown countdown = new Countdown(100);
    countdown.start();
    assertFalse(countdown.isDone());
    countdown.update(50);
    assertFalse(countdown.isDone());
    countdown.update(50);
    assertTrue(countdown.isDone());
  }
  
  public void testReset() {
    Countdown countdown = new Countdown(100);
    countdown.start();
    countdown.update(100);
    assertTrue(countdown.isDone());
    
    countdown.reset(200);
    assertFalse(countdown.isDone());
    countdown.update(100);
    assertFalse(countdown.isDone());
    
    countdown.start();
    countdown.update(200);
    assertTrue(countdown.isDone());
  }
}
