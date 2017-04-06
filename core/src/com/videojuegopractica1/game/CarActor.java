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

public class CarActor extends Actor implements Disposable{
    private Texture car;
    public Rectangle bounds;

    public CarActor(){
        car = new Texture("car.png");
        setX(0);
        setY(0);
        setSize(71, 116);
        setScale(Gdx.graphics.getDensity());
        setOrigin(getWidth()/2, getHeight()/2);
        //bounds = new Rectangle(getX(),getY(),getWidth(),getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        TextureRegion textureRegion = new TextureRegion(car);
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void dispose() {
        car.dispose();
    }

    public Rectangle getBounds(){return bounds;};
}
