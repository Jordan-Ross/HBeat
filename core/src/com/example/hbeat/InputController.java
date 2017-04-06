package com.example.hbeat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

import java.util.Locale;

/**
 * Created by Jordan on 12/7/2016.
 * Handles touch input
 */
public class InputController extends InputAdapter {

    // TODO: Optimize this for multi taps: array of touches that gets processed elsewhere.
    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        //Gdx.app.log("touchDown", String.format(Locale.US, "Touch location: %d, %d", x, y));
        Vector2 transform = new Vector2(x, y);
        transform = GameScreen.graphicsController.viewport.unproject(transform);
        Gdx.app.log("touchDown", String.format(Locale.US,
                "Touch location (Transformed): %f, %f", transform.x, transform.y));

        //TODO: IS THE TIMING BETTER WITH OR WITHOUT HITCIRCLE ASSIGNMENTS??????
        for (HitCircle hit : GameScreen.graphicsController.activeHitCircles) {
            if (!hit.isFail()
                    && GameScreen.graphicsController.checkInLineHitbox(transform.x, transform.y)
                    && hit.checkTouched(transform.x, transform.y)) {
                //Tapped inside hitline and HitCircle hit
                GameScreen.audioController.playHitsound();
                GameScreen.graphicsController.activeExplosions.add(new Explosion(transform.x));
                //Gdx.app.log("touchDown", "Hitcircle touched!");

                // Timing of hit gives the number added to score (see readme)
                GameScreen.incrementScore(GameScreen.audioController.checkTiming(hit.getHit_time(), transform.x));

                // Remove hit
                hit.setAlive(false);
                // TODO: determine performance of this
                GameScreen.graphicsController.activeHitCircles.removeValue(hit, true);
                break;
            }
        }
        return true; // return true to indicate the event was handled
    }
}
