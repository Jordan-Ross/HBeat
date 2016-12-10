package com.example.reflectbeat;

/**
 * Created by Jordan on 12/7/2016.
 * Data contained in map file. Map files format as:
 * time_ms,x_pos,y_pos,x_vel,y_vel
 */
public class HitObject {
    public long time_ms;
    public float x_pos;
    public float y_pos;
    public float x_vel;
    public float y_vel;

    // Full
    HitObject(int time_ms, float x_pos, float y_pos, float x_vel, float  y_vel) {
        this.time_ms = time_ms;
        this.x_pos = x_pos;
        this.y_pos = y_pos;
        this.x_vel = x_vel;
        this.y_vel = y_vel;
    }

    // Read in map data from a string
    // Formatted as ',' separated integers

    /***
     * Initialize HitObject with a string of map data
     * @param str HitObject data formatted as "time_ms,x_pos", all other info ignored for now (TODO)
     */
    HitObject(String str) {
        String arr[] = str.split(",");
        time_ms = (long)Float.parseFloat(arr[0]);
        x_pos = (Integer.parseInt(arr[1]) %
                (GraphicsController.RENDER_WIDTH - 2 * GraphicsController.HIT_SPRITE_SIZE))
                + GraphicsController.HIT_SPRITE_SIZE;
        //y_pos = Integer.parseInt(arr[2]);
        if (x_pos > GraphicsController.RENDER_WIDTH + GraphicsController.HIT_SPRITE_SIZE) {
            x_vel = 200;
        }
        y_pos = GraphicsController.RENDER_HEIGHT;
        x_vel = 200;    // TODO: no constants pls
        y_vel = -300;
    }
}
