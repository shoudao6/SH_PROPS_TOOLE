package com.zn.yang.main;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class MyMidlet extends MIDlet {
	
	SH_TOOLE st;
	private Image arrow;
	private Image arrowInfo;
	

	public MyMidlet() {
		st=new SH_TOOLE(this, "P10172", null, SH_TOOLE.NORMAL);
		arrow=Tooles.loadImage("/charge/arrow.png");
		arrowInfo=Tooles.loadImage("/charge/arrowInfo.png");
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		st.do_buyProps(arrow, arrowInfo, 2,3);
//		st.startThread();

	}

}
