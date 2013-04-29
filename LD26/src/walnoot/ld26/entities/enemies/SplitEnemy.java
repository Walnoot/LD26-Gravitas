package walnoot.ld26.entities.enemies;

import walnoot.ld26.GameWorld;
import walnoot.ld26.Util;
import walnoot.ld26.WaveManager;
import walnoot.ld26.entities.PlayerEntity;

import com.badlogic.gdx.math.Vector2;

public class SplitEnemy extends EnemyEntity{
	private static final float START_RADIUS = 2f;
	private static final float SPEED = 5f;
	private final int splitTimes;
	private final float size;
	
	public SplitEnemy(float x, float y, PlayerEntity player){
		this(x, y, player, 2, START_RADIUS, 60f);
	}
	
	public SplitEnemy(float x, float y, PlayerEntity player, int splitTimes, float size, float maxHealth){
		super(Util.getCircleBody(Vector2.tmp.set(x, y), Util.getCircleShape(size)), player, 0.3f, 0.6f, maxHealth);
		this.splitTimes = splitTimes;
		this.size = size;
		
		setRadius(size);
	}
	
	@Override
	public void update(){
		super.update();
		
		Vector2 translation =
				Vector2.tmp.set(player.getBody().getPosition()).sub(body.getPosition()).nor().mul(SPEED / size);
		
		body.setLinearVelocity(translation);
	}
	
	@Override
	public void onRemove(){
		super.onRemove();
		
		if(splitTimes > 0){
			GameWorld.instance.addEntity(new SplitEnemy(pos.x - 1f, pos.y, player, splitTimes - 1, size * 0.5f,
					getMaxHealth() * 0.25f));
			GameWorld.instance.addEntity(new SplitEnemy(pos.x + 1f, pos.y, player, splitTimes - 1, size * 0.5f,
					getMaxHealth() * 0.25f));
			
			WaveManager.instance.enemiesRemaining += 2;
		}
	}
}
