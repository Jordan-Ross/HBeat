package com.example.reflectbeat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ReflectBeat extends ApplicationAdapter {
	private Viewport viewport;
	private Camera camera;
	private SpriteBatch batch;
	private Texture hitcircle;
	private Texture hitline;

	private final int render_width = 480;
	private final int render_height = 800;
	private final int line_height = 100;
	//Test
	private float speed = 100;
	private float hitcircle_y_pos;	// Where the hitcircle is in Y

	@Override
	public void create() {
		batch = new SpriteBatch();
		hitcircle = new Texture("hitcircle.png");
        hitline = new Texture("linething2x.png");
		Gdx.graphics.setContinuousRendering(true);
		Gdx.graphics.requestRendering();

		hitcircle_y_pos = render_height;	// Start at the top
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        hitcircle_y_pos -= speed * Gdx.graphics.getDeltaTime();	// Move down

        if (hitcircle_y_pos < line_height - hitcircle.getHeight()) {
            // Hitcircle passed line
            hitcircle = new Texture("hitcircle_fail.png");
        }

		batch.begin();
		batch.draw(hitline, 0, line_height);
		batch.draw(hitcircle, render_width/2 - 32, hitcircle_y_pos);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		batch.getProjectionMatrix().setToOrtho2D(0, 0, render_width, render_height);
	}

	@Override
	public void dispose() {
		batch.dispose();
		hitcircle.dispose();
        hitline.dispose();
	}
}
