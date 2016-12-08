package com.example.reflectbeat;

/**
 * Created by Jordan on 12/7/2016.
 * Data contained in map file. Map files format as:
 * time_ms,x_pos,y_pos,x_vel,y_vel
 */
public class MapData {
    public int time_ms;
    public int x_pos;
    public int y_pos;
    public int x_vel;
    public int y_vel;

    // Full
    MapData(int time_ms, int x_pos, int y_pos, int x_vel, int  y_vel) {
        this.time_ms = time_ms;
        this.x_pos = x_pos;
        this.y_pos = y_pos;
        this.x_vel = x_vel;
        this.y_vel = y_vel;
    }

    // Current (unimplemented velocity) version
    MapData(int time_ms, int x_pos, int y_pos) {
        this(time_ms, x_pos, y_pos, 0, 0);
    }

    // Read in map data from a string
    // Formatted as ',' separated integers
    MapData(String str) {
        String arr[] = str.split(",");
        time_ms = Integer.parseInt(arr[0]);
        x_pos = Integer.parseInt(arr[1]);
        y_pos = Integer.parseInt(arr[2]);
        x_vel = 0;
        y_vel = 0;
    }
}
