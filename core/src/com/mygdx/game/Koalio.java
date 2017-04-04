package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.helpers.AssetManager;
import com.mygdx.game.screens.SplashScreen;

/**
 * Created by Eric on 04/04/2017.
 */

public class Koalio extends Game {

    @Override
    public void create() {
        AssetManager.load();
        setScreen(new SplashScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetManager.dispose();
    }
}
