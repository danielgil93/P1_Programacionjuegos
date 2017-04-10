package com.videojuegopractica1.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Juan Antonio on 04/04/2017.
 */

public class CarActor extends Actor implements Disposable{
    private Texture car;
    public Rectangle bounds;
    public Polygon polygon;

    public CarActor(){
        car = new Texture("car.png");
        setX(0);
        setY(0);
        setSize(71, 116);
        setOrigin(getWidth()/2, getHeight()/2);
        bounds = new Rectangle(getX(),getY(),getWidth(),getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        TextureRegion textureRegion = new TextureRegion(car);
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

        polygon = new Polygon(new float[]{0,0,bounds.getWidth(),0,bounds.getWidth(),
                bounds.getHeight(),0,bounds.getHeight()});
        polygon.setScale(getScaleX(),getScaleY());
        polygon.setPosition(getX(),getY());
        polygon.setOrigin(getWidth()/2, getHeight()/2);
        polygon.setRotation(getRotation());
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

    public Vector2 getCenter(){
        return new Vector2(getX()+getOriginX(),getY()+getOriginY());
    }
}
