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

package com.zeddic.game.common.util;

import java.lang.reflect.Array;

import android.util.Log;

/**
 * Represents a reusable pool of objects. Objects are created on initialization
 * and can be taken, used, then given back to the pool. This allows you to 
 * avoid initializing new objects at runtime which leads to garbage collection
 * and choppyness. 
 */
public class ObjectPool<T> {
  
  //public ArrayList<T> raw;
  //public ArrayBlockingQueue<T> queue
  
  private static final int NOTHING_TO_WRITE = -1;
  
  public T[] items;
  private T[] queue;
  int read = 0;
  int write = NOTHING_TO_WRITE;
  
  /**
   * Creates a new pool using the builder to return new objects.
   */
  @SuppressWarnings("unchecked")
  public ObjectPool(Class<T> elementType, int size, ObjectBuilder<T> builder) {
    
    items = (T[]) Array.newInstance(elementType, size);
    queue = (T[]) Array.newInstance(elementType, size);
    builder.pool = this;
    
    for (int i = 0 ; i < items.length ; i++) {
      items[i] = builder.get(i);
      queue[i] = items[i];
    }
  }
  
  @SuppressWarnings("unchecked")
  public ObjectPool(Class<T> elementType, T[] pool) {
    items = pool;
    queue = (T[]) Array.newInstance(elementType, items.length);
    for ( int i = 0 ; i < pool.length ; i++) {
      queue[i] = pool[i];
    }
  }

  /**
   * Returns how many items are left in the pool.
   */
  public int numLeft() {
    if (write == NOTHING_TO_WRITE)
      return items.length;
    return write >= read ? write - read : items.length - read + write;
  }
  
  /**
   * Takes an object from the pool. Returns null if none are left.
   */
  public T take() {
    if (numLeft() == 0)
      return null;
    
    T item = queue[read];
    
    // If taking this item leaves new room to write, recenter
    // the writing pointer;
    if (write == NOTHING_TO_WRITE)
      write = read;
    
    read = (read + 1) % queue.length;
    return item;
  }
  
  /**
   * Gives an object back to the pool. Should only provide values
   * that were originally taken from the pool.
   */
  public void restore(T object) {
    if (write == NOTHING_TO_WRITE) {
      Log.e(ObjectPool.class.getCanonicalName(), 
          "Attempted to restore more items than taken from the pool! " + 
          "Theres an error somewhere!");
      return;
    }
    queue[write] = object;
    write = (write + 1) % queue.length;
 
    if (write == read)
      write = NOTHING_TO_WRITE;
  }
  
  public static abstract class ObjectBuilder<T> {
    public ObjectPool<T> pool;
    public abstract T get(int count);
  }

}
