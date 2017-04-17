package com.drap.games.racegame;

/**
 * Created by Juan Antonio on 04/04/2017.
 */

public abstract class Screen implements com.badlogic.gdx.Screen {
    protected Game game;

    public Screen(final Game game){
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //batch.draw((Texture) bullAnimation.getKeyFrame(deltaTime, true), 0f, 0f);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
