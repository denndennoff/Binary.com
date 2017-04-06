package selenium;

import java.nio.channels.WritableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.MouseAction.Button;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.Screen;

import binopt.Utils;

public class SeleniumTickTrade {

	private static double stake;
	private static double index;
	private static double defaultStake;

	public SeleniumTickTrade(double stake, double index, double defaultStake) {
		SeleniumTickTrade.stake = stake;
		SeleniumTickTrade.index = index;
		SeleniumTickTrade.defaultStake = defaultStake;
	}

	public static void setProperty() {
		System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");
	}

	public static ChromeDriver createDriver() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		ChromeDriver c = new ChromeDriver(options);
		return c;
	}

	public static void login(ChromeDriver c, String loginEmail, String password) {
		Screen s = new Screen();
		c.get("https://oauth.binary.com/oauth2/authorize?app_id=1&l=DE");
		s.wait(3d);
		WebElement email = c.findElementByCssSelector("#txtEmail");
		email.sendKeys(loginEmail);
		WebElement pwd = c.findElementByCssSelector("#txtPass");
		pwd.sendKeys(password);
		WebElement login = c.findElementByCssSelector("#frmLogin > div:nth-child(3) > button");
		login.click();
		s.wait(3d);
	}

	public static double getProc(ChromeDriver c, String trend) {
		WebElement proc = c.findElementByCssSelector("#price_container_" + trend + " > div.col.price_comment");
		String str = proc.getText();
		double procReturn = 0;
		int strLength = str.length();
		try {
			String trimedStr = str.substring(strLength - 6, strLength - 1);
			procReturn = Double.parseDouble(trimedStr);
		} catch (StringIndexOutOfBoundsException e) {

		}
		return procReturn;
	}

	// public void trade(ChromeDriver c) throws InterruptedException {
	// WebElement balanceState = c
	// .findElementByCssSelector("#main-account > li > a > div.main-account >
	// div.topMenuBalance");
	//
	// System.out.println(balanceState.getText());
	// Calendar cal = Calendar.getInstance();
	// SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	// System.out.println(sdf.format(cal.getTime()));
	// boolean loop = true;
	// int lostCount = 0;
	//
	// while (loop) {
	// boolean waitForProc = true;
	// // startTime = System.currentTimeMillis();
	// while (waitForProc) {
	// if (getProc(c, "top") - getProc(c, "bottom") <= 25 && getProc(c, "top") -
	// getProc(c, "bottom") >= 10) {
	// WebElement purchase = c.findElementByCssSelector("#purchase_button_top");
	// Thread.sleep(1000);
	// purchase.click();
	// Thread.sleep(500);
	// if (c.findElementById("confirmation_error").isDisplayed()) {
	// WebElement close = c.findElementById("close_confirmation_container");
	// close.click();
	// } else {
	// break;
	// }
	// } else if (getProc(c, "top") - getProc(c, "bottom") <= -10
	// && getProc(c, "top") - getProc(c, "bottom") >= -29) {
	// WebElement purchase =
	// c.findElementByCssSelector("#purchase_button_bottom");
	// Thread.sleep(1000);
	// purchase.click();
	// Thread.sleep(500);
	// WebElement error = c.findElementById("confirmation_error");
	// if (error.isDisplayed()) {
	// WebElement close = c.findElementById("close_confirmation_container");
	// close.click();
	// } else {
	// break;
	// }
	// }
	// }
	//
	// if (stakeWon(c)) {
	// lostCount = 0;
	// loop = true;
	// setStake(getDefaultStake());
	//
	// } else {
	// lostCount++;
	//
	// if (lostCount <= 1)
	// setStake(10);
	// if (lostCount > 1) {
	// lostCount=0;
	// setStake(0.5);
	// Thread.sleep(60000);
	// }
	// }
	//
	// WebElement close = c.findElementById("close_confirmation_container");
	// close.click();
	//
	// WebElement amount = c.findElementById("amount");
	// amount.clear();
	// amount.sendKeys(Double.toString(getStake()));
	// // WebDriverWait buttons = new WebDriverWait(c, 60);
	// //
	// buttons.until(ExpectedConditions.presenceOfElementLocated(By.id("purchase_button_top")));
	// Thread.sleep(5000);
	// }
	// }
	//
	public static void setTradeParams(ChromeDriver c, String durationValue, String amountValue)
			throws InterruptedException {
		Select market = new Select(c.findElementById("contract_markets"));
		market.selectByValue(Market.FOREXMARKET);
		Select tradeValue = new Select(c.findElementById("underlying"));
		// frxEURUSD
		tradeValue.selectByValue("frxEURUSD");
		Select durUnit = new Select(c.findElementById("duration_units"));
		durUnit.selectByValue("t");
		WebElement duration = c.findElementById("duration_amount");
		duration.clear();
		duration.sendKeys(durationValue);
		Select stake = new Select(c.findElementById("amount_type"));
		stake.selectByValue("stake");
		WebElement amount = c.findElementById("amount");
		amount.clear();
		amount.sendKeys(amountValue);
		WebDriverWait buttons = new WebDriverWait(c, 60);
		buttons.until(ExpectedConditions.presenceOfElementLocated(By.id("purchase_button_top")));
		Thread.sleep(1000);
	}

	/*
	 * // public void trade(ChromeDriver c) throws InterruptedException { //
	 * WebElement balanceState = c // .findElementByCssSelector("#main-account >
	 * li > a > div.main-account > // div.topMenuBalance"); // //
	 * System.out.println(balanceState.getText()); // Calendar cal =
	 * Calendar.getInstance(); // SimpleDateFormat sdf = new
	 * SimpleDateFormat("HH:mm:ss"); //
	 * System.out.println(sdf.format(cal.getTime())); // boolean loop = true; //
	 * int lostCount = 0; // while (loop) { // // WebElement purchase =
	 * c.findElementByCssSelector("#purchase_button_top"); //
	 * Thread.sleep(1000); // purchase.click(); // Thread.sleep(500); // if
	 * (c.findElementById("confirmation_error").isDisplayed()) { // WebElement
	 * close = c.findElementById("close_confirmation_container"); //
	 * close.click(); // Thread.sleep(30000); // purchase.click(); // } //
	 * WebElement potProfit = c.findElementById("contract_purchase_profit"); //
	 * String pofitStr = potProfit.getText(); // System.out.println(pofitStr);
	 * // if (stakeWon(c)) { // lostCount = 0; // loop = true; //
	 * setStake(getDefaultStake()); // Thread.sleep(60000); // // } else { //
	 * lostCount++; // // if(lostCount == 1) { //
	 * setStake(Utils.round(getStake()) * 10); // Thread.sleep(60000); // } //
	 * if(lostCount == 2) { // setStake(Utils.round(getStake()) * 2); //
	 * Thread.sleep(60000); // } // if(lostCount > 2) { //
	 * setStake(Utils.round(0.5)); // Thread.sleep(60000); // } // // } // //
	 * WebElement close = c.findElementById("close_confirmation_container"); //
	 * close.click(); // // WebElement amount = c.findElementById("amount"); //
	 * amount.clear(); //
	 * amount.sendKeys(Double.toString(Utils.round(getStake()))); //
	 * Thread.sleep(1000); // } // }
	 * 
	 * // public void trade(ChromeDriver c) throws InterruptedException { //
	 * WebElement balanceState = c // .findElementByCssSelector("#main-account >
	 * li > a > div.main-account > // div.topMenuBalance"); // //
	 * System.out.println(balanceState.getText()); // Calendar cal =
	 * Calendar.getInstance(); // SimpleDateFormat sdf = new
	 * SimpleDateFormat("HH:mm:ss"); //
	 * System.out.println(sdf.format(cal.getTime())); // boolean loop = true; //
	 * double balance = 200; // // while (loop) { // if (getProc(c, "top") > 100
	 * && getProc(c, "bottom") > 100) { // if (balance - getStake() * 2 <= 0) {
	 * // break; // } // String balance1Str = c //
	 * .findElementByCssSelector("#main-account > li > a > div.main-account > //
	 * div.topMenuBalance") // .getText().substring(1).replace(",", ""); //
	 * double balance1 = Double.parseDouble(balance1Str); // // WebElement
	 * purchaseTop = // c.findElementByCssSelector("#purchase_button_top"); //
	 * purchaseTop.click(); // Thread.sleep(1); // if
	 * (c.findElementById("confirmation_error").isDisplayed()) { // WebElement
	 * close = c.findElementById("close_confirmation_container"); //
	 * close.click(); // Thread.sleep(100); // purchaseTop.click(); // } //
	 * WebDriverWait waitClose1 = new WebDriverWait(c, 10); // WebElement close1
	 * = waitClose1 // .until(ExpectedConditions.elementToBeClickable(By.id(
	 * "close_confirmation_container"))); // // WebElement close = // //
	 * c.findElementById("close_confirmation_container"); // close1.click(); //
	 * // Thread.sleep(5000); // WebDriverWait waitBottom = new WebDriverWait(c,
	 * 10); // WebElement purchaseBottom = waitBottom //
	 * .until(ExpectedConditions.elementToBeClickable(By.cssSelector(
	 * "#purchase_button_bottom"))); // purchaseBottom.click(); //
	 * Thread.sleep(150); // if
	 * (c.findElementById("confirmation_error").isDisplayed()) { // WebElement
	 * close = c.findElementById("close_confirmation_container"); //
	 * close.click(); // Thread.sleep(200); // purchaseBottom.click(); // } //
	 * Thread.sleep(1000); // String balance2Str = c //
	 * .findElementByCssSelector("#main-account > li > a > div.main-account > //
	 * div.topMenuBalance") // .getText().substring(1).replace(",", ""); //
	 * double balance2 = Double.parseDouble(balance2Str); // // balance =
	 * balance - (balance1 - balance2); // System.out.println(
	 * "balance after stakes: " + Utils.round(balance)); // // WebDriverWait
	 * waitClose2 = new WebDriverWait(c, 10); // // WebElement close2 =
	 * waitClose2 // // // .until(ExpectedConditions.elementToBeClickable(By.id(
	 * "close_confirmation_container"))); // // close2.click(); //
	 * Thread.sleep(5000); // // while (true) { // Thread.sleep(5000); // if //
	 * (c.findElementById("contract_purchase_heading").getText().contains("This
	 * // contract")) { // WebElement close =
	 * c.findElementById("close_confirmation_container"); // close.click(); //
	 * break; // } // } // // Thread.sleep(6000); // balance1Str = c //
	 * .findElementByCssSelector("#main-account > li > a > div.main-account > //
	 * div.topMenuBalance") // .getText().substring(1).replace(",", ""); //
	 * balance1 = Double.parseDouble(balance1Str); // if (balance1 == balance2)
	 * { // // setStake(getStake() * 3); // // WebElement amount =
	 * c.findElementById("amount"); // // amount.clear(); // //
	 * amount.sendKeys(Double.toString(Utils.round(getStake()))); // //
	 * Thread.sleep(3000); // } // if (balance1 != balance2) { // balance =
	 * balance + (balance1 - balance2); // } // // System.out.println(
	 * "balance after profit/loss: " + Utils.round(balance)); //
	 * System.out.println("=========="); // Thread.sleep(30000); // } // // } //
	 * }
	 * 
	 * /*public void trade(ChromeDriver c) throws InterruptedException {
	 * WebElement balanceState = c .findElementByCssSelector(
	 * "#main-account > li > a > div.main-account > div.topMenuBalance");
	 * 
	 * System.out.println(balanceState.getText()); Calendar cal =
	 * Calendar.getInstance(); SimpleDateFormat sdf = new
	 * SimpleDateFormat("HH:mm:ss");
	 * System.out.println(sdf.format(cal.getTime())); boolean loop = true;
	 * 
	 * while (loop) { if (getProc(c, "top") > 100) {
	 * 
	 * WebElement purchaseTop =
	 * c.findElementByCssSelector("#purchase_button_top"); purchaseTop.click();
	 * Thread.sleep(500); if
	 * (c.findElementById("confirmation_error").isDisplayed()) { WebElement
	 * close = c.findElementById("close_confirmation_container"); close.click();
	 * Thread.sleep(500); purchaseTop.click(); } // WebDriverWait waitClose1 =
	 * new WebDriverWait(c, 10); // WebElement close1 = waitClose1 //
	 * .until(ExpectedConditions.elementToBeClickable(By.id(
	 * "close_confirmation_container")));
	 * 
	 * if (stakeWon(c)) { setStake(getDefaultStake()); } else {
	 * setStake(getStake() * 2); } WebElement close =
	 * c.findElementById("close_confirmation_container"); close.click();
	 * WebElement amount = c.findElementById("amount"); amount.clear();
	 * amount.sendKeys(Double.toString(getStake())); Thread.sleep(1000);
	 * 
	 * balanceState = c .findElementByCssSelector(
	 * "#main-account > li > a > div.main-account > div.topMenuBalance");
	 * 
	 * System.out.println(balanceState.getText());
	 * System.out.println("==============="); Thread.sleep(20000); } if
	 * (getProc(c, "bottom") > 100) { WebDriverWait waitBottom = new
	 * WebDriverWait(c, 10); WebElement purchaseBottom = waitBottom
	 * .until(ExpectedConditions.elementToBeClickable(By.cssSelector(
	 * "#purchase_button_bottom"))); purchaseBottom.click(); Thread.sleep(500);
	 * if (c.findElementById("confirmation_error").isDisplayed()) { WebElement
	 * close3 = c.findElementById("close_confirmation_container");
	 * close3.click(); Thread.sleep(500); purchaseBottom.click(); }
	 * 
	 * if (stakeWon(c)) { setStake(getDefaultStake()); } else {
	 * setStake(getStake() * 1.8); } WebElement close =
	 * c.findElementById("close_confirmation_container"); close.click();
	 * WebElement amount = c.findElementById("amount"); amount.clear();
	 * amount.sendKeys(Double.toString(getStake())); Thread.sleep(10000); } }
	 * 
	 * }
	 */

	public void trade(final ChromeDriver c1, final ChromeDriver c2)
			throws InterruptedException, BrokenBarrierException {
		WebElement balanceState = c1
				.findElementByCssSelector("#main-account > li > a > div.main-account > div.topMenuBalance");
		String balanceStateStr = balanceState.getText();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Utils.writeToFile(sdf.format(cal.getTime()));
		Utils.writeToFile("Start balance:  " +  balanceStateStr);
		
		final CyclicBarrier gate = new CyclicBarrier(3);
		while (true) {
			if (SeleniumTickTrade.getProc(c1, "top") > 100) {

				Thread tc1 = new Thread() {
					public void run() {
						try {
							gate.await();
							WebElement purchaseTop = c1.findElementByCssSelector("#purchase_button_top");
							purchaseTop.click();
							Thread.sleep(200);
							// WebElement close =
							// c1.findElementById("close_confirmation_container");
							// close.click();
							if (c1.findElementById("confirmation_error").isDisplayed()) {
								WebElement close1 = c1.findElementById("close_confirmation_container");
								close1.click();
								Thread.sleep(500);
								purchaseTop.click();
							}

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (BrokenBarrierException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				};

				Thread tc2 = new Thread() {
					public void run() {
						try {
							gate.await();
							WebElement purchaseTop = c2.findElementByCssSelector("#purchase_button_bottom");
							purchaseTop.click();
							Thread.sleep(200);
							if (c2.findElementById("confirmation_error").isDisplayed()) {
								WebElement close = c2.findElementById("close_confirmation_container");
								close.click();
								Thread.sleep(500);
								purchaseTop.click();
							}

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (BrokenBarrierException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				};

				tc1.start();
				tc2.start();
				gate.await();

				if (SeleniumTickTrade.stakeWon(c1)) {
					SeleniumTickTrade.setStake(SeleniumTickTrade.getDefaultStake());
					Utils.writeToFile("Top won");
					
				} else {
					Utils.writeToFile("Bottom won");
				}
				WebElement close = c1.findElementById("close_confirmation_container");
				close.click();
				close = c2.findElementById("close_confirmation_container");
				close.click();
				Thread.sleep(3000);
			}

		}
	}

	public static boolean stakeWon(ChromeDriver c) {
		WebElement conf = c.findElementByCssSelector("#contract_purchase_heading");
		boolean won = false;
		boolean notFound = true;
		while (notFound) {
			String status = conf.getText();

			if (status.contains("This contract")) {

				if (status.contains("won")) {

					won = true;
				}
				notFound = false;
			}
		}
		return won;
	}

	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		// Thread.sleep(35 * // minutes to sleep
		// 60 * // seconds to a minute
		// 1000);
		setProperty();

		ChromeDriver c1 = createDriver();
		ChromeDriver c2 = createDriver();
		
		login(c1, "", "");
		login(c2, "", "");

		double stake = 10;
		setTradeParams(c1, "8", Double.toString(stake));
		setTradeParams(c2, "8", Double.toString(stake));
		Thread.sleep(5000);

		SeleniumTickTrade trade = new SeleniumTickTrade(stake, 2.5, stake);
		trade.trade(c1, c2);

	}

	public static double getStake() {
		return stake;
	}

	public static void setStake(double d) {
		SeleniumTickTrade.stake = d;
	}

	public static double getIndex() {
		return index;
	}

	public static void setIndex(double index) {
		SeleniumTickTrade.index = index;
	}

	public static double getDefaultStake() {
		return defaultStake;
	}

	public static void setDefaultStake(double defaultStake) {
		SeleniumTickTrade.defaultStake = defaultStake;
	}

}
