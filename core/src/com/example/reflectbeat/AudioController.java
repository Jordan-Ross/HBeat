package com.example.reflectbeat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by Jordan on 12/7/2016.
 * This class handles all audio
 */
public class AudioController {

    private Music currentSong;
    private long currentSongPos;

    private float speed = 300;
    private long songGraphicsLatency;
    private long timingLatency;

    private Array<HitObject> currentNotes;
    private static int note_index;
    private HitObject note;

    // The time it takes for a note to move down screen at the single speed
    // TODO: make this work for mixed note speeds
    // Approx time to move down screen at speed
    private long audioLeadInMS = (long)(((GraphicsController.RENDER_HEIGHT
            - GraphicsController.LINE_HEIGHT
            + GraphicsController.LINE_WIDTH/2
            - GraphicsController.HIT_SPRITE_SIZE/2)
                / speed) * 1000);
    private long currentSongStart;

    private Sound hitSound;

    public void loadSong() {
        //TODO: more songs and selection screen
        // This is a stream, i.e. not loaded in ram.
        currentSong = Gdx.audio.newMusic(Gdx.files.internal("songs/flower.mp3"));
        //currentSong.setLooping(true);

        // TODO: Add additional info to note array (x,y,speed, etc)
        // TODO: Make Hitcircle and Hitobject one thing ?
        // Parse map for data
        currentNotes = new Array<HitObject>();
        FileHandle handle = Gdx.files.internal("songs/flower.rbm");
        String strings[] = handle.readString().split("\\r\\n");
        for (String string : strings) {
            currentNotes.add(new HitObject(string));
        }

        Gdx.app.log("currentNotes: ", Integer.toString(currentNotes.size));

        //TODO: Improve latency calculations
        // Manually tweaked for now
        songGraphicsLatency = -100; // ms
        songGraphicsLatency -= GraphicsController.HIT_SPRITE_SIZE/2;

        timingLatency = 40; // ms

        initSounds();

        note_index = 0;

        currentSongPos = 0;
        currentSongStart = System.currentTimeMillis();
        Timer tt = new Timer();
        tt.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                currentSong.play();
            }
        }, audioLeadInMS / 1000f);
    }

    // TODO: something is very fucky with timing, anything spawned before the song is off by ~100ms
    public void updateSongTime() {
        //Manage song timing
        if (currentSong.isPlaying()) {
            currentSongPos = (long)(currentSong.getPosition() * 1000) + audioLeadInMS;
        }
        else {
            currentSongPos = System.currentTimeMillis() - currentSongStart - 100;
        }
    }

    public void processHitcircles() {

        updateSongTime();

        for (int k = note_index; k < currentNotes.size; k++) {
            note = currentNotes.get(k);
            if ((currentSongPos > note.time_ms + songGraphicsLatency)) {
                // Spawn note, remove actual note from currentNotes
                // Note, currentSongPos is the time of spawn, currentSongPos + audioLeadInMS is the expected time of hit
                GameScreen.graphicsController.spawnHitcircle(note);
                //currentNotes.removeIndex(note_index);
                note_index++;
                //Gdx.app.log("Created Note at", Float.toString(currentSongPos));
                //Gdx.app.log("Created Note, delta", Float.toString(Gdx.graphics.getDeltaTime()));
            }
            // All notes are in chrono order. When the song reaches a note it hasn't passed, exit loop.
            else break;
        }
    }

    // Judge a hit from inputcontroller
    public int checkTiming(long spawnTime, float xpos) {
        updateSongTime();
        // Subtract lead in time to compare
        return Judgement.judgeNote(spawnTime, currentSongPos - audioLeadInMS + timingLatency, xpos);
    }

    private void initSounds() {
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
    }

    public void playHitsound() {
        hitSound.play();
    }


}

