package walnoot.ld26;

import walnoot.ld26.entities.enemies.BasicEnemy;
import walnoot.ld26.entities.enemies.ChargeEnemy;
import walnoot.ld26.entities.enemies.SplitEnemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class WaveManager{
	public static WaveManager instance;
	
	private static String[] NUMBERS = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
			"eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
			"twenty", "twenty-one", "twenty-two", "twenty-three", "twenty-four", "twenty-six", "twenty-seven",
			"twenty-eight", "twenty-nine", "thirty"};
	
	private int currentWave = 0;
	public int enemiesRemaining;
	
	private final Label waveText;
	private final GameWorld world;
	
	private int startTimer = LDGame.LOGO_SHOW_TIME;
	
	public WaveManager(Label waveText, GameWorld world){
		this.world = world;
		this.waveText = waveText;
		
		instance = this;
	}
	
	public void update(){
		if(currentWave == 0){
			startTimer--;
			if(startTimer == 0){
				currentWave++;
				initWave();
			}
		}else if(enemiesRemaining == 0){
			currentWave++;
			initWave();
		}
	}
	
	private void initWave(){
		world.player.heal(world.player.getMaxHealth() / 3f);
		
		SoundManager.instance.newWave.play();
		
		if(currentWave - 1 < NUMBERS.length) waveText.setText("wave " + NUMBERS[currentWave - 1]);
		else waveText.setText("wave potato");
		
		enemiesRemaining = 3 + currentWave;
		
		float angle = MathUtils.random(0f, 2f * MathUtils.PI);
		Vector2 spawnPoint = new Vector2(MathUtils.cos(angle), MathUtils.sin(angle)).mul(20f).add(world.player.pos);
		
		for(int i = 0; i < enemiesRemaining; i++){
			float randAngle = MathUtils.random(0f, 2f * MathUtils.PI);
			
			Vector2 pos = Vector2.tmp.set(MathUtils.cos(randAngle), MathUtils.sin(randAngle)).mul(5f).add(spawnPoint);
			
			if(i < enemiesRemaining / 3){
				world.addEntity(new ChargeEnemy(pos.x, pos.y, world.player));
				continue;
			}
			if(i == enemiesRemaining / 3 && currentWave >= 3 && currentWave % 2 == 1){//odd wave numbers so the split enemy doesnt become to common
				world.addEntity(new SplitEnemy(pos.x, pos.y, world.player));
				continue;
			}
			world.addEntity(new BasicEnemy(pos.x, pos.y, world.player));
		}
	}
	
	public void onEnemyDeath(){
		enemiesRemaining--;
	}
}
