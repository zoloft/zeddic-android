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

package com.zeddic.game.common.collision;

import android.util.Log;

import com.zeddic.game.common.Component;
import com.zeddic.game.common.PhysicalObject;

/**
 * A component that allows physical objects to have collision detection.
 * When a collision occurs, the PhysicalObject will be notified with
 * it's Collide method being called.
 * 
 * Objects may add a CollisionComponent to themselves in various modes.
 * For example, an object may register to see if it hits other objects,
 * but never check if an object collides with it. This can be useful
 * to reduce collision checks to increase performance. For example,
 * bullets may register themselves as TYPE_HIT_ONLY to avoid wasted
 * collision checks between each other. For a full list of types, 
 * see {@link CollisionManager}.
 *
 * @author baileys (Scott Bailey)
 */
public class CollisionComponent extends Component {
  
  /** The object to do collisions for. */
  public PhysicalObject object;
  
  /** The type of collision checking to do. */
  public int type; 
  
  /** True if registered to be tracked in the collision system. */
  public boolean inCollisionSystem = false;
  
  /** 
   * The current grid position that this object is contained in within
   * the collision grid.
   */
  public GridSpot gridSpot;
  
  public CollisionComponent(
      PhysicalObject parent,
      int type) {
    this.object = parent;
    this.type = type;
  }
  
  /**
   * Registers the object with the collision management system so other
   * objects may collide with it.
   */  
  public void registerObject() {
    if (inCollisionSystem)
      return;
    
    if (type == CollisionManager.TYPE_HIT_RECEIVE || 
        type == CollisionManager.TYPE_RECEIVE_ONLY) {
      CollisionManager.get().addObject(this);
    } else if (type == CollisionManager.TYPE_STATIONARY) {
      CollisionManager.get().addStationaryObject(this);
    }
    
    inCollisionSystem = true;
  }
  
  public void unregisterObject() {
    if (!inCollisionSystem)
      return;
    
    CollisionManager.get().removeObject(this);
    
    inCollisionSystem = false;
  }
  
  @Override
  public void update(long time) {
    
    try {
      if (object.active) {
        
        if (!inCollisionSystem) {
          registerObject();
        }
        
        if (type == CollisionManager.TYPE_HIT_ONLY || 
            type == CollisionManager.TYPE_HIT_RECEIVE) {
          CollisionManager.get().checkForCollision(this, time); 
        } else if (type == CollisionManager.TYPE_RECEIVE_ONLY) {
          CollisionManager.get().updatePosition(this);
        }
      }
    } catch (Exception e) {
      Log.e(CollisionComponent.class.getName(),"Error detecting collision:", e);
    }
  }
}
