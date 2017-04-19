package com.drap.games.racegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by DrAP on 10/05/2016.
 */
public class LoadScreen extends Screen {
    Stage stage;
    //WaterActor water;
    Texture flag_texture = new Texture("flag.png");
    Table table;
    Label playerName;
    ImageButton mute_button;
    Texture music_texture;
    ImageButton banner_button;
    Texture banner_texture;
    TextButtonColor play_button;
    TextButtonColor start_3_lap_button;
    TextButtonColor start_5_lap_button;
    TextButtonColor rate_button;
    TextButtonColor quit_button;
    TextButtonColor sign_button;
    TextButtonColor leaderboard_button;
    private TextButtonColor leaderboard_3_button;
    private TextButtonColor leaderboard_5_button;
    boolean isSigned;
    Label max_points;
    Skin skin;
    Color button_background;
    Color button_font;

    final float screen_density= Gdx.graphics.getDensity();
    private Texture playgame_text;
    private Sprite sprite;

    public LoadScreen(final Game game) {
        super(game);
    }

    @Override
    public void show() {
        float rgb=0.75f;
        button_background=new Color(rgb,rgb,rgb,1);
        button_font= Color.WHITE;
        skin=new Skin(Gdx.files.internal("skin/uiskin.json"));
        stage=new Stage();
        Gdx.input.setInputProcessor(stage);
        table=new Table(skin);
        float density= Gdx.graphics.getDensity();
        //water=new WaterActor();
        //stage.addActor(water);

        isSigned=game.playServices.isSignedIn();
        if(isSigned) {
            playerName=new Label("",skin);
            playerName.setText(game.playServices.getPlayerName());
            playerName.setColor(Color.GREEN);
            playerName.setFontScale(density * 2);
            playerName.setHeight(30*density);
            playerName.setPosition(5,
                    Gdx.graphics.getHeight() - 35*density);
            stage.addActor(playerName);
        }

        if(!game.isSOUND_ON())
            music_texture=new Texture("musicOff.png");
        else
            music_texture=new Texture("musicOn.png");
        mute_button=new ImageButton(new SpriteDrawable(new Sprite(music_texture)));
        mute_button.getImage().setScale(screen_density);
        mute_button.setPosition(Gdx.graphics.getWidth() - mute_button.getWidth() * screen_density,
                Gdx.graphics.getHeight() - mute_button.getHeight() * screen_density);
        mute_button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setSOUND_ON(!game.isSOUND_ON());
                show();
                return false;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Example", "touch done at (" + x + ", " + y + ")");
            }
        });
        stage.addActor(mute_button);

        /*fasterTime = new Label("Max: "+String.valueOf(game.getMax_points()), skin);
        fasterTime.setColor(Color.GREEN);
        fasterTime.setFontScale(density * 2);
        table.add(fasterTime).pad(5 * density).colspan(2).height(30 * density);
        table.row();*/

        play_button =new TextButtonColor("PLAY",skin);
        play_button.setColor(button_background);
        play_button.getLabel().setColor(button_font);
        play_button.getLabel().setFontScale(density);
        play_button.setTouchable(Touchable.disabled);
        table.add(play_button).fill().pad(1 * density).height(30 * density).colspan(2);//.size(100 * density, 30 * density);
        table.row();

        start_3_lap_button =new TextButtonColor("3 LAP",skin);
        start_3_lap_button.setColor(button_background);
        start_3_lap_button.getLabel().setColor(button_font);
        start_3_lap_button.getLabel().setFontScale(density);
        start_3_lap_button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.restartRace();
                //game.setGame_mode(GameMode.CLASSIC);
                game.circuitScreen.setN_LAPS(3);
                game.setScreen(game.circuitScreen);
                Gdx.input.setCatchBackKey(true);
                return false;
            }
        });
        table.add(start_3_lap_button).fill().pad(1 * density).padBottom(10 * density).height(60*density).uniformX();//.size(100 * density, 30 * density);

        start_5_lap_button =new TextButtonColor("5 LAP",skin);
        start_5_lap_button.setColor(button_background);
        start_5_lap_button.getLabel().setColor(button_font);
        start_5_lap_button.getLabel().setFontScale(density);
        start_5_lap_button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.restartRace();
                //game.setGame_mode(GameMode.CLASSIC);
                game.circuitScreen.setN_LAPS(5);
                game.setScreen(game.circuitScreen);
                Gdx.input.setCatchBackKey(true);
                return false;
            }
        });
        table.add(start_5_lap_button).fill().pad(1*density).padBottom(10 * density).height(60*density).uniformX();//.size(100 * density, 30 * density);
        table.row().row();
        if (isSigned) {
            leaderboard_button =new TextButtonColor("LEADERBOARD",skin);
            leaderboard_button.getLabel().setFontScale(density);
            leaderboard_button.setTouchable(Touchable.disabled);
            leaderboard_button.setColor(Color.GREEN);
            table.add(leaderboard_button).fill().pad(1 * density).height(30 * density).colspan(2);//.size(100 * density, 30 * density);
            table.row();

            leaderboard_3_button = new TextButtonColor("3 LAP", skin);
            leaderboard_3_button.getLabel().setFontScale(density);
            leaderboard_3_button.setColor(Color.GREEN);
            leaderboard_3_button.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    //game.playServices.showScore();
                    return true;
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    game.playServices.showScore(3);
                }
            });
            table.add(leaderboard_3_button).fill().pad(1 * density).padBottom(10 * density).height(60 * density).uniformX();//.size(100 * density, 20 * density);

            leaderboard_5_button = new TextButtonColor("5 LAP", skin);
            leaderboard_5_button.getLabel().setFontScale(density);
            leaderboard_5_button.setColor(Color.GREEN);
            leaderboard_5_button.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    //game.playServices.showScore();
                    return true;
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    game.playServices.showScore(5);
                }
            });
            table.add(leaderboard_5_button).fill().pad(1*density).padBottom(10 * density).height(60*density).uniformX();//.size(100 * density, 20 * density);
            table.row();
        }

        playgame_text=new Texture("playgames.png");
        sprite=new Sprite(playgame_text);
        ImageButton image=new ImageButton(new SpriteDrawable(sprite));
        sign_button=new TextButtonColor("Play Games Sign in",skin);
        sign_button.getLabel().setFontScale(density);
        sign_button.setColor(Color.GREEN);
        sign_button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (isSigned) {
                    game.playServices.signOut();
                } else {
                    game.playServices.signIn();
                }
                show();
                return false;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Example", "touch done at (" + x + ", " + y + ")");
            }
        });
        if(isSigned)
            sign_button.setText("Play Games Sign out");
        else
            sign_button.setText("Play Games Sing in");
        table.add(sign_button).fill().pad(1 * density).padBottom(10 * density).colspan(2).height(30*density);//.size(100 * density, 20 * density);
        table.row();

        rate_button=new TextButtonColor("RATE",skin);
        rate_button.setColor(button_background);
        rate_button.getLabel().setColor(button_font);
        rate_button.getLabel().setFontScale(density);
        rate_button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.playServices.rateGame();
            }
        });
        table.add(rate_button).fill().pad(1 * density).padBottom(10 * density).colspan(2).height(30 * density);//.size(100 * density, 30 * density);
        table.row();

        quit_button=new TextButtonColor("QUIT GAME",skin);
        quit_button.setColor(button_background);
        quit_button.getLabel().setColor(button_font);
        quit_button.getLabel().setFontScale(density);
        quit_button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return false;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Example", "touch done at (" + x + ", " + y + ")");
            }
        });
        table.add(quit_button).fill().pad(1 * density).colspan(2).height(30*density);//.size(100 * density, 30 * density);

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
        stage.getBatch().begin();
        stage.getBatch().draw(flag_texture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
        stage.draw();
        stage.act(delta);
        //if(isSigned!=game.playServices.isSignedIn())
         //   show();
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
        flag_texture.dispose();
        //playgame_text.dispose();
        music_texture.dispose();
        if(banner_texture!=null)
            banner_texture.dispose();
    }
}
