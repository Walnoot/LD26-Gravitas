package walnoot.ld26.entities.enemies;

import walnoot.ld26.LDGame;
import walnoot.ld26.entities.PlayerEntity;

import com.badlogic.gdx.math.Vector2;

public class ChargeEnemy extends EnemyEntity{
	private static final int CHARGE_TIME = (int) (0.5f * LDGame.UPDATES_PER_SECOND);
	private static final float CHARGE_FORCE = 60f;
	private static final float SPEED_THRESHOLD_SQUARED = 1f;
	
	private int chargeTimer = 0;
	private Vector2 force = new Vector2();
	
	public ChargeEnemy(float x, float y, PlayerEntity player){
		super(x, y, player, 0.5f, 0.4f, 20f);
		
		body.setLinearDamping(1f);
	}
	
	@Override
	public void update(){
		super.update();
		
		if(body.getLinearVelocity().len2() < SPEED_THRESHOLD_SQUARED){
			chargeTimer = CHARGE_TIME;
			
			force.set(player.pos).sub(pos).nor().mul(CHARGE_FORCE);
		}
		
		if(chargeTimer > 0){
			chargeTimer--;
			
			body.applyForceToCenter(force);
		}
	}
}
