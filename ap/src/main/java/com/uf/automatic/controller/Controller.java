package com.uf.automatic.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uf.automatic.ap.OrderedProperties;
import com.uf.automatic.util.Utils;

@RestController
public class Controller {
	@Autowired
	private JdbcTemplate jdbcTemplate;
    
	int i = 0 ;
	int bi = 0 ; 
	int over_i = 0 ; 

	@RequestMapping("/getUid")
	public String getUid(@RequestParam("user") String user, @RequestParam("pwd") String pwd) {
		String Step1 = "http://203.160.143.110/www_new/app/login/chk_data.php?active=newlogin&" + "username=" + user
				+ "" + "&passwd=" + pwd + "" + "&langx=zh-cn";
		try {
			String ret = Utils.httpClientGet(Step1);
			String uid = ret.substring(ret.indexOf("uid") + 4, ret.indexOf("uid") + 37);
			String mid = ret.substring(ret.indexOf("mid") + 4, ret.indexOf("mid") + 8);

			return uid+"@"+mid;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "null";
	}

	@RequestMapping("/getTodayWin")
	public String getTodayWin(@RequestParam("uid") String uid, @RequestParam("user") String user) {
		long unixTimestamp = Instant.now().getEpochSecond();
		String timestamp = Long.toString(unixTimestamp) + getRandom();
		String url = "http://203.160.143.110/www_new/app/getData/reloadMem.php?";
		String parameter = "smstime=" + timestamp + "" + "&allms=1117" + "&uid=" + convertUid(uid)
				+ "&langx=zh-cn&gtype=CA";
		try {
			System.out.println(url + parameter);
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			String ret = Utils.httpClientGet(url + parameter);
			System.out.println(ret);
			System.out.println("*****************");

			JsonParser parser = new JsonParser();
			JsonObject o = parser.parse(ret).getAsJsonObject();
			JsonObject MemAry = o.getAsJsonObject("MemAry");
			String todayWin = MemAry.get("todayWin").toString();
			String ltype = MemAry.get("ltype").toString();

			JsonObject j = new JsonObject();
			j.addProperty("todayWin", todayWin);
			j.addProperty("ltype", ltype.substring(1, 2));

			FileInputStream fileIn = null;
			try {
				Properties configProperty = new Properties();
				String path = System.getProperty("user.dir");
				String hisFile = path + "/" + user + ".properties";
				File file = new File(hisFile);
				if (!file.exists()) {
					file.createNewFile();
				}
				fileIn = new FileInputStream(file);
				configProperty.load(fileIn);
				String type = configProperty.getProperty("type");
				String betlist = configProperty.getProperty("betlist");
				String betlist2 = configProperty.getProperty("betlist2");
				String betlist3 = configProperty.getProperty("betlist3");
				String betlist4 = configProperty.getProperty("betlist4");
				String betlist5 = configProperty.getProperty("betlist5");
				String stoplose = configProperty.getProperty("stoplose");
				String stopwin = configProperty.getProperty("stopwin");
				String startstatus = configProperty.getProperty("startstatus");
				
				j.addProperty("type", type);
				j.addProperty("betlist", betlist);
				j.addProperty("betlist2", betlist2);
				j.addProperty("betlist3", betlist3);
				j.addProperty("betlist4", betlist4);
				j.addProperty("betlist5", betlist5);

				j.addProperty("stoplose", stoplose);
				j.addProperty("stopwin", stopwin);
				j.addProperty("startstatus", startstatus);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {

				try {
					fileIn.close();
				} catch (Exception ex) {
				}
			}

			return j.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "null";
	}
	
	
	@RequestMapping("/getPredictLog")
	public String getPredictLog(  @RequestParam("user") String user) {
		 
		 
		try {
			  
			JsonObject j = new JsonObject();
		 

			FileInputStream fileIn = null;
			try {
				Properties configProperty = new OrderedProperties();
				String path = System.getProperty("user.dir");
				String hisFile = path + "/" + user + "_log.properties";
				File file = new File(hisFile);
				if(!file.exists())
					file.createNewFile();
				fileIn = new FileInputStream(file);
				configProperty.load(new InputStreamReader(fileIn , "UTF-8"));
				
				//String logHtml="";
				StringBuilder logHtml = new StringBuilder();
//				for (Map.Entry<Object, Object> e : configProperty.entrySet()) {
//					  String key = (String) e.getKey();
//					  String value = (String) e.getValue();
//					  System.out.println(value);
//					  logHtml.insert(0, "<tr><td style=\"border: 1px solid black\">"+value+"</td></tr>");
//					  //logHtml+="<tr><td style=\"border: 1px solid black\">"+value+"</td></tr>";
//				}
				for (Enumeration e = configProperty.propertyNames(); e.hasMoreElements();) { 
					  String v = configProperty.getProperty(e.nextElement().toString()) ; 
					  logHtml.insert(0, "<tr><td style=\"border: 1px solid black\">"+v+"</td></tr>");
				}
				
				
				 j.addProperty("logHtml", "<table style=\"border-collapse: collapse;\">"+logHtml+"</table>");

			} catch (Exception e) {
				e.printStackTrace();
			} finally {

				try {
					fileIn.close();
				} catch (Exception ex) {
				}
			}

			return j.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "null";
	}
	

	@RequestMapping("/getPhase")
	public String getPhase() {
		try {
//			String url = "http://api.1680210.com/pks/getLotteryPksInfo.do?issue=&lotCode=10001";
//			String ret = Utils.httpClientGet(url);
//			// 发送GET,并返回一个HttpResponse对象，相对于POST，省去了添加NameValuePair数组作参数
//
//			JsonParser parser = new JsonParser();
//			JsonObject o = parser.parse(ret).getAsJsonObject();
//			JsonObject result = o.getAsJsonObject("result");
//
//			JsonObject data = result.getAsJsonObject("data");
//
//			String drawIssue = data.get("drawIssue").toString();
//			return drawIssue;
			
			//彩世界
			long unixTimestamp = Instant.now().getEpochSecond();
			String url = "https://www.1399p.com/pk10/getawarddata?r="+unixTimestamp+"";
			String ret = Utils.httpClientGet(url);
			// 发送GET,并返回一个HttpResponse对象，相对于POST，省去了添加NameValuePair数组作参数

			JsonParser parser = new JsonParser();
			JsonObject o = parser.parse(ret).getAsJsonObject();
			JsonObject result = o.getAsJsonObject("next");

			//JsonObject data = result.getAsJsonObject("period");

			String drawIssue = result.get("period").toString();
			return drawIssue;

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

		}
		return "null";

	}

	@RequestMapping("/getCode")
	public String getCode(@RequestParam("phase") String phase) {
		FileInputStream fileIn = null;
		try {
			Properties configProperty = new Properties();
			String path = System.getProperty("user.dir");
			String hisFile = path + "/history.properties";
			File file = new File(hisFile);
			if (!file.exists()) {
				file.createNewFile();
			}
			fileIn = new FileInputStream(file);
			configProperty.load(fileIn);
			if (configProperty.getProperty(phase) != null)
				return configProperty.getProperty(phase);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "null";
	}

	@RequestMapping("/saveParam")
	public String saveParam(@RequestParam("user") String user, @RequestParam("type") String type,
			@RequestParam("betlist") String betlist,
			@RequestParam("betlist2") String betlist2,
			@RequestParam("betlist3") String betlist3,
			@RequestParam("betlist4") String betlist4,
			@RequestParam("betlist5") String betlist5,@RequestParam("stoplose") String stoplose,
			@RequestParam("stopwin") String stopwin,  @RequestParam("startstatus") String startstatus) {
		FileInputStream fileIn = null;
		FileOutputStream fileOut = null;

		try {
			Properties configProperty = new Properties();
			String path = System.getProperty("user.dir");
			String hisFile = path + "/" + user + ".properties";
			File file = new File(hisFile);
			if (!file.exists()) {
				file.createNewFile();
			}
			fileIn = new FileInputStream(file);
			configProperty.load(fileIn);

			configProperty.setProperty("type", type);
			configProperty.setProperty("betlist", betlist);
			configProperty.setProperty("betlist2", betlist2);
			configProperty.setProperty("betlist3", betlist3);
			configProperty.setProperty("betlist4", betlist4);
			configProperty.setProperty("betlist5", betlist5);
			configProperty.setProperty("stoplose", stoplose);
			configProperty.setProperty("stopwin", stopwin);
			configProperty.setProperty("startstatus", startstatus);
			fileOut = new FileOutputStream(file);
			configProperty.store(fileOut, "sample properties");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				fileIn.close();
				fileOut.close();
			} catch (Exception ex) {
			}
		}

		return "null";
	}

	@RequestMapping("/saveLog")
	public String saveParam(@RequestParam("user") String user, @RequestParam("log") String log) {
		FileInputStream fileIn = null;
		FileOutputStream fileOut = null;

		try {
			Properties configProperty = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
			String path = System.getProperty("user.dir");
			String hisFile = path + "/" + user + "_log.properties";
			File file = new File(hisFile);
			if (!file.exists()) {
				file.createNewFile();
			}
			fileIn = new FileInputStream(file);
			configProperty.load(new InputStreamReader(fileIn , "UTF-8"));
   			String result = java.net.URLDecoder.decode(log, "UTF-8");
  			i++;
			configProperty.setProperty(fillZero(Integer.toString(i)), result);

			fileOut = new FileOutputStream(file);
			configProperty.store(new OutputStreamWriter(fileOut, "UTF-8"), "sample properties");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				fileIn.close();
				fileOut.close();
			} catch (Exception ex) {
			}
		}

		return "null";
	}
	
	
	@RequestMapping("/saveOverLog")
	public String saveOverLog(@RequestParam("user") String user, @RequestParam("log") String log) {
		FileInputStream fileIn = null;
		FileOutputStream fileOut = null;

		try {
			Properties configProperty = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
			String path = System.getProperty("user.dir");
			String hisFile = path + "/" + user + "_over_log.properties";
			File file = new File(hisFile);
			if (!file.exists()) {
				file.createNewFile();
			}
			fileIn = new FileInputStream(file);
			configProperty.load(new InputStreamReader(fileIn , "UTF-8"));
   			String result = java.net.URLDecoder.decode(log, "UTF-8");
  			 
			configProperty.setProperty(log, "YES");

			fileOut = new FileOutputStream(file);
			configProperty.store(new OutputStreamWriter(fileOut, "UTF-8"), "sample properties");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				fileIn.close();
				fileOut.close();
			} catch (Exception ex) {
			}
		}

		return "null";
	}
	
	
	@RequestMapping("/checkOver")
	public String checkOver(@RequestParam("user") String user,
			@RequestParam("phase") String phase,
			@RequestParam("code") String code) {
		FileInputStream fileIn = null;
		FileOutputStream fileOut = null;

		try {
			Properties configProperty = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
			String path = System.getProperty("user.dir");
			String hisFile = path + "/" + user + "_over_log.properties";
			File file = new File(hisFile);
			if (!file.exists()) {
				file.createNewFile();
			}
			fileIn = new FileInputStream(file);
			configProperty.load(new InputStreamReader(fileIn , "UTF-8"));
   			 
			String c[] = code.split(",");
			
			JsonObject j = new JsonObject();
			for(int i = 0 ;i <10 ;i++){
				int sn = i+1 ;
				if( i == 9) {
					sn = 0 ;
				}
				String key = phase  + "@" + sn + "@" + c[i] ;
				if(configProperty.getProperty(key) != null){
					Utils.WritePropertiesFile(user+"overLOGDIS_log", fillZero(Integer.toString(over_i)), "第"+phase + "期，第" + sn + "名，號碼(" + code + ") 已過關!");
				 
					j.addProperty(covertIntToLatter(sn), "Y");

				}	
			}
			return j.toString();
			 
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				fileIn.close();
				fileOut.close();
			} catch (Exception ex) {
			}
		}

		return "null";
	}
	
	@RequestMapping("/clearLog")
	public String clearLog(@RequestParam("user") String user ) {
		 

		try {
			 
			String path = System.getProperty("user.dir");
			String hisFile = path + "/" + user + "_log.properties";
			File file = new File(hisFile);
			System.out.println(hisFile);
			System.out.println(file.exists());

			if(file.exists()){
				file.delete();
				System.out.println("delete suc");
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
 
		}

		return "null";
	}
	
	//sn : 1~ 0 , code : 01~10
	@RequestMapping("/bet")
	public String bet(@RequestParam("user") String user, @RequestParam("uid") String uid, @RequestParam("mid") String mid, @RequestParam("gid") String gid,
			@RequestParam("sn") String sn,@RequestParam("code") String code,@RequestParam("amount") String amount
			,@RequestParam("ltype") String ltype
			) {
		
		long unixTimestamp = Instant.now().getEpochSecond();
        String timeStampe = Long.toString(unixTimestamp)+ getRandom()  ;
        String rate = getRate(ltype);
		String url = "http://203.160.143.110/www_new/app/CA/CA_bet.php?" ; 
	    String parameter =  "smstime=" + timeStampe + ""
				+ "&allms=1117" + "&uid=" +  convertUid(uid)  + "&langx=zh-cn&betStr="+sn+"SN"+code+","+ltype+",,"+rate+","+amount+",1,"+amount+"" + "&gid=" + gid + ""
				+ "&mid="+mid+"&gtype=CA&active=bet&usertype=a&ltype="+ltype+"&username="+user+"" + "&timestamp=" + timeStampe + "";
		int phase = 554432 + Integer.parseInt(gid) ; 
	    try {
	    	//url += URLEncoder.encode(prameter, "UTF-8");
	    	 
			HttpGet httpget = new HttpGet(url + parameter);
			System.out.println(url + parameter);
			//httpget.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip");
			// 建立HttpPost对象
			HttpResponse response = new DefaultHttpClient().execute(httpget);
			// 发送GET,并返回一个HttpResponse对象，相对于POST，省去了添加NameValuePair数组作参数
			if (response.getStatusLine().getStatusCode() == 200) {// 如果状态码为200,就是正常返回
				String ret = EntityUtils.toString(response.getEntity());
				bi++;
			 
				Utils.WritePropertiesFile(user+"bet", fillZero(Integer.toString(bi)), "第"+phase + "期，第" + sn + "名，號碼(" + code + ")，金額(" + amount + ") @" + ret);
 
				return ret;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

		}

		return "";
	}
	
	public String getRate(String latter){
		if(latter.equals("A"))
			return "9.918";
		else if(latter.equals("B"))
			return "9.818";
		else if(latter.equals("C"))
			return "9.718";
		else 
			return "9.818";
	}
	public String getRandom() {
		
		int r = (int)(Math.random()*998);
		return fillZero(Integer.toString(r));	
	}
	public String convertUid(String uid) {
		String u1 = uid.substring(0, 16);
		String u2 = uid.substring(17, 33);

		String encode = u1 + URLEncoder.encode("|") + u2;
		return encode;
	}
	
	public String fillZero(String str) {
		if(str.length()==1)
			return "0000"+str;
		else if(str.length()==2)
			return "000"+str;
		else if(str.length()==3)
			return "00"+str;
		else if(str.length()==4)
			return "0"+str;
		else return str;
	}
	
	public String covertIntToLatter(int i) {
		if(i==1) return "a";
		if(i==2) return "b";
		if(i==3) return "c";
		if(i==4) return "d";
		if(i==5) return "e";
		if(i==6) return "f";
		if(i==7) return "g";
		if(i==8) return "h";
		if(i==9) return "i";
		if(i==0) return "j";

		
		return "";
	}

}
