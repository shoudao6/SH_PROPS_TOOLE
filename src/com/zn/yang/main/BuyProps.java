package com.zn.yang.main;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.zn.yang.net.Props;

public class BuyProps {
	
	private Image buyConfirmInfo;
	private Image bg;
	private Image buyBg;
	private Image buy_success;
	private Image buy_faile;
	private Image x;
	private Image[] num_buyProp;
	private Image tvb;
//	public boolean buyConfirm;
	
	private int bgX=150;
	private int bgY=120;
	private boolean showInfo;
	private Image anyKey;

	public BuyProps(){
		buyConfirmInfo=Tooles.loadImage("/charge/buyConfirmInfo.png");
		bg=Tooles.loadImage("/charge/bg.png");
		buyBg=Tooles.loadImage("/charge/buyBg.png");
		buy_success=Tooles.loadImage("/charge/buy_success.png");
		buy_faile=Tooles.loadImage("/charge/buy_faile.png");
		x=Tooles.loadImage("/charge/x.png");
		num_buyProp=new Image[10];
		Image once=Tooles.loadImage("/charge/num.png");
		for(int i=0;i<num_buyProp.length;i++){
			num_buyProp[i]=Image.createImage(once,10*i,0,10,15,0);
		}
		once=null;
		tvb=Tooles.loadImage("/charge/tvb.png");
		anyKey=Tooles.loadImage("/charge/anyKey.png");
		median=getMedian(SH_TOOLE.propNum);
		once=null;
		
	}
	public void removePic(){
		buyConfirmInfo=null;
		bg=null;
		buyBg=null;
		buy_success=null;
		buy_faile=null;
		tvb=null;
		anyKey=null;
	}
//	private int count=0;
	/**
	 * 获得道具数量是几位数
	 * @param num
	 * @return
	 */
	private int getMedian(int num){
		int count=0;
		while(num!=0){
			num/=10;
			count++;
		}
		return count;
	}
	private int median=0;
	public void showBuyProp(Graphics g){
//		while(Props.propPrice==null){
//			
//		}
		if(!showInfo){
			g.drawImage(bg, bgX-30, bgY, 0);
			g.drawImage(buyConfirmInfo, bgX+70, bgY+60, 0);
			Tooles.DrawNumber(num_buyProp, Props.propCoinPrice[SH_TOOLE.propType], 330, 228, 10, g);
			g.drawImage(SH_TOOLE.propName, 225, 248, 0);
			g.drawImage(x, 344-11*median, 253, 0);
			Tooles.DrawNumber(num_buyProp, SH_TOOLE.propNum, 345, 250, 10, g);
//			showButton(g);
		}else{
//			g.drawImage(bg, bgX-30, bgY, 0);
			g.drawImage(buyBg, bgX-30, bgY, 0);
			g.drawImage(SH_TOOLE.propName, 260, 175, 0);
			Tooles.DrawNumber(num_buyProp, Props.propCoinPrice[SH_TOOLE.propType], 260, 215, 10, g);
			g.drawImage(tvb, 290, 213, 0);
			g.drawImage(SH_TOOLE.propInfo, 246, 250, 0);
			
			
//			showButton(g);
			
		}
	}
	public void showBuySuccess(Graphics g){
		g.drawImage(bg, bgX-30, bgY, 0);
		g.drawImage(buy_success, bgX+120,bgY+120, 0);
//		g.drawImage(anyKey, 240, 375, 0);
//		int result =Integer.parseInt(Props.BUY_RESULT);
//		if(Props.BUY_RESULT!=null  && result>0){
//			g.setColor(-1);
//			g.drawString(Props.BUY_RESULT, 190, 190, 0);
//		}
	}
	
	public void showBuyFaile(Graphics g){
		g.drawImage(bg, bgX-30, bgY, 0);
		g.drawImage(buy_faile, bgX+120,bgY+120, 0);
//		g.setColor(0);
//		g.drawImage(anyKey, 240, 375, 0);
		int result =Integer.parseInt(Props.BUY_RESULT);
		if(Props.BUY_RESULT!=null  && result>0){
			g.setColor(-1);
			g.drawString(Props.BUY_RESULT, 190, 190, 0);
		}
	}
	public void setShowInfo(boolean b){
		showInfo=b;
	}
	public boolean getShowInfo(){
		return showInfo;
	}
	

}
