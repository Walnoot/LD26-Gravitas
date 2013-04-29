package walnoot.ld26.particles;

import walnoot.ld26.LDGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector2;

public class Particle{
	private int lifeTime;
	public boolean removed = false;
	private final float spinSpeed;
	
	private Vector2 position, velocity;
	private final Mesh mesh;
	private final Color color = new Color();
	private float angle;
	
	public Particle(float x, float y, float xVel, float yVel, Mesh mesh, Color color, int lifeTime, float spinSpeed){
		position = new Vector2(x, y);
		velocity = new Vector2(xVel * LDGame.SECONDS_PER_UPDATE, yVel * LDGame.SECONDS_PER_UPDATE);
		this.mesh = mesh;
		this.color.set(color);
		this.lifeTime = lifeTime;
		this.spinSpeed = spinSpeed * LDGame.SECONDS_PER_UPDATE;
	}
	
	public void update(){
		lifeTime--;
		if(lifeTime == 0) removed = true;
		
		angle += spinSpeed;
		position.add(velocity);
	}
	
	public void render(){
		Gdx.gl10.glPushMatrix();
		
		Gdx.gl10.glTranslatef(position.x, position.y, 0f);
		Gdx.gl10.glColor4f(color.r, color.g, color.b, color.a);
		Gdx.gl10.glRotatef(angle, 0f, 0f, 1f);
		mesh.render(GL10.GL_TRIANGLE_FAN);
		
		Gdx.gl10.glPopMatrix();
	}
}
