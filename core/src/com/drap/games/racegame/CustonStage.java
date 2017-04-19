package com.drap.games.racegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by DrAP on 10/05/2016.
 */
public class CustonStage extends Stage {
    Game game;
    public CustonStage(Game game) {
        this.game=game;
    }

    @Override public boolean keyUp(final int keycode) {
        if (keycode == Input.Keys.BACK) {
            //if(game.getScreen()==game.gameScreen)
            game.setScreen(game.loadScreen);
            Gdx.input.setCatchBackKey(false);
            //else
              //  Gdx.app.exit();
        }
        return false;
    }
}
