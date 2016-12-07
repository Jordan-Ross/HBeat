package com.example.reflectbeat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

import java.util.Locale;

/**
 * Created by Jordan on 12/7/2016.
 */
public class Input extends InputAdapter {

    private Graphics graphics_controller;
    private Audio audio_controller;

    Input(Graphics graphics_controller, Audio audio_controller) {
        this.graphics_controller = graphics_controller;
        this.audio_controller = audio_controller;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        //Gdx.app.log("touchDown", String.format(Locale.US, "Touch location: %d, %d", x, y));
        Vector2 transform = new Vector2(x, y);
        transform = graphics_controller.viewport.unproject(transform);
        Gdx.app.log("touchDown", String.format(Locale.US,
                "Touch location (Transformed): %f, %f", transform.x, transform.y));

        //TODO: Improve this process
        //This is going through *every* hitcircle on the screen to check for touch,
        //which is probably going to suck later (like when there are 100 circles to check)
        int size = graphics_controller.active_hitcircles.size;
        for (int i = 0; i < size; i++) {
            // TODO: Calibrate hitboxes
            if (graphics_controller.checkHitbox(transform.x, transform.y)) {
                //Tapped inside hitline, so check for hit_circle_pool
                if (graphics_controller.active_hitcircles.get(i).checkTouched(transform.x, transform.y)) {
                    // Hitcircle was touched while one line
                    audio_controller.playHitsound();

                    Gdx.app.log("touchDown", "Hitcircle touched!");
                    HitCircle hit = graphics_controller.active_hitcircles.get(i);
                    // Remove hit
                    hit.alive = false;
                    //spawnHitcircle(0, -speed);

                    ReflectBeat.score++;
                }
            }

        }
        return true; // return true to indicate the event was handled
    }
}
