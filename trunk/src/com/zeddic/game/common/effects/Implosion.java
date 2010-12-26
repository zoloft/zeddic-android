package com.zeddic.game.common.effects;

import com.zeddic.game.common.particle.ParticleEmitter;
import com.zeddic.game.common.particle.ParticleEmitter.ParticleEmitterBuilder;

public class Implosion extends Explosion {

  public Implosion() {
    super(0, 0);
  }
  
  public Implosion(float x, float y) {
    super(x, y);
  }

  protected void createEmitter() {
    emitter = new ParticleEmitterBuilder()
        .at(x, y)
        .withEmitMode(ParticleEmitter.MODE_OMNI)
        .withEmitSpawnDistance(40)
        .withParticleSpeed(0)
        .withEmitLife(5 * 1000)
        .withMaxParticleSpeed(20)
        //.withParticleAlpha(100)
        //.withParticleAlphaRate(0)
        .withParticleLife(500)
        .withMaxParticles(30)
        .withEmitRate(100)
        //.withParticleClass(SpawnParticle.class)
        .withGravityWell(this, 10)
        .withGravityWellDispawnDistance(5)
        .build();
  }
}
