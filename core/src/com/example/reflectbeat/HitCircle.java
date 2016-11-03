package com.example.reflectbeat;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Jordan on 11/3/2016.
 */

public class HitCircle extends Texture {

    private float x_pos, y_pos;
    public boolean x_direction; // Right is true

    public HitCircle(boolean fail) {
        //super(asset_name);
        this(fail, ReflectBeat.RENDER_WIDTH / 2 - ReflectBeat.HIT_SPRITE_SIZE,
                ReflectBeat.RENDER_HEIGHT, false);
    }

    public HitCircle(boolean fail, HitCircle hit) {
        this(fail, hit.getX_pos(), hit.getY_pos(), hit.getX_direction());
    }

    public HitCircle(boolean fail, float x, float y, boolean xdir) {
        super(fail ? "hitcircle_fail.png" : "hitcircle.png");
        x_pos = x;
        y_pos = y;
        // TODO: Make this not suck
        if (x_pos < 2)
            x_direction = true;
        else if (x_pos > ReflectBeat.RENDER_WIDTH - ReflectBeat.HIT_SPRITE_SIZE - 2)
            x_direction = false;
        else
            x_direction = xdir;
    }

    // Move in x plane by delta
    public void moveX(float delta) {
        if (x_pos + delta > ReflectBeat.RENDER_WIDTH - ReflectBeat.HIT_SPRITE_SIZE
                || x_pos - delta < 0) {
            x_direction = !x_direction;
        }
        if (x_direction) x_pos += delta;
        else x_pos -= delta;
    }

    //Move in y plane down by delta
    public void moveDown(float delta) {
        this.y_pos -= delta;
    }

    public float getX_pos() {
        return x_pos;
    }

    public float getY_pos() {
        return y_pos;
    }

    public boolean getX_direction() {
        return x_direction;
    }
}
