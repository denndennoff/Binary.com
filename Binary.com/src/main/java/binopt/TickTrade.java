package binopt;

import org.sikuli.basics.Settings;
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
	private int[] stat;

	public TickTrade(double defaultStake, int[] stat) {
		this.defaultStake = defaultStake;
		this.stake = defaultStake;
		this.stat = stat;
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
			setResult(true);
		} else {
			setResult(false);
		}
		s.click("close");
		s.wait("up", 20);
	}

	public void trade() throws FindFailed {
		stat = new int[10];
		int redCount = 0;
		int lost = 0;
		int lostToPrint = 0;
		setLoop(true);
		while (loop) {
			setTrend(up);
			int countUp = 0;
			while (countUp < 2) {
				countUp++;
				makeStake(stake);

				if (isResult()) {
					stat[0] = stat[0] + 1;
					lost = 0;
					redCount = 0;
					setStake(getDefaultStake());

				}
				if (!isResult()) {
					redCount++;
					lost++;
					stat[lost] = stat[lost] + 1;

					if (redCount == 1) {
						setIndex(3);
					}
					if (redCount == 2) {
						setIndex(5);
					}
					if (redCount == 3) {
						setIndex(2.5);
					}
					if (redCount > 3) {
						setIndex(2.3);
					}

					setStake(Utils.round(getStake() * getIndex()));
					if (lost > lostToPrint) {
						lostToPrint = lost;
						System.out.println(lostToPrint);
					}

				}
			}
			setTrend(down);
			int countDown = 0;
			while (countDown < 2) {
				countDown++;
				makeStake(stake);
				if (isResult()) {
					stat[0] = stat[0] + 1;
					lost = 0;
					redCount = 0;
					setStake(getDefaultStake());

				}
				if (!isResult()) {
					redCount++;
					lost++;
					stat[lost] = stat[lost] + 1;
					if (redCount == 1) {
						setIndex(3);
					}
					if (redCount == 2) {
						setIndex(5);
					}
					if (redCount == 3) {
						setIndex(2.5);
					}
					if (redCount > 3) {
						setIndex(2.3);
					}
					setStake(Utils.round(getStake() * getIndex()));
					if (lost > lostToPrint) {
						lostToPrint = lost;
						System.out.println(lostToPrint);
					}
				}
			}
			if (lostToPrint >= 8) {
				loop = false;
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
		ImagePath.add("src/main/resources/images");
		Settings.ActionLogs = false;
		// Screen s = new Screen();
		// s.find("thisContract");
		double defaultStake = 0.5;
		int[] stat = new int[10];

		TickTrade trade = new TickTrade(defaultStake, stat);
		trade.trade();
		for (int i : stat) {
			System.out.println(stat[i]);
		}

	}

}
