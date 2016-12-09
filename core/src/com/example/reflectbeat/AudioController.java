package com.example.reflectbeat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.sun.corba.se.impl.orbutil.graph.Graph;

/**
 * Created by Jordan on 12/7/2016.
 * This class handles all audio
 */
public class AudioController {

    private Music currentSong;
    private Array<HitObject> currentNotes;
    private long currentSongPos;

    private float speed = 300;

    private long latency;

    // TODO: Stop using constants you fuck
    private long audioLeadInMS = (long)(((GraphicsController.RENDER_HEIGHT - GraphicsController.LINE_HEIGHT - 20) / speed) * 1000);    // Approx time to move down screen at speed 300
    private long currentSongStart;

    private Sound hitSound;

    public void loadSong() {
        //TODO: more songs and selection screen
        // This is a stream, i.e. not loaded in ram.
        currentSong = Gdx.audio.newMusic(Gdx.files.internal("daisy.mp3"));
        //currentSong.setLooping(true);

        // TODO: Add additional info to note array (x,y,speed, etc)
        // TODO: Make Hitcircle and Hitobject one thing ?
        // Parse map for data
        currentNotes = new Array<HitObject>();
        FileHandle handle = Gdx.files.internal("daisy.rbm");
        String strings[] = handle.readString().split("\\r\\n");
        for (String string : strings) {
            currentNotes.add(new HitObject(string));
        }

        //TODO: fix latency
        // Manually tweaked for now
        //latency = -2800;
        latency = -50;  //ms

        initSounds();

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

    public void processHitcircles(GraphicsController graphics_controller) {

        //Manage song timing
        if (currentSong.isPlaying()) {
            currentSongPos = (long)(currentSong.getPosition() * 1000) + audioLeadInMS;
        }
        else {
            currentSongPos = System.currentTimeMillis() - currentSongStart;
        }

        int note_index = 0;
        for (HitObject note : currentNotes) {
            // Note needs to spawn at actual time - time to move down screen
            //if ((currentSongPos +  latency > (note.time_ms-TIME_IT_TAKES_TO_MOVE_DOWN_SCREEN))) {
            if ((currentSongPos > note.time_ms + latency)) {
                // Spawn note, remove actual note from currentNotes
                graphics_controller.spawnHitcircle(note);
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

