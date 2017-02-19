package com.example.hbeat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import java.util.Locale;

/**
 * Created by Jordan on 12/7/2016.
 * This class handles all audio
 */
public class AudioController {

    private Music currentSong;
    private long currentSongPos;

    private long songGraphicsLatency;
    private long timingLatency;

    private Array<HitCircle> currentNotes;
    private static int note_index;
    private HitCircle note;

    // Time it takes for first note to reach timing point (used to determine song lead in time)
    private long firstNoteLeadIn;
    private long currentSongStart;

    private Sound hitSound;

    public void loadSong() {
        //TODO: more songs and selection screen
        // This is a stream, i.e. not loaded in ram.
        currentSong = Gdx.audio.newMusic(Gdx.files.internal("songs/sakura_reflection.mp3"));
        //currentSong.setLooping(true);
        // Parse map for data

        currentNotes = new Array<HitCircle>();
        FileHandle handle = Gdx.files.internal("songs/sakura_reflection.rbm");
        String strings[] = handle.readString().split("\\r\\n");
        for (String string : strings) {
            currentNotes.add(new HitCircle(string));
        }
        firstNoteLeadIn = currentNotes.get(0).getHit_time() - currentNotes.get(0).getSpawn_time();


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
                Gdx.app.log("Timer", "Song started!");
                currentSong.play();
            }
        }, firstNoteLeadIn / 1000f);
    }

    // TODO: something is very fucky with timing, anything spawned before the song is off by ~200ms (song takes 200ms to start?)
    // (this is also why the first condition needs song position > 0)
    public void updateSongTime() {
        //Manage song timing
        if (currentSong.isPlaying() && currentSong.getPosition() > 0) {
            currentSongPos = (long)(currentSong.getPosition() * 1000);
        }
        else {
            currentSongPos = System.currentTimeMillis() - currentSongStart - firstNoteLeadIn - 200L;
            Gdx.app.log("updateSongTime", String.format(Locale.US, "Time: %d, Time-start: %d, CurrentTime: %d", System.currentTimeMillis(), System.currentTimeMillis() - currentSongStart, currentSongPos));

        }
    }

    public void processHitcircles() {

        updateSongTime();

        for (int k = note_index; k < currentNotes.size; k++) {
            note = currentNotes.get(k);
            if ((currentSongPos > note.getSpawn_time() + songGraphicsLatency)) {
                // Spawn note, remove actual note from currentNotes
                // Note, currentSongPos is the time of spawn, currentSongPos + firstNoteLeadIn is the expected time of hit
                GameScreen.graphicsController.spawnHitcircle(note);
                //currentNotes.removeIndex(note_index);
                Gdx.app.log("processHitcircles", String.format(Locale.US, "Created Note %d at %d", note_index, currentSongPos));
                note_index++;
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
        return Judgement.judgeNote(spawnTime, currentSongPos + timingLatency, xpos);
    }

    private void initSounds() {
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
    }

    public void playHitsound() {
        hitSound.play();
    }


}

