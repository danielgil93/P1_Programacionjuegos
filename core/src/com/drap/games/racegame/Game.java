package com.drap.games.racegame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Game extends com.badlogic.gdx.Game {
	SpriteBatch batch;
	public CircuitScreen circuitScreen;
	public LoadScreen loadScreen;
	public GameOverScreen gameOverScreen;

	float fasterTime=-1;
	float lastTime=-1;

	private boolean SOUND_ON=true;

	public static PlayServices playServices;

	public Game(PlayServices playServices)
	{
		this.playServices = playServices;
	}
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

	public void gameOver(int laps, float time){
		lastTime=time;
		if(fasterTime==-1||fasterTime>lastTime) {
			this.playServices.submitScore(laps, (int) (lastTime*1000));
			fasterTime = lastTime;
		}
		setScreen(gameOverScreen);
	}

	public float getFasterTime() {
		return fasterTime;
	}

	public float getLastTime() {
		return lastTime;
	}

	public void restartRace(int n_Laps){
		circuitScreen.restartRace(n_Laps);
	}
}
