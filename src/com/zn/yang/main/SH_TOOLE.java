package com.zn.yang.main;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

import com.zn.yang.net.Props;

/**
 * 
 * @author bugYang
 * @version 创建时间：2013年12月9日 上午11:41:52
 */
public class SH_TOOLE extends Canvas implements PropsListenner,Runnable {

	public static final int NULL = 0;
	/**购买道具界面 */
	public static final int BUY_PROP = 1;
	/**充值界面 */
	public static final int BUY_COIN = 2;
	/**购买成功界面 */
	private final static int BUY_SUCCESS = 3;
	/**购买失败界面 */
	private final static int BUY_FAILE = 4;
	/**充值失败界面 */
	private static final int CHARGE_FAILE = 5;
	/**显示消费查询 */
	private static final int SHOW_CONSUMPTION=6;
	/** 充值成功*/
	private static final int CHARGE_SUCCESS=7;
	// private static final int SHOW_ERROR=6;

	public static Image propName;
	public static Image propInfo;
	private MIDlet mid;
	private SHListenner listenner;
	private int testType;
	private int selectIndex;
	private int state;

	private BuyProps buyProps;
	private Charge charge;

	private String UserID;

	private int[] buyPropPosition = { 195, 375 };
	public static int propType;
	// private boolean buyConfirm;
	private Displayable lastCanvas;
	public static int propNum;
	private Image button;
	private Image yes;
	private Image no;
	private Image buyPropSelected;
	private boolean keyLock;
	private ConsumptionListenner consumptionListenner;
	private ShowConsumption consumption;

	public final static int TEST_RETURN_SUCCESS = 0;
	public final static int TEST_RETURN_FAILE = 1;
	public final static int NORMAL = 2;

	public final static int RESULT_SUCCESS = 0;
	public final static int RESULT_FAILE = 1;
	public final static int RESULT_CANCEL = 2;
	public final static int RESULT_BACK_TO_MENU=3;

	public SH_TOOLE(MIDlet mid, String kindid, SHListenner listenner,
			int testType) {
		this.mid = mid;
		this.listenner = listenner;
		this.setFullScreenMode(true);
		this.testType = testType;
		Props.kindid = kindid;
		UserID = mid.getAppProperty("userId");
		Props.tempKey = mid.getAppProperty("tempKey");
		Props.serverurl = mid.getAppProperty("serverUrl");
		if (UserID == null) {
			UserID = "13000056";
		}
		if (Props.tempKey == null) {
			Props.tempKey = "e94f9bb7fcf54405a43c0a612ca5dc73";
		}
		selectIndex = Tooles.BUTTON_NO;
		state = BUY_PROP;

		new Props().getPropID();
		getCoin(UserID);
		// new Props(UserID).getCoin();
		button = Tooles.loadImage("/charge/button.png");
		yes = Tooles.loadImage("/charge/yes.png");
		no = Tooles.loadImage("/charge/no.png");
		buyPropSelected = Tooles.loadImage("/charge/buyPropSelected.png");

		// startThread();

		System.out.println("-----------------------");
		System.out.println("|                                 |");
		System.out.println("|   SH_Version_1.2.6 |");
		System.out.println("|                                 |");
		System.out.println("-----------------------");
	}
	
	public SH_TOOLE(MIDlet mid,ConsumptionListenner listenner,String kindid,boolean isTest){
		if(this.mid==null){
			this.mid=mid;
		}
		this.setFullScreenMode(true);
		UserID = mid.getAppProperty("userId");
		if (UserID == null) {
			UserID = "13000056";
		}
//		consumptionListenner=listenner;
		beRun=true;
		state=SHOW_CONSUMPTION;
		lastCanvas = Display.getDisplay(mid).getCurrent();
		consumption=new ShowConsumption(kindid,UserID,isTest,mid,lastCanvas,listenner);
		Display.getDisplay(mid).setCurrent(this);
		new Thread(this).start();
	}

	private void removePic() {
		if (buyProps != null)
			buyProps.removePic();
		if (charge != null)
			charge.removePic();
	}

	/**
	 * 设置道具id，道具人民币价格，道具tv币价格默认值
	 * 
	 * @param propsid
	 * @param propsPrice
	 * @param propsCoinPrice
	 */
	public static void setProps_ID_Price_CoinPrice(String[] propsid,
			int[] propsPrice, int[] propsCoinPrice) {
		if(Props.propCoinPrice==null){
			Props.propid = propsid;
			Props.propPrice = propsPrice;
			Props.propCoinPrice = propsCoinPrice;
		}
		
	}

	// public void setKindid(String kindid){
	// Props.kindid=kindid;
	// }
	public void getCoin(String userid) {
		new Props(userid, this).getCoin();
		System.out.println("fuck---------");
	}

	public void do_buyProps(Image propName, Image propInfo, int propType,
			int propNum) {
		SH_TOOLE.propName = propName;
		SH_TOOLE.propInfo = propInfo;
		SH_TOOLE.propType = propType;
		SH_TOOLE.propNum = propNum;
		getCoin(UserID);
		if (buyProps == null) {
			buyProps = new BuyProps();
		}
		lastCanvas = Display.getDisplay(mid).getCurrent();
		Display.getDisplay(mid).setCurrent(this);

	}

	protected void paint(Graphics g) {
		switch (state) {
		case BUY_PROP:
			buyProps.showBuyProp(g);
			showButton(g);
			break;
		case BUY_SUCCESS:
			buyProps.showBuySuccess(g);
			break;
		case BUY_FAILE:
			buyProps.showBuyFaile(g);
			break;
		case BUY_COIN:
			charge.showCharge(g);
			showButton(g);
			break;
		case CHARGE_FAILE:
			charge.showChargeFaile(g);
			break;
		case CHARGE_SUCCESS:
			charge.showChargeSuccess(g);
		case SHOW_CONSUMPTION:
			consumption.paint(g);
			break;
		}
		// g.drawString("state="+state, 400, 130, 0);
		// if(Props.Coin.equals("-1")){
		// g.setColor(0);
		// g.drawString("失败:  "+Props.lastResult, 240, 130, 0);
		// }else{
		// g.setColor(0);
		// g.drawString("成功:  "+Props.lastResult, 240, 130, 0);
		// }
//		if(Props.BUYCOIN!=null){
//			g.setColor(-1);
//			g.drawString(Props.BUYCOIN_1, 20, 110, 0);
//			g.drawString(Props.BUYCOIN, 20, 130, 0);
//		}
		if(Props.Error!=null){
			g.setColor(-1);
			g.drawString(Props.Error, 160, 130, 0);
		}
	}
	

	public void keyPressed(int keyCode) {
		if (keyLock) {
//			repaint();
			return;
		}
		switch (state) {
		case BUY_PROP:
			buyPropKeyPressed(keyCode);
			break;
		case BUY_COIN:
			chargeKeyPressed(keyCode);
			break;
//		case BUY_SUCCESS:
//		case BUY_FAILE:
//		case CHARGE_FAILE:
//			successOrFaileKeyPressed(keyCode);
//			break;
		case SHOW_CONSUMPTION:
			consumption.keyPressed(keyCode);
			break;
		}
		repaint();
	}

	private void successOrFaileKeyPressed(int keyCode) {
		System.out.println("fuck");
		t.cancel();
		if (state == BUY_SUCCESS) {
			backToGame(RESULT_SUCCESS);
		} else {// if(state==BUY_FAILE){
			backToGame(RESULT_FAILE);
		}
	}

	private void buyPropKeyPressed(int keyCode) {

		switch (keyCode) {
		case Tooles.KEY_LEFT:
			if (selectIndex != Tooles.BUTTON_YES) {
				selectIndex = Tooles.BUTTON_YES;
			}
			break;
		case Tooles.KEY_RIGHT:
			if (selectIndex != Tooles.BUTTON_NO) {
				selectIndex = Tooles.BUTTON_NO;
			}
			break;
		case Tooles.KEY_FIRE:
			if(keyLock){
				return;
			}
			if (selectIndex == Tooles.BUTTON_YES) {
				if (!buyProps.getShowInfo()) {
					switch (testType) {
					case TEST_RETURN_SUCCESS:
						// state=BUY_SUCCESS;
						// autoClose();
						buyProps.setShowInfo(true);
						selectIndex = Tooles.BUTTON_NO;
						break;
					case TEST_RETURN_FAILE:
						state = BUY_FAILE;
						autoClose();
						break;
					case NORMAL:
						int price = Integer.parseInt(Props.Coin);
						if (price >= Props.propCoinPrice[propType]) {
							buyProps.setShowInfo(true);
							selectIndex = Tooles.BUTTON_NO;
						} else {
//							keyLock = false;
							state = BUY_COIN;
							if (charge == null) {
								charge = new Charge();
							}
							selectIndex = Tooles.BUTTON_NO;
						}

						break;
					}
				} else {
					switch (testType) {
					case TEST_RETURN_SUCCESS:
						state = BUY_SUCCESS;
						autoClose();
						break;
					case TEST_RETURN_FAILE:
						state = BUY_FAILE;
						autoClose();
						break;
					default:
						keyLock = true;
						new Props(UserID, this).buyProp(propType);
						selectIndex = Tooles.BUTTON_NO;
//						state = BUY_FAILE;
//						autoBuyProp();
						break;
					}
				}
			} else {
				backToGame(RESULT_CANCEL);
			}
			break;
		}
	}

	private void chargeKeyPressed(int keyCode) {
		switch (keyCode) {
		case Tooles.KEY_LEFT:
			if (selectIndex != Tooles.BUTTON_YES) {
				selectIndex = Tooles.BUTTON_YES;
			}
			break;
		case Tooles.KEY_RIGHT:
			if (selectIndex != Tooles.BUTTON_NO) {
				selectIndex = Tooles.BUTTON_NO;
			}
			break;
		case Tooles.KEY_FIRE:
			if(keyLock){
				return;
			}
			if (selectIndex == Tooles.BUTTON_YES) {
				if (charge.chargeConfirm) {
					keyLock = true;
					System.out.println("888888888888");
					new Props(UserID, this).buyCoin(Tooles.getRMB(propType),propType);
//					state = CHARGE_SUCCESS;
//					autoBuyProp();
				} else {
					charge.chargeConfirm = true;
					selectIndex = Tooles.BUTTON_NO;
				}
			} else {
				backToGame(RESULT_CANCEL);
			}
			break;
		}
	}

	private void showButton(Graphics g) {
		g.drawImage(button, 200, 365, 0);
		g.drawImage(yes, 215, 370, 0);
		g.drawImage(button, 380, 365, 0);
		g.drawImage(no, 395, 370, 0);
		g.drawImage(buyPropSelected, buyPropPosition[selectIndex], 363, 0);
	}

	Timer t;
	private String error;

	private void autoClose() {
		// Timer t = new Timer();
		t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				if (state == BUY_SUCCESS) {
					backToGame(RESULT_SUCCESS);
				} else {// if(state==BUY_FAILE){
					backToGame(RESULT_FAILE);
				}
			}
		}, 3000);
	}
	private void autoBuyProp(){
		t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				keyLock=false;
				state=BUY_PROP;
				repaint();
			}
		}, 2000);
	}
	private void autoCharge(){
		t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				keyLock=false;
				state=BUY_COIN;
				repaint();
			}
		}, 2000);
	}

	private void backToGame(int type) {
		try {
			reset();
		} catch (Exception e) {
			error = "清空资源出错了！！！";
		}

		listenner.ResultState(type, propType);
		Display.getDisplay(mid).setCurrent(lastCanvas);
	}

	private void reset() {
		state = BUY_PROP;
		selectIndex = Tooles.BUTTON_NO;
		removePic();
		buyProps = null;
		charge = null;
		error = null;
		t = null;
		System.gc();
	}

	/**
	 * 获得道具tv币价格
	 * 
	 * @param index
	 * @return
	 */
	public static int getPropPrice(int index) {
		return Props.propCoinPrice[index];
	}

	/**
	 * 发起查询消费信息协议
	 * 
	 * @param pageid
	 */
	public static void getConsumption(int pageid, String userid, int num,
			String kindid) {
		new Props(userid).getConsumption(pageid, num, kindid);
	}

	/**
	 * 获得消费信息总页数
	 * 
	 * @return
	 */
//	public static int getConsumptionTotalPage() {
//		return Props.totalpage;
//	}

	/**
	 * 获得消费总信息
	 * 
	 * @return
	 */
//	public static Vector getConsumptionInfo() {
//		return Props.consumptionInfo;
//	}

	long runStartTime;
	long spendTime;
	public static boolean beRun;

	public void ResultState(int resultState) {
		switch (resultState) {
		case Props.BUY:
			if (Props.prop_result != 0) {
				switch (Props.prop_result) {
				case 1:
					Props.prop_result = 0;
					state = BUY_SUCCESS;
					// getCoin(UserID);
					autoClose();
					keyLock = false;
					break;
				case 2:
					Props.prop_result = 0;
					keyLock = false;
					state = BUY_COIN;
					if (charge == null) {
						charge = new Charge();
					}
					break;
				case 3:
					Props.prop_result = 0;
					System.out.println("faile-----------------");
					state = BUY_FAILE;
					autoBuyProp();
//					autoClose();
//					keyLock = false;
					break;
				}
				Props.prop_result = 0;
			}
			break;
		case Props.CHARGE:
			if (Props.coin_result != 0) {
				switch (Props.coin_result) {
				case 1:
					keyLock=true;
					Props.coin_result = 0;
					state=CHARGE_SUCCESS;
					getCoin(UserID);
					autoBuyProp();
//					new Props(UserID, this).buyProp(propType);
					// Props.prop_result=1;
					break;
				case 2:
					Props.coin_result = 0;
					break;
				case 3:
					Props.coin_result = 0;
					state = CHARGE_FAILE;
					autoCharge();
//					autoClose();
//					keyLock = false;
					break;
				}
				Props.coin_result = 0;
			}
			break;
		}
		repaint();
	}

	public void run() {
		while(beRun){
			if(Props.isGetConsumption){
				repaint();
//				Props.isGetConsumption=false;
				System.out.println("isGetConsumption--------------->");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
