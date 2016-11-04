package com.example.reflectbeat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Locale;

/**
 * Created by Jordan on 11/3/2016.
 */

public class HitCircle extends Sprite {

    private int x_direction; // -1 is left

    public HitCircle(boolean fail, float x, float y, int xdir) {
        //super(fail ? "hitcircle_fail.png" : "hitcircle.png");
        super(fail ? ReflectBeat.hitcircle_fail_texture : ReflectBeat.hitcircle_texture);

        this.setPosition(x, y);
        // TODO: Make this not suck

        if (this.getX() < 2)
            x_direction = 1;
        else if (this.getX() > ReflectBeat.RENDER_WIDTH - ReflectBeat.HIT_SPRITE_SIZE - 2)
            x_direction = -1;
        else
            x_direction = xdir;


        Gdx.app.log("HitCircle constructor", String.format(Locale.US, "x: %f   y: %f    xdir: %d",
                getX(), getY(), x_direction));
    }

    public void move(float xAmount, float yAmount) {
        if (getX() + xAmount > ReflectBeat.RENDER_WIDTH - ReflectBeat.HIT_SPRITE_SIZE
                || getX() - xAmount < 0) {
            x_direction = -1 * x_direction;
        }
        // Warning: don't use this it's dumb
        //setRotation(getRotation() + x_direction * -4);
        super.translate((float) x_direction * xAmount, yAmount);
    }
}
