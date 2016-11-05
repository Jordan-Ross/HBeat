package com.example.reflectbeat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;
import java.util.Random;

public class ReflectBeat extends ApplicationAdapter {
    public Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Sprite hit_line;
    private Pool<HitCircle> hitCirclePool;
    private Array<HitCircle> active_hitcircles;
    private Random random;
    BitmapFont font;

    public static final int RENDER_WIDTH = 540;
    public static final int RENDER_HEIGHT = 960;
    public static final int LINE_HEIGHT = 100;
    public static final int HIT_SPRITE_SIZE = 64;

    public static Texture hitcircle_texture;
    public static Texture hitcircle_fail_texture;
    private Texture hit_line_texture;

    private float speed = 300;
    private int score;

    Music music;
    Array<Float> note_array;
    int next_note_index;
    float song_time;
    float song_playhead;    // Last report of where we are in the music

    boolean TEST;

    @Override
    public void create() {
        batch = new SpriteBatch();
        Gdx.graphics.setContinuousRendering(true);
        Gdx.graphics.requestRendering();

        camera = new OrthographicCamera();
        viewport = new StretchViewport(RENDER_WIDTH, RENDER_HEIGHT, camera);

        // TODO: Split up all code a lot more

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointer, int button) {
                //Gdx.app.log("touchDown", String.format(Locale.US, "Touch location: %d, %d", x, y));
                Vector2 transform = new Vector2(x, y);
                transform = viewport.unproject(transform);
                Gdx.app.log("touchDown", String.format(Locale.US,
                        "Touch location (Transformed): %f, %f", transform.x, transform.y));

                //TODO: Improve this process
                //This is going through *every* hitcircle on the screen to check for touch,
                //which is probably going to suck later (like when there are 100 circles to check)
                int size = active_hitcircles.size;
                for (int i = 0; i < size; i++ ) {
                    // TODO: Calibrate hitboxes
                    if (hit_line.getBoundingRectangle().contains(transform.x, transform.y)) {
                        //Tapped inside hitline, so check for hitCirclePool
                        if (active_hitcircles.get(i).getBoundingRectangle().contains(transform.x, transform.y)) {
                            // Hitcircle was touched while one line
                            Gdx.app.log("touchDown", "Hitcircle touched!");
                            HitCircle hit = active_hitcircles.get(i);
                            // Remove hit
                            hit.alive = false;
                            //spawnHitcircle(0, -speed);

                            score++;
                        }
                    }
                }

                return true; // return true to indicate the event was handled
            }
        });

        random = new Random();
        font = new BitmapFont();
        font.getData().setScale(20, 20);

        hitcircle_texture = new Texture("hitcircle.png");
        hitcircle_fail_texture = new Texture("hitcircle_fail.png");
        hit_line_texture = new Texture("hitline.png");

        hit_line = new Sprite(hit_line_texture);
        hit_line.setPosition(0, LINE_HEIGHT);

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

        active_hitcircles = new Array<HitCircle>();

        //spawnHitcircle(0, -speed);

        startSong();

        score = 0;

        //TODO: Make this not shit
        // Ignore the first tick becuase it's always off.
        TEST = false;
    }

    private void startSong() {
        // This is a stream, i.e. not loaded in ram.
        music = Gdx.audio.newMusic(Gdx.files.internal("clicktrack.mp3"));
        music.setLooping(true);

        // TODO: Add additional info to note array (x,y,speed, etc)
        note_array = new Array<Float>();
        FileHandle handle = Gdx.files.internal("ClickTrackMapping.map");
        String strings[] = handle.readString().split("\\r\\n");
        for (String string : strings) {
            note_array.add(Float.parseFloat(string));
        }
        next_note_index = 0;

        song_time = 0;
        song_playhead = 0;

        music.play();
    }

    // TODO: Add x/y pos
    private void spawnHitcircle(float xspeed, float yspeed) {
        //hitCirclePool.add(new HitCircle(
        //        false,
        //        (float) random.nextInt(RENDER_WIDTH - 2 * HIT_SPRITE_SIZE) + HIT_SPRITE_SIZE,
        //        RENDER_HEIGHT,
        //        random.nextBoolean() ? 1 : -1,
        //        xspeed,
        //        yspeed
        //));
        HitCircle hit = hitCirclePool.obtain();
        hit.init(false, xspeed, yspeed);
        active_hitcircles.add(hit);
    }

    private void removeHitcircles() {
        for (int i = 0; i < active_hitcircles.size; i++) {
            HitCircle hit = active_hitcircles.get(i);
            if (!hit.alive) {
                active_hitcircles.removeIndex(i);
                hitCirclePool.free(hit);
            }
        }
    }


    //Handle Hitcircle movement
    private void moveHitcircles() {
        int size = active_hitcircles.size;
        for (int i = 0; i < size; i++) {
            HitCircle hit = active_hitcircles.get(i);

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
                    hit.setTexture(hitcircle_fail_texture);
                }
            }
        }
    }

    @Override
    public void render() {
        camera.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //song_time += Gdx.graphics.getDeltaTime();
        //float song_position = music.getPosition();
        //if (song_position != song_playhead) {
        //    song_time = (song_time + song_position) / 2;
        //    song_playhead = song_position;
        //}
        // Above code probably not needed, musig.getPosition() seems fine
        //Gdx.app.log("songposition", Float.toString(music.getPosition()));

        while (next_note_index < note_array.size && TEST) {
            // TODO: Improve this; delay and such
            if (music.getPosition() + Gdx.graphics.getDeltaTime() > note_array.get(next_note_index)) {
                spawnHitcircle(0, -speed);
                next_note_index++;
                Gdx.app.log("Created Note at", Float.toString(music.getPosition()));
                Gdx.app.log("Created Note, delta", Float.toString(Gdx.graphics.getDeltaTime()));
            }
            else break;
        }

        TEST = true;

        moveHitcircles();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
            // TODO: Custom score font? Actually, just fix the score thing in general
            font.draw(batch,
                    Integer.toString(score),
                    camera.viewportWidth/2 - 70,
                    camera.viewportHeight/2);

            // TODO: Make the line thinner, fix the height and get the center of the line, etc
            batch.draw(hit_line, 0, LINE_HEIGHT);

            for (int i = 0; i < active_hitcircles.size; i++ ) {
                HitCircle hit = active_hitcircles.get(i);
                hit.draw(batch);

            }
        batch.end();

        removeHitcircles();
    }



    @Override
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
        hit_line_texture.dispose();
        hitCirclePool.freeAll(active_hitcircles);
        score = 0;
    }
}
