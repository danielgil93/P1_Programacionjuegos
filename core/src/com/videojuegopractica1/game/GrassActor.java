package com.videojuegopractica1.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Juan Antonio on 04/04/2017.
 */

public class GrassActor extends Actor implements Disposable{
    private Texture road;
    Rectangle bounds;

    public GrassActor(){
        road = new Texture("grass.png");
        setX(0);
        setY(0);
        setOrigin(0, 0);
        setSize(128*2, 128*2);
        setScale(Gdx.graphics.getDensity());
        bounds = new Rectangle(0,0,getWidth(),getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        TextureRegion textureRegion = new TextureRegion(road);
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), 0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void dispose() {
        road.dispose();
    }

    public Rectangle getBounds(){return bounds;};
}
