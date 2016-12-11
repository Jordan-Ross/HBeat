package com.example.reflectbeat;

import com.badlogic.gdx.Game;

import java.util.Random;

public class ReflectBeat extends Game {
    public static final float RENDER_WIDTH = 540;
    public static final float RENDER_HEIGHT = 960;

    public static Random random;

    @Override
    public void create() {
        random = new Random();
        random.setSeed(System.currentTimeMillis());
        this.setScreen(new TitleScreen(this));
    }
}
