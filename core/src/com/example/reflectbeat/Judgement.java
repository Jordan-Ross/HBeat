package com.example.reflectbeat;

import com.badlogic.gdx.Gdx;

/**
 * Created by Jordan on 12/9/2016.
 * Manages how notes are judged when hit
 * "Judgement" refers to the text shown indicating the score gained from a note hit.
 */
public class Judgement {
    private static final long JUST = 70;
    private static final long GREAT = 140;

    private static final int LIVE_TIME = 50;

    public enum Judge {
        JUST, GREAT, GOOD, MISS
    }

    Judge j;
    public int index;
    private int liveframes;
    private boolean alive;
    private boolean toRemove;

    Judgement() {
        alive = false;
    }

    private Judgement(Judge judgement, int index) {
        this.j = judgement;
        this.index = index;
        liveframes = LIVE_TIME;
        alive = true;
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



    /***
     * Judges the timing of the note hit
     *
     * Judgements are stored in graphicsController.judgements, index 0,1,2: only 3 judgements will
     *      be on screen at a time.
     *
     * @param expectedTime Time the note should have been hit
     * @param hitTime Time the note was hit
     * @param xpos X position where the note was hit
     * @return Points to add to score based on judgement
     */
    public static int judgeNote(long expectedTime, long hitTime, float xpos) {
        long diff = Math.abs(expectedTime - hitTime);
        Gdx.app.log("judgeNote", Long.toString(expectedTime - hitTime));
        //int index =(int) (xpos / (ReflectBeat.graphicsController.RENDER_WIDTH/3));
        int index = calculateIndex(xpos);
        if (diff < JUST) {
            //ReflectBeat.graphicsController.judgements.set(index, new Judgement(Judge.JUST, index));
            spawnJudgement(index, Judge.JUST);
            return 3;
        }
        else if (diff < GREAT) {
            //ReflectBeat.graphicsController.judgements.set(index, new Judgement(Judge.GREAT, index));
            spawnJudgement(index, Judge.GREAT);

            return 2;
        }
        else {  // GOOD
            //ReflectBeat.graphicsController.judgements.set(index, new Judgement(Judge.GOOD, index));
            spawnJudgement(index, Judge.GOOD);

            return 1;
        }
    }

    /***
     * Calculates the index of the corresponding judgement text position
     * @param x x position to be approximated by the judgement text position
     * @return index of position
     */
    public static int calculateIndex(float x) {
        return (int) (x / (ReflectBeat.graphicsController.RENDER_WIDTH/3));
    }

    public static void spawnJudgement(int index, Judge judge) {
        ReflectBeat.graphicsController.judgements.set(index, new Judgement(judge, index));

    }
}

