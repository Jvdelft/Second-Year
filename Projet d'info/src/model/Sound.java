package model;

import java.io.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;


public class Sound implements Runnable {
	private String sound;
	private long time;
	private double gain;
	public Sound(String s) {
		this(s,0);
	}
	public Sound(String s, double gain) {
		this(s,gain,0);
	}
	public Sound(String s, double gain, long time) {
		sound = s;
		this.time = time;
		this.gain = gain; //volume 
	}
	public void run() {
		try {
			String path = "Sound/" + sound + ".wav";	//on joue le fichier wav indiqué.
			File file = new File (path);
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			// set the gain (between 0.0 and 1.0)
			gain = 0.15;   
			float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
			gainControl.setValue(dB);
			clip.start();
			if (time == 0) { //si aucune durée indiquée, joué le fichier en entier
				time = clip.getMicrosecondLength();
			}
			Thread.sleep(time);
			clip.stop();
		}
		catch (Exception e) {}
	}

}