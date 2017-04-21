package com.drap.games.racegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Juan Antonio on 04/04/2017.
 */

public class RoadActor extends Actor implements Disposable{
    private Texture road;
    int id;
    boolean goal;
    Rectangle bounds;
    public Polygon polygon;

    public RoadActor(int id, boolean goal, boolean withDir){
        this.id=id;
        this.goal=goal;
        if(goal)
            road = new Texture("goal.png");
        else if(withDir)
            road = new Texture("road_with_dir.png");
        else
            road = new Texture("road.png");
        setX(0);
        setY(0);
        setSize(128*2, 128*2);
        setOrigin(getWidth()/2, getHeight()/2);
        if(goal)
            bounds = new Rectangle(getX(),getY()+getHeight()/3,getWidth(),getHeight()/3);
        else
            bounds = new Rectangle(getX(),getY()+getHeight(),getWidth(),getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        TextureRegion textureRegion = new TextureRegion(road);
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void dispose() {
        road.dispose();
    }
    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);

        polygon = new Polygon(new float[]{0,0,bounds.getWidth(),0,bounds.getWidth(),
                bounds.getHeight(),0,bounds.getHeight()});
        polygon.setScale(getScaleX(),getScaleY());
        polygon.setPosition(getX(),(goal)?getY()+getHeight()/3:getY());
    }
    public int getId() {
        return id;
    }

    public boolean isGoal() {
        return goal;
    }
}
