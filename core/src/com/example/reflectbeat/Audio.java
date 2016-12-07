package com.example.reflectbeat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by Jordan on 12/7/2016.
 * This class handles the timing of songs
 */
public class Audio {
    private Music music;
    private Array<MapData> note_array;


    private float speed = 300;

    private float latency;

    private int TIME_IT_TAKES_TO_MOVE_DOWN_SCREEN = 600;
    private int AudioLeadIn = 2000;    // Delay at beginning of song

    private long current_song_start;
    private long song_time;
    private float song_playhead;    // Last report of where we are in the music

    private Sound hit_sound;

    public void loadSong() {
        // This is a stream, i.e. not loaded in ram.
        music = Gdx.audio.newMusic(Gdx.files.internal("flower.mp3"));
        music.setLooping(true);

        // TODO: Add additional info to note array (x,y,speed, etc)
        // Parse map for data
        note_array = new Array<MapData>();
        FileHandle handle = Gdx.files.internal("flower.map");
        String strings[] = handle.readString().split("\\r\\n");
        for (String string : strings) {
            note_array.add(new MapData(string));
        }

        song_time = 0;
        song_playhead = 0;

        latency = 0.015f;

        initSounds();

        current_song_start = System.currentTimeMillis();
        Timer tt = new Timer();
        tt.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                music.play();
            }
        }, AudioLeadIn / 1000);
    }

    public void processHitcircles(Graphics graphics_controller) {
        song_time = System.currentTimeMillis() - current_song_start;

        int note_index = 0;
        for (MapData note : note_array) {
            // Note needs to spawn at actual time - time to move down screen
            if ((song_time +  latency > (note.time_ms-TIME_IT_TAKES_TO_MOVE_DOWN_SCREEN))) {
                // Spawn note, remove actual note from note_array
                graphics_controller.spawnHitcircle(50, -speed);
                note_array.removeIndex(note_index);
                note_index++;

                //Gdx.app.log("Created Note at", Float.toString(music.getPosition()));
                //Gdx.app.log("Created Note, delta", Float.toString(Gdx.graphics.getDeltaTime()));
            }
            // All notes are in order. If the song isn't past the note (pos < time_ms), exit loop.
            else break;
        }
    }

    private void initSounds() {
        hit_sound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
    }

    public void playHitsound() {
        hit_sound.play();
    }
}

