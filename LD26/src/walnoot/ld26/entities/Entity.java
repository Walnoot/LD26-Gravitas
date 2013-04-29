package walnoot.ld26.entities;

import com.badlogic.gdx.math.Vector2;

public abstract class Entity{
	public Vector2 pos = new Vector2();
	public boolean removed;
	
	public Entity(float x, float y){
		pos.set(x, y);
	}
	
	public abstract void render();
	
	public abstract void update();
	
	public void remove(){
		removed = true;
		
//		onRemove();
	}
	
	public void onRemove(){
	}
}
