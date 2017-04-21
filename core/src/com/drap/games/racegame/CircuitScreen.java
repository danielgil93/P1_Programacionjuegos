package com.drap.games.racegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

/**
 * Created by Juan Antonio on 04/04/2017.
 */

public class CircuitScreen extends Screen {

    CustonStage stage;
    ArrayList<RoadActor> roadActor;
    ArrayList<Integer> checkPoints;
    int lastCheckPoint=0;
    float timeOutRoad=0;
    ArrayList<GrassActor> grassActor;
    ArrayList<GrassActor> grassActorExtern;
    //GoalActor goalActor;
    Polygon checkPoint;
    CarActor carActor;
    Label timeLabel;
    Label lapLabel;
    Label speedLabel;
    SpriteBatch hudSpriteBatch;
    Skin skin;

    int N_LAPS=0;

    int lap=0;
    float lapTime=0;
    float raceTime=0;
    boolean check=false;

    float speed = 0;
    float aceleration=0;

    boolean outRoad=false;
    boolean acelerando=false;
    boolean frenando=false;

    float MIN_SPEED=20f;
    float MAX_SPEED=200f;
    float ACCELERATE=20f;
    float BRAKE=-50f;
    float NEUTRAL=-6;
    float DIRECTION_FACTOR=-.7f;
    float DIRECTION_DEADZONE=1;
    float MAX_TIME_OUTROAD=2f;

    public CircuitScreen(final Game game){
        super(game);
        skin=new Skin(Gdx.files.internal("skin/uiskin.json"));
        hudSpriteBatch = new SpriteBatch();
        stage = new CustonStage(game);

        ArrayList<Vector2> posiciones = getPosiciones();
        roadActor = new ArrayList<RoadActor>();
        checkPoints = new ArrayList<Integer>();
        grassActor = new ArrayList<GrassActor>();
        grassActorExtern = new ArrayList<GrassActor>();
        for (int i = 0; i<getPosiciones().size(); i++) {
            roadActor.add(i, new RoadActor(i, i==0, i%3==0));
            checkPoints.add(i, 0);
            RoadActor aux=roadActor.get(i);
            aux.setPosition(posiciones.get(i).x*aux.getWidth(),
                    posiciones.get(i).y*aux.getHeight());
            //CADA 3 PONEMOS UNA CARRETERA CON LA DIRECCION DE LA SIGUIENTE
            if (posiciones.get(i).x == posiciones.get((i + 1) % posiciones.size()).x) {
                if (posiciones.get(i).y > posiciones.get((i + 1) % posiciones.size()).y)
                    aux.rotateBy(180);
            } else if (posiciones.get(i).y == posiciones.get((i + 1) % posiciones.size()).y) {
                if (posiciones.get(i).x > posiciones.get((i + 1) % posiciones.size()).x)
                    aux.rotateBy(90);
                else
                    aux.rotateBy(-90);
            }
            stage.addActor(aux);
        }
        //Se pone el ultimo check point como pasado para poder salir de la meta
        checkPoints.set(checkPoints.size()-1,1);
        for (int j = -2; j < 14; j++) {
            for (int k = -3; k < 9; k++) {
                if (!posiciones.contains(new Vector2(j, k))) {
                    GrassActor aux = new GrassActor();
                    aux.setPosition(j * aux.getWidth() * aux.getScaleX(),
                            k * aux.getHeight() * aux.getScaleY());
                    if (j < 0 || j > 11 || k < -1 || k > 6)
                        grassActorExtern.add(aux);
                    else
                        grassActor.add(aux);
                    stage.addActor(aux);
                }
            }
        }
        //goalActor = new GoalActor();
        //goalActor.setPosition(goalActor.getWidth(),goalActor.getHeight()*3);
        //stage.addActor(goalActor);
        carActor = new CarActor();
        carActor.setPosition(128*3-carActor.getWidth()/2,128*7-carActor.getHeight()/2);
        stage.addActor(carActor);

        RoadActor aux = roadActor.get((int)roadActor.size()/2);
        //Rectangle rectangle = new Rectangle(aux.getX(),aux.getY(),aux.getWidth(),aux.getHeight());
        checkPoint = new Polygon(new float[]{0,0,aux.getWidth(),0,aux.getWidth(),
                aux.getHeight(),0,aux.getHeight()});
        checkPoint.setPosition(aux.getX(),aux.getY());

        float FONT_SCALE=4*((OrthographicCamera)stage.getCamera()).zoom;
        float LABEL_HEIGHT=100*((OrthographicCamera)stage.getCamera()).zoom;
        float LABEL_Y=Gdx.graphics.getHeight()-LABEL_HEIGHT;
        Color LABEL_COLOR=Color.BLACK;

        speedLabel = new Label(String.valueOf(speed),skin);
        speedLabel.setSize(Gdx.graphics.getWidth(), LABEL_HEIGHT);
        speedLabel.setFontScale(FONT_SCALE);
        speedLabel.setPosition(0,LABEL_Y);
        speedLabel.setAlignment(Align.right);
        speedLabel.setColor(LABEL_COLOR);

        lapLabel = new Label(String.valueOf(lap),skin);
        lapLabel.setSize(Gdx.graphics.getWidth(), LABEL_HEIGHT);
        lapLabel.setFontScale(FONT_SCALE);
        lapLabel.setPosition(0,LABEL_Y);
        lapLabel.setAlignment(Align.left);
        lapLabel.setColor(LABEL_COLOR);

        timeLabel = new Label(String.valueOf(lapTime),skin);
        timeLabel.setSize(Gdx.graphics.getWidth(), LABEL_HEIGHT);
        timeLabel.setFontScale(FONT_SCALE);
        timeLabel.setPosition(0,LABEL_Y);
        timeLabel.setAlignment(Align.center);
        timeLabel.setColor(LABEL_COLOR);

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
                new Vector2(3, 0),
                new Vector2(2, 0),
                new Vector2(2, 1),
                new Vector2(2, 2),
                new Vector2(1, 2)
        };
        ArrayList<Vector2> posiciones = new ArrayList<Vector2>();
        for (int i = 0; i<pos.length; i++)
            posiciones.add(pos[i]);
        return posiciones;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        lapTime+=delta;
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.act(delta);
        timeLabel.setText(String.valueOf(Math.round(lapTime * 100.0) / 100.0));
        //Vector3 relativo= stage.getCamera().unproject(new Vector3(0,Gdx.graphics.getHeight()/2-lapLabel.getHeight(),0));
        //lapLabel.setPosition(relativo.x,relativo.y);
        inputManager(delta);

        ShapeRenderer shapeRenderer=new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1);
        Vector2 camPos=carActor.getCenter();
        stage.getCamera().position.set(camPos.x,
                camPos.y/*+Gdx.graphics.getHeight()*((OrthographicCamera)stage.getCamera()).zoom/2-carActor.getHeight()*/, 0);
        outRoad = false;
        for (int i = 0; i < grassActor.size(); i++) {
            GrassActor grass = grassActor.get(i);
            if (Intersector.overlapConvexPolygons(carActor.polygon, grass.polygon)) {
                outRoad = true;
                break;
            }
        }
        /*//for (int i = lastCheckPoint; i < 2; i++) {
            if (Intersector.overlapConvexPolygons(carActor.polygon, roadActor.get(lastCheckPoint+1).polygon)) {
                    /*shapeRenderer.polygon(grassActor.polygon.getTransformedVertices());
                    shapeRenderer.polygon(carActor.polygon.getTransformedVertices());*/
/*
                    if(lastCheckPoint!=i&&checkPoints.get((i==0)?checkPoints.size()-1:i-1)==0){
                        //ATAJO DETECTADO
                        carActor.setPosition(roadActor.get(lastCheckPoint).getX(),roadActor.get(lastCheckPoint).getY());
                        stage.getCamera().rotate(new Vector3(0, 0, 1), -carActor.getRotation());
                        carActor.setRotation(roadActor.get(lastCheckPoint).getRotation());
                        stage.getCamera().rotate(new Vector3(0, 0, 1), carActor.getRotation());
                        speed=0;
                    } else {
                        checkPoints.set(i,1);
                        checkPoints.set((i<2)?checkPoints.size()-2+i:i-2,0);
                        lastCheckPoint=i;
                    }
            }
        }*/
        if (Intersector.overlapConvexPolygons(carActor.polygon, roadActor.get((lastCheckPoint+1)%roadActor.size()).polygon)) {
            lastCheckPoint = (lastCheckPoint + 1) % roadActor.size();
            if(lastCheckPoint==0){
                lap++;
                lapLabel.setText(String.valueOf(lap+1));
                raceTime += lapTime;
                lapTime=0;
                if(lap>=N_LAPS)
                    game.gameOver(N_LAPS,raceTime);
            }
        } else if (!Intersector.overlapConvexPolygons(carActor.polygon, roadActor.get(lastCheckPoint).polygon)){
            RoadActor lastRoad = roadActor.get(lastCheckPoint);
            float rotation = roadActor.get(lastCheckPoint).getRotation();
            carActor.setPosition((lastRoad.getX() + (lastRoad.getWidth() / 2)) - (carActor.getWidth() / 2),
                    (lastRoad.getY() + (lastRoad.getHeight() / 2)) - (carActor.getHeight() / 2));
            stage.getCamera().rotate(new Vector3(0, 0, 1), -carActor.getRotation());
            carActor.setRotation(rotation);
            stage.getCamera().rotate(new Vector3(0, 0, 1), carActor.getRotation());
            speed=0;
        }
        //shapeRenderer.polygon(carActor.polygon.getTransformedVertices());
        shapeRenderer.end();

        /*ShapeRenderer shapeRendererHud=new ShapeRenderer();
        shapeRendererHud.begin(ShapeRenderer.ShapeType.Line);
        shapeRendererHud.setColor(1, 0, 0, 1);
        shapeRendererHud.rect(lapLabel.getX(),lapLabel.getY(),lapLabel.getWidth(),lapLabel.getHeight());*/
        hudSpriteBatch.begin();
        lapLabel.draw(hudSpriteBatch,1);
        timeLabel.draw(hudSpriteBatch,1);
        speedLabel.draw(hudSpriteBatch,1);
        hudSpriteBatch.end();
        //shapeRendererHud.end();
    }

    private float getWheelRestriction(){
        if(speed<100)
            return 1;
        else
            return (float) (-Math.log10( (((speed - 100) / 25) + 2) - 1) + 1);
    }
    private void inputManager(float delta){

        float direction=Gdx.input.getAccelerometerY();
        float restriction=getWheelRestriction();
        if(Math.abs(direction)> DIRECTION_DEADZONE) {
            carActor.rotateBy(DIRECTION_FACTOR * direction * restriction);
            stage.getCamera().rotate(new Vector3(0, 0, 1), DIRECTION_FACTOR * direction * restriction);
        }

        float angle = (float) ((carActor.getRotation()*Math.PI/180)+(Math.PI/2)); // Body angle in radians.

        float velX = MathUtils.cos(angle); // X-component.
        float velY = MathUtils.sin(angle); // Y-component.

        Vector2 dir= new Vector2(velX,velY);
        dir=dir.nor();

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

        dir.scl(speed/10);
        speedLabel.setText(String.valueOf(Math.round(speed)));
        speedLabel.setColor(1,(speed<80)?1:1-((speed-80)/120f),0,1);

        carActor.moveBy(dir.x,dir.y);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(stage);
        Gdx.app.log("zoom", String.valueOf(((OrthographicCamera)stage.getCamera()).zoom));
        ((OrthographicCamera)stage.getCamera()).zoom = 1/(Gdx.graphics.getDensity()/1.5f);
        //((OrthographicCamera)stage.getCamera()).zoom = 3;
    }

    @Override
    public void resize(int width, int height) {
        ((OrthographicCamera)stage.getCamera()).setToOrtho(false,width,height);
    }

    public int getN_LAPS() {
        return N_LAPS;
    }

    public void setN_LAPS(int n_LAPS) {
        N_LAPS = n_LAPS;
    }

    public void restartRace(){
        lap=0;
        raceTime=0;
        lapTime=0;
        check=false;
        lapLabel.setText(String.valueOf(lap+1));

        speed = 0;
        aceleration=0;

        outRoad=false;
        acelerando=false;
        frenando=false;

        carActor.setPosition(128*3-carActor.getWidth()/2,128*7-carActor.getHeight()/2);
        stage.getCamera().rotate(new Vector3(0, 0, 1), -carActor.getRotation());
        carActor.setRotation(0);

        for(int i =0;i<checkPoints.size();i++)
            checkPoints.set(i, 0);
        //Se pone el ultimo check point como pasado para poder salir de la meta
        checkPoints.set(checkPoints.size()-1,1);
        lastCheckPoint=0;
    }
}
