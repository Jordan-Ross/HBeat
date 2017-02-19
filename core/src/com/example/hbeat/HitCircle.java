package com.example.hbeat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool;

import java.util.Locale;

/**
 * Created by Jordan on 11/3/2016.
 * Acts as a single hit object on the screen, stores attributes
 */

public class HitCircle  {

    private float x;
    private float y;
    private float xspeed = 0;
    private float yspeed = 0;
    private int x_direction; // 1 is right, -1 is left

    private boolean alive = false;
    private boolean fail = false;

    private long hit_time = 0;  // Time the note should be hit
    private long spawn_time = 0;    // Time the note is spawned; based on y velocity

    private static final float HITBOX_DIFF = 32;   // HitCircle touch tolerance
    public static float HIT_SPRITE_SIZE = GraphicsController.HIT_SPRITE_SIZE;
    private static float RENDER_WIDTH = ReflectBeat.RENDER_WIDTH;
    private static float RENDER_HEIGHT = ReflectBeat.RENDER_HEIGHT;
    public static float MIN_X = 0;     // Min x pos a circle can spawn
    public static float MAX_X = RENDER_WIDTH - HIT_SPRITE_SIZE;    // Max x pos a circle can spawn

    public HitCircle() {
        init(false, false, 0, 0, 0, 0, 0, 0);
        this.setAlive(false);
    }

    /***
     * Temporary (NON-INITIALIZED DO NOT RENDER) HitCircle with a string of map data, to be used for
     *  initializing real HitCircles (from the pool in graphicsController)
     * @param str HitCircle data formatted as "spawn_time,x_pos,x_vel,y_vel"
     */
    HitCircle(String str) {
        String arr[] = str.split(",");
        //setHit_time((long)Float.parseFloat(arr[0]));
        setX((Float.parseFloat(arr[1]) %
                (HitCircle.MAX_X - HitCircle.MIN_X))
                + HitCircle.MIN_X);
        //x_vel = Integer.parseInt(arr[2]);
        //y_vel = Integer.parseInt(arr[3]);
        if (getX() > HitCircle.MAX_X) {
            setX(HitCircle.MAX_X);
        }
        setY(ReflectBeat.RENDER_HEIGHT);
        //this.xspeed = 200;    // /no constants pls
        //this.yspeed = -400;
        // TODO: Possibly set speeds in map, but make individual components random?
        this.xspeed = Math.abs(ReflectBeat.random.nextInt() % 400);
        this.yspeed = -300 - Math.abs(ReflectBeat.random.nextInt() % 100);    // Between -300 and -400
        //this.yspeed = Float.parseFloat(arr[* ]);


        this.x_direction = ReflectBeat.random.nextBoolean() ? -1 : 1;

        // Also sets spawn time
        setHit_time((long)Float.parseFloat(arr[0]));

        Gdx.app.log("HitCircle", String.format(Locale.US, "xspeed: %f   yspeed: %f  hit_time: %d    spawn_time: %d", this.xspeed, this.yspeed, this.hit_time, this.spawn_time));

        // The note will spawn at a time dependent on time it takes to get from spawn to hitline
        //setSpawn_time(this.getHit_time() - ((long)(GameScreen.graphicsController.HIT_LINE_TO_TOP_DISTANCE / (-1 * yspeed)) * 1000L));

        setAlive(false);
        setFail(false);
    }

    public void moveCircle(float deltaTime) {
        float xAmount = xspeed * deltaTime;
        Gdx.app.log("moveCircle", String.format(Locale.US, "Before: x_dir: %d", x_direction));
        // *boing*
        if ((x_direction == 1 && getX() + xAmount > MAX_X) || (x_direction == -1 && getX() - xAmount < MIN_X)) {
            x_direction *= -1;
        }
        Gdx.app.log("moveCircle", String.format(Locale.US, "After:  X: %f   xAmt: %f    x_dir: %d", getX(), xAmount, x_direction));
        // Rotates the circles while they move (looks kinda interesting)
        //setRotation(getRotation() + x_direction * -4);
        translate((float) x_direction * xAmount, yspeed * deltaTime);
    }

    public void initActive(HitCircle other) {
        init(true, false, other.x_direction, other.xspeed, other.yspeed, other.getX(), other.getY(), other.getHit_time());
    }

    // TODO: this is only being called with fail=false, should this be removed?
    public void init(boolean alive, boolean fail, int xdir, float xspeed, float yspeed, float xpos, float ypos, long hit_time) {
        //setPosition(RENDER_WIDTH/2, RENDER_HEIGHT);

        // Reverses direction if too close to one edge on spawn
        if (this.getX() < MIN_X)
            x_direction = 1;
        else if (this.getX() > MAX_X)
            x_direction = -1;
        else
            x_direction = xdir;

        this.xspeed = xspeed;
        this.yspeed = yspeed;
        setPosition(xpos, ypos);
        this.setAlive(alive);
        this.setFail(fail);

        this.setHit_time(hit_time);
    }

    public boolean checkTouched(float x, float y) {
        return x < this.getX() + HIT_SPRITE_SIZE + HITBOX_DIFF &&
                y < this.getY() + HIT_SPRITE_SIZE + HITBOX_DIFF &&
                x > this.getX() - HITBOX_DIFF &&
                y > this.getY() - HITBOX_DIFF;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void translate(float xdelt, float ydelt) {
        setX(getX() + xdelt);
        setY(getY() + ydelt);
    }

    public float getXspeed() {
        return xspeed;
    }

    public float getYspeed() {
        return yspeed;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isFail() {
        return fail;
    }

    public void setFail(boolean fail) {
        this.fail = fail;
    }

    public long getSpawn_time() {
        return spawn_time;
    }

    public void setSpawn_time(long spawn_time) {
        this.spawn_time = spawn_time;
    }

    public long getHit_time() {
        return hit_time;
    }

    // Also sets spawn time if a speed is provided
    public void setHit_time(long hit_time) {
        this.hit_time = hit_time;
        if (yspeed != 0) {
            this.setSpawn_time(this.getHit_time() - (long)((GameScreen.graphicsController.HIT_LINE_TO_TOP_DISTANCE / (-1f * yspeed)) * 1000f));
        }
    }
}
