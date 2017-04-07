package com.mygdx.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Eric on 24/03/2017.
 */

public class AssetManager {
    public static Texture sheet;
    public static Music music;
    public static BitmapFont font;
    public static BitmapFont fontVidas;
    public static TextureRegion background;
    public static Texture koalaTexture;
    public static Sound checkPoint;
    public static Sound lifeUp;
    public static Sound lifeLess;
    public static Sound win;
    public static Sound gameOver;
    public static Texture vidas;

    public static void load(){
        //Fons
        sheet = new Texture(Gdx.files.internal("MTiZGmj.png"));
        sheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        //Musica
        music = Gdx.audio.newMusic(Gdx.files.internal("04-banana-jungle.mp3"));
        music.setVolume(0.2f);
        music.setLooping(true);
        //Font
        FileHandle fontFile = Gdx.files.internal("fonts/space.fnt");
        font = new BitmapFont(fontFile, false);
        font.getData().setScale(1.4f);
        fontVidas = new BitmapFont(fontFile, false);
        fontVidas.getData().setScale(0.1f);
        //Fons
        background = new TextureRegion(sheet, 0, 0, 1920, 1080);
        background.flip(false, false);
        //Texturas
        koalaTexture = new Texture("koalio.png");
        vidas = new Texture("life.png");

        //Sounds
        checkPoint = Gdx.audio.newSound(Gdx.files.internal("checkpoint.mp3"));
        checkPoint.setVolume(1, 0.4f);
        checkPoint.setLooping(1 ,false);
        lifeLess = Gdx.audio.newSound(Gdx.files.internal("lifeleft.wav"));
        lifeLess.setVolume(2, 0.4f);
        lifeLess.setLooping(2 ,false);
        lifeUp = Gdx.audio.newSound(Gdx.files.internal("lifeup.wav"));
        lifeUp.setVolume(3, 0.4f);
        lifeUp.setLooping(3 ,false);
        win = Gdx.audio.newSound(Gdx.files.internal("win.wav"));
        win.setVolume(1, 0.4f);
        win.setLooping(1 ,false);
        gameOver = Gdx.audio.newSound(Gdx.files.internal("gameover.wav"));
        gameOver.setVolume(1, 0.4f);
        gameOver.setLooping(1 ,false);



    }

    public static void dispose(){
        sheet.dispose();
        music.dispose();
    }
}
