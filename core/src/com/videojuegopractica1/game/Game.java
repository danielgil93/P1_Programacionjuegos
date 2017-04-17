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
	public CircuitScreen circuitScreen;
	public LoadScreen loadScreen;
	public GameOverScreen gameOverScreen;

	float fasterTime=-1;
	float lastTime=-1;

	private boolean SOUND_ON=true;

	@Override
	public void create () {
		batch = new SpriteBatch();
		circuitScreen = new CircuitScreen(this);
		loadScreen = new LoadScreen(this);
		gameOverScreen = new GameOverScreen(this);
		setScreen(loadScreen);
	}

	public boolean isSOUND_ON() {
		return SOUND_ON;
	}

	public void setSOUND_ON(boolean SOUND_ON) {
		this.SOUND_ON = SOUND_ON;
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	public void gameOver(float time){
		lastTime=time;
		if(fasterTime==-1||fasterTime>lastTime)
			fasterTime=lastTime;
		setScreen(gameOverScreen);
	}

	public float getFasterTime() {
		return fasterTime;
	}

	public float getLastTime() {
		return lastTime;
	}

	public void restartRace(){
		circuitScreen.restartRace();
	}
}
