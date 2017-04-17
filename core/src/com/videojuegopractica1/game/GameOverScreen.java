package com.videojuegopractica1.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by DrAP on 10/05/2016.
 */
public class GameOverScreen extends Screen {
    Stage stage;
    //WaterActor water;
    Table table;
    TextButton play_again_button;
    TextButton main_menu_button;
    //Label game_over;
    Label fasterTime;
    Label lastTime;
    Skin skin;

    final float screen_density= Gdx.graphics.getDensity();

    public GameOverScreen(final Game game) {
        super(game);
    }

    @Override
    public void show() {
        skin=new Skin(Gdx.files.internal("skin/uiskin.json"));
        stage=new Stage();
        Gdx.input.setInputProcessor(stage);
        table=new Table(skin);
        //table.setDebug(true);
        float density= Gdx.graphics.getDensity();
        //water=new WaterActor();
        //stage.addActor(water);

        fasterTime = new Label("Faster: "+String.valueOf(game.getFasterTime()), skin);
        fasterTime.setColor(Color.GREEN);
        fasterTime.setFontScale(density * 2);
        table.add(fasterTime);
        table.row();

        //game.setGAME_STARTED(false);
        /*game_over = new Label("GAME OVER!", skin);
        game_over.setColor(Color.RED);
        game_over.setFontScale(density * 3);
        table.add(game_over);
        table.row();*/

        lastTime = new Label("Time: " + String.valueOf(game.getLastTime()), skin);
        lastTime.setColor(Color.BLACK);
        lastTime.setFontScale(density * 2);
        table.add(lastTime).colspan(2);
        table.row();

        play_again_button =new TextButton("TRY AGAIN",skin);
        play_again_button.getLabel().setFontScale(density);
        play_again_button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.restartRace();
                game.setScreen(game.circuitScreen);
                //Gdx.input.setCatchBackKey(true);
                return false;
            }
        });
        table.add(play_again_button).size(150 * density, 30 * density).pad(5*density);
        table.row();

        main_menu_button=new TextButton("MAIN MENU",skin);
        main_menu_button.getLabel().setFontScale(density);
        main_menu_button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.input.setCatchBackKey(false);
                game.setScreen(game.loadScreen);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Example", "touch done at (" + x + ", " + y + ")");
            }
        });
        table.add(main_menu_button).size(150 * density, 30 * density).pad(5*density);

        table.setFillParent(true);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        renderManager(delta);
    }
    private void renderManager(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
       // water.dispose();
        stage.dispose();
    }
}
