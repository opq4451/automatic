package com.uf.automatic.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uf.automatic.util.Utils;

@RestController
public class Controller {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@RequestMapping("/getUid")
	public String getUid(@RequestParam("user") String user, @RequestParam("pwd") String pwd) {
		String Step1 = "http://203.160.143.110/www_new/app/login/chk_data.php?active=newlogin&" + "username=" + user
				+ "" + "&passwd=" + pwd + "" + "&langx=zh-cn";
		try {
			String ret = Utils.httpClientGet(Step1);
			String uid = ret.substring(ret.indexOf("uid") + 4, ret.indexOf("uid") + 37);
			return uid;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "null";
	}

	@RequestMapping("/getTodayWin")
	public String getTodayWin(@RequestParam("uid") String uid) {
		long unixTimestamp = Instant.now().getEpochSecond();
		String timestamp = Long.toString(unixTimestamp) + "000";
		String url = "http://203.160.143.110/www_new/app/getData/reloadMem.php?";
		String parameter = "smstime=" + timestamp + "" + "&allms=1117" + "&uid=" + convertUid(uid)
				+ "&langx=zh-cn&gtype=CA";
		try {
			System.out.println(url + parameter);

			String ret = Utils.httpClientGet(url + parameter);
			System.out.println(ret);
			JsonParser parser = new JsonParser();
			JsonObject o = parser.parse(ret).getAsJsonObject();
			JsonObject MemAry = o.getAsJsonObject("MemAry");
			String todayWin = MemAry.get("todayWin").toString();
			return todayWin;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "null";
	}

	@RequestMapping("/getPhase")
	public String getPhase() {
		try {
			String url = "http://api.1680210.com/pks/getLotteryPksInfo.do?issue=&lotCode=10001";
			String ret = Utils.httpClientGet(url);
			// 发送GET,并返回一个HttpResponse对象，相对于POST，省去了添加NameValuePair数组作参数

			JsonParser parser = new JsonParser();
			JsonObject o = parser.parse(ret).getAsJsonObject();
			JsonObject result = o.getAsJsonObject("result");

			JsonObject data = result.getAsJsonObject("data");

			String drawIssue = data.get("drawIssue").toString();
			return drawIssue;

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

		}
		return "";

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

	public String convertUid(String uid) {
		String u1 = uid.substring(0, 16);
		String u2 = uid.substring(17, 33);

		String encode = u1 + URLEncoder.encode("|") + u2;
		return encode;
	}

}
