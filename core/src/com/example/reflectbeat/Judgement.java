package com.example.reflectbeat;

import com.badlogic.gdx.Gdx;

/**
 * Created by Jordan on 12/9/2016.
 * Manages how notes are judged when hit
 * "Judgement" refers to the text shown indicating the score gained from a note hit.
 */
public class Judgement {
    private static final long JUST = 90;
    private static final long GREAT = 180;

    private static final int LIVE_TIME = 50;

    public enum judge {
        JUST, GREAT, GOOD, MISS
    }

    judge j;
    public int index;
    private int liveframes;
    private boolean alive;
    private boolean toRemove;

    Judgement() {
        alive = false;
    }

    Judgement(judge judgement, int index) {
        this.j = judgement;
        this.index = index;
        liveframes = LIVE_TIME;
        alive = true;
    }

    /***
     * Judges the timing of the note hit
     * @param expectedTime Time the note should have been hit
     * @param hitTime Time the note was hit
     * @param xpos X position where the note was hit
     * @return Points to add to score based on judgement
     */
    public static int judgeNote(long expectedTime, long hitTime, float xpos) {
        long diff = Math.abs(expectedTime - hitTime);
        Gdx.app.log("judgeNote", Long.toString(diff));
        int index =(int) (xpos / (ReflectBeat.graphicsController.RENDER_WIDTH/3));
        if (diff < JUST) {
            ReflectBeat.graphicsController.judgements.set(index, new Judgement(judge.JUST, index));
            return 3;
        }
        else if (diff < GREAT) {
            ReflectBeat.graphicsController.judgements.set(index, new Judgement(judge.GREAT, index));
            return 2;
        }
        else {  // GOOD
            ReflectBeat.graphicsController.judgements.set(index, new Judgement(judge.GOOD, index));
            return 1;
        }
    }

    /***
     * Move frame counter down; used for timing the animation of the judgement
     */
    public void reduceLiveFrames() {
        if (liveframes < 0) {
            alive = false;
        }
        liveframes--;
    }

    public boolean isAlive() {
        return alive;
    }
}

