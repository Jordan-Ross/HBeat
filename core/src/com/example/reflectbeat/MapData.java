package com.example.reflectbeat;

/**
 * Created by Jordan on 12/7/2016.
 * Data contained in map file. Map files format as:
 * time_ms,x_pos,y_pos,x_vel,y_vel
 */
public class MapData {
    public double time_ms;
    public double x_pos;
    public double y_pos;
    public double x_vel;
    public double y_vel;

    // Full
    MapData(double time_ms, double x_pos, double y_pos, double x_vel, double y_vel) {
        this.time_ms = time_ms;
        this.x_pos = x_pos;
        this.y_pos = y_pos;
        this.x_vel = x_vel;
        this.y_vel = y_vel;
    }

    // Current (unimplemented velocity) version
    MapData(double time_ms, double x_pos, double y_pos) {
        this(time_ms, x_pos, y_pos, 0, 0);
    }

    MapData(String str) {
        String arr[] = str.split(",");
        x_pos = Float.parseFloat(arr[0]);
        y_pos = Float.parseFloat(arr[1]);
        time_ms = Float.parseFloat(arr[2]);
        x_vel = 0;
        y_vel = 0;
    }
}
