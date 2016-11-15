package binopt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;

import org.sikuli.script.FindFailed;
import org.sikuli.script.ImagePath;
import org.sikuli.script.Key;
import org.sikuli.script.Location;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

public class ScriptVolat {

	static Screen s = new Screen();
	static double stavka = 1;
	static double defaultStavka = 1;
	static boolean loop = true;
	static String up = "up";
	static String down = "down";
	static String trend = "up";
	static boolean stavkaReset;
	static double index = 2.6;
	static double max = 0;
	static double balance = 100.0; // auch den Wert fürs Zurücksetzen in der
									// Line
									// 105 anpassen!!!
	static double payout = 0.94;
	static double profit = 0.0;
	public static int count = 0;

	public static double getPayout() {

		if (stavka >= 0.5 && stavka < 1.0) {
			return 0.92;
		} else {
			return 0.94;
		}

	}

	public static void writeToFile(String text) {

		File log = new File("C:/Users/igork/workspace_sep/Binary/balance.txt");

		try {
			if (!log.exists()) {
				log.createNewFile();
			}
			String timeStamp = new SimpleDateFormat("HH:mm,  dd.MM.yyyy").format(new java.util.Date());
			FileWriter fileWriter = new FileWriter(log, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write("**" + text + "** " + timeStamp + "\r\n" + "Balance: "
					+ Double.toString(round(balance, 2)) + "Maxstavka:  " + max + "\r\n");
			bufferedWriter.close();
		} catch (IOException e) {
			System.out.println("COULD NOT LOG!!");
		}
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
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

	public static boolean stake(double stake, String direction) {
		boolean itog = false;
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
			s.wait("contrConf", 120);
			s.find("close").below(20).hover();
			s.wait("thisContract", 60);
			if (s.find("thisContract").nearby().exists(new Pattern("wonn").exact()) != null) {
				profit = round((profit + stake * getPayout()), 2);
				balance = 100.00;
				itog = true;
			} else {
				round((profit = profit - stake), 2);
				itog = false;
			}
			s.click("close");
		} catch (FindFailed e) {
			e.printStackTrace();
		}
		writeToFile(Double.toString(round(profit, 2)));
		return itog;
	}

	// public static String catchTrend() {
	// if (stake(stavka, "up")) {
	// trend = up;
	// stavkaReset = true;
	// } else {
	// stavka *= index;
	// if (stake(stavka, "up")) {
	// trend = up;
	// stavkaReset = true;
	// } else {
	// stavka *= index;
	// trend = down;
	// stavkaReset = false;
	// }
	// }
	// System.out.println("trend = " + trend);
	// return trend;
	// }

	// public static String catchTrend() {
	// if (stake(stavka, trend)) {
	// if (trend.equals(up)) {
	// trend = up;
	// stavkaReset = true;
	// } else {
	// trend = down;
	// stavkaReset = true;
	// }
	// } else {
	// stavka *= index;
	// stavka = round(stavka, 2);
	// if (stake(stavka, trend)) {
	// if (trend.equals(up)) {
	// trend = up;
	// stavkaReset = true;
	// } else {
	// trend = down;
	// stavkaReset = true;
	// }
	// } else {
	// stavka *= index;
	// stavka = round(stavka, 2);
	// if (!trend.equals(up)) {
	// trend = up;
	// stavkaReset = false;
	// } else {
	// trend = down;
	// stavkaReset = false;
	// }
	//
	// }
	// }
	// System.out.println("trend = " + trend);
	// return trend;
	// }

	// public static String catchTrend() {
	// if (stake(stavka, trend)) {
	// if (trend.equals(up)) {
	// trend = up;
	// stavkaReset = true;
	// } else {
	// trend = down;
	// stavkaReset = true;
	// }
	// } else {
	// stavka *= index;
	// stavka = round(stavka, 2);
	// if (!trend.equals(up)) {
	// trend = up;
	// stavkaReset = false;
	// } else {
	// trend = down;
	// stavkaReset = false;
	// }
	// }
	// System.out.println("trend = " + trend);
	// return trend;
	// }

	public static String catchTrend() {
		if (stake(stavka, trend)) {
			if (trend.equals(up)) {
				trend = down;
				stavkaReset = true;
				count = 0;
			} else {
				trend = up;
				stavkaReset = true;
				count = 0;
			}
		} else {
			count++;
			stavka *= index;
			stavka = round(stavka, 2);
			if (count >= 1) {
				if (trend.equals(up)) {
					trend = down;
					stavkaReset = false;
				} else {
					trend = up;
					stavkaReset = false;
				}
			} else {
				if (trend.equals(up)) {
					trend = up;
					stavkaReset = false;
				} else {
					trend = down;
					stavkaReset = false;
				}
			}
		}
		System.out.println("trend = " + trend);
		return trend;
	}

//	public static void trade() {
//		catchTrend();
//		System.out.println("stavkaReset = " + stavkaReset);
//		if (stavkaReset) {
//			stavka = defaultStavka;
//		}
//
//		while (loop) {
//			if (stavkaReset) {
//				stavka = defaultStavka;
//			}
//			boolean itog = stake(stavka, trend);
//			if (!itog) {
//				stavka *= index;
//				catchTrend();
//			}
//		}
//	}

//	public static void trade() {
//		while (loop) {
//			if (stavkaReset) {
//				stavka = defaultStavka;
//			}
//			boolean itog = stake(stavka, trend);
//			if (!itog) {
//				if (balance <= -1000) {
//					loop = false;
//				} else {
//					stavka *= index;
//					stavka = round(stavka, 2);
//					if (trend.equals(up)) {
//						trend = up;
//					} else {
//						trend = down;
//					}
//					catchTrend();
//				}
//			} else {
//				if (trend.equals(up)) {
//					trend = down;
//				} else {
//					trend = up;
//				}
//				stavka = defaultStavka;
//			}
//
//		}
//	}

	public static void trade() {
		while (loop) {
			double a = Math.random();
			int b = (int) (a * 100);
			System.out.println(b);
			System.out.println(b % 2);
			if (b % 2 == 0) {
				trend = up;
			} else {
				trend = down;
			}
			boolean itog = ScriptVolat.stake(stavka, trend);
			if (!itog) {
				stavka = round((stavka * index), 2);
				
			} else {
				stavka = defaultStavka;
			}

		}

	}

	public static void tries() {
		try {
			
//			int x = s.find("close").getX();
//			int y = s.find("close").getY();
//			int width = s.find("close").left(800).getX();
//			int height = s.find("close").below(50).getY();
//			Location l = new Location(x, y);
//			Region r = new Region(x,y, width, height);
//			s.hover(r.left().above());
//			s.wait(2d);
//			s.hover(r.right().above());
//			s.wait(2d);
//			s.hover(r.below().right());
//			s.wait(2d);
//			s.hover(r.below().left());
//			
//			s.hover(s.find("close").left(700));
//			if (s.find("close").left(700).below(50).exists(new Pattern("won").exact()) != null) {
//				System.out.println("da");
//				
//			} else {
//				System.out.println("nicht da");
//			}
//			s.hover(s.find("close").left(700).below(50));
//			 if(s.find("thisContract").right(300).exists(new Pattern("wonn").exact()) != null ){
//				 System.out.println("it works");
//			 } else {
//				 System.out.println("shit");
//			 }
			 s.find("lostt");
			 System.out.println("aaaaaa");
			 s.find("thisContract").nearby().find("wonn");
			 System.out.println("ahahahahha");
			
			}

		catch (FindFailed e) {
			e.printStackTrace();
			System.out.println(max);

		}

	}

}
