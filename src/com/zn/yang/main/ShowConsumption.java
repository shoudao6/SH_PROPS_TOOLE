package com.zn.yang.main;

import Constant;
import Draw;

import java.util.Vector;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

import com.zn.yang.net.Props;

/**
 * @author bugYang
 * @version 创建时间：2014年1月2日 下午2:53:53
 */
public class ShowConsumption {

	Image bg;
	Image[] bt_page;
	Image[] num;
	Image slash;

	String userId;
	String kindid;

	ConsumptionListenner listenner;

	public static Vector consumptionInfo;
	int[] position = { 205, 410 };
	int pid = 1;
	private int selectIndex;
	private Image[] bt_choose;
	public static int totalpage = 1;

	MIDlet mid;
	Displayable lastCanvas;

	public ShowConsumption(String kindid, String userid, boolean isTest,
			MIDlet mid, Displayable lastCanvas, ConsumptionListenner listenner) {
		SH_TOOLE.getConsumption(1, userid, 11, kindid);
		this.userId = userid;
		this.kindid = kindid;
		this.mid = mid;
		this.listenner=listenner;
		this.lastCanvas = lastCanvas;
		if (isTest) {
			consumptionInfo = new Vector();
			consumptionInfo
					.addElement(new String[] { "金币", "30", "2014-3-03" });
			consumptionInfo
					.addElement(new String[] { "流星镖", "30", "2014-3-03" });
			consumptionInfo
					.addElement(new String[] { "护腕", "20", "2014-3-03" });
		}
		if (bg == null) {
			bg = Draw.loadImage("/consumption/bg.png");
			bt_page = new Image[2];
			bt_choose = new Image[2];
			bt_page[0] = Draw.loadImage("/consumption/pageUp.png");
			bt_page[1] = Draw.loadImage("/consumption/pageDown.png");
			bt_choose[0] = Draw.loadImage("/consumption/on.png");
			bt_choose[1] = Draw.loadImage("/consumption/re.png");
			num = new Image[10];
			Image once = Draw.loadImage("/consumption/num.png");
			for (int i = 0; i < num.length; i++) {
				num[i] = Image.createImage(once, 10 * i, 0, 10, 15, 0);
			}
			slash = Image.createImage(once, 10 * 10, 0, 10, 15, 0);
			once = null;
		}
	}

	public void paint(Graphics g) {
		g.drawImage(bg, 0, 0, 0);
		showInfo(g);
		g.drawImage(bt_page[0], position[0], 515, g.BOTTOM | g.HCENTER);
		g.drawImage(bt_page[1], position[1], 515, g.BOTTOM | g.HCENTER);
		g.drawImage(bt_choose[1], position[selectIndex], 515, g.BOTTOM
				| g.HCENTER);

		Draw.DrawNumber(num, pid, 280, 495, 16, g);
		g.drawImage(slash, 310, 495, 0);
		Draw.DrawNumber(num, totalpage, 330, 495, 16, g);
	}

	Font myFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,
			Font.SIZE_SMALL);

	public void showInfo(Graphics g) {
		if (consumptionInfo == null){
			System.out.println("consumptionInfo="+consumptionInfo);
			return;
		}
		for (int i = 0; i < consumptionInfo.size(); i++) {
			String[] str = new String[3];
			str = (String[]) consumptionInfo.elementAt(i);
			g.setFont(myFont);
			g.drawString(str[0], 100, 115 + 32 * i, 0);
			g.drawString(str[1], 260 ,115 + 32 * i, 0);
			g.drawString("TV币", 280, 115 + 32 * i, 0);
			g.drawString(str[2], 400, 115 + 32 * i, 0);
		}
	}

	public void removeResource() {
		bg=null;
		for(int i=0;i<bt_page.length;i++){
			bt_page[i]=null;
			bt_choose[i]=null;
		}
		bt_page=null;
		bt_choose=null;
		for(int i=0;i<num.length;i++){
			num[i]=null;
		}
		num=null;
		slash=null;
		if(consumptionInfo!=null){
		consumptionInfo.removeAllElements();
		consumptionInfo=null;
		}
		
		System.gc();
	}

	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case Constant.KEY_BACK:
		case Constant.KEY_0:
			backToMenu();
			break;
		case Constant.KEY_LEFT:
			selectIndex = 0;
			break;
		case Constant.KEY_RIGHT:
			selectIndex = 1;
			break;
		case Constant.KEY_FIRE:
			switch (selectIndex) {
			case 0:
				if (pid > 1) {
					pid -= 1;
					SH_TOOLE.getConsumption(pid, userId, 11, kindid);
				}
				break;
			case 1:
				if (pid < totalpage) {
					pid += 1;
					SH_TOOLE.getConsumption(pid, userId, 11, kindid);
				}
				break;
			}

			break;
		}
	}

//	public void logic() {
//		if (Props.isGetConsumption) {
//			resetData();
//			Props.isGetConsumption = false;
//		}
//	}

//	private void resetData() {
//		totalpage = SH_TOOLE.getConsumptionTotalPage();
////		consumptionInfo = SH_TOOLE.getConsumptionInfo();
//	}

	private void backToMenu() {
		removeResource();
		if(SH_TOOLE.beRun){
			SH_TOOLE.beRun=false;
		}
		listenner.consumptionListenner(SH_TOOLE.RESULT_BACK_TO_MENU);
		Display.getDisplay(mid).setCurrent(lastCanvas);
	}

}
