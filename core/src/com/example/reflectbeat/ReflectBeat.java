package com.example.reflectbeat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;
import java.util.Random;

public class ReflectBeat extends ApplicationAdapter {
    //private Viewport viewport;
    //private Camera camera;
    private SpriteBatch batch;
    private Texture hitline;
    private Array<HitCircle> hitcircles;
    private Random random;

    public static final int RENDER_WIDTH = 480;
    public static final int RENDER_HEIGHT = 800;
    public static final int LINE_HEIGHT = 100;
    public static final int HIT_SPRITE_SIZE = 64;
    //Test
    private float speed = 300;

    @Override
    public void create() {
        batch = new SpriteBatch();
        Gdx.graphics.setContinuousRendering(true);
        Gdx.graphics.requestRendering();
        Gdx.input.setInputProcessor(new CustomInputAdapter());

        random = new Random();

        hitline = new Texture("linething2x.png");

        hitcircles = new Array<HitCircle>();
        hitcircles.add(new HitCircle(false, random.nextInt(RENDER_WIDTH - HIT_SPRITE_SIZE),
                RENDER_HEIGHT, random.nextBoolean()));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (int i = 0; i < hitcircles.size; i++) {
            HitCircle hit = hitcircles.get(i);
            hit.moveDown(speed * Gdx.graphics.getDeltaTime());    // Move down at speed
            hit.moveX(speed * Gdx.graphics.getDeltaTime());
            // If hitcircle passed line
            if (hit.getY_pos() < LINE_HEIGHT) {
                hitcircles.removeIndex(i);
                if (hit.getY_pos() < -HIT_SPRITE_SIZE) {
                    // Below Screen
                    hit.dispose();
                    hitcircles.add(new HitCircle(false, random.nextInt(RENDER_WIDTH),
                            RENDER_HEIGHT, random.nextBoolean()));
                }
                else {
                    // Just below line
                    hitcircles.add(new HitCircle(true, hit));
                }
            }

        }


        batch.begin();
        batch.draw(hitline, 0, LINE_HEIGHT);
        for (int i = 0; i < hitcircles.size; i++ ) {
            HitCircle hit = hitcircles.get(i);
            batch.draw(hit, hit.getX_pos(), hit.getY_pos());
        }
        //batch.draw(hitcircles.first(), hitcircles.first().getX_pos(), hitcircles.first().getY_pos());
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        batch.getProjectionMatrix().setToOrtho2D(0, 0, RENDER_WIDTH, RENDER_HEIGHT);
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (HitCircle h : hitcircles) {
            h.dispose();
        }
        hitline.dispose();
    }
}
