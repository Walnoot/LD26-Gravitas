package walnoot.ld26.entities.enemies;

import walnoot.ld26.entities.PlayerEntity;

import com.badlogic.gdx.math.Vector2;

public class BasicEnemy extends EnemyEntity{
	private static final float FORCE = 8f;
	
	public BasicEnemy(float x, float y, PlayerEntity player){
		super(x, y, player, 0.1f, 0.9f, 10f);
	}
	
	@Override
	public void update(){
		super.update();
		
		Vector2 force = Vector2.tmp.set(player.getBody().getPosition()).sub(body.getPosition()).nor().mul(FORCE);
		
		body.applyForceToCenter(force);
	}
}
