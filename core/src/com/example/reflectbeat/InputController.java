package com.example.reflectbeat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

import java.util.Locale;

/**
 * Created by Jordan on 12/7/2016.
 * Handles touch input
 */
public class InputController extends InputAdapter {
    int tempScore;

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        //Gdx.app.log("touchDown", String.format(Locale.US, "Touch location: %d, %d", x, y));
        Vector2 transform = new Vector2(x, y);
        transform = ReflectBeat.graphicsController.viewport.unproject(transform);
        Gdx.app.log("touchDown", String.format(Locale.US,
                "Touch location (Transformed): %f, %f", transform.x, transform.y));

        //TODO: IS THE TIMING BETTER WITH OR WITHOUT HITCIRCLE ASSIGNMENTS??????
        //This is going through *every* hitcircle on the screen to check for touch,
        //which is probably going to suck later (like when there are 100 circles to check)
        int size = ReflectBeat.graphicsController.activeHitCircles.size;
        for (int i = 0; i < size; i++) {
            if (ReflectBeat.graphicsController.checkInLineHitbox(transform.x, transform.y)) {
                //Tapped inside hitline, so check for hit_circle_pool
                if (ReflectBeat.graphicsController.activeHitCircles.get(i).checkTouched(transform.x, transform.y)) {
                    // Hitcircle was touched while one line
                    ReflectBeat.audioController.playHitsound();
                    ReflectBeat.graphicsController.activeExplosions.add(new Explosion(transform.x));

                    //Gdx.app.log("touchDown", "Hitcircle touched!");
                    HitCircle hit = ReflectBeat.graphicsController.activeHitCircles.get(i);
                    //TODO TIMING, ADJUST SCORE INCREMENT TO BE DEPENDANT ON THE TIMING OF THE NOTE
                    // I.e., Get a return value from checkTiming below, then pass that to incrementScore
                    // The return value will be 1-3 depending on judgement
                    tempScore = ReflectBeat.audioController.checkTiming(hit.spawn_time, transform.x);
                    // Remove hit
                    hit.alive = false;


                    ReflectBeat.incrementScore(tempScore);
                    break;
                }
            }

        }
        return true; // return true to indicate the event was handled
    }
}
