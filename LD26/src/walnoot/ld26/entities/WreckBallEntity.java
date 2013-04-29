package walnoot.ld26.entities;

import walnoot.ld26.LDGame;
import walnoot.ld26.Util;

import com.badlogic.gdx.math.Vector2;

public class WreckBallEntity extends PhysicsEntity{
	public static final float DRAG = (float) Math.pow(0.8, 1.0 / LDGame.UPDATES_PER_SECOND);
	
	public WreckBallEntity(float x, float y){
		super(Util.getCircleBody(Vector2.tmp.set(x, y)), 0.5f, 0.9f);
	}
}
