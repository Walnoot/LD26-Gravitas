package walnoot.ld26;

import walnoot.ld26.entities.PlayerEntity;
import walnoot.ld26.entities.WreckBallEntity;
import walnoot.ld26.particles.ParticleHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;

public class LDGame implements ApplicationListener{
	public static final float UPDATES_PER_SECOND = 60, SECONDS_PER_UPDATE = 1 / UPDATES_PER_SECOND;
	
	public static final int LOGO_SHOW_TIME = (int) (9 * UPDATES_PER_SECOND),
			LOGO_FADE_TIME = (int) (1 * UPDATES_PER_SECOND);
	public static final int GAME_OVER_TIME = (int) (4 * UPDATES_PER_SECOND);
	
	private static final float MIN_ZOOM = 16f;
	
	private OrthographicCamera camera;
	private GameWorld world;
	
	private float updateTimer;
	
	private Stage stage;
	private int logoTimer;
	private int gameOverTimer;
	private Image logo;
	private Label waveLabel;
	private Label tutorialLabel;
	private Table tutorialTable;
	private Label gameOverLabel;
	
	private WaveManager waveManager;
	
	@Override
	public void create(){
		camera = new OrthographicCamera();
		updateCamera(MIN_ZOOM);
		
		loadStage();
		
		loadWorld();
	}
	
	private void loadWorld(){
		world = new GameWorld();
		
		WreckBallEntity ball = new WreckBallEntity(-3f, 0);
		world.addEntity(ball);
		
		PlayerEntity player = new PlayerEntity(0, 0, ball);
		
		world.addEntity(player);
		Gdx.input.setInputProcessor(player);
		
//		world.addEntity(new EnemyEntity(8f, 8f, player));
		
		waveManager = new WaveManager(waveLabel, world);
		
//		world.addEntity(new EnemyEntity(4f, 4f, player));
	}
	
	private void loadStage(){
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		
		Table logoTable = new Table();
		logoTable.top().setFillParent(true);
		
		logo = new Image(new Texture("logo.png"));
		logoTable.add(logo);
		
		stage.addActor(logoTable);
		
		LabelStyle labelStyle = new LabelStyle(new BitmapFont(Gdx.files.internal("font.fnt"), false), Color.WHITE);
		
		Table waveTable = new Table();
		waveTable.left().bottom().pad(32).setFillParent(true);
		
		waveLabel = new Label("", labelStyle);
		waveTable.add(waveLabel);
		
		stage.addActor(waveTable);
		
		tutorialTable = new Table();
		tutorialTable.setFillParent(true);
		
		tutorialLabel = new Label("move with directional keys", labelStyle);
		tutorialTable.add(tutorialLabel);
		
		stage.addActor(tutorialTable);
		
		Table gameOverTable = new Table();
		gameOverTable.setFillParent(true);
		
		gameOverLabel = new Label("game over", labelStyle);
		gameOverLabel.color.a = 0;
		gameOverTable.add(gameOverLabel);
		
		stage.addActor(gameOverTable);
	}
	
	@Override
	public void dispose(){
		stage.dispose();
	}
	
	@Override
	public void render(){
		updateTimer += Gdx.graphics.getDeltaTime();
		while(updateTimer > SECONDS_PER_UPDATE){
			updateTimer -= SECONDS_PER_UPDATE;
			
			update();
		}
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		camera.update();
		camera.apply(Gdx.gl10);
		
		Gdx.gl10.glEnable(GL10.GL_DEPTH_TEST);
		world.render(camera);
		Gdx.gl10.glDisable(GL10.GL_DEPTH_TEST);
		
		ParticleHandler.instance.render();
		
		stage.draw();
	}
	
	private void update(){
		float zoom =
				Math.max(Math.abs(world.player.pos.x - world.ball.pos.x),
						Math.abs(world.player.pos.y - world.ball.pos.y));
		
//		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();
		
		updateCamPos();
		updateCamera(Math.max(zoom + 2f, MIN_ZOOM));
		
		waveManager.update();
		
		world.update();
		
		handleFade();
		
		ParticleHandler.instance.update();
	}
	
	private void handleFade(){
		logoTimer++;
		
		if(world.player.removed){
			if(gameOverTimer == 0)
				gameOverLabel.setText("game over\n(killed " + world.player.murderCount + " enemies)");
			
			gameOverLabel.color.a = Math.min(1f, (float) gameOverTimer / LOGO_FADE_TIME);
			waveLabel.color.a = Math.max(0f, 1f - (float) gameOverTimer / LOGO_FADE_TIME);
			
			if(gameOverTimer > LOGO_FADE_TIME) waveLabel.setText("");
			
			gameOverTimer++;
			
			if(gameOverTimer > GAME_OVER_TIME){
				loadWorld();
				gameOverLabel.color.a = 0f;
				waveLabel.color.a = 1f;
				
				gameOverTimer = 0;
			}
		}
		
		if(logoTimer > LOGO_SHOW_TIME / 3) tutorialLabel.setText("space attracts the ball");
		if(logoTimer > LOGO_SHOW_TIME / 3 * 2) tutorialLabel.setText("don't get hit");
		
		if(logoTimer > LOGO_SHOW_TIME){
			logo.color.a = 1f - (float) (logoTimer - LOGO_SHOW_TIME) / LOGO_FADE_TIME;
			stage.removeActor(tutorialTable);
		}
		
		if(logoTimer > LOGO_SHOW_TIME + LOGO_FADE_TIME) stage.removeActor(logo);
	}
	
	private void updateCamPos(){
		float targetX = (world.ball.pos.x + world.player.pos.x) * 0.5f;
		float targetY = (world.ball.pos.y + world.player.pos.y) * 0.5f;
		
		camera.position.x += (targetX - camera.position.x) * 0.05f;
		camera.position.y += (targetY - camera.position.y) * 0.05f;
	}
	
	private void updateCamera(float zoom){
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera.viewportHeight = zoom;
		camera.viewportWidth = zoom * w / h;
	}
	
	@Override
	public void resize(int width, int height){
//		updateCamera(MIN_ZOOM);
		
		stage.setViewport(width, height, true);
	}
	
	@Override
	public void pause(){
	}
	
	@Override
	public void resume(){
	}
}
