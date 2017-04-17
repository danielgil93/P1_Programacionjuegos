package com.videojuegopractica1.game;

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

    Stage stage;
    ArrayList<RoadActor> roadActor;
    ArrayList<GrassActor> grassActor;
    GoalActor goalActor;
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
    float DIRECTION_FACTOR=-1f;
    float DIRECTION_DEADZONE=1;

    public CircuitScreen(final Game game){
        super(game);
        skin=new Skin(Gdx.files.internal("skin/uiskin.json"));
        hudSpriteBatch = new SpriteBatch();
        stage = new Stage();

        ArrayList<Vector2> posiciones = getPosiciones();
        roadActor = new ArrayList<RoadActor>();
        grassActor = new ArrayList<GrassActor>();
        for (int i = 0; i<getPosiciones().size(); i++) {
            roadActor.add(i, new RoadActor(i%3==0));
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
        int contador = 0;
        for (int j = 0; j < 12; j++) {
            for (int k = -1; k < 7; k++) {
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
        goalActor.setPosition(goalActor.getWidth(),goalActor.getHeight()*3);
        stage.addActor(goalActor);
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
        //shapeRenderer.polygon(checkPoint.getTransformedVertices());
        if(Intersector.overlapConvexPolygons(carActor.polygon, checkPoint)) {
            check = true;
            //Gdx.app.log("meta check", String.valueOf(check));
        }
        if(Intersector.overlapConvexPolygons(goalActor.polygon, carActor.polygon)){
           // Gdx.app.log("meta meta", String.valueOf(check));
            if(check){
                check=false;
                lap++;
                lapLabel.setText(String.valueOf(lap));
                raceTime += lapTime;
                Gdx.app.log("meta vuelta", String.valueOf(lap));
                Gdx.app.log("meta tiempo", String.valueOf(lapTime));
                lapTime=0;
                if(lap>=N_LAPS)
                    game.gameOver(raceTime);
            }
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

        dir.scl(speed/10);
        speedLabel.setText(String.valueOf(Math.round(speed)));
        speedLabel.setColor(1,(speed<80)?1:1-((speed-80)/120f),0,1);

        carActor.moveBy(dir.x,dir.y);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(stage);
        ((OrthographicCamera)stage.getCamera()).zoom = 1/(Gdx.graphics.getDensity()/1.5f);
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
        lapLabel.setText(String.valueOf(lap));

        speed = 0;
        aceleration=0;

        outRoad=false;
        acelerando=false;
        frenando=false;

        carActor.setPosition(128*3-carActor.getWidth()/2,128*7-carActor.getHeight()/2);
        stage.getCamera().rotate(new Vector3(0, 0, 1), -carActor.getRotation());
        carActor.setRotation(0);
    }
}
