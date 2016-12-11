package com.example.hbeat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Jordan on 12/10/2016.
 */

public class TitleScreen implements Screen {
    private Game game;
    private SpriteBatch batch;
    private Texture instructions;
    private OrthographicCamera camera;
    private Viewport viewport;

    TitleScreen(Game g) {
        this.game = g;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(ReflectBeat.RENDER_WIDTH, ReflectBeat.RENDER_HEIGHT, camera);
        Gdx.graphics.setContinuousRendering(false);

        instructions = new Texture(Gdx.files.internal("TITLESCREEN.png"));


    }

    @Override
    public void render(float delta) {
        camera.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
            batch.draw(instructions, 0, 0);
        batch.end();
        if(Gdx.input.justTouched())
            game.setScreen(new GameScreen());

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
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
