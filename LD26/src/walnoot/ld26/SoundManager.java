package walnoot.ld26;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager{
	public static SoundManager instance = new SoundManager();
	
	public final Sound hit, newWave;
	
	private SoundManager(){
		hit = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.wav"));
		newWave = Gdx.audio.newSound(Gdx.files.internal("sounds/newwave.wav"));
	}
}
