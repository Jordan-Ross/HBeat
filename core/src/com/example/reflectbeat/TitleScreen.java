package com.example.reflectbeat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Jordan on 12/10/2016.
 */

public class TitleScreen implements Screen {
    private Game game;
    private SpriteBatch batch;
    private Texture instructions;

    TitleScreen(Game g) {
        this.game = g;
        // TODO: Make this not shit
        instructions = new Texture(Gdx.files.internal("TITLESCREEN.png"));
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        batch.begin();
            batch.draw(instructions, 0, 0);
        batch.end();
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
