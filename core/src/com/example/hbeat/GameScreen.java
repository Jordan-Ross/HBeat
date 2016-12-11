package com.example.hbeat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * Created by Jordan on 12/10/2016.
 * Base class for game screen
 * TODO: why is home button and back button functionality different?
 */

public class GameScreen implements Screen {
    public static AudioController audioController;

    public static GraphicsController graphicsController;

    // TODO: is there an actual performance difference with these strings or am I just being dumb?
    static private int score;
    static public String scoreStr; // Score in a converted string ?????
    static private int combo;
    static public String comboStr;

    GameScreen() {
        graphicsController = new GraphicsController();
        audioController = new AudioController();
        Gdx.input.setInputProcessor(new InputController());

        audioController.loadSong();

        resetScore();
    }

    @Override
    public void render(float delta) {
        audioController.processHitcircles();
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

    public static void incrementScore(int points) {
        score += points;
        scoreStr = Integer.toString(score);

        // TODO: handle combo better (if a note is hit after a previous note not yet a "miss", both notes reset)
        if (points > 0) {
            combo++;
            comboStr = "Combo: " + Integer.toString(combo);
        }
        else {
            combo = 0;
            comboStr = "Combo: " + Integer.toString(combo);
        }
    }

    public static void resetScore() {
        score = 0;
        combo = 0;
        scoreStr = Integer.toString(score);
        comboStr = "Combo: " + Integer.toString(combo);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
