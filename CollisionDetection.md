# Contents #



# Overview #
Collision detection is a means of detecting whether [PhysicalObjects](PhysicalObjects.md) in your game have 'hit', or otherwise collided with another object in the game world.

For example:
  * Has a bullet hit a wall or enemy?
  * Has the player collided with an obstacle?
  * Has an enemy collided with the player?

# Initializing the Collision System #
Before using the collision system, you must initialize it by telling it the size of the world.

In your Game's `init` method, simply call:

` CollisionManager.setup(MAX_WORLD_WIDTH, MAX_WORLD_HEIGHT); `

# Registering an Object with the Collision System #
To have the collision system check for collisions by or from your game object you must register it with the collision system. If your object isn't registered with the collision system, no collision checks will be done on it.

To do this, you'll want to create a [CollisionComponent](http://code.google.com/p/zeddic-android/source/browse/trunk/src/com/zeddic/game/common/collision/CollisionComponent.java) as a child component for your `GameObject`. Heres an example:

```

public class Ball extends PhysicalObject {
  private ComponentManager components;

  public Ball() {
    components = new ComponentManager(this);
    components.add(new CollisionComponent(this, CollisionManager.TYPE_HIT_RECEIVE);
    // Add other components here.

    setBounds(new Bounds(new Circle(5));
  }

  public void draw(Canvas c) {
    components.draw(c);

    // Draw the ball here
  }

  public void update(long time) {
    // Update the ball's physical location.
    super.update(time);

    // Check for a collision (and update all components)
    components.update(time);
  }

  // The collide method will automatically be called when this object
  // collides with some other object in the world. It is provided with
  // two objects: the other object that it collided with, and a vector
  // that when applied to the current object will cause it to no longer
  // be collided.
  public void collide(PhysicalObject object, Vector2d avoidVector) {
    if (object instanceof Bullet) {
      damage(5);
    } else {
      super.collide(object, avoidVector);
    }
  }
```

# Types of Collision Checks #

When you register your object with the collision system, you can specify different Collision Types. The collision types determine how the Collision System monitors your object and allows it to be more efficient.

There are 3 collision types to choose from inside `CollisionManager`

```
  /**
   * An object that can hit other objects but can never be hit. 
   * This is useful when many of a given type of object may exist
   * in the world, but it doesn't matter if anything runs into them. 
   * For example, bullets, where you want to avoid wasted checks to see
   * if bullets have hit other bullets.
   */
  public static final int TYPE_HIT_ONLY = 1;
  
  /**
   * An object that can hit other objects and be hit. For example:
   * the players ship.
   */
  public static final int TYPE_HIT_RECEIVE = 2;
  
  /**
   * An object that can receive collisions but can never hit something.
   * For example: asteroids that can't be hit by each other but can be hit
   * by a player ship. 
   */
  public static final int TYPE_RECEIVE_ONLY = 3;

```

# Object Bounds #
Every object registered with the Collision System has some 'bounding area' that the collision system uses to check for overlap.

The library uses a [Seperating Axis Theorem](http://en.wikipedia.org/wiki/Separating_axis_theorem) implementation of collision detection, meaning that these bounds may be complex polygons and not simply squares or circles. Note, however, that the collision system has special checks for collisions between circles, allowing these checks to be very fast if you do choose to use them.

![http://zeddic-android.googlecode.com/svn/wiki/bounds.png](http://zeddic-android.googlecode.com/svn/wiki/bounds.png)

You can set the bounds of your object by calling `setBounds()` within your objects constructor.

For example:

```
setBounds(new Bounds(new Circle(BALL_RADIUS));
```

or

```
setBounds(new Bounds(
    new PolygonBuilder()
        .add(-10, -2)
        .add(10, -2)
        .add(10, 2)
        .add(-10, 2)
        .build()));
```

You may also rotate and scale [PhysicalObject](http://code.google.com/p/zeddic-android/source/browse/trunk/src/com/zeddic/game/common/PhysicalObject.java)s and their bounds will update accordingly.


# Custom Collision Checks #
Sometimes you'll want to optimize the collision system for certain types of collisions. For example, you may want enemy bullets to ignore other enemies. While you could simply provide an empty collide method, you still waste a lot of CPU time calculating and checking for collisions that you never intend to handle.

In cases like this, you can use a http://code.google.com/p/zeddic-android/source/browse/trunk/src/com/zeddic/game/common/collision/CustomCollisionCheck.javaCustomCollisionCheck.

A class that implements this interface will have a single method `shouldSkipCollisionCheck` which you can return a boolean from. Heres an example CustomCollisionCheck:

```
public class CustomCollisionCheck extends CustomCollisionCheck {

  public boolean shouldSkipCollisionCheck(
    PhysicalObject src,
    PhysicalObject dest,
    long time) {

    if (src instanceof Bullet && 
       ((Bullet) src).firedByEnemy &&
       dest instanceof EnemyShip) {
      return true;
    }
    return false;
  }
}
```

You would then need to register your collision check when you initialized the collision system:

```
CollisionManager.setup(MAX_WORLD_WIDTH, MAX_WORLD_HEIGHT);
CollisionManager.get().setCustomCollisionCheck(new CustomCollisionCheck());
```


# Setting the Size of the Collision Grid #
Behind the scenes the Collision System makes use of a [CollisionGrid](http://code.google.com/p/zeddic-android/source/browse/trunk/src/com/zeddic/game/common/collision/CollisionGrid.java) to speed up checks. The world space is split up into a grid. When checking for a collision, the system only needs to check against objects within the grid spots immediately neighboring the object being checked.

![http://zeddic-android.googlecode.com/svn/wiki/grid.png](http://zeddic-android.googlecode.com/svn/wiki/grid.png)

As a rule of thumb, your grid size should be just a bit larger than the size of your game objects, though you can experiment for best performance.

To set the grid size, use the alternative CollisionManager `setup` method:

```
CollisionManager.setup(MAX_WORLD_WIDTH, MAX_WORLD_HEIGHT, GRID_SIZE);
```