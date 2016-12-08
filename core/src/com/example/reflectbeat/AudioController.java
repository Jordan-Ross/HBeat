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
public class AudioController {

    private Music currentSong;
    private Array<MapData> currentNotes;
    private long currentSongPos;


    private float speed = 300;

    private float latency;

    private int TIME_IT_TAKES_TO_MOVE_DOWN_SCREEN = 550;
    private int AudioLeadIn = 2000;    // Delay at beginning of song

    private long current_song_start;
    private float song_playhead;    // Last report of where we are in the currentSong

    private Sound hitSound;

    public void loadSong() {
        //TODO: more songs and selection
        // This is a stream, i.e. not loaded in ram.
        currentSong = Gdx.audio.newMusic(Gdx.files.internal("flower.mp3"));
        currentSong.setLooping(true);

        // TODO: Add additional info to note array (x,y,speed, etc)
        // Parse map for data
        currentNotes = new Array<MapData>();
        FileHandle handle = Gdx.files.internal("flower.rbm");
        String strings[] = handle.readString().split("\\r\\n");
        for (String string : strings) {
            currentNotes.add(new MapData(string));
        }

        currentSongPos = 0;
        song_playhead = 0;

        latency = 0.015f;

        initSounds();

        current_song_start = System.currentTimeMillis();
        Timer tt = new Timer();
        tt.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                currentSong.play();
            }
        }, AudioLeadIn / 1000);
    }

    public void processHitcircles(GraphicsController graphics_controller) {
        currentSongPos = System.currentTimeMillis() - current_song_start;

        int note_index = 0;
        for (MapData note : currentNotes) {
            // Note needs to spawn at actual time - time to move down screen
            if ((currentSongPos +  latency > (note.time_ms-TIME_IT_TAKES_TO_MOVE_DOWN_SCREEN))) {
                // Spawn note, remove actual note from currentNotes
                graphics_controller.spawnHitcircle(50, -speed);
                currentNotes.removeIndex(note_index);
                note_index++;

                //Gdx.app.log("Created Note at", Float.toString(currentSong.getPosition()));
                //Gdx.app.log("Created Note, delta", Float.toString(Gdx.graphics.getDeltaTime()));
            }
            // All notes are in order. If the song isn't past the note (pos < time_ms), exit loop.
            else break;
        }
    }

    private void initSounds() {
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
    }

    public void playHitsound() {
        hitSound.play();
    }
}

