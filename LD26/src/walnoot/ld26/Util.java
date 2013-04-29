package walnoot.ld26;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

public class Util{
	public static final CircleShape CIRCLE_SHAPE = getCircleShape(1f);
	
	/**
	 * A mesh with points (-1, -1), (1, -1), (1, 1), (-1, 1)
	 */
	public static final Mesh SQUARE_MESH = getSquareMesh();
	
	public static void hsvToRgb(float hue, float saturation, float value, Color color){
		float r, g, b;
		
		int h = (int) (hue * 6);
		float f = hue * 6 - h;
		float p = value * (1 - saturation);
		float q = value * (1 - f * saturation);
		float t = value * (1 - (1 - f) * saturation);
		
		if(h == 0){
			r = value;
			g = t;
			b = p;
		}else if(h == 1){
			r = q;
			g = value;
			b = p;
		}else if(h == 2){
			r = p;
			g = value;
			b = t;
		}else if(h == 3){
			r = p;
			g = q;
			b = value;
		}else if(h == 4){
			r = t;
			g = p;
			b = value;
		}else if(h == 5){
			r = value;
			g = p;
			b = q;
		}else{
			throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", "
					+ saturation + ", " + value);
		}
		
		color.set(r, g, b, 1f);
	}
	
	private static Mesh getSquareMesh(){
		Mesh mesh = new Mesh(true, 4, 0, new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));
		
		mesh.setVertices(new float[]{-1, -1, 1, -1, 1, 1, -1, 1});
		
		return mesh;
	}
	
	public static Body getCircleBody(Vector2 position){
		return getCircleBody(position, CIRCLE_SHAPE);
	}
	
	public static Body getCircleBody(Vector2 position, CircleShape shape){
		BodyDef bodyDef = getBodyDef();
		
		bodyDef.position.set(position);
		Body body = GameWorld.instance.physicsWorld.createBody(bodyDef);
		
		body.createFixture(getFixtureDef(shape));
		
		return body;
	}
	
	private static FixtureDef getFixtureDef(Shape shape){
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 1f; // Make it bounce a little bit
		
		return fixtureDef;
	}
	
	public static CircleShape getCircleShape(float radius){
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);
		
		return circle;
	}
	
	private static BodyDef getBodyDef(){
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.linearDamping = 0.5f;
		bodyDef.angularDamping = 0.9f;
		// We set our body to dynamic, for something like ground which doesnt move we would set it to StaticBody
		bodyDef.type = BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(0f, 0f);
		
		return bodyDef;
	}
	
	public static boolean keyDown(int... keys){
		for(int i = 0; i < keys.length; i++){
			if(Gdx.input.isKeyPressed(keys[i])) return true;
		}
		
		return false;
	}
}
