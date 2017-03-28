package com.mygdx.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Created by Eric on 24/03/2017.
 */

public class AssetManager {

    public static Music music;

    public static void load(){
        music = Gdx.audio.newMusic(Gdx.files.internal("04-banana-jungle.mp3"));
        music.setVolume(0.2f);
        music.setLooping(true);
    }
}
