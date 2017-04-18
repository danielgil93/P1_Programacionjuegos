package com.drap.games.racegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.net.URI;

/**
 * Created by DrAP on 14/05/2016.
 */
public interface PlayServices
{
    public void signIn();
    public void signOut();
    public void rateGame();
    public void unlockAchievement(String achievement);
    public void submitScore(int nLap, int highScore);
    public void showAchievement();
    public void showScore(int nLap);
    public boolean isSignedIn();
    public String getPlayerName();
    public void getPlayerBanner();
}