package walnoot.ld26.entities;

import com.badlogic.gdx.physics.box2d.Body;

public class PhysicsEntity extends MeshEntity{
	protected final Body body;
	
	public PhysicsEntity(Body body, float saturation, float value){
		super(0, 0, saturation, value);
		this.body = body;
		
		body.setUserData(this);
		
		pos.set(body.getPosition());
	}
	
	@Override
	public void update(){
		pos.set(body.getPosition());
	}
	
	public Body getBody(){
		return body;
	}
}
