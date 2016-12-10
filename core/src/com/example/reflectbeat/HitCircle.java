package com.example.reflectbeat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool;

import java.util.Locale;

/**
 * Created by Jordan on 11/3/2016.
 * Acts as a single hit object on the screen
 */

public class HitCircle extends Sprite implements Pool.Poolable {

    private float xspeed, yspeed;
    private int x_direction; // 1 is right, -1 is left
    public boolean alive;
    public boolean fail;
    public long spawn_time;

    private final float HITBOX_DIFF = 32;   // HitCircle touch tolerance
    public static float HIT_SPRITE_SIZE = GraphicsController.HIT_SPRITE_SIZE;
    private static float RENDER_WIDTH = GraphicsController.RENDER_WIDTH;
    private static float RENDER_HEIGHT = GraphicsController.RENDER_HEIGHT;
    public static float MIN_X = 0;     // Min x pos a circle can spawn
    public static float MAX_X = RENDER_WIDTH - HIT_SPRITE_SIZE;    // Max x pos a circle can spawn

    public HitCircle(boolean fail, int xdir) {
        super(fail ? GraphicsController.hitcircleFailTexture : GraphicsController.hitcircleTexture);
        x_direction = xdir;
    }

    public HitCircle(boolean fail, float x, float y, int xdir, float xspeed, float yspeed) {
        this(fail, xdir);
        this.setPosition(x, y);

        // Reverses direction if too close to one edge on spawn
        if (this.getX() < MIN_X)
            x_direction = 1;
        else if (this.getX() > MAX_X)
            x_direction = -1;
        else
            x_direction = xdir;

        this.xspeed = xspeed;
        this.yspeed = yspeed;

        this.alive = false;
        this.fail = false;

        Gdx.app.log("HitCircle constructor", String.format(Locale.US, "x: %f   y: %f    xdir: %d",
                getX(), getY(), x_direction));
    }

    public HitCircle(boolean fail, float x, float y, int xdir) {
        this(fail, x, y, xdir, 0, -300);
    }

    public void moveCircle(float deltaTime) {
        float xAmount = xspeed * deltaTime;

        // *boing*
        if (getX() + xAmount > MAX_X
                || getX() - xAmount < MIN_X) {
            x_direction *= -1;
        }
        // Warning: don't use this it's dumb (but looks interesting)
        //setRotation(getRotation() + x_direction * -4);
        super.translate((float) x_direction * xAmount, yspeed * deltaTime);
    }

    // TODO: this is only being called with fail=false, should this be removed?
    public void init(boolean fail, float xspeed, float yspeed, float xpos, float ypos, long spawn_time) {
        this.setTexture(fail ? GraphicsController.hitcircleFailTexture : GraphicsController.hitcircleTexture);
        setPosition(RENDER_WIDTH/2, RENDER_HEIGHT);
        this.xspeed = xspeed;
        this.yspeed = yspeed;
        this.setX(xpos);
        this.setY(ypos);
        alive = true;
        this.fail = false;

        this.spawn_time = spawn_time;
    }

    public boolean checkTouched(float x, float y) {
        return x < this.getX() + HIT_SPRITE_SIZE + HITBOX_DIFF &&
                y < this.getY() + HIT_SPRITE_SIZE + HITBOX_DIFF &&
                x > this.getX() - HITBOX_DIFF &&
                y > this.getY() - HITBOX_DIFF;
    }

    /***
     * Used when poolable HitCircle object is freed
     */
    @Override
    public void reset() {
        setPosition(0,0);
        alive = false;
    }
}
