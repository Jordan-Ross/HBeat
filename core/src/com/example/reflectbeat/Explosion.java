package com.example.reflectbeat;

/**
 * Created by Jordan on 12/9/2016.
 * Manages the explosion thing when the user hits a circle
 */
public class Explosion {
    public float xpos;
    public float stateTime;
    public static float EXPLOSION_HEIGHT = 116;

    Explosion(float xpos) {
        this.xpos = xpos - EXPLOSION_HEIGHT /2;
        stateTime = 0f;
    }

    float getCurrentFrame(float deltaTime) {
        stateTime += deltaTime;
        return stateTime;
    }
}
