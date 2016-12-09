package com.example.reflectbeat;

/**
 * Created by Jordan on 12/9/2016.
 * Manages how notes are judged when hit
 */
public class Judgement {
    private static final long JUST = 200;
    private static final long GREAT = 300;
    private static final long GOOD = 400;

    public enum judge {
        JUST, GREAT, GOOD, MISS
    }

    judge j;
    float xPos;

    Judgement(judge judgement, float xPos) {
        this.xPos = xPos;
        this.j = judgement;
    }

    // Check the timing of a touch event
    //  This is literally making an instance of itself inside graphicsController's judgements array
    //  So maybe fix this to make sense eventually
    public static void judgeNote(long expectedTime, long hitTime, float xpos) {
        long diff = Math.abs(expectedTime - hitTime);
        if (diff < JUST) {
            ReflectBeat.graphicsController.judgements.add(new Judgement(judge.JUST, xpos));
        }
        if (diff < GREAT) {
            ReflectBeat.graphicsController.judgements.add(new Judgement(judge.GREAT, xpos));
        }
        if (diff < GOOD) {
            ReflectBeat.graphicsController.judgements.add(new Judgement(judge.GOOD, xpos));
        }
        else {  // MISS
            ReflectBeat.graphicsController.judgements.add(new Judgement(judge.MISS, xpos));
        }
    }
}
