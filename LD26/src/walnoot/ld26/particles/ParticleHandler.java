package walnoot.ld26.particles;

import walnoot.ld26.LDGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ParticleHandler{
	public static ParticleHandler instance = new ParticleHandler();
	
	private static final Color TEMP_COLOR = new Color();
	
	private Array<Particle> particles = new Array<Particle>(false, 128);
	
	private final Mesh[] meshes = new Mesh[8];
	
	private ParticleHandler(){
		for(int i = 0; i < meshes.length; i++){
			meshes[i] = getRandomMesh();
		}
	}
	
	private Mesh getRandomMesh(){
		int numVertices = MathUtils.random(3, 7);
		
		Mesh mesh =
				new Mesh(true, numVertices, 0, new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));
		
		float[] vertices = new float[numVertices * 2];
		
		for(int i = 0; i < numVertices; i++){
			float rad = 2f * MathUtils.PI * i / numVertices;
			float dist = MathUtils.random(0.1f, 0.4f);
			
			vertices[i * 2] = MathUtils.cos(rad) * dist;
			vertices[i * 2 + 1] = MathUtils.sin(rad) * dist;
		}
		
		mesh.setVertices(vertices);
		
		return mesh;
	}
	
	public void createEffect(Vector2 pos){
		for(int i = 0; i < 16; i++){
			float rad = MathUtils.random(0f, 2f * MathUtils.PI);
			
			float xVel = MathUtils.cos(rad);
			float yVel = MathUtils.sin(rad);
			
			int life = (int) (MathUtils.random(1f, 1.5f) * LDGame.UPDATES_PER_SECOND);
			
			float spinSpeed = MathUtils.random(0f, 180f);
			if(MathUtils.randomBoolean()) spinSpeed = -spinSpeed;
			
			TEMP_COLOR.set(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1f);
			
			particles.add(new Particle(pos.x, pos.y, xVel, yVel, meshes[MathUtils.random(0, meshes.length - 1)],
					TEMP_COLOR, life, spinSpeed));
		}
	}
	
	public void render(){
		for(Particle p : particles){
			p.render();
		}
	}
	
	public void update(){
//		for(Particle p : particles){
//			p.update();
//		}
		
		for(int i = 0; i < particles.size; i++){
			Particle particle = particles.get(i);
			
			if(!particle.removed) particle.update();
			else{
				particles.removeIndex(i);
				i--;
			}
		}
	}
}
