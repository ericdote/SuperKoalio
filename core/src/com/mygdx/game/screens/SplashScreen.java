package com.mygdx.game.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.mygdx.game.Koalio;
import com.mygdx.game.helpers.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.utils.Settings;

/**
 * Created by Eric on 24/03/2017.
 */

public class SplashScreen implements Screen {

    private Stage stage;
    private Label.LabelStyle textStyle;
    private Koalio game;
    private Label txtLbl;

    public SplashScreen(Koalio game){
        this.game = game;

        Gdx.app.log("HOLA", "w");

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(true);

        stage = new Stage();

        // Afegim el fons
        stage.addActor(new Image(AssetManager.background));

        textStyle = new Label.LabelStyle(AssetManager.font, null);
        txtLbl = new Label("SuperKoalio", textStyle);
        stage.addActor(txtLbl);
        Container container = new Container(txtLbl);
        container.setTransform(true);
        container.center();
        container.setPosition(350, 350);
        stage.addActor(container);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta){
        stage.draw();
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }


}
