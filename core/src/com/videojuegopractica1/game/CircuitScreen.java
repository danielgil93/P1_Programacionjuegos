package com.videojuegopractica1.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Juan Antonio on 04/04/2017.
 */

public class CircuitScreen extends Screen {

    Stage stage;
    RoadActor[] roadActor;
    GrassActor[] grassActor;
    CarActor carActor;
    float speed = 5;

    float MIN_SPEED=2f;
    float MAX_SPEED=10f;

    public CircuitScreen(final Game game){
        super(game);
        stage = new Stage();
        ArrayList<Vector2> posiciones = getPosiciones();
        roadActor = new RoadActor[32];
        grassActor = new GrassActor[34];
        for (int i = 0; i<32; i++) {
            roadActor[i] = new RoadActor();
            roadActor[i].setPosition(posiciones.get(i).x*roadActor[i].getWidth()*roadActor[i].getScaleX(),
                    posiciones.get(i).y*roadActor[i].getHeight()*roadActor[i].getScaleY());
            stage.addActor(roadActor[i]);
        }
        int contador = 0;
        for (int j = 0; j < 11; j++) {
            for (int k = 0; k < 6; k++) {
                if (!posiciones.contains(new Vector2(j, k))) {
                    grassActor[contador] = new GrassActor();
                    grassActor[contador].setPosition(j*grassActor[contador].getWidth()*grassActor[contador].getScaleX(),
                            k*grassActor[contador].getHeight()*grassActor[contador].getScaleY());
                    stage.addActor(grassActor[contador]);
                    contador++;
                }
            }
        }
        carActor = new CarActor();
        stage.addActor(carActor);
        carActor.bounds=new Rectangle(stage.getCamera().position.x+carActor.getX(),
                stage.getCamera().position.y+carActor.getY(),
                carActor.getWidth()*carActor.getScaleX(),
                carActor.getScaleY()*carActor.getHeight());
        //carActor.setPosition(128*Gdx.graphics.getDensity()*4+64*Gdx.graphics.getDensity(),
          //      128*Gdx.graphics.getDensity()*2+64*Gdx.graphics.getDensity());
        stage.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                /*Gdx.app.log("tocado", String.valueOf(x));
                if(x<0)
                    carActor.rotateBy(15f);
                else
                    carActor.rotateBy(-15f);*/
                return false;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Example", "touch done at (" + x + ", " + y + ")");
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
        stage.getCamera().position.set(carActor.getX(),carActor.getY(),0);
        for(int i=0;i < stage.getActors().size;i++){
            if(stage.getActors().get(i).getClass()==GrassActor.class) {
                GrassActor grassActor=(GrassActor) stage.getActors().get(i);
                if (carActor.getBounds().overlaps(grassActor.getBounds())) {
                    speed=MIN_SPEED;
                    Gdx.app.log("estado","hierba");
                    break;
                }
                else {
                    speed = MAX_SPEED;
                    Gdx.app.log("estado","carretera");
                }
            }
        }
        ShapeRenderer shapeRenderer=new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.rect(carActor.getBounds().x, carActor.getBounds().y,
                carActor.getBounds().width, carActor.getBounds().height);
        shapeRenderer.end();
    }

    private void inputManager(float delta){

        float acceleration=Gdx.input.getAccelerometerY();
        carActor.rotateBy(-0.5f*acceleration);
        stage.getCamera().rotate(new Vector3(0,0,1),-0.5f*acceleration);


float angle = (float) ((carActor.getRotation()*Math.PI/180)+(Math.PI/2)); // Body angle in radians.

        //Gdx.app.log("angle", String.valueOf(angle));

        float velX = MathUtils.cos(angle) * speed; // X-component.
        float velY = MathUtils.sin(angle) * speed; // Y-component.

        //Gdx.app.log("x", String.valueOf(velX));
       // Gdx.app.log("y", String.valueOf(velY));

        carActor.moveBy(velX,velY);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(stage);
    }
}
