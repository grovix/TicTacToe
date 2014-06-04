package com.grovix.tictactoe.screens;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.grovix.tictactoe.tween.ActorAccessor;
import com.grovix.tictactoe.tween.SpriteAccessor;

public class MainMenu implements Screen {
	
	private SpriteBatch batch;
	private Sprite splash;
	private TweenManager tweenManager;
	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table;
	private TextButton buttonExit, buttonOnePlay,buttonTwoPlay;
	private Label heading;
	
	private float menuDelta=0.4f;
	
	private void setupTable(){
		table.clear();
		table.setBounds(0,0, stage.getWidth(), stage.getHeight());
		//putting stuff together
		table.add(heading);
		table.getCell(heading).spaceBottom(10);
		table.row();
		table.add(buttonOnePlay).size(200*stage.getWidth()/1280, 70*stage.getHeight()/720).spaceBottom(15).row();
		table.row();
		table.add(buttonTwoPlay).size(200*stage.getWidth()/1280, 70*stage.getHeight()/720).spaceBottom(15).row();
		table.row();
		table.add(buttonExit).size(200*stage.getWidth()/1280, 70*stage.getHeight()/720).spaceBottom(15).row();
		table.row();
	}
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tweenManager.update(delta);
		
		batch.begin();
		splash.draw(batch);
		batch.end();
		
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		//stage.setViewport(width, height, true);
		stage.getCamera().viewportHeight = height;
		stage.getCamera().viewportWidth = width;
		setupTable();
		table.invalidateHierarchy();
	}

	@Override
	public void show() {
		//Texture.setEnforcePotImages(false);
		
		batch = new SpriteBatch();
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		
		Texture splashTexture = new Texture(Gdx.files.internal("img/menu.png"));
		splash = new Sprite(splashTexture);
		splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		Tween.set(splash, SpriteAccessor.ALPHA).target(1).start(tweenManager);
		
		//creating menu
    	OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	Viewport viewport = new ScreenViewport(camera);
		stage = new Stage(viewport);
		Gdx.input.setInputProcessor(stage); 
		
		atlas = new TextureAtlas(Gdx.files.internal("ui/button.pack"));
		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"),atlas);
		
		table = new Table(skin);
		
		
		buttonExit = new TextButton("exit", skin);
		buttonExit.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
           	Gdx.app.exit();
		}});
		buttonExit.pad(15);
		
		buttonOnePlay  = new TextButton("1 player", skin);
		buttonOnePlay.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(false));
			}
		}
		);
		buttonOnePlay.pad(15);
		
		buttonTwoPlay = new TextButton("2 player",skin);
		buttonTwoPlay.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(true));
			}
		}
		);
		buttonTwoPlay.pad(15);
		
		
		heading = new Label("MainMenu",skin);
		heading.setFontScale(1);
		
		setupTable();
		
		stage.addActor(table);
		//creating animation
		Tween.registerAccessor(Actor.class, new ActorAccessor());
		
		//heading and button fade-in
		Timeline.createSequence().beginSequence()
			.push(Tween.to(heading, ActorAccessor.RGB, menuDelta).target(0, 0, 1))
			.push(Tween.to(heading, ActorAccessor.RGB, menuDelta).target(0, 1, 0))
			.push(Tween.to(heading, ActorAccessor.RGB, menuDelta).target(1, 0, 0))
			.push(Tween.to(heading, ActorAccessor.RGB, menuDelta).target(0, 1, 1))
			.push(Tween.to(heading, ActorAccessor.RGB, menuDelta).target(1, 1, 0))
			.push(Tween.to(heading, ActorAccessor.RGB, menuDelta).target(1, 0, 1))
			.push(Tween.to(heading, ActorAccessor.RGB, menuDelta).target(1, 1, 1))
			.end().repeat(Tween.INFINITY, 0).start(tweenManager);
		
		Timeline.createSequence().beginSequence()
			.push(Tween.set(buttonOnePlay, ActorAccessor.ALPHA).target(0))
			.push(Tween.set(buttonTwoPlay, ActorAccessor.ALPHA).target(0))
			.push(Tween.set(buttonExit, ActorAccessor.ALPHA).target(0))
			.push(Tween.from(heading, ActorAccessor.ALPHA, menuDelta).target(0))
			.push(Tween.to(buttonOnePlay, ActorAccessor.ALPHA, menuDelta).target(1))
			.push(Tween.to(buttonTwoPlay, ActorAccessor.ALPHA, menuDelta ).target(1))
			.push(Tween.to(buttonExit, ActorAccessor.ALPHA, menuDelta ).target(1))
			.end().start(tweenManager);
		
		//table fade-in
		Tween.from(table, ActorAccessor.ALPHA, menuDelta*2).target(0).start(tweenManager);
		Tween.from(table, ActorAccessor.Y, menuDelta*2).target(Gdx.graphics.getHeight()/8).start(tweenManager);
		
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		stage.dispose();
		atlas.dispose();
		skin.dispose();
	}

}
