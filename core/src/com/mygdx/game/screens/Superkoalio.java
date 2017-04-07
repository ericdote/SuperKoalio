package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.Koalio;
import com.mygdx.game.helpers.AssetManager;
import com.mygdx.game.utils.Settings;

import java.util.ArrayList;
import java.util.List;


/** Super Mario Brothers-like very basic platformer, using a tile map built using <a href="http://www.mapeditor.org/">Tiled</a> and a
 * tileset and sprites by <a href="http://www.vickiwenderlich.com/">Vicky Wenderlich</a></p>
 *
 * Shows simple platformer collision detection as well as on-the-fly map modifications through destructible blocks!
 * @author mzechner */
public class Superkoalio implements Screen {
	/** The player character, has state and state time, */

	Batch batch;
	private int vida = 4; //Contador de vidas del koala

	static class Koala {
		static float WIDTH;
		static float HEIGHT;
		static float MAX_VELOCITY = 10f;
		static float JUMP_VELOCITY = 40f;
		static float DAMPING = 0.87f;

		enum State {
			Standing, Walking, Jumping, Win
		}

		final Vector2 position = new Vector2();
		final Vector2 velocity = new Vector2();
		State state = State.Walking;
		float stateTime = 0;
		boolean facesRight = true;
		boolean grounded = false;
	}


	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;

	private Texture koalaTexture;
	private Texture life;
	private Animation<TextureRegion> stand;
	private Animation<TextureRegion> walk;
	private Animation<TextureRegion> jump;
	private Animation<TextureRegion> win; //Animacion para cuando gana el Koala
	private Koala koala;
	float altu = 0;//Contador para que el koala "baile"
	private int contador = 0;
	private boolean checkpoint;//Booleano para cuando pasamos el checkpoint
	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject () {
			return new Rectangle();
		}
	};
	private Array<Rectangle> tiles = new Array<Rectangle>();
	private Koalio game;//Game que le llega al constructor
	private boolean finalWin;//Booleano para deshabilitar las teclas al ganar

	private static final float GRAVITY = -2.5f;

	private boolean debug = false;
	private ShapeRenderer debugRenderer;

	public Superkoalio(Koalio game) {
		this.game = game;
		finalWin = true;
		AssetManager.load();
		AssetManager.music.play();
		// load the koala frames, split them, and assign them to Animations
		koalaTexture = AssetManager.koalaTexture;
		life = AssetManager.vidas;//Texturas de las vidas
		TextureRegion[] regions = TextureRegion.split(koalaTexture, 18, 26)[0];
		stand = new Animation(0, regions[0]);
		jump = new Animation(0, regions[1]);
		walk = new Animation(0.15f, regions[2], regions[3], regions[4]);
		walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		win = new Animation(0.25f, regions[5], regions[6]);//Animacion para cuando ganamos
		win.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

		// figure out the width and height of the koala for collision
		// detection and rendering by converting a koala frames pixel
		// size into world units (1 unit == 16 pixels)
		Koala.WIDTH = 1 / 16f * regions[0].getRegionWidth();
		Koala.HEIGHT = 1 / 16f * regions[0].getRegionHeight();

		// load the map, set the unit scale to 1/16 (1 unit == 16 pixels)
		map = new TmxMapLoader().load("level1.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / 16f);

		// create an orthographic camera, shows us 30x20 units of the world
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 30, 20);
		camera.position.x = 15;
		camera.update();

		// create the Koala we want to move around the world
		koala = new Koala();
		koala.position.set(0, 10);




		debugRenderer = new ShapeRenderer();
	}

	private void updateKoala (float deltaTime) {


		if (deltaTime == 0) return;

		if (deltaTime > 0.1f)
			deltaTime = 0.1f;

		koala.stateTime += deltaTime;

		// check input and apply to velocity & state
		if ((Gdx.input.isKeyPressed(Keys.SPACE) || isTouched(0.5f, 1)) && koala.grounded && finalWin) {
			koala.velocity.y += Koala.JUMP_VELOCITY;
			koala.state = Koala.State.Jumping;
			koala.grounded = false;
		}

		if ((Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A) || isTouched(0, 0.25f)) && finalWin) {
			koala.velocity.x = -Koala.MAX_VELOCITY;
			if (koala.grounded) koala.state = Koala.State.Walking;
			koala.facesRight = false;
		}

		if ((Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D) || isTouched(0.25f, 0.5f))&& finalWin) {
			koala.velocity.x = Koala.MAX_VELOCITY;
			if (koala.grounded) koala.state = Koala.State.Walking;
			koala.facesRight = true;
		}

		if (Gdx.input.isKeyJustPressed(Keys.B) && finalWin)
			debug = !debug;


		// apply gravity if we are falling
		koala.velocity.add(0, GRAVITY);

		// clamp the velocity to the maximum, x-axis only
		koala.velocity.x = MathUtils.clamp(koala.velocity.x,
				-Koala.MAX_VELOCITY, Koala.MAX_VELOCITY);

		// If the velocity is < 1, set it to 0 and set state to Standing
		if (Math.abs(koala.velocity.x) < 1) {
			koala.velocity.x = 0;
			if (koala.grounded) koala.state = Koala.State.Standing;
		}

		// multiply by delta time so we know how far we go
		// in this frame
		koala.velocity.scl(deltaTime);

		// perform collision detection & response, on each axis, separately
		// if the koala is moving right, check the tiles to the right of it's
		// right bounding box edge, otherwise check the ones to the left
		Rectangle koalaRect = rectPool.obtain();
		koalaRect.set(koala.position.x, koala.position.y, Koala.WIDTH, Koala.HEIGHT);
		int startX, startY, endX, endY;
		if (koala.velocity.x > 0) {
			startX = endX = (int)(koala.position.x + Koala.WIDTH + koala.velocity.x);
		} else {
			startX = endX = (int)(koala.position.x + koala.velocity.x);
		}
		startY = (int)(koala.position.y);
		endY = (int)(koala.position.y + Koala.HEIGHT);
		getTiles(startX, startY, endX, endY, tiles);
		koalaRect.x += koala.velocity.x;
		for (Rectangle tile : tiles) {
			if (koalaRect.overlaps(tile)) {
				koala.velocity.x = 0;
				break;
			}
		}
		koalaRect.x = koala.position.x;

		// if the koala is moving upwards, check the tiles to the top of its
		// top bounding box edge, otherwise check the ones to the bottom
		if (koala.velocity.y > 0) {
			startY = endY = (int)(koala.position.y + Koala.HEIGHT + koala.velocity.y);
		} else {
			startY = endY = (int)(koala.position.y + koala.velocity.y);
		}
		startX = (int)(koala.position.x);
		endX = (int)(koala.position.x + Koala.WIDTH);
		getTiles(startX, startY, endX, endY, tiles);
		koalaRect.y += koala.velocity.y;
		for (Rectangle tile : tiles) {
			if (koalaRect.overlaps(tile)) {
				// we actually reset the koala y-position here
				// so it is just below/above the tile we collided with
				// this removes bouncing :)
				if (koala.velocity.y > 0) {
					koala.position.y = tile.y - Koala.HEIGHT;
					// we hit a block jumping upwards, let's destroy it!
					TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("walls");
					//layer.setCell((int)tile.x, (int)tile.y, null);
				} else {
					koala.position.y = tile.y + tile.height;
					// if we hit the ground, mark us as grounded so we can jump
					koala.grounded = true;
				}
				koala.velocity.y = 0;
				break;
			}
		}
		rectPool.free(koalaRect);

		// unscale the velocity by the inverse delta time and set
		// the latest position
		koala.position.add(koala.velocity);
		koala.velocity.scl(1 / deltaTime);

		// Apply damping to the velocity on the x-axis so we don't
		// walk infinitely once a key was pressed
		koala.velocity.x *= Koala.DAMPING;
		//Si la vida del Koala es inferior a 0 le ponemos que reaparezca al principio, le restamos una vida y posicionamos la camara.
		if (koala.position.y <= 0)
		{
			koala.position.set(0, 10);
			camera.position.x = 15;
			vida--;
		//Si el Koala ya ha pasado el checkpoint y cae, le restamos una vida y le posicionamos en el checkpoint asi como la camara.
		} else if (checkpoint && koala.position.y < 14){
			koala.position.y = 17;
			koala.position.x = 168;
			camera.position.x = koala.position.x;
			camera.position.y = koala.position.y;
			vida--;
		}


	}

	private boolean isTouched (float startX, float endX) {
		// Check for touch inputs between startX and endX
		// startX/endX are given between 0 (left edge of the screen) and 1 (right edge of the screen)
		for (int i = 0; i < 2; i++) {
			float x = Gdx.input.getX(i) / (float)Gdx.graphics.getWidth();
			if (Gdx.input.isTouched(i) && (x >= startX && x <= endX)) {
				return true;
			}
		}
		return false;
	}

	private void getTiles (int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
		TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("walls");
		rectPool.freeAll(tiles);
		tiles.clear();
		for (int y = startY; y <= endY; y++) {
			for (int x = startX; x <= endX; x++) {
				Cell cell = layer.getCell(x, y);
				if (cell != null) {
					Rectangle rect = rectPool.obtain();
					rect.set(x, y, 1, 1);
					tiles.add(rect);
				}
			}
		}
	}

	private void renderKoala (float deltaTime) {
		// based on the koala state, get the animation frame
		TextureRegion frame = null;
		switch (koala.state) {
			case Standing:
				frame = stand.getKeyFrame(koala.stateTime);
				break;
			case Walking:
				frame = walk.getKeyFrame(koala.stateTime);
				break;
			case Jumping:
				frame = jump.getKeyFrame(koala.stateTime);
				break;
			case Win:
				frame = win.getKeyFrame(koala.stateTime);
				break;
		}

		// draw the koala, depending on the current velocity
		// on the x-axis, draw the koala facing either right
		// or left
		batch = renderer.getBatch();
		batch.begin();
		if (koala.facesRight) {
			batch.draw(frame, koala.position.x, koala.position.y, Koala.WIDTH, Koala.HEIGHT);

		} else {
			batch.draw(frame, koala.position.x + Koala.WIDTH, koala.position.y, -Koala.WIDTH, Koala.HEIGHT);
		}
		batch.end();
	}

	private void renderDebug () {
		debugRenderer.setProjectionMatrix(camera.combined);
		debugRenderer.begin(ShapeType.Line);

		debugRenderer.setColor(Color.RED);
		debugRenderer.rect(koala.position.x, koala.position.y, Koala.WIDTH, Koala.HEIGHT);

		debugRenderer.setColor(Color.YELLOW);
		TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("walls");
		for (int y = 0; y <= layer.getHeight(); y++) {
			for (int x = 0; x <= layer.getWidth(); x++) {
				Cell cell = layer.getCell(x, y);
				if (cell != null) {
					if (camera.frustum.boundsInFrustum(x + 0.5f, y + 0.5f, 0, 1, 1, 0))
						debugRenderer.rect(x, y, 1, 1);
				}
			}
		}
		debugRenderer.end();
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		// clear the screen

		// get the delta time
		float deltaTime = Gdx.graphics.getDeltaTime();



		// let the camera follow the koala, x-axis only
		batch = renderer.getBatch();
		//Si el koala sobrepasa el pixel 15, la camara le siguie
		if(koala.position.x >= 15) {
			camera.position.x = koala.position.x;
			//Si pasa del 197 la camara se queda estatica. Con esto simulamos inicio y fin de mapa.
			if(koala.position.x >= 197){
				camera.position.x = 197;
			}
		}
		//Si el koala esta entre la posicion 16 y 17 la camara le sigue
		if(koala.position.y <= 17 && koala.position.y >= 16){
			camera.position.y = koala.position.y;
			//Si la camara esta por debajo de 16 la camara se posiciona en la "mitad" del mapa
		} if(koala.position.y < 16){
			camera.position.y = 10;
		}
		//Si el koala llega a esta posiciones se le assigna que ha ganado, se pone el boleano a falso y asi se deshabilita los controles, se suma un contador
		//Para que cada 70 veces que se haga el render se el koala gire.
		if(koala.position.x > 6 && koala.position.x < 6.5 && koala.position.y > 20.5){
			koala.state = Koala.State.Win;
			finalWin = false;
			contador++;
			if(contador == 70){
				koala.facesRight = !koala.facesRight;
				contador = 0;
				altu++;
				//Cuando el koala haya girado 8 veces, vuelve al menu inicial.
				if(altu == 8){
					game.setScreen(new SplashScreen(game));
				}
			}
		//Si llegamos al checkpoint se guarda como pasado y suena un sonido.
		} else if(!checkpoint && koala.position.x < 168 && koala.position.x > 167 && koala.position.y == 17){
			checkpoint = true;
			AssetManager.checkPoint.play();

		}
		Gdx.app.log("", koala.position.x+"X"+koala.position.y);
		camera.update();

		// set the TiledMapRenderer view based on what the
		// camera sees, and render the map
		renderer.setView(camera);
		renderer.render();




		// render debug rectangles
		if (debug) renderDebug();
		//Spritebatch usado para dibujar las vidas
		SpriteBatch btch = new SpriteBatch();
		btch.begin();
		//Si tenemos 4 vidas, dibuja 4, y asi con todas..
		if(vida == 4){
			btch.draw(life, 0, 8);
			btch.draw(life, 20, 8);
			btch.draw(life, 40, 8);
			btch.draw(life, 60, 8);

		} else if(vida == 3){
			btch.draw(life, 0, 8);
			btch.draw(life, 20, 8);
			btch.draw(life, 40, 8);
		} else if (vida == 2){
			btch.draw(life, 0, 8);
			btch.draw(life, 20, 8);
		} else if (vida == 1){
			btch.draw(life, 0, 8);
			//Si no quedan vidas salta al menu de inicio
		} else if (vida <= 0){
			game.setScreen(new SplashScreen(game));

		}
		Gdx.app.log("Vidas", vida+"");
		btch.end();

		// render the koala
		renderKoala(deltaTime);
		// update the koala (process input, collision detection, position update)
		updateKoala(deltaTime);
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
	public void dispose () {
	}
}