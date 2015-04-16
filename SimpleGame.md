# Overview #

To create your game you'll need to start with two basic files that kick-start everything:

  * An activity class with layout that will 'hookup' everything
  * A game class

# The Activity #
The activity is the main entry point for your application. To use the library, you'll need a simple Activity class with a bare bones layout. The Activity will create an instance of an Updater and supply it with your game that it should maintain.

Create a layout file named main.xml and place it in your res/layout folder. A new project wizard will usually do this for you. Set the content of the .xml file to:

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
  xmlns:android="http://schemas.android.com/apk/res/android"
  android:layout_width="fill_parent"
  android:layout_height="fill_parent">
    
    <LinearLayout android:id="@+id/game"
      android:layout_width="fill_parent"
      android:layout_height="fill_parent"
      android:orientation="vertical"/>
      
</RelativeLayout>
```

In your activity file, create the following:

```
package com.zeddic.demo;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.zeddic.game.common.GameSurface;
import com.zeddic.game.common.Updater;

public class DemoActivity extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Create the drawing Surface;
        GameSurface surface = new GameSurface(this);
        surface.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
        // Create the game. (REPLACE WITH YOUR OWN GAME)
        DemoGame game = new DemoGame();
        
        // Create the updater to coordinate the background update/render thread.
        Updater updater = new Updater(surface);
        updater.setGame(game);
        updater.showFps(true);

        // Add the drawing surface to the screen.
        LinearLayout container = (LinearLayout)findViewById(R.id.game);
        container.addView(surface);
        
        // Start the background thread which will do our setup.
        updater.start();
    }
}
```


# The Game #

Next will be to create the Game class. The game is class is the starting point for your game. It is where you would implement your game tree, starting drawing/updates, and handle user input.

Your game class will extend the Game class and will implement a few methods. Heres a class as an example:

```
package com.zeddic.demo;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.zeddic.game.common.Game;

public class DemoGame extends Game {
  
  @Override
  public void init(int screenWidth, int screenHeight) {
    if (initialized)
      return;
    
    // The first method to be called by your class. This will be triggered
    // as soon as the canvas is available to be drawn on. Draw and update
    // will not be triggered until after init has been called.
    
    // Also provides you with the drawable width and heigh of your canvas.
  }
 
  
  @Override
  public void draw(Canvas c) {
   	// Draw your game tree here.
  }
  
  @Override
  public void update(long time) {
	// Update and move your game objects around the world here.
  }
  
  //// USER INPUT
  
  public boolean onTouchEvent(MotionEvent e) {
    // Also available:
	// onKeyUp
	// onKeyDown
	// onTrackballEvent
    return true;
  }
}
```