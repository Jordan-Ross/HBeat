package com.example.hbeat;

/**
 * Created by Jordan on 12/9/2016.
 * Manages the explosion thing when the user hits a circle
 */
public class Explosion {
    public float xpos;
    public float stateTime;
    public static final float EXPLOSION_SIZE = 116; // Size of sanicnew2x.png individual sprites

    Explosion(float xpos) {
        this.xpos = xpos - EXPLOSION_SIZE /2;
        stateTime = 0f;
    }

    float getCurrentFrame(float deltaTime) {
        stateTime += deltaTime;
        return stateTime;
    }
}
