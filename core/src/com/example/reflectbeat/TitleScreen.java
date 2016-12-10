package com.example.reflectbeat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * Created by Jordan on 12/10/2016.
 */

public class TitleScreen implements Screen {
    Game game;

    TitleScreen(Game g) {
        this.game = g;
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched())
            game.setScreen(new GameScreen());

    }

    @Override
    public void resize(int width, int height) {

    }
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public void dispose() {

    }
}
