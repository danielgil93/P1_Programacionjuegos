package com.videojuegopractica1.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


/**
 * Created by DrAP on 15/05/2016.
 */
public class TextButtonColor extends TextButton {
    public TextButtonColor(String text, Skin skin) {
        super(text, skin);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color col=getColor();
        batch.setColor(col.r, col.g, col.b, col.a * parentAlpha);
        super.draw(batch, parentAlpha);
        batch.setColor(Color.WHITE);
    }
    /*Image image;
    Texture playgames_text;
    Sprite playgames_sprite;
    Label button;

    public TextButtonColor(String text,Skin skin, String texture) {
        this.image=image;
        float density=Gdx.graphics.getDensity();
        playgames_text=new Texture(texture);
        playgames_sprite=new Sprite(playgames_text);
        image=new Image(playgames_text);
        image.setScale(density);
        this.add(image).expand();
        button=new Label(text,skin);
        this.add(button).expand();
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Label getButton() {
        return button;
    }

    public void setButton(Label button) {
        this.button = button;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color col=getColor();
        batch.setColor(col.r, col.g, col.b, col.a * parentAlpha);
        super.draw(batch, parentAlpha);
        batch.setColor(Color.WHITE);
    }
    public void dispose(){
        playgames_text.dispose();
    }
    @Override
    public boolean remove() {
        dispose();
        return super.remove();
    }*/
}
