package binopt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;

import org.sikuli.basics.Settings;
import org.sikuli.script.*;

public class Utils {
	static Screen s = new Screen();
	static double stavka = 1;
	static double defaultStavka = 1;
	static boolean loop = true;
	static String up = "up";
	static String down = "down";
	static String trend = "up";
	static boolean stavkaReset;
	static double index = 2.5;
	static double max = 0;
	static double balance = 50000.0;
	static double profit = 0.0;
	public static int count = 0;
	static double target = 200;
	static boolean itog = true;

	public static void writeToFile(String text) {

		File log = new File("C:" + File.separator + "binary logs" + File.separator + "tradelog.txt");
		log.getParentFile().mkdirs();
	    

		try {
			if (!log.exists()) {
				log.createNewFile();

			}
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date());
			FileWriter fileWriter = new FileWriter(log, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(timeStamp + ": " + text + "\r\n ");
			bufferedWriter.close();
		} catch (IOException e) {
			System.out.println("COULD NOT LOG!!");
			e.printStackTrace();
		}
	}

	public static double round(double value) {

		BigDecimal bd = new BigDecimal(value);
		int places = 2; // Rounding onto two places after point
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static void setEurUsd5Ticks() {
		ImagePath.add("images");
		try {
			Match forex = s.find("forex");
			if (s.find("forex").right(400).exists("eurUsd") == null) {
				forex.right(140).click();
				s.click("eurUsd");
			}
			s.find("duration").right(80).doubleClick();
			s.type(Key.DELETE);
			s.type("5");
			s.find("duration").right(200).click();
			s.click("ticks");

		} catch (FindFailed e) {
			e.printStackTrace();
		}
	}

	public static void stake(double stake, String direction) {

		try {
			if (stake > max) {
				max = stake;
			}
			s.wait(1.0);
			s.find("stake").right(40).click();
			s.keyDown(Key.CTRL);
			s.type("a");
			s.keyUp();
			s.type(Key.DELETE);
			s.type(Double.toString(stake));
			balance = balance - stake;
			s.wait(0.5);
			s.click(direction);
			s.wait(1.0);
			if (s.exists("contrConf") == null) {
				s.wait(1.0);
				s.click(direction);
			}
			double b = 0.0;
			String a = s.find("potent").below(30).text();
			b = Double.parseDouble(a);
			System.out.println("==POTENTIALLLLL profit: " + a);
			s.wait("contrConf", 240);
			s.find("close").below(20).hover();
			s.wait("thisContract", 60);
			if (s.find("thisContract").nearby().exists(new Pattern("wonn").exact()) != null) {
				profit = round(profit + b);
				balance = round(balance + stavka + b);
				itog = true;
			} else {
				round(profit = profit - stake);
				itog = false;
			}
			s.click("close");
		} catch (FindFailed e) {
			e.printStackTrace();
		}
		if (profit >= target) {
			loop = false;
		}
		if (balance - stavka * index <= 0) {
			loop = false;
		}
		writeToFile(Double.toString(round(profit)));
	}

	// public static String catchTrend() {
	// if (stake(stavka, "up")) {
	// trend = up;
	// stavkaReset = true;
	// } else {
	// stavka *= index;
	// s.wait(1d);
	// if (stake(stavka, "up")) {
	// trend = up;
	// stavkaReset = true;
	// } else {
	// trend = down;
	// stavkaReset = false;
	// }
	// }
	// System.out.println("trend = " + trend);
	// return trend;
	// }
	//
	// public static void trade() {
	// catchTrend();
	// System.out.println("stavkaReset = " + stavkaReset);
	// if (stavkaReset) {
	// stavka = 0.5;
	// }
	//
	// while (loop) {
	// if (stavkaReset) {
	// stavka = 0.5;
	// }
	// boolean itog = stake(stavka, trend);
	// if (!itog) {
	// stavka *= index;
	// catchTrend();
	// }
	// }
	// }

	// public static void trade() {
	// while (loop) {
	// double a = Math.random();
	// int b = (int) (a * 100);
	// System.out.println(b);
	// System.out.println(b % 2);
	// if (b % 2 == 0) {
	// trend = up;
	// } else {
	// trend = down;
	// }
	// boolean itog = stake(stavka, trend);
	// if (!itog) {
	// stavka = round((stavka * index), 2);
	//
	// } else {
	// stavka = defaultStavka;
	// }
	// }
	//
	// }

	// one minute trade
	// public static boolean stake(double stake, String direction) {
	// boolean itog = false;
	// try {
	// if (stake > max) {
	// max = stake;
	// }
	// s.wait(1.0);
	// s.find("stake").right(40).click();
	// s.keyDown(Key.CTRL);
	// s.type("a");
	// s.keyUp();
	// s.type(Key.DELETE);
	// s.type(Double.toString(stake));
	// balance = balance - stake;
	// s.wait(0.5);
	// s.click(direction);
	// double b = 0.0;
	// String a = s.find("potent").below(30).text();
	// b = Double.parseDouble(a);
	// s.wait(59d);
	// s.click("myAccount");
	// s.wait("profitTable", 20);
	// s.click("profitTable");
	// s.wait("profitLoss");
	//
	// if (s.find("profitLoss").below(30).exists(new Pattern("green").exact())
	// != null) {
	// itog = true;
	// profit = profit + b;
	//
	// } else {
	// itog = false;
	// }
	// s.click("startTrading");
	// s.wait("up", 60);
	// } catch (FindFailed e) {
	// e.printStackTrace();
	// }
	// if (profit >= target) {
	// loop = false;
	// }
	// if (balance - stavka * index <= 0) {
	// loop = false;
	// }
	// writeToFile(Double.toString(round(profit, 2)));
	// return itog;
	// }
	//
	// public static void trade() {
	// while (loop) {
	// boolean itog = stake(stavka, trend);
	// if (itog) {
	// stavka = defaultStavka;
	// if (trend.equals(up)) {
	// trend = up;
	// } else {
	// trend = down;
	// }
	// } else {
	// stavka = stavka * index;
	// if(trend.equals(up)) trend = down;
	// else trend = up;
	// }
	//
	// itog = stake(stavka, trend);
	// if (itog) {
	// stavka = defaultStavka;
	// if (trend.equals(up)) {
	// trend = up;
	// } else {
	// trend = down;
	// }
	// } else {
	// stavka = stavka * index;
	// if(trend.equals(up)) trend = down;
	// else trend = up;
	// }
	// }
	// }

	public static void trade() {
		while (loop) {
			stake(stavka, trend);
			if (itog) {
				stavka = defaultStavka;
			}
		}
	}

	public static void main(String[] args) throws FindFailed, IOException {

		writeToFile("hahahahaha");
		writeToFile("hahahahaha");
		writeToFile("hahahahaha");

	}

}
