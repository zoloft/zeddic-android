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

package com.zeddic.game.common.effects;

import com.zeddic.game.common.particle.BasicParticleOptions;
import com.zeddic.game.common.particle.ParticleEmitter;
import com.zeddic.game.common.particle.PixelParticle;
import com.zeddic.game.common.particle.ParticleEmitter.ParticleEmitterBuilder;

public class HitExplosion extends Explosion {

  public HitExplosion() {
    this(0, 0);
  }
  
  public HitExplosion(float x, float y) {
    super(x, y);
  }

  protected void createEmitter() {
    emitter = new ParticleEmitterBuilder()
      .at(x, y)
      .withEmitMode(ParticleEmitter.MODE_DIRECTIONAL)
      .withEmitSpeedJitter(18)
      .withEmitLife(400)
      .withParticleSpeed(20)
      .withParticleAlphaRate(-4)
      .withParticleLife(400)
      .withMaxParticles(5)
      .withEmitRate(200)
      .withEmitAngle(0)
      .withEmitCycle(false)
      .withEmitAngleJitter(20)
      .withParticleClass(PixelParticle.class)
      .withDataForParticles(new BasicParticleOptions())
      .build();
  }
}
