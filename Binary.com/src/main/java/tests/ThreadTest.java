package tests;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import selenium.SeleniumTickTrade;

public class ThreadTest {

	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		// TODO Auto-generated method stub
		SeleniumTickTrade.setProperty();
		double stake = 0.5;
		

		final ChromeDriver c1 = new ChromeDriver();
		final ChromeDriver c2 = new ChromeDriver();

		final CyclicBarrier gate = new CyclicBarrier(3);
		
		SeleniumTickTrade.login(c1, "", "");
		SeleniumTickTrade.login(c2, "", "");

		SeleniumTickTrade.setTradeParams(c1, "8", Double.toString(stake));
		SeleniumTickTrade.setTradeParams(c2, "8", Double.toString(stake));

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
					System.err.println("top won");
				} else {
					System.out.println("bottom won");
				}
				WebElement close = c1.findElementById("close_confirmation_container");
				close.click();
				close = c2.findElementById("close_confirmation_container");
				close.click();
				Thread.sleep(5000);

			}

		}

	}
}
