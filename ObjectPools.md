# Perils of Garbage Collection #

When making games in Android you want to avoid creating new objects in you main game loop. The main reason for this is garbage collection. If you are creating new objects, the game will have to clean up after them, resulting in continuous stutters and a generally unplayable game.

As a rule of thumb, this means **you should create all major game objects you intend to use before starting the main game loop**.

For example, if you intend on having at max 50 enemy ships on the screen, you should create all 50 enemies before the game starts. You don't have to show the ships, but they should be created and stored in memory in advance.

# Object Pools #
`ObjectPool` is a class that allows you to create a set of objects once, then reuse them throughout the life of a game.

For example, you could create 200 bullets in advance, then simply 'take' a bullet from the pool once a gun has been triggered. Once the bullet is destroyed, you can return the object to the pool.

This allows the same set of objects to simply be cycled around, without having to create or delete any objects within the main game thread.

# Using Object Pools #
The easiest way to use object pools is with a class known as the `[http://code.google.com/p/zeddic-android/source/browse/trunk/src/com/zeddic/game/common/util/ObjectStockpile.java ObjectStockpile]`. This class provides a wrapper around the `ObjectPool` class and allows you to create pools for multiple types of objects.

An example use of the ObjectStockpile

```

public class SampleGame extends Game {
  
  private ObjectStockpile stockpile = new ObjectStockpile();
  
  public void init() {
    stockpile.createSupply(Bullet.class, 200);
    stockpile.createSupply(EnemyShip.class, 20);
    stockpile.createSupply(Explosion.class, 50);
  }

  public void draw(Canvas c) {

    // Draw all objects in the stockpile.
    stockpile.draw(c);
  }

  public void update(long time) {

    // Update all objects in the stockpile.
    stockpile.update(time);
  }

  private void fire() {

    // Take an object from the pool.
    Bullet bullet = stockpile.take(Bullet.class);
    
    // Make sure the pool wasn't empty.
    if (bullet == null) 
      return;

    // Reset the spawned bullet to a fresh state, clearing
    // anything from its prior life.
    bullet.reset();
    bullet.x = 50;
    bullet.y = 50;

    // While the bullet is taken from the pool, it will not
    // be drawn or updated until it is enabled. This allows you
    // to take an object from the pool and potentially display a
    // spawning animation before beginning to draw it.
    bullet.enable();

    // To restore a bullet to the pool, simply call kill(). The
    // next time the stockpile is updated, it will reclaim any
    // object that has been killed.
    bullet.kill();

    // Note that you don't need to manually call draw() or
    // update() on taken objects. The stockpile will do this for
    // you for any enabled object that it contains
  }
}

```