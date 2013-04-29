package walnoot.ld26.entities;

import walnoot.ld26.LDGame;
import walnoot.ld26.SoundManager;
import walnoot.ld26.Util;
import walnoot.ld26.particles.ParticleHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.physics.box2d.Body;

public class LivingEntity extends PhysicsEntity{
	public static final float HEALTH_BAR_WIDTH = 1.5f;
	public static final float HEALTH_BAR_HEIGHT = 0.1f;
	public static final float HEALTH_BAR_BORDER = 0.05f;
	
	private static final float DAMAGE_THRESHOLD = 1f;
	
	private float health, displayHealth;
	private final float maxHealth;
	
	public LivingEntity(Body body, float saturation, float value, float maxHealth){
		super(body, saturation, value);
		
		this.maxHealth = maxHealth;
		health = maxHealth;
	}
	
	public void onCollision(float strength){
		health -= strength;
	}
	
	public void damage(float damage){
		if(damage > DAMAGE_THRESHOLD){
			health -= damage;
			SoundManager.instance.hit.play(1f - (1f / (0.1f * damage + 1)));
			
			if(health < 0) remove();
		}
	}
	
	public void heal(float amount){
		health = Math.min(maxHealth, health + amount);
	}
	
	@Override
	public void update(){
		super.update();
		
		displayHealth += Math.min(100f * LDGame.SECONDS_PER_UPDATE, health - displayHealth);
	}
	
	@Override
	public void render(){
		super.render();
		
		Gdx.gl10.glPushMatrix();
		
		Gdx.gl10.glTranslatef(pos.x, pos.y + getRadius() + 0.5f, 0f);
		
		Gdx.gl10.glColor4f(0.8f, 0.8f, 0.8f, 1f);
		Gdx.gl10.glScalef(HEALTH_BAR_WIDTH * (Math.max(displayHealth, 0f) / maxHealth), HEALTH_BAR_HEIGHT, 1f);
		Util.SQUARE_MESH.render(GL10.GL_TRIANGLE_FAN);
		
		Gdx.gl10.glPopMatrix();
		
		Gdx.gl10.glPushMatrix();
		
		Gdx.gl10.glTranslatef(pos.x, pos.y + getRadius() + 0.5f, -0.1f);
		
		Gdx.gl10.glColor4f(0.5f, 0.5f, 0.5f, 1f);
		Gdx.gl10.glScalef(HEALTH_BAR_WIDTH + HEALTH_BAR_BORDER, HEALTH_BAR_HEIGHT + HEALTH_BAR_BORDER, 1f);
		Util.SQUARE_MESH.render(GL10.GL_TRIANGLE_FAN);
		
		Gdx.gl10.glPopMatrix();
	}
	
	public float getMaxHealth(){
		return maxHealth;
	}
	
	@Override
	public void onRemove(){
		super.onRemove();
		
		ParticleHandler.instance.createEffect(pos);
	}
}
