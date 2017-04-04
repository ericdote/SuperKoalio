package com.mygdx.game.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
    private TextButton jugar;
    private TextButton salir;
    private TextButton.TextButtonStyle textButtonStyle;

    public SplashScreen(Koalio game){
        this.game = game;

        Gdx.app.log("HOLA", "w");

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(true);

        stage = new Stage();

        // Afegim el fons
        stage.addActor(new Image(AssetManager.background));
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = AssetManager.font;

        textStyle = new Label.LabelStyle(AssetManager.font, null);
        txtLbl = new Label("SuperKoalio", textStyle);
        stage.addActor(txtLbl);
        Container container = new Container(txtLbl);
        container.setTransform(true);
        container.center();
        container.setPosition(350, 350);
        container.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.scaleTo(1.5f, 1.5f, 1), Actions.scaleTo(1, 1, 1))));
        jugar = new TextButton("Jugar", textButtonStyle);
        salir = new TextButton("Salir", textButtonStyle);
        Container contJugar = new Container(jugar);
        contJugar.setTransform(true);
        contJugar.center();
        contJugar.setPosition(350, 250);
        Container contSalir = new Container(salir);
        contSalir.setTransform(true);
        contSalir.center();
        contSalir.setPosition(350,150);
        stage.addActor(container);
        stage.addActor(contJugar);
        stage.addActor(contSalir);

        jugar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SplashScreen.this.game.setScreen(new Superkoalio());
            }
        });
        Gdx.input.setInputProcessor(stage);//TODO mirar esto

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta){
        stage.draw();
        stage.act(delta);
        if(Gdx.input.isTouched()){

        }
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
