package com.example.reflectbeat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

import java.util.Locale;

/**
 * Created by Jordan on 11/3/2016.
 */
public class CustomInputAdapter extends InputAdapter {
    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        Gdx.app.log("touchDown", String.format(Locale.US, "Touch location: %d, %d", x, y));
        return true; // return true to indicate the event was handled
    }
}
