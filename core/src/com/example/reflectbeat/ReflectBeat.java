package com.example.reflectbeat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class ReflectBeat extends ApplicationAdapter {


    public static AudioController audioController;
    public static GraphicsController graphicsController;

    /**
     * TODO: There's a bug with score:
     * if a note is missed but the next one is hit, the hit note
     *   will _not_ be counted toward the new score because the note fail is below the line
     *   and gets activated after the new hit note, resetting that hit.
     */
    static public int score;
    static public String scoreStr; // Score in a converted string ?????

    @Override
    public void create() {
        graphicsController = new GraphicsController();
        audioController = new AudioController();
        Gdx.input.setInputProcessor(new InputController());

        audioController.loadSong();

        resetScore();
    }

    @Override
    public void render() {
        audioController.processHitcircles(graphicsController);
        graphicsController.processGraphics();
    }

    @Override
    public void resize(int width, int height){
        //super.resize(width, height);
        //batch.getProjectionMatrix().setToOrtho2D(0, 0, RENDER_WIDTH, RENDER_HEIGHT);
        graphicsController.resize(width, height);
    }

    @Override
    public void dispose() {
        graphicsController.dispose();
    }

    public static void incrementScore() {
        score++;
        scoreStr = Integer.toString(score);
    }

    public static void resetScore() {
        score = 0;
        scoreStr = Integer.toString(score);
    }
}
