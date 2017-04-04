package com.videojuegopractica1.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Game extends com.badlogic.gdx.Game {
	SpriteBatch batch;
	Texture img;
	CircuitScreen circuitScreen;

	@Override
	public void create () {
		batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		circuitScreen = new CircuitScreen(this);
		setScreen(circuitScreen);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
