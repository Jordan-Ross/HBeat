package com.example.reflectbeat;

import com.badlogic.gdx.Gdx;

/**
 * Created by Jordan on 12/9/2016.
 * Manages how notes are judged when hit
 */
public class Judgement {
    private static final long JUST = 85;
    private static final long GREAT = 140;
    //private static final long GOOD = 300;

    private static final int LIVE_TIME = 30;

    public enum judge {
        JUST, GREAT, GOOD, MISS
    }

    judge j;
    float xPos;
    private int liveframes;
    private boolean alive;

    Judgement(judge judgement, float xPos) {
        this.xPos = xPos;
        this.j = judgement;
        liveframes = LIVE_TIME;
        alive = true;
    }

    // Check the timing of a touch event
    //  This is literally making an instance of itself inside graphicsController's judgements array
    //  So maybe fix this to make sense eventually
    public static void judgeNote(long expectedTime, long hitTime, float xpos) {
        long diff = Math.abs(expectedTime - hitTime);
        Gdx.app.log("judgeNote", Long.toString(diff));
        if (diff < JUST) {
            ReflectBeat.graphicsController.judgements.add(new Judgement(judge.JUST, xpos));
        }
        else if (diff < GREAT) {
            ReflectBeat.graphicsController.judgements.add(new Judgement(judge.GREAT, xpos));
        }
        //else if (diff < GOOD) {
        //    ReflectBeat.graphicsController.judgements.add(new Judgement(judge.GOOD, xpos));
        //}
        else {  // GOOD
            ReflectBeat.graphicsController.judgements.add(new Judgement(judge.GOOD, xpos));
        }
    }

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

