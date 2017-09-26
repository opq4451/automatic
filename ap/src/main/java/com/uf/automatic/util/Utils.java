package com.uf.automatic.util;

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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

 
public class Utils {
	 
	public static String httpClientGet(String uri) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();

		HttpGet httpget = new HttpGet(uri);
		httpget.setConfig(requestConfig);

		String result = null;

		try {
			HttpResponse httpresponse = httpClient.execute(httpget);
			HttpEntity entity = httpresponse.getEntity();
			result = EntityUtils.toString(entity);
		} catch (Exception e) {
			throw e;
		} finally {
			httpClient.close();
		}
		return result;
	}
	
	public static void writeHistory() {
		try {
			String url = "http://api.1680210.com/pks/getPksHistoryList.do?lotCode=10001";
			String ret = Utils.httpClientGet(url);
			JsonParser parser = new JsonParser();
			JsonObject o = parser.parse(ret).getAsJsonObject();
			JsonArray data = o.getAsJsonObject("result").getAsJsonArray("data");
			for (JsonElement pa : data) {
			    JsonObject paymentObj = pa.getAsJsonObject();
			    String     preDrawCode     = paymentObj.get("preDrawCode").getAsString(); //開獎
			    String     preDrawIssue = paymentObj.get("preDrawIssue").getAsString(); //期數
			    WritePropertiesFile(preDrawIssue,preDrawCode);

			}
 		}catch(Exception e) {
			
			
		}
		
	}
	
	public static void WritePropertiesFile(String key, String data) {
        FileOutputStream fileOut = null;
        FileInputStream fileIn = null;
        try {
            Properties configProperty = new Properties();
            String path = System.getProperty("user.dir");
            String hisFile = path + "/history.properties";
            File file = new File(hisFile);
            if(!file.exists()) {
            		file.createNewFile();
            }
            fileIn = new FileInputStream(file);
            configProperty.load(fileIn); 
            if(configProperty.getProperty(key)==null)
            		configProperty.setProperty(key, data);
            fileOut = new FileOutputStream(file);
            configProperty.store(fileOut, "sample properties");  
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

            try {
                fileOut.close();
            } catch (Exception ex) {
            }
        }
    }
}
