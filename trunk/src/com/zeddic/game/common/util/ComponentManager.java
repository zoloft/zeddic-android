package com.zeddic.game.common.util;

import android.graphics.Canvas;

import com.zeddic.game.common.Component;
import com.zeddic.game.common.GameObject;

public class ComponentManager extends GameObject {

  SimpleList<Component> components;
  GameObject parent;
  
  public ComponentManager(GameObject parent) {
    this.components = new SimpleList<Component>(Component.class);
    this.parent = parent;
  }
  
  public void reset() {
    int size = components.size;
    Component component;
    for ( int i = 0 ; i < size ; i++) {
      component = components.items[i];
      component.reset();
    }
  }
  
  public void draw(Canvas canvas) {
    int size = components.size;
    Component component;
    for ( int i = 0 ; i < size ; i++) {
      component = components.items[i];
      component.draw(canvas);
    }
  }
  
  public void update(long time) {
    int size = components.size;
    Component component;
    for ( int i = 0 ; i < size ; i++) {
      component = components.items[i];
      component.update(time);
    }
  }
  
  public void add(Component component) {
    component.parent = this;
    components.add(component);
  }
}
