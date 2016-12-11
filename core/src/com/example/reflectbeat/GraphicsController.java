package com.example.reflectbeat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Jordan on 12/7/2016.
 * Handles all Graphics of the application
 */
public class GraphicsController {

    private SpriteBatch batch;
    public Viewport viewport;
    private OrthographicCamera camera;

    private Pool<HitCircle> hitCirclePool;
    public Array<HitCircle> activeHitCircles;
    public static final float LINE_HEIGHT = 150;
    public static final float LINE_WIDTH = 40;
    public static final float HIT_SPRITE_SIZE = 64;
    public static Texture hitcircleTexture;
    public static Texture hitcircleFailTexture;

    private Sprite hitLine;
    public static final int HIT_LINE_TOLERANCE = 40;
    private Texture hitLineTexture;

    public float HIT_LINE_TO_TOP_DISTANCE = ReflectBeat.RENDER_HEIGHT
            - GraphicsController.LINE_HEIGHT
            + GraphicsController.LINE_WIDTH/2
            - GraphicsController.HIT_SPRITE_SIZE/2;

    private Texture explosion;
    private static final int FRAME_COLS = 8;
    private static final int FRAME_ROWS = 1;
    //private static final int FRAME_COLS = 8;
    //private static final int FRAME_ROWS = 4;
    TextureRegion[] explosionFrames;
    private Animation explosionAnimation;
    public Array<Explosion> activeExplosions;
    //private TextureRegion currentFrame;


    private BitmapFont scoreFont;

    private BitmapFont comboFont;

    public Array<Judgement> judgements;
    public float JUDGEMENT_HEIGHT = LINE_HEIGHT + LINE_WIDTH + 30;  // Height the judgement rating appears
    BitmapFont judgementFont;
    Array<Float> judgePositions;// = {RENDER_WIDTH/3f, RENDER_WIDTH/2f, RENDER_WIDTH * (2f/3f)};
    private Array<Texture> judgeTextures;
    private int textureIndex;

    /***
     * Initialize everything
     * TODO: make this less of a clusterfuck
     */
    GraphicsController() {
        batch = new SpriteBatch();
        Gdx.graphics.setContinuousRendering(true);
        //Gdx.graphics.requestRendering();

        camera = new OrthographicCamera();
        viewport = new StretchViewport(ReflectBeat.RENDER_WIDTH, ReflectBeat.RENDER_HEIGHT, camera);

        hitcircleTexture = new Texture("hitcircle.png");
        hitcircleFailTexture = new Texture("hitcircle_fail.png");
        hitLineTexture = new Texture("hitline_red.png");

        // EXPLOSION
        // TODO: Less copyright infringement
        explosion = new Texture("sanicnew2x.png");
        //explosion = new Texture("unused/explosionFull.png");
        TextureRegion[][] temp = TextureRegion.split(explosion, explosion.getWidth() / FRAME_COLS, explosion.getHeight() / FRAME_ROWS);
        explosionFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                explosionFrames[index++] = temp[i][j];
            }
        }
        explosionAnimation = new Animation(0.025f, explosionFrames);
        activeExplosions = new Array<Explosion>();

        hitLine = new Sprite(hitLineTexture);
        hitLine.setPosition(0, LINE_HEIGHT);

        // The pool is better on memory or something
        hitCirclePool = new Pool<HitCircle>() {
            @Override
            protected HitCircle newObject() {
                //return new  HitCircle(false, random.nextBoolean() ? 1 : -1);
                return new  HitCircle();
            }
        };

        activeHitCircles = new Array<HitCircle>();


        // TODO: Font could probably look a bit better
        scoreFont = new BitmapFont(Gdx.files.internal("gothic.fnt"), false);
        scoreFont.getData().setScale(1.5f);
        // TODO: Set font color in a way that actually works (so probably go and change some files)
        //scoreFont.setColor(215, 115, 150, 0.5f);
        //judgementFont = new BitmapFont(Gdx.files.internal("gothic.fnt"), false);
        //judgementFont.getData().setScale(.2f);

        comboFont = new BitmapFont(Gdx.files.internal("gothic.fnt"), false);
        comboFont.getData().setScale(.15f);

        judgements = new Array<Judgement>();
        judgements.add(new Judgement());
        judgements.add(new Judgement());
        judgements.add(new Judgement());
        judgeTextures = new Array<Texture>();
        judgeTextures.add(new Texture(Gdx.files.internal("JUST_downscale.png")));
        judgeTextures.add(new Texture(Gdx.files.internal("GREAT_downscale.png")));
        judgeTextures.add(new Texture(Gdx.files.internal("GOOD_downscale.png")));
        judgeTextures.add(new Texture(Gdx.files.internal("MISS_downscale.png")));
        judgePositions = new Array<Float>();
        judgePositions.add(ReflectBeat.RENDER_WIDTH/5f - (judgeTextures.get(0).getWidth()/2));
        judgePositions.add(ReflectBeat.RENDER_WIDTH/2f - (judgeTextures.get(0).getWidth()/2));
        judgePositions.add(ReflectBeat.RENDER_WIDTH * (4f/5f) - (judgeTextures.get(0).getWidth()/2));
    }

    public void processGraphics() {
        camera.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Process hitcircle movement
        moveHitcircles();

        // Process Graphics
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
            scoreFont.draw(batch,
                    GameScreen.scoreStr,
                    0,
                    camera.viewportHeight / 2,
                    ReflectBeat.RENDER_WIDTH,
                    Align.center,
                    false);

            // Render judgements (how close a hit was)
            // TODO: Give judgements animations
            for (Judgement judge : judgements) {
                if (judge.isAlive()) {
                    batch.draw(judgeTextures.get(
                            judge.j.ordinal()),
                            judgePositions.get(judge.index),
                            JUDGEMENT_HEIGHT);
                    judge.reduceLiveFrames();

                    // Show combo
                    // TODO: Render this better
                    comboFont.draw(batch, GameScreen.comboStr, judgePositions.get(judge.index), JUDGEMENT_HEIGHT - 10);
                }
            }

            // Render Hit Line
            batch.draw(hitLine, 0, LINE_HEIGHT);

            // Render all circles
            for (HitCircle hit : activeHitCircles) {
                if (hit.isAlive())
                    hit.draw(batch);
            }

            // Render active explosion animations
            for (Explosion exp : activeExplosions) {
                float stateTime = exp.getCurrentFrame(Gdx.graphics.getDeltaTime());
                if (explosionAnimation.isAnimationFinished(stateTime)) {
                    activeExplosions.removeValue(exp, true);
                }
                else {
                    batch.draw(explosionAnimation.getKeyFrame(stateTime), exp.xpos, LINE_HEIGHT + 20 - Explosion.EXPLOSION_SIZE/2);
                }
            }
        batch.end();

        // Set hitcircles for removal (allows them to be GC'd)
        removeHitcircles();
    }

    public void spawnHitcircle(HitCircle note) {
        HitCircle hit = hitCirclePool.obtain();
        hit.initActive(note);
        //hit.init(true, false, -1, note.x_vel, note.y_vel, note.x_pos, note.y_pos, note.time_ms);
        activeHitCircles.add(hit);
        //Gdx.app.log("spawnHitcircle", String.format(Locale.US, "x : %f, y : %f, dir: %d", hit.getX(), hit.getY(), hit.x_direction));
    }

    /***
     * Check if a touch happened on the line, incorporating tolerance
     * @param x touch x position
     * @param y touch y position
     * @return true if inside hitbox of line
     */
    public boolean checkInLineHitbox(float x, float y) {
        return y < hitLine.getY() + hitLine.getHeight() + HIT_LINE_TOLERANCE &&
                    y > hitLine.getY() - HIT_LINE_TOLERANCE;
    }

    /***
     * Handle Hitcircle movement
     */
    private void moveHitcircles() {
        for (HitCircle hit : activeHitCircles) {
            //long test = System.nanoTime();
            hit.moveCircle(Gdx.graphics.getDeltaTime());
            //test -= System.nanoTime();
            //Gdx.app.log("moveHitcircles", Long.toString(test));

            // If hitcircle passed below line
            if (hit.getY() < LINE_HEIGHT - HIT_SPRITE_SIZE - HIT_LINE_TOLERANCE && hit.isAlive()) {
                if (hit.getY() < -HIT_SPRITE_SIZE) {
                    // Below Screen (Remove hitcircle)
                    hit.setAlive(false);
                    //spawnHitcircle(0, -speed);
                }
                // JUDGE FAILURES BASED ON TIMING?
                else if (!hit.isFail()){
                    // Just below line (Hit fail)
                    hit.setTexture(hitcircleFailTexture);
                    //ReflectBeat.resetScore();
                    GameScreen.incrementScore(-3);
                    Judgement.spawnJudgement(Judgement.calculateIndex(hit.getX()), Judgement.Judge.MISS);
                    hit.setFail(true);
                }
            }
        }
    }

    /***
     * Removes all dead hitcircles from the active array
     * Tags them as free within the pool
     */
    private void removeHitcircles() {
        for (HitCircle hit : activeHitCircles) {
            if (!hit.isAlive()) {
                activeHitCircles.removeValue(hit, true);
                hitCirclePool.free(hit);
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
        hitCirclePool.freeAll(activeHitCircles);
        GameScreen.resetScore();
    }
}
