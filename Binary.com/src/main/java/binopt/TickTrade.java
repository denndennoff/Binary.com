package binopt;

import org.sikuli.script.FindFailed;
import org.sikuli.script.ImagePath;
import org.sikuli.script.Key;
import org.sikuli.script.Screen;

public class TickTrade {
	
	private final String up = "up";
	private final String down = "down";
	private String trend;
	private boolean loop;
	private boolean stakeReset;
	private boolean result;
	private double index;
	private double max;
	private double balance;
	private double profit;
	private double target;
	private double stake;
	private double stavka;
	private double defaultStake;
	private double time;
	private int count;

	public TickTrade(double defaultStake, double index, String trend, double time) {
		this.defaultStake = defaultStake;
		this.index = index;
		this.trend = trend;
		this.stake = defaultStake;
		this.time = time;
	}

	// === Trading methods

	public void makeStake(double stake) throws FindFailed {
		Screen s = new Screen();

		s.find("stake").right(30).click();
		s.keyDown(Key.CTRL);
		s.type("a");
		s.keyUp();
		s.type(Key.DELETE);
		s.paste(Double.toString(stake));
		s.wait(1d);
		s.click(getTrend());
		s.wait(3d);
		if (s.exists("contrConf") == null) {
			s.click(getTrend());
		}
		s.wait("thisContract", 20);		
		if (s.exists("wonn") != null) {
			setStake(getDefaultStake());
			setResult(true);
		} else {
			setStake(Utils.round(getStake() * getIndex()));
			setResult(false);
		}
		s.click("close");
		s.wait("up", 20);
		if (isResult()) {
			System.out.println("it was true");
		} else {
			System.out.println("it was false");
		}
	}

	public void trade() throws FindFailed {
		setLoop(true);
		while(loop) {
			makeStake(stake);
			if(!isResult()) {
				if(getTrend().equals(up)) {
					setTrend(down);
				} else {
					setTrend(up);
				}
			}
		}

	}

	// === Getters & Setters ===

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public double getStake() {
		return stake;
	}

	public void setStake(double stake) {
		this.stake = stake;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public double getDefaultStake() {
		return defaultStake;
	}

	public void setDefaultStake(double defaultStake) {
		this.defaultStake = defaultStake;
	}

	public String getTrend() {
		return trend;
	}

	public void setTrend(String trend) {
		this.trend = trend;
	}

	public double getIndex() {
		return index;
	}

	public void setIndex(double index) {
		this.index = index;
	}

	public static void main(String[] args) throws FindFailed {
		ImagePath.add("images");

		double stake = 1;
		double defaultStake = 2;

		TickTrade trade = new TickTrade(defaultStake, 2.5, "up", 11);
		trade.trade();

	}

}
