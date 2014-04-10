package com.zn.yang.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import Net;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Image;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.zn.yang.main.PropsListenner;
import com.zn.yang.main.ShowConsumption;

public class Props extends Thread {

	public static final int NULL = 0;
	public static final int BUY = 1;
	public static final int CHARGE = 2;
	public static final int GET_PRICE = 3;
	public static final int GET_CONSUMPTION = 4;

	private final String APP_KEY = "bH2i2F3h58cj9bfl9A2baSeQ0A7Z7p0T8seC36aT9Cbn8jcn8z3DcH3S5g6U9Z3D";

	public String appid = "114";
	public static String userToken;
	public static String tempKey;// ="e94f9bb7fcf54405a43c0a612ca5dc73";
	public static String fixedKey = "B2C85F3ACA2956D6";
	public static final String COMPANYURL = "http://222.68.195.207:9001/www.iptvtest.com/";// 上海

	public static String serverurl;
	private String submit_url;
	private String last_url;
	public String buyUrl;

	private HttpConnection hc;

	// public static String lastResult="null";

	public static int once = 0;

	// private Thread thread;

	private int type;

	private String iptvid;

	private md5 md5class;
	public static String Coin = "-1";
	public static boolean isGetConsumption;
	// public static int totalpage;
	// public static Vector consumptionInfo;

	// public static long StartTime;

	public static int buyOrCharge;
	public static String kindid;//
	// public static String[] propid = { "DJ11357", "DJ11358",
	// "DJ11359","DJ11360","DJ11361","DJ11362",
	// "DJ11363","DJ11364","DJ11365","DJ11366","DJ11367","DJ11368" };//
	// public static float[] propPrice={6,6,5,5,5,5,4,3,3,3,3,5};//
	public static int[] propCoinPrice = { 6, 6, 5, 5, 5, 5, 4, 3, 3, 3, 3, 5 };//
	public static String[] propid;
	public static int[] propPrice;
	// public static int [] propCoinPrice;

	public static int coin_result;//
	public static int prop_result;//
	private int coinprice;//
	private int buy_propid;
	private PropsListenner listenner;
	private int buyCoinID;
	private int pid;
	private int pagesize;
	public static String BUYCOIN_1;
	public static String BUYCOIN;

	// private Thread thread;
	// public static String buypropStr;

	// public static long getCoinStartTime;

	public Props(String iptvid, PropsListenner listenner) {
		this.listenner = listenner;
		this.iptvid = iptvid;
		md5class = new md5();
	}

	public Props() {
		md5class = new md5();
	}

	public Props(String iptvid) {
		this.iptvid = iptvid;
		md5class = new md5();
	}

	public void cleanAll() {
		submit_url = null;
		last_url = null;
		hc = null;
		// thread = null;
	}

	/** 获得道具相关信息 */
	public void getPropID() {
		// buyOrCharge=GET_PRICE;
		type = 4;
		// StartTime=System.currentTimeMillis();
		submit_url = "kindid=" + kindid;
		last_url = getUrl("iptv.props.get.php", submit_url);
		System.out.println("getPropIDlast_url=" + last_url);
		// if(thread!=null){
		// thread=null;
		// }
		// thread=new Thread(this);
		// thread.start();
		new Thread(this).start();
	}

	/** 购买道具 */
	public void buyProp(int id) {
		if (once == 0) {
			once = 1;
		} else {
			return;
		}
		buyOrCharge = BUY;
		type = 2;
		// StartTime=System.currentTimeMillis();
		buy_propid = id;
		submit_url = "account=" + iptvid + "&tempkey=" + tempKey + "&prodCode="
				+ kindid + "&propsCode=" + propid[id] + "&price="
				+ propPrice[id] + "&serverurl=" + serverurl;
		System.out.println("prop------------:" + submit_url);
		last_url = getUrl("iptv.consumer.php", submit_url);
		// buypropStr=submit_url;
		Error = "buyProp---->";
		new Thread(this).start();
	}

	/** 查询余额 */
	public void getCoin() {
		type = 1;
		submit_url = "account=" + iptvid + "&tempkey=" + tempKey + "&kindid="
				+ kindid + "&serverurl=" + serverurl;
		last_url = getUrl("iptv.gamegold.Inquiry.php", submit_url);
		new Thread(this).start();
	}

	/** 充值 */
	public void buyCoin(int price, int id) {
		if (once == 0) {
			once = 1;
		} else {
			return;
		}
		buyOrCharge = CHARGE;
		type = 3;
		// StartTime=System.currentTimeMillis();
		coinprice = price;
		buyCoinID = id;
		submit_url = "userid=" + iptvid + "&price="
				+ price// + //"&pwd=" + pwd
				+ "&tempkey=" + tempKey + "&kindid=" + kindid + "&propid="
				+ propid[id] + "&serverurl=" + serverurl;
//		BUYCOIN_1 = "userid=" + iptvid + "price=" + price + "tempkey="
//				+ tempKey;
//		BUYCOIN = "kindid=" + kindid + "propid=" + propid[id] + "serverurl="
//				+ serverurl;
		System.out.println("sub:" + submit_url);
		last_url = getUrl("gameorder.php", submit_url);
		Error = "charge--->";
		new Thread(this).start();
	}

	/**
	 * 获得消费信息
	 * 
	 * @param pid
	 * @param pagesize
	 */
	public void getConsumption(int pid, int pagesize, String kindid) {
		type = 5;
		buyOrCharge = GET_CONSUMPTION;
		this.pid = pid;
		this.pagesize = pagesize;
		this.kindid = kindid;
		submit_url = "account=" + iptvid + "&prodCode=" + kindid + "&pid="
				+ pid + "&pagesize=" + pagesize;
		System.out.println("sub:" + submit_url);
		last_url = getUrl("iptv.gamegold.consumer.record.php", submit_url);
		new Thread(this).start();
		// autoSend();
	}

	// Timer t;
	// static HttpConnection httpConn;
	public static String Error = "no no no";
	public static String CHARGE_RESULT;
	public static String BUY_RESULT;
	// /**
	// * 如果3秒后服务器无返回继续发送
	// */
	// private void autoSend(){
	// // Timer t=new Timer();
	// if(t!=null){
	// t.cancel();
	// t=null;
	// }
	// t=new Timer();
	// t.schedule(new TimerTask() {
	// public void run() {
	// if(buyOrCharge!=NULL){
	// switch(buyOrCharge){
	// case BUY:
	// buyProp(buy_propid);
	// break;
	// case CHARGE:
	// buyCoin(coinprice,buyCoinID);
	// break;
	// case GET_CONSUMPTION:
	// getConsumption(pid, pagesize, kindid);
	// break;
	// }
	// }
	// }
	// }, 4000);
	// }
	String lastResult;

	public void run() {
		
		try {
//			do{
				 lastResult =  getViaHttpConnection(last_url);
//			}while(lastResult==null);
			
			
			switch (type) {
			case 1:
				int result=0;
				if(lastResult.equals("6000333")){
					getCoin();
				}else if(lastResult.equals("")){
					result=0;
				}else{
					result=Integer.parseInt(lastResult);
					if(result>100000){
						buyOrCharge=NULL;
					}else{
						Coin = lastResult;
					}
				}
				System.out.println("coin="+Coin);
				break;
			case 2:
				if(lastResult.equals("6000333")){
					CHARGE_RESULT="网络连接错误";
					prop_result = 3;
				}else{
					BUY_RESULT=lastResult;
					if ("0".equals(lastResult)) {
						System.out.println("购买成功");
						prop_result = 1;
					} else if ("60010002".equals(lastResult)) {
						System.out.println("余额不足");
						prop_result = 2;
					} else if ("60010001".equals(lastResult)) {
						BUY_RESULT="消费失败";
						prop_result = 3;
					} else if ("60020003".equals(lastResult)) {
						BUY_RESULT="外部接口消费失败";
						prop_result = 3;
					} else if ("60020012".equals(lastResult)) {
						BUY_RESULT="验证错误";
						prop_result = 3;
					} else {
						System.out.println("购买失败");
						prop_result = 3;
					}
				}
				buyOrCharge=NULL;
				once=0;
				listenner.ResultState(BUY);
				break;
			case 3:
				if(lastResult.equals("6000333")){
					CHARGE_RESULT="网络连接错误";
					coin_result = 3;
				}else{
					CHARGE_RESULT=lastResult;
					if ("0".equals(lastResult)) {
						System.out.println("充值成功");
						coin_result = 1;
					} else if ("9103".equals(lastResult)) {
						System.out.println("童锁参数不对");
						coin_result = 2;
					} else if ("60000001".equals(lastResult)){
						CHARGE_RESULT="参数不合法";
						coin_result = 3;
					}else if ("60000002".equals(lastResult)){
						CHARGE_RESULT="数据库访问异常";
						coin_result = 3;
					}else if ("60000003".equals(lastResult)){
						CHARGE_RESULT="外部接口访问异常";
						coin_result = 3;
					}else if ("60000004".equals(lastResult)){
						CHARGE_RESULT="外部接口访问超时";
						coin_result = 3;
					}else if ("60000005".equals(lastResult)){
						CHARGE_RESULT="签名验证不通过";
						coin_result = 3;
					}else {
						System.out.println("充值失败");
						coin_result = 3;
					}
				}
				buyOrCharge=NULL;
				once=0;
				listenner.ResultState(CHARGE);
				break;
			case 4:
				JSONObject jsonObject = new JSONObject(lastResult);
				JSONArray jsonArray = (JSONArray) jsonObject.get("res");
				JSONObject obj;
				propPrice = new int[jsonArray.length()];
				propCoinPrice = new int[jsonArray.length()];
				propid=new String[jsonArray.length()];
				for (int i = 0; i < jsonArray.length(); i++) {
					obj = jsonArray.getJSONObject(i);
					propid[i] = obj.getString("Proid");
					propPrice[i] =Integer.parseInt(obj.getString("price"));
					propCoinPrice[i] = Integer.parseInt(obj.getString("gamegolds"));
					
					System.out.println("coinPrice="+propCoinPrice[i]);
				}
				buyOrCharge=NULL;
				break;
			case 5:
				JSONObject jsonObject2 = new JSONObject(lastResult);
				ShowConsumption.totalpage=Integer.parseInt(jsonObject2.getString("totalnum"));
				if(ShowConsumption.totalpage==0){
					ShowConsumption.totalpage=1;
				}
				System.out.println("totalpage="+ShowConsumption.totalpage);
				String consumptioninfo = jsonObject2.getString("res");
				if(ShowConsumption.consumptionInfo==null)
					ShowConsumption.consumptionInfo=new Vector();
				ShowConsumption.consumptionInfo=Net.getJsonArrayData(consumptioninfo, new String[]{"Proid","slownum","creattime"});
				isGetConsumption=true;
				buyOrCharge=NULL;
//				t.cancel();
				break;
			}
			
			
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			cleanAll();
		}
	}

	// /**
	// * 链接网络
	// */
	// public byte[] getViaHttpConnection(String url) throws IOException {
	// // InputStream is = null;
	// // byte[] data = null;
	// // System.gc();
	// // int rc;
	// //
	// // try {
	// // hc = (HttpConnection) Connector.open(url);
	// // System.out.println("888888888888888");
	// // //hc.setRequestMethod(HttpConnection.POST);
	// // rc = hc.getResponseCode();
	// // if (rc != HttpConnection.HTTP_OK) {
	// // throw new IOException("HTTP response code: " + rc);
	// // }
	// //
	// // is = hc.openInputStream();
	// // int len = (int) hc.getLength();
	// // if (len > 0) {
	// // int actual = 0;
	// // int bytesread = 0;
	// // data = new byte[len];
	// // while ((bytesread != len) && (actual != -1)) {
	// // actual = is.read(data, bytesread, len - bytesread);
	// // bytesread += actual;
	// // }
	// // } else {
	// // }
	// // } catch (ClassCastException e) {
	// // throw new IllegalArgumentException("Not an HTTP URL");
	// // } finally {
	// // is.close();
	// // hc.close();
	// // hc = null;
	// // is = null;
	// // System.gc();
	// // }
	// // return data;//data;
	//
	//
	// // HttpConnection httpConn = null;
	// // InputStream is = null;
	// // ByteArrayOutputStream baos = null;
	// // byte[] data = null;
	// // try {
	// // httpConn = (HttpConnection) Connector.open(url);
	// // int responseCode;
	// // if ((responseCode = httpConn.getResponseCode()) ==
	// HttpConnection.HTTP_OK) {
	// // is = httpConn.openInputStream();
	// //
	// // int len = (int) httpConn.getLength();
	// // if (len <= 0) {
	// // return null;
	// // }// //第一种方式
	// // int actual = 0;
	// // int bytesread = 0;
	// // data = new byte[len];
	// // do {
	// // actual = is.read(data, bytesread, len - bytesread);
	// // bytesread += actual;
	// // if (bytesread == len)
	// // break;
	// // } while (actual != -1);
	// //// image = Image.createImage(data, 0, data.length);
	// //// System.out.println("创建"+URL+"图片完成。。");
	// //
	// // //第二种方式
	// //// baos = new ByteArrayOutputStream();
	// //// int ch = 0;
	// //// while ((ch = is.read()) != -1) {
	// //// baos.write(ch);
	// //// }
	// //// data = baos.toByteArray();
	// //// baos.flush();
	// //// image = Image.createImage(imageData, 0, imageData.length);
	// //// System.out.println("创建"+URL+"图片完成。。");
	// //
	// // } else {
	// // System.out.println("response code:" + responseCode);
	// // }
	// // } catch (IOException e) {
	// // e.printStackTrace();
	// // } finally {
	// // try {
	// // if (baos != null)
	// // baos.close();
	// // if (is != null)
	// // is.close();
	// // if (httpConn != null)
	// // httpConn.close();
	// // } catch (IOException e) {
	// // e.printStackTrace();
	// // }
	// // }
	//
	// HttpConnection httpConn= null;
	// InputStream is = null;
	// int rc;
	// byte[] data = null;
	// try {
	// try{
	// httpConn = (HttpConnection) Connector.open(url);
	// }catch(Exception e){
	// httpConn=null;
	// httpConn = (HttpConnection) Connector.open(url,Connector.READ_WRITE,
	// true);
	// }
	// Error="no problem";
	// httpConn.setRequestMethod(HttpConnection.GET);
	// // Getting the response code will open the connection,
	// // send the request, and read the HTTP response headers.
	// // The headers are stored until requested.
	// rc = httpConn.getResponseCode();
	// if (rc != HttpConnection.HTTP_OK) {
	// Error=rc+"";
	// throw new IOException("HTTP response code: " + rc);
	// }
	//
	// is = httpConn.openInputStream();
	//
	// // Get the ContentType
	// String type = httpConn.getType();
	//
	// // Get the length and process the data
	// int len = (int)httpConn.getLength();
	// if (len > 0) {
	// int actual = 0;
	// int bytesread = 0 ;
	// data = new byte[len];
	// while ((bytesread != len) && (actual != -1)) {
	// actual = is.read(data, bytesread, len - bytesread);
	// bytesread += actual;
	// }
	// } else {
	// ByteArrayOutputStream bStrm = new ByteArrayOutputStream();
	// while ((rc = is.read()) != -1) {
	// bStrm.write(rc);
	// }
	// data = bStrm.toByteArray();
	// // int ch;
	// // while ((ch = is.read()) != -1) {
	// //// process((byte)ch);
	// // }
	// }
	//
	//
	// // } catch (ClassCastException e) {
	// // throw new IllegalArgumentException("Not an HTTP URL");
	// }catch(Exception e){
	// Error=e.toString();
	// } finally {
	// if (is != null)
	// is.close();
	// if (httpConn!= null)
	// httpConn.close();
	// }
	//
	// return data;
	// }

	/**
	 * 连接网络，下载数据
	 */
	public static String getViaHttpConnection(String url) {
		HttpConnection httpConn = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		byte[] data = null;
		try {
			httpConn = (HttpConnection) Connector.open(url,
					Connector.READ_WRITE, true);
			int responseCode;
			if ((responseCode = httpConn.getResponseCode()) == HttpConnection.HTTP_OK) {
				is = httpConn.openInputStream();

				int len = (int) httpConn.getLength();
				// 第一种方式
				int actual = 0;
				int bytesread = 0;
				data = new byte[len];
				do {
					actual = is.read(data, bytesread, len - bytesread);
					bytesread += actual;
					if (bytesread == len)
						break;
				} while (actual != -1);
			} else {
				System.out.println("response code:" + responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Error=e.toString();
			return "6000333";
		} finally {
			try {
				if (baos != null)
					baos.close();
				if (is != null)
					is.close();
				if (httpConn != null)
					httpConn.close();
				System.gc();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new String(data);
	}

	// public static String getViaHttpConnection(String url) throws IOException
	// {
	// HttpConnection c = null;
	// InputStream is = null;
	// byte[] data = null;
	// int rc;
	// int step=0;
	//
	// // try{
	// //
	// // }catch(Exception e){
	// // c.close();
	// // return null;
	// // }finally{
	// //
	// // }
	//
	// try {
	// // try{
	// c = (HttpConnection) Connector.open(url,Connector.READ_WRITE,true);
	// // }catch(Exception e){
	// // return null;
	// // }
	//
	// c.setRequestMethod(HttpConnection.GET);
	// Error="no problem";
	// rc = c.getResponseCode();
	// step=1;
	// if (rc != HttpConnection.HTTP_OK) {
	// throw new IOException("HTTP response code: " + rc);
	// }
	//
	// is = c.openInputStream();
	// // String type = c.getType();
	// int len = (int) c.getLength();
	// // System.out.println("------------len:"+len);
	// if (len > 0) {
	// int actual = 0;
	// int bytesread = 0;
	// data = new byte[len];
	// while ((bytesread != len) && (actual != -1)) {
	// actual = is.read(data, bytesread, len - bytesread);
	// bytesread += actual;
	// }
	// } else {
	// ByteArrayOutputStream bStrm = new ByteArrayOutputStream();
	// while ((rc = is.read()) != -1) {
	// bStrm.write(rc);
	// }
	// data = bStrm.toByteArray();
	// // int ch = 0;
	// // while ((ch = is.read()) != -1) {
	// //
	// // }
	// }
	// // }catch(NullPointerException e){
	// // return "333";
	// // }catch(ConnectException e){
	// // return "333";
	// }
	// catch (Exception e) {
	// Error=e.toString();
	// System.out.println("Error="+Error);
	// if(step==0){
	// return null;
	// }else if(step==1){
	// return "6000333";
	// }
	// } finally {
	// if (is != null)
	// is.close();
	// if (c != null)
	// c.close();
	// System.gc();
	// }
	// return new String(data);
	// }

	/**
	 * 
	 */
	public String getUrl(String action, String param) {
		String input = encrypt(param);
		input = md5class.getMD5ofStr((input + APP_KEY)) + input;
		String url = COMPANYURL + action + "?input=" + input;
		return url;
	}

	/**
	 * 
	 * 
	 * @param param
	 * @return
	 */
	private String encrypt(String param) {
		byte[] mkey = md5class.getMD5ofStr(APP_KEY).substring(1, 19).getBytes();
		int mlen = mkey.length;
		int mk;
		int num = param.length();
		byte[] output = new byte[num];
		byte[] strbyte = param.getBytes();
		for (int i = 0; i < num; i++) {
			mk = i % mlen;
			output[i] = (byte) ((int) strbyte[i] ^ (int) mkey[mk]);
		}

		return Base64.encode(output);
	}

}
