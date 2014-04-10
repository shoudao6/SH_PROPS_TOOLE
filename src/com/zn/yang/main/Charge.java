package com.zn.yang.main;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.zn.yang.net.Props;

public class Charge {
	
	
	
	private Image confirm;
	private Image secondConfirm;
	private Image bg;
	public boolean chargeConfirm;
	
	private int bgX=150;
	private int bgY=120;
	private Image[] num_charge;
	private Image chargeFaile;
	private Image anyKey;
	private Image chargeSuccess;

	public Charge(){
		bg=Tooles.loadImage("/charge/bg.png");
		confirm=Tooles.loadImage("/charge/confirm.png");
		secondConfirm=Tooles.loadImage("/charge/secondConfirm.png");
		num_charge=new Image[10];		Image once=Tooles.loadImage("/charge/num.png");
		for(int i=0;i<num_charge.length;i++){
			num_charge[i]=Image.createImage(once,10*i,0,10,15,0);
		}
		chargeFaile=Tooles.loadImage("/charge/chargeFaile.png");
		chargeSuccess=Tooles.loadImage("/charge/chargeSuccess.png");
		anyKey=Tooles.loadImage("/charge/anyKey.png");
	}
	public void removePic(){
		bg=null;
		confirm=null;
		secondConfirm=null;
		chargeFaile=null;
		anyKey=null;
		chargeSuccess=null;
	}
	
	public void showCharge(Graphics g){
		if(chargeConfirm){
			g.drawImage(bg, bgX-30, bgY, 0);
			g.drawImage(secondConfirm, bgX+30, bgY+50, 0);
			Tooles.DrawNumber(num_charge, Tooles.getRMB(SH_TOOLE.propType), 315, 195, 10, g);
			Tooles.DrawNumber(num_charge, Tooles.getRMB(SH_TOOLE.propType)*10, 265, 215, 10, g);
			Tooles.DrawNumber(num_charge, Tooles.getRMB(SH_TOOLE.propType), 320, 240, 10, g);
			Tooles.DrawNumber(num_charge, Tooles.getRMB(SH_TOOLE.propType)*10, 300, 265, 10, g);
			Tooles.DrawNumber(num_charge, Tooles.getRMB(SH_TOOLE.propType), 300, 290, 10, g);
		}else{
			g.drawImage(bg, bgX-30, bgY, 0);
			g.drawImage(confirm, bgX+30, bgY+50, 0);
			g.drawImage(SH_TOOLE.propName, 290, 215, 0);
			Tooles.DrawNumber(num_charge, Tooles.getRMB(SH_TOOLE.propType)*10, 310, 240, 10, g);
			Tooles.DrawNumber(num_charge, Tooles.getRMB(SH_TOOLE.propType), 430, 240, 10, g);
			
		}
	}
	public void showChargeFaile(Graphics g){
		g.drawImage(bg, bgX-30, bgY, 0);
		g.drawImage(chargeFaile, bgX+120,bgY+120, 0);
//		g.setColor(0);
//		g.drawString("请按任意键回到游戏", 260, 375, 0);
//		g.drawImage(anyKey, 240, 375, 0);
//		int result =Integer.parseInt(Props.CHARGE_RESULT);
		if(Props.CHARGE_RESULT!=null){
			g.setColor(-1);
			g.drawString(Props.CHARGE_RESULT, 190, 190, 0);
		}
		
	}
	public void showChargeSuccess(Graphics g){
		g.drawImage(bg, bgX-30, bgY, 0);
		g.drawImage(chargeSuccess, bgX+120,bgY+120, 0);
	}

	
}
