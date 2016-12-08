package com.example.reflectbeat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

/**
 * Created by Jordan on 12/7/2016.
 * Handles all Graphics of the application
 */
public class GraphicsController {

    private Random random;

    public Viewport viewport;
    private OrthographicCamera camera;

    private Pool<HitCircle> hitCirclePool;
    public Array<HitCircle> hitCircles;

    public static final int RENDER_WIDTH = 540;
    public static final int RENDER_HEIGHT = 960;
    public static final int LINE_HEIGHT = 100;
    public static final int HIT_SPRITE_SIZE = 64;

    private SpriteBatch batch;
    private Sprite hitLine;

    public static Texture hitcircleTexture;
    public static Texture hitcircleFailTexture;
    private Texture hitLineTexture;

    BitmapFont font;

    GraphicsController() {
        batch = new SpriteBatch();
        Gdx.graphics.setContinuousRendering(true);
        Gdx.graphics.requestRendering();

        camera = new OrthographicCamera();
        viewport = new StretchViewport(RENDER_WIDTH, RENDER_HEIGHT, camera);

        hitcircleTexture = new Texture("hitcircle.png");
        hitcircleFailTexture = new Texture("hitcircle_fail.png");
        hitLineTexture = new Texture("hitline.png");

        hitLine = new Sprite(hitLineTexture);
        hitLine.setPosition(0, LINE_HEIGHT);

        hitCirclePool = new Pool<HitCircle>() {
            @Override
            protected HitCircle newObject() {
                return new  HitCircle(
                        false,
                        (float) random.nextInt(RENDER_WIDTH - 2 * HIT_SPRITE_SIZE) + HIT_SPRITE_SIZE,
                        RENDER_HEIGHT,
                        random.nextBoolean() ? 1 : -1
                );
            }
        };

        hitCircles = new Array<HitCircle>();

        random = new Random();
        font = new BitmapFont();
        font.getData().setScale(20, 20);
    }

    public void processGraphics() {
        camera.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        moveHitcircles();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
            // TODO: Custom score font
            font.draw(batch,
                    Integer.toString(ReflectBeat.score),
                    camera.viewportWidth/2 - 70,
                    camera.viewportHeight/2);

            // TODO: Make the line thinner, fix the height and get the center of the line, etc
            batch.draw(hitLine, 0, LINE_HEIGHT);

            for (int i = 0; i < hitCircles.size; i++ ) {
                HitCircle hit = hitCircles.get(i);
                hit.draw(batch);

            }
        batch.end();

        removeHitcircles();
    }



    // TODO: Add x/y pos
    public void spawnHitcircle(float xspeed, float yspeed) {
        HitCircle hit = hitCirclePool.obtain();
        hit.init(false, xspeed, yspeed);
        hitCircles.add(hit);
    }


    public boolean checkHitbox(float x, float y) {
        return hitLine.getBoundingRectangle().contains(x, y);
    }

    private void removeHitcircles() {
        for (int i = 0; i < hitCircles.size; i++) {
            HitCircle hit = hitCircles.get(i);
            if (!hit.alive) {
                hitCircles.removeIndex(i);
                hitCirclePool.free(hit);
            }
        }
    }

    //Handle Hitcircle movement
    private void moveHitcircles() {
        int size = hitCircles.size;
        for (int i = 0; i < size; i++) {
            HitCircle hit = hitCircles.get(i);

            hit.moveCircle(Gdx.graphics.getDeltaTime());

            // If hitcircle passed below line
            if (hit.getY() < LINE_HEIGHT - HIT_SPRITE_SIZE) {
                if (hit.getY() < -HIT_SPRITE_SIZE) {
                    // Below Screen (Remove hitcircle)
                    hit.alive = false;
                    //spawnHitcircle(0, -speed);
                }
                else {
                    // Just below line (Hit fail)
                    hit.setTexture(hitcircleFailTexture);
                    ReflectBeat.score = 0;
                }
            }
        }
    }


    public void resize(int width, int height) {
        viewport.update(width,height);
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
    }

    public void dispose() {
        batch.dispose();
        hitcircleFailTexture.dispose();
        hitcircleTexture.dispose();
        hitLineTexture.dispose();
        hitCirclePool.freeAll(hitCircles);
        ReflectBeat.score = 0;
    }
}
