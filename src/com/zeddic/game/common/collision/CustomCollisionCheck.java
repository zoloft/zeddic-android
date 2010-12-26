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

import com.zeddic.game.common.PhysicalObject;

/**
 * Custom collision check optimizations. A game may specify special 
 * collision checks by extending this class and providing it to the
 * {@link CollisionManager}.
 * 
 * @author baileys@google.com (Scott Bailey)
 */
public class CustomCollisionCheck {
  
  /**
   * Returns true if the collision management system should skip checking
   * a collision between the src and destination objects.
   */
  public boolean shouldSkipCollisionCheck(
      PhysicalObject src,
      PhysicalObject dest,
      long time) {
    
    return false;
  }
}
