package com.zn.yang.main;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.zn.yang.net.Props;

public class Tooles {
	/**
	 * ��Ϸ��������
	 */
	public final static int KEY_0 = 48;
	public final static int KEY_1 = 49;
	public final static int KEY_2 = 50;
	public final static int KEY_3 = 51;
	public final static int KEY_4 = 52;
	public final static int KEY_5 = 53;
	public final static int KEY_6 = 54;
	public final static int KEY_7 = 55;
	public final static int KEY_8 = 56;
	public final static int KEY_9 = 57;

	public final static int KEY_UP = -1;
	public final static int KEY_DOWN = -2;
	public final static int KEY_LEFT = -3;
	public final static int KEY_RIGHT = -4;
	public final static int KEY_FIRE = -5;

	public final static int KEY_BACK = -31;
	public final static int KEY_RETURN = -7;

	public final static Font FONT_BOLD_LARGE = Font.getFont(Font.FACE_SYSTEM,
			Font.STYLE_BOLD, Font.SIZE_LARGE);
	
	public final static int BUTTON_YES=0;
	public final static int BUTTON_NO=1;

	/**
	 * ����ͼƬ��Դ
	 */
	public static Image GetImageSource(String path) {
		Image getimg = null;
		try {
			getimg = Image.createImage(path);
		} catch (IOException e) {
			System.out.println("��ȡͼƬʧ��" + path);
		}
		return getimg;
	}
	private static Image getSerImage(String URL) {
//		URL = getHttpUrl()+URL+".png";
//		System.out.println(URL);
		HttpConnection httpConn = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		Image image = null;
		byte[] data = null;
		try {
			httpConn = (HttpConnection) Connector.open(URL);
			int responseCode;
			if ((responseCode = httpConn.getResponseCode()) == HttpConnection.HTTP_OK) {
				is = httpConn.openInputStream();

				int len = (int) httpConn.getLength();
				if (len <= 0) {
					return null;
				}
				//��һ�ַ�ʽ
				int actual = 0;
				int bytesread = 0;
				data = new byte[len];
				do {
					actual = is.read(data, bytesread, len - bytesread);
					bytesread += actual;
					if (bytesread == len)
						break;
				} while (actual != -1);
				image = Image.createImage(data, 0, data.length);
				System.out.println("����"+URL+"ͼƬ��ɡ���");
				
				//�ڶ��ַ�ʽ
//				baos = new ByteArrayOutputStream();
//				int ch = 0;
//				while ((ch = is.read()) != -1) {
//					baos.write(ch);
//				}
//				byte[] imageData = baos.toByteArray();
//				baos.flush();
//				image = Image.createImage(imageData, 0, imageData.length);
//				System.out.println("����"+URL+"ͼƬ��ɡ���");
				
			} else {
				System.out.println("response code:" + responseCode);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null)
					baos.close();
				if (is != null)
					is.close();
				if (httpConn != null)
					httpConn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return image;
	}
	/**
	 * ��÷�����ͼƬ
	 * @param url
	 * @return
	 */
	public static Image getServerImage(String url){
		Image img=null;
		img=getSerImage(url);
		if(img==null){
			img=getSerImage(url);
		}
		return img;
	}

	/**
	 * ���Զ���ͼ�ֿ��������
	 * 
	 * @param g
	 * @param img
	 *            �ֿ�ͼ
	 * @param str
	 *            Ҫ���Ƶ��ַ���
	 * @param fontStoreroom
	 *            �ֿ��ַ���
	 * @param x
	 *            ���Ƶ�xλ��
	 * @param y
	 *            ���Ƶ�yλ��
	 * @param clipW
	 *            ͼ�Ͳü����(ÿ����)
	 * @param clipH
	 *            ͼ�Ͳü��߶�(ÿ����)
	 * @param anchor
	 *            �Ե�ǰһ�����ֽ���ê��
	 * @param space
	 *            ÿ�����ֵĿ�϶
	 * @param numDigit
	 *            �����ֽ��в�0 ,�����Ϊ6����Ϊ1��ôӦ�û���Ϊ000001
	 * @param color
	 *            ͼƬ��û������ַ���ֱ��drawChar����ɫ
	 */
	public static void drawString(Graphics g, Image img, String str,
			String fontStoreroom, int x, int y, int anchor, int space,
			int numDigit, int color) {
		int clipW = img.getWidth()/fontStoreroom.length();
		int clipH = img.getHeight();
		StringBuffer sb = new StringBuffer();
		sb.append(str);
		for (int i = sb.length(); i < numDigit; i++) {
			sb.insert(0, "0");// maxNumDigit �������Ļ���0 ��1 == 001
		}
		int charId;
		str = sb.toString();
		int strLength = str.length();
		int offset = getAnchorOffset(anchor, strLength * clipW + space, clipH);
		x -= (offset & 0xffff0000) >> 16;
		y -= (offset & 0xffff);
		for (int i = 0; i < strLength; i++, x += clipW + space) {
			charId = fontStoreroom.indexOf(str.charAt(i));
			if (charId == -1) {
				g.setClip(x, y, clipW, FONT_BOLD_LARGE.getHeight());
				g.setColor(color);
				g.drawChar(str.charAt(i), x, y, 0);
				// continue;
			} else {
				g.setClip(x, y, clipW, clipH);
				g.drawImage(img, x - charId * clipW, y, 0);
			}
		}
		g.setClip(0, 0, 640, 530);
	}

	/**
	 * �Զ����ê�㷽��. ��Ϊ����ͼ�ͺ����ֽ���ê�� �˷�����֧��Graphics.BASELINE��Ϊ transform���� �÷�int
	 * offsetXY = getAnchorOffset(anchor,transform,width,height); x_dest -=
	 * (short)((offsetXY >> 16) & 0xffff); y_dest -= (short)(offsetXY & 0xffff);
	 * 
	 * @param anchor
	 *            Graphics���ê��ֵ
	 * @param w
	 *            ����Ŀ��
	 * @param h
	 *            ����ĸ߶�
	 * @return һ��intֵ�������ê����λ��ƫ������
	 */
	public static int getAnchorOffset(int anchor, int w, int h) {
		int offX = 0;
		int offY = 0;
		if ((anchor & Graphics.BASELINE) != 0)
			throw new java.lang.IllegalArgumentException();
		if ((anchor & Graphics.HCENTER) != 0) {
			offX = w >> 1;
		} else if ((anchor & Graphics.RIGHT) != 0) {
			offX = w;
		}
		if ((anchor & Graphics.VCENTER) != 0) {
			offY = h >> 1;
		} else if ((anchor & Graphics.BOTTOM) != 0) {
			offY = h;
		}
		return (offX << 16) | offY;
	}// :~QN
	
	
	/**
	 * ������ͼƬ
	 * 
	 * @param imagea_number
	 * @param x
	 * @param y
	 * @param offer
	 * @param canvas
	 */
	public static void DrawNumber(Image[] imagea_number, int key,
			int x, int y, int offer, Graphics g) {
		if(key<0)return;
		String strkey = String.valueOf(key);
		byte strlenght = (byte) strkey.length();//
		byte[] bytea_str = new byte[strlenght];// �õ�����int�͵ĳ���
		for (int a = 0; a < strlenght; a++) {
			bytea_str[a] = (byte) Integer.parseInt(strkey.substring(a, a + 1));
		}
		for (int a = 0; a < strlenght; a++) {
			g.drawImage(imagea_number[bytea_str[(strlenght - a - 1)]], x
					- a * offer, y, Graphics.LEFT | Graphics.TOP);
		}
	}
	/**
	 * ����ͼƬ
	 */
	public static Image loadImage(String path){
		Image img=null;
		try {
			img=Image.createImage(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return img;
	}
	/**
	 * ���������õ��������ȼ۸�
	 * @param index
	 * @return
	 */
	public static int getRMB(int index){
		int RMB=0;
		if(Props.propCoinPrice[index]%10!=0){
			RMB=Props.propCoinPrice[index]/10+1;
		}else{
			RMB=Props.propCoinPrice[index]/10;
		}
		return RMB;
	}
	
	public static byte[] getData(String URL) {
		HttpConnection httpConn = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		byte[] data = null;
		try {
			httpConn = (HttpConnection) Connector.open(URL);
			int responseCode;
			if ((responseCode = httpConn.getResponseCode()) == HttpConnection.HTTP_OK) {
				is = httpConn.openInputStream();

				int len = (int) httpConn.getLength();
				if (len <= 0) {
					return null;
				}
				//��һ�ַ�ʽ
				int actual = 0;
				int bytesread = 0;
				data = new byte[len];
				do {
					actual = is.read(data, bytesread, len - bytesread);
					bytesread += actual;
					if (bytesread == len)
						break;
				} while (actual != -1);
//				image = Image.createImage(data, 0, data.length);
//				System.out.println("����"+URL+"ͼƬ��ɡ���");
				
				//�ڶ��ַ�ʽ
//				baos = new ByteArrayOutputStream();
//				int ch = 0;
//				while ((ch = is.read()) != -1) {
//					baos.write(ch);
//				}
//				byte[] imageData = baos.toByteArray();
//				baos.flush();
//				image = Image.createImage(imageData, 0, imageData.length);
//				System.out.println("����"+URL+"ͼƬ��ɡ���");
				
			} else {
				System.out.println("response code:" + responseCode);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null)
					baos.close();
				if (is != null)
					is.close();
				if (httpConn != null)
					httpConn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
}
