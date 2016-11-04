package com.example.reflectbeat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;

/**
 * Created by Jordan on 11/3/2016.
 */
public class CustomInputAdapter extends InputAdapter {

    Viewport viewport;

    CustomInputAdapter(Viewport viewport) {
        this.viewport = viewport;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        Gdx.app.log("touchDown", String.format(Locale.US, "Touch location: %d, %d", x, y));
        Vector2 points = new Vector2(x, y);
        points = viewport.unproject(points);
        Gdx.app.log("touchDown", String.format(Locale.US,
                "Touch location (Transformed): %f, %f", points.x, points.y));
        return true; // return true to indicate the event was handled
    }
}
