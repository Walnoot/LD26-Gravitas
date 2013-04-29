package walnoot.ld26.entities.enemies;

import walnoot.ld26.Util;
import walnoot.ld26.WaveManager;
import walnoot.ld26.entities.LivingEntity;
import walnoot.ld26.entities.PlayerEntity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class EnemyEntity extends LivingEntity{
	protected final PlayerEntity player;
	
	public EnemyEntity(float x, float y, PlayerEntity player, float saturation, float value, float maxHealth){
		this(Util.getCircleBody(Vector2.tmp.set(x, y)), player, saturation, value, maxHealth);
	}
	
	public EnemyEntity(Body body, PlayerEntity player, float saturation, float value, float maxHealth){
		super(body, saturation, value, maxHealth);
		this.player = player;
	}
	
	@Override
	public void onRemove(){
		super.onRemove();
		
		player.murderCount++;
		
		if(WaveManager.instance != null) WaveManager.instance.onEnemyDeath();
	}
}
