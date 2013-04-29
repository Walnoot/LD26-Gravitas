package walnoot.ld26;

import walnoot.ld26.entities.Entity;
import walnoot.ld26.entities.LivingEntity;
import walnoot.ld26.entities.PhysicsEntity;
import walnoot.ld26.entities.PlayerEntity;
import walnoot.ld26.entities.WreckBallEntity;
import walnoot.ld26.entities.enemies.EnemyEntity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class GameWorld implements ContactListener{
	public static GameWorld instance;
	
	private Array<Entity> entities = new Array<Entity>(false, 32);
	
	private Mesh lineMesh;
	
	public WreckBallEntity ball;
	public PlayerEntity player;
	
	public World physicsWorld;
	
	public GameWorld(){
		instance = this;
		
		lineMesh = new Mesh(true, 3, 0, new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));
		lineMesh.setVertices(new float[]{1f, 0f, 0f, 0f, 0f, 1f});
		
		setupBox2D();
	}
	
	private void setupBox2D(){
		physicsWorld = new World(new Vector2(), true);
		physicsWorld.setContactListener(this);
		
//		Body body = Util.getCircleBody(new Vector2());
//		body.applyForceToCenter(50f, 50f);
		
//		addEntity(new LivingEntity(Util.getCircleBody(new Vector2(3f, 3f)), 0.2f, 0.5f, 50f));
	}
	
	public void render(OrthographicCamera camera){
		renderGrid(camera);
		
		for(Entity e : entities){
			e.render();
		}
		
//		renderer.render(physicsWorld, camera.combined);
	}
	
	public void update(){
		physicsWorld.step(LDGame.SECONDS_PER_UPDATE, 6, 2);
		
		for(int i = 0; i < entities.size; i++){
			Entity e = entities.get(i);
			
			if(!e.removed){
				e.update();
			}else{
				e.onRemove();
				
				entities.removeIndex(i);
				i--;
				
				//this is done here because Box2D didn't like removing bodies onRemove()
				if(e instanceof PhysicsEntity) physicsWorld.destroyBody(((PhysicsEntity) e).getBody());
			}
		}
	}
	
	private void renderGrid(OrthographicCamera camera){
		Gdx.gl10.glLineWidth(2f);
		Gdx.gl10.glColor4f(.1f, .1f, .1f, 1f);
		
		int startX = (int) (camera.position.x - camera.viewportWidth * 0.5f);
		int startY = (int) (camera.position.y - camera.viewportHeight * 0.5f);
		
		for(int x = -1; x < (int) camera.viewportWidth + 1; x++){
			for(int y = -1; y < (int) camera.viewportHeight + 1; y++){
				Gdx.gl10.glPushMatrix();
				
				Gdx.gl10.glTranslatef(startX + x, startY + y, -1f);
				lineMesh.render(GL10.GL_LINE_STRIP);
				
				Gdx.gl10.glPopMatrix();
			}
		}
	}
	
	public void addEntity(Entity e){
		entities.add(e);
		
		if(e instanceof WreckBallEntity) ball = (WreckBallEntity) e;
		if(e instanceof PlayerEntity) player = (PlayerEntity) e;
	}
	
	@Override
	public void beginContact(Contact contact){
	}
	
	@Override
	public void endContact(Contact contact){
	}
	
	@Override
	public void preSolve(Contact contact, Manifold oldManifold){
	}
	
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse){
		Object a = contact.getFixtureA().getBody().getUserData();
		Object b = contact.getFixtureB().getBody().getUserData();
		
		if(!(a instanceof EnemyEntity && b instanceof EnemyEntity)){//so enemies can't hurt each other
			checkHurt(a, impulse.getNormalImpulses()[0]);
			checkHurt(b, impulse.getNormalImpulses()[0]);
		}
	}
	
	private void checkHurt(Object userData, float damage){
		if(userData instanceof LivingEntity) ((LivingEntity) userData).damage(damage);
	}
}
