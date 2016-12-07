package com.example.reflectbeat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import java.util.Random;

public class ReflectBeat extends ApplicationAdapter {


    private Audio audio_controller;
    private Graphics graphics_controller;

    static public int score;

    @Override
    public void create() {
        graphics_controller = new Graphics();
        audio_controller = new Audio();
        Gdx.input.setInputProcessor(new Input(graphics_controller, audio_controller));

        audio_controller.loadSong();

        score = 0;
    }

    @Override
    public void render() {
        audio_controller.processHitcircles(graphics_controller);
        graphics_controller.processGraphics();
    }

    @Override
    public void resize(int width, int height){
        //super.resize(width, height);
        //batch.getProjectionMatrix().setToOrtho2D(0, 0, RENDER_WIDTH, RENDER_HEIGHT);
        graphics_controller.resize(width, height);
    }

    @Override
    public void dispose() {
        graphics_controller.dispose();
    }
}
