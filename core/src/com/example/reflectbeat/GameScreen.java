package com.example.reflectbeat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * Created by Jordan on 12/10/2016.
 */

public class GameScreen implements Screen {
    public static AudioController audioController;
    public static GraphicsController graphicsController;

    static public int score;
    static public String scoreStr; // Score in a converted string ?????
    static public int combo; //TODO

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
    }

    public static void resetScore() {
        score = 0;
        scoreStr = Integer.toString(score);
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
