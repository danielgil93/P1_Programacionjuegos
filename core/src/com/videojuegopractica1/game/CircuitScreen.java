package com.videojuegopractica1.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Juan Antonio on 04/04/2017.
 */

public class CircuitScreen extends Screen {

    Stage stage;
    ArrayList<RoadActor> roadActor;
    ArrayList<GrassActor> grassActor;
    GoalActor goalActor;
    CarActor carActor;
    float speed = 0;
    float aceleration=0;

    boolean outRoad=false;
    boolean acelerando=false;
    boolean frenando=false;

    float MIN_SPEED=2f;
    float MAX_SPEED=20f;
    float ACCELERATE=5f;
    float BRAKE=-10f;
    float NEUTRAL=-3;
    float DIRECTION_FACTOR=-1f;
    float DIRECTION_DEAD=2;

    public CircuitScreen(final Game game){
        super(game);
        stage = new Stage();
        ArrayList<Vector2> posiciones = getPosiciones();
        roadActor = new ArrayList<RoadActor>();
        grassActor = new ArrayList<GrassActor>();
        for (int i = 0; i<getPosiciones().size(); i++) {
            roadActor.add(i, new RoadActor());
            RoadActor aux=roadActor.get(i);
            aux.setPosition(posiciones.get(i).x*aux.getWidth()*aux.getScaleX(),
                    posiciones.get(i).y*aux.getHeight()*aux.getScaleY());
            stage.addActor(aux);
        }
        int contador = 0;
        for (int j = 0; j < 11; j++) {
            for (int k = 0; k < 6; k++) {
                if (!posiciones.contains(new Vector2(j, k))) {
                    grassActor.add(contador, new GrassActor());
                    GrassActor aux= grassActor.get(contador);
                    aux.setPosition(j*aux.getWidth()*aux.getScaleX(),
                            k*aux.getHeight()*aux.getScaleY());
                    stage.addActor(aux);
                    contador++;
                }
            }
        }
        goalActor = new GoalActor();
        goalActor.setPosition(128*2,128*6);
        stage.addActor(goalActor);
        carActor = new CarActor();
        carActor.setPosition(128*3-carActor.getWidth()/2,128*7-carActor.getHeight()/2);
        stage.addActor(carActor);

        stage.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Vector3 relativo= stage.getCamera().project(new Vector3(x,y,0));
                if(relativo.x<Gdx.graphics.getWidth()/2) {
                    frenando=true;
                }
                else {
                    acelerando=true;
                }
                if(frenando==acelerando)
                    aceleration=NEUTRAL;
                else if(acelerando)
                    aceleration=ACCELERATE;
                else if(frenando)
                    aceleration=BRAKE;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Vector3 relativo= stage.getCamera().project(new Vector3(x,y,0));
                if(relativo.x<Gdx.graphics.getWidth()/2) {
                    frenando=false;
                }
                else {
                    acelerando=false;
                }
                if(frenando==acelerando)
                    aceleration=NEUTRAL;
                else if(acelerando)
                    aceleration=ACCELERATE;
                else if(frenando)
                    aceleration=BRAKE;
            }
        });
    }

    private ArrayList<Vector2> getPosiciones (){
        Vector2[] pos = {
                new Vector2(2, 0),
                new Vector2(2, 1),
                new Vector2(2, 2),
                new Vector2(1, 2),
                new Vector2(1, 3),
                new Vector2(1, 4),
                new Vector2(2, 4),
                new Vector2(3, 4),
                new Vector2(4, 4),
                new Vector2(4, 5),
                new Vector2(5, 5),
                new Vector2(6, 5),
                new Vector2(7, 5),
                new Vector2(8, 5),
                new Vector2(9, 5),
                new Vector2(10, 5),
                new Vector2(10, 4),
                new Vector2(10, 3),
                new Vector2(10, 2),
                new Vector2(10, 1),
                new Vector2(9, 1),
                new Vector2(8, 1),
                new Vector2(8, 2),
                new Vector2(8, 3),
                new Vector2(7, 3),
                new Vector2(6, 3),
                new Vector2(6, 2),
                new Vector2(6, 1),
                new Vector2(5, 1),
                new Vector2(4, 1),
                new Vector2(4, 0),
                new Vector2(3, 0)
        };
        ArrayList<Vector2> posiciones = new ArrayList<Vector2>();
        for (int i = 0; i<pos.length; i++)
            posiciones.add(pos[i]);
        return posiciones;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.act(delta);
        inputManager(delta);

        ShapeRenderer shapeRenderer=new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1);
        Vector2 camPos=carActor.getCenter();
        stage.getCamera().position.set(camPos.x,
                camPos.y/*+Gdx.graphics.getHeight()*((OrthographicCamera)stage.getCamera()).zoom/2-carActor.getHeight()*/, 0);
        for(int i=0;i < stage.getActors().size;i++){
            if(stage.getActors().get(i).getClass()==GrassActor.class) {
                GrassActor grassActor=(GrassActor) stage.getActors().get(i);
                if(Intersector.overlapConvexPolygons(carActor.polygon,grassActor.polygon)){
                    /*shapeRenderer.polygon(grassActor.polygon.getTransformedVertices());
                    shapeRenderer.polygon(carActor.polygon.getTransformedVertices());*/
                    outRoad=true;
                    break;
                }
                else {
                    outRoad=false;
                }
            }
        }
        //shapeRenderer.polygon(carActor.polygon.getTransformedVertices());
        shapeRenderer.end();
    }

    private void inputManager(float delta){

        float direction=Gdx.input.getAccelerometerY();
        if(Math.abs(direction)>DIRECTION_DEAD) {
            //carActor.rotateBy(DIRECTION_FACTOR * direction);
            //stage.getCamera().rotate(new Vector3(0, 0, 1), DIRECTION_FACTOR * direction);
        }

        float angle = (float) ((carActor.getRotation()*Math.PI/180)+(Math.PI/2)); // Body angle in radians.

        float velX = MathUtils.cos(angle); // X-component.
        float velY = MathUtils.sin(angle); // Y-component.

        Vector2 dir= new Vector2(velX,velY);
        dir=dir.nor();
        /*Gdx.app.log("aceleracion fuera", String.valueOf(outRoad));
        Gdx.app.log("aceleracion speed", String.valueOf(speed));
        Gdx.app.log("aceleracion aceleracion", String.valueOf(aceleration));
        Gdx.app.log("aceleracion frenando", String.valueOf(frenando));
        Gdx.app.log("aceleracion acelerando", String.valueOf(acelerando));*/
        //Gdx.app.log("aceleracion direccion", String.valueOf(direction));
        if(outRoad)
            speed=speed+BRAKE*delta;
        else
            speed=speed+aceleration*delta;

        if(speed<0)
            speed=0;
        else if(speed>MAX_SPEED)
            speed=MAX_SPEED;

        if(speed<MIN_SPEED&&aceleration==ACCELERATE)
            speed=MIN_SPEED;

        dir.scl(speed);

        carActor.moveBy(dir.x,dir.y);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(stage);
        ((OrthographicCamera)stage.getCamera()).zoom /= Gdx.graphics.getDensity()/1.5f;
    }

    @Override
    public void resize(int width, int height) {
        ((OrthographicCamera)stage.getCamera()).setToOrtho(false,width,height);
    }
}
