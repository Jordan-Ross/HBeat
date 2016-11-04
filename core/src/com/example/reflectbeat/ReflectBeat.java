package com.example.reflectbeat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;
import java.util.Random;

public class ReflectBeat extends ApplicationAdapter {
    public Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture hit_line;
    private Array<HitCircle> hitcircles;
    private Random random;

    public static final int RENDER_WIDTH = 540;
    public static final int RENDER_HEIGHT = 960;
    public static final int LINE_HEIGHT = 100;
    public static final int HIT_SPRITE_SIZE = 64;

    public static Texture hitcircle_texture;
    public static Texture hitcircle_fail_texture;
    //Test
    private float speed = 300;

    @Override
    public void create() {
        batch = new SpriteBatch();
        Gdx.graphics.setContinuousRendering(true);
        Gdx.graphics.requestRendering();

        camera = new OrthographicCamera();
        viewport = new StretchViewport(RENDER_WIDTH, RENDER_HEIGHT, camera);

        //Gdx.input.setInputProcessor(new CustomInputAdapter(viewport));

        // TODO: Start using Scene2d because actors handle touch events better and this is going to be hard
        // https://github.com/libgdx/libgdx/wiki/Scene2d


        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointer, int button) {
                //Gdx.app.log("touchDown", String.format(Locale.US, "Touch location: %d, %d", x, y));
                Vector2 points = new Vector2(x, y);
                points = viewport.unproject(points);
                Gdx.app.log("touchDown", String.format(Locale.US,
                        "Touch location (Transformed): %f, %f", points.x, points.y));

                return true; // return true to indicate the event was handled
            }
        });

        random = new Random();

        hitcircle_texture = new Texture("hitcircle.png");
        hitcircle_fail_texture = new Texture("hitcircle_fail.png");
        hit_line = new Texture("hitline.png");

        hitcircles = new Array<HitCircle>();
        spawnHitcircle();
    }

    @Override
    public void render() {
        camera.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        moveHitcircles();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(hit_line, 0, LINE_HEIGHT);
        for (int i = 0; i < hitcircles.size; i++ ) {
            HitCircle hit = hitcircles.get(i);
            hit.draw(batch);
        }
        batch.end();
    }

    private void spawnHitcircle() {
        hitcircles.add(new HitCircle(
                false,
                (float) random.nextInt(RENDER_WIDTH - 2 * HIT_SPRITE_SIZE) + HIT_SPRITE_SIZE,
                RENDER_HEIGHT,
                random.nextBoolean() ? 1 : -1)
        );
    }

    //Handle Hitcircle movement
    private void moveHitcircles() {
        for (int i = 0; i < hitcircles.size; i++) {
            HitCircle hit = hitcircles.get(i);

            hit.move(speed * Gdx.graphics.getDeltaTime(), -speed * Gdx.graphics.getDeltaTime());

            // If hitcircle passed below line
            if (hit.getY() < LINE_HEIGHT - HIT_SPRITE_SIZE) {
                if (hit.getY() < -HIT_SPRITE_SIZE) {
                    // Below Screen (Remove hitcircle)
                    hitcircles.removeIndex(i);
                    spawnHitcircle();
                }
                else {
                    // Just below line (Hit fail)
                    hit.setTexture(hitcircle_fail_texture);
                }
            }
        }
    }

    public void resize(int width, int height){
        //super.resize(width, height);
        //batch.getProjectionMatrix().setToOrtho2D(0, 0, RENDER_WIDTH, RENDER_HEIGHT);
        viewport.update(width,height);
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
    }
    @Override
    public void dispose() {
        batch.dispose();
        hitcircle_fail_texture.dispose();
        hitcircle_texture.dispose();
        hit_line.dispose();
    }
}
