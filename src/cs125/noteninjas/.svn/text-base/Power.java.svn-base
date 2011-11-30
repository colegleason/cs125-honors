package cs125.noteninjas;

import android.util.Log;
import android.widget.ProgressBar;

public class Power {
	private double value = 0;
	private boolean selected;
	private ProgressBar bar;
	
	public Power(ProgressBar bar){
		this.bar = bar;
		this.selected = false;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public void updateValue(float x) {
		if (selected) {
			if (value + x > 100)
				value = 100;
			else value += x;
		}
		else {
			value = value - value*0.1;
		}
		bar.setProgress((int) value);
	}
	
	public void setSelected(boolean x) {
		selected = x;
	}
	
	public boolean isSelected() {
		return selected;
	}
}
