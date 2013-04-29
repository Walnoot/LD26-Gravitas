package walnoot.ld26.entities;

import walnoot.ld26.Util;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class PlayerEntity extends LivingEntity implements InputProcessor{
	public static final float SPEED = 8f;
	public static final float TURN_SPEED = 4f;
	
	public int murderCount;
	
	private final WreckBallEntity ball;
	
	public PlayerEntity(float x, float y, WreckBallEntity ball){
		super(Util.getCircleBody(Vector2.tmp.set(0, 0)), 1f, 0.9f, 200f);
		this.ball = ball;
	}
	
	@Override
	public void update(){
		super.update();
		
		Vector2 translation = Vector2.tmp.set(0f, 0f);
		
		if(Util.keyDown(Keys.UP, Keys.W)) translation.add(0f, 1f);
		if(Util.keyDown(Keys.DOWN, Keys.S)) translation.add(0f, -1f);
		if(Util.keyDown(Keys.LEFT, Keys.A)) translation.add(-1f, 0f);
		if(Util.keyDown(Keys.RIGHT, Keys.D)) translation.add(1f, 0f);
		
		translation.nor().mul(SPEED);
		
		body.setLinearVelocity(translation);
		
		if(Util.keyDown(Keys.CONTROL_LEFT, Keys.CONTROL_RIGHT, Keys.SPACE)){
			Vector2 offset = Vector2.tmp.set(ball.pos).sub(pos);
			float len = offset.len();
			
			if(len < 0.1f) return;
			
			offset.nor().mul(-200f / len);
			
			ball.body.applyForceToCenter(offset);
		}
	}
	
	@Override
	public boolean keyDown(int keycode){
		if(keycode == Keys.BACKSPACE){
			ball.remove();
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode){
		return false;
	}
	
	@Override
	public boolean keyTyped(char character){
		return false;
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button){
		return false;
	}
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button){
		return false;
	}
	
	@Override
	public boolean touchDragged(int x, int y, int pointer){
		return false;
	}
	
	@Override
	public boolean touchMoved(int x, int y){
		return false;
	}
	
	@Override
	public boolean scrolled(int amount){
		return false;
	}
}
