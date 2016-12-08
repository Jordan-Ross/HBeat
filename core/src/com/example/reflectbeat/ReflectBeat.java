package com.example.reflectbeat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class ReflectBeat extends ApplicationAdapter {


    private AudioController audioController;
    private GraphicsController graphicsController;

    static public int score;

    @Override
    public void create() {
        graphicsController = new GraphicsController();
        audioController = new AudioController();
        Gdx.input.setInputProcessor(new Input(graphicsController, audioController));

        audioController.loadSong();

        score = 0;
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
}
