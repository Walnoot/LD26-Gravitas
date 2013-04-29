package walnoot.ld26.entities;

import walnoot.ld26.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;

public abstract class MeshEntity extends Entity{
	protected Mesh mesh;
	private float radius = 1f;
	
	public MeshEntity(float x, float y, float saturation, float value){
		super(x, y);
		
		int numPoints = 100;
		
		mesh =
				new Mesh(true, numPoints + 2, 0, new VertexAttribute(Usage.Position, 2,
						ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(Usage.ColorPacked, 4,
						ShaderProgram.COLOR_ATTRIBUTE));
		
		float[] vertices = new float[(numPoints + 2) * 3];
		Color temp = new Color(0.5f, 0.5f, 0.5f, 1f);
		
		vertices[0] = 0f;
		vertices[1] = 0f;
		vertices[2] = temp.toFloatBits();
		
		for(int i = 0; i < numPoints; i++){
			float completeness = (float) i / (float) numPoints;
			
			setVertice((i + 1) * 3, vertices, temp, completeness, saturation, value);
		}
		
		setVertice((numPoints + 1) * 3, vertices, temp, 0f, saturation, value);
		
		mesh.setVertices(vertices);
	}
	
	private void setVertice(int index, float[] vertices, Color temp, float completeness, float saturation, float value){
		vertices[index] = MathUtils.cos(2f * MathUtils.PI * completeness);
		vertices[index + 1] = MathUtils.sin(2f * MathUtils.PI * completeness);
		
		Util.hsvToRgb(completeness, saturation, value, temp);
		vertices[index + 2] = temp.toFloatBits();
	}
	
	@Override
	public void render(){
		Gdx.gl10.glPushMatrix();
		
		Gdx.gl10.glColor4f(1f, 1f, 1f, 1f);
		Gdx.gl10.glTranslatef(pos.x, pos.y, -0.5f);
		Gdx.gl10.glScalef(radius, radius, 1f);
//		Gdx.gl10.glLineWidth(4f);
		mesh.render(GL10.GL_TRIANGLE_FAN);
//		mesh.render(GL10.GL_LINE_LOOP);
		
		Gdx.gl10.glPopMatrix();
	}
	
	public float getRadius(){
		return radius;
	}
	
	public void setRadius(float radius){
		this.radius = radius;
	}
}
