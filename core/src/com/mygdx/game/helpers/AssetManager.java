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
    public static Texture vidas;

    public static void load(){
        sheet = new Texture(Gdx.files.internal("MTiZGmj.png"));
        sheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        music = Gdx.audio.newMusic(Gdx.files.internal("04-banana-jungle.mp3"));
        music.setVolume(0.2f);
        music.setLooping(true);

        FileHandle fontFile = Gdx.files.internal("fonts/space.fnt");
        font = new BitmapFont(fontFile, false);
        font.getData().setScale(1.4f);
        fontVidas = new BitmapFont(fontFile, false);
        fontVidas.getData().setScale(0.1f);

        background = new TextureRegion(sheet, 0, 0, 1000, 1000);
        background.flip(false, false);

        koalaTexture = new Texture("koalio.png");
        vidas = new Texture("life.png");


        checkPoint = Gdx.audio.newSound(Gdx.files.internal("checkpoint.mp3"));
        checkPoint.setVolume(1, 0.4f);
        checkPoint.setLooping(1 ,false);



    }

    public static void dispose(){
        sheet.dispose();
        music.dispose();
    }
}
