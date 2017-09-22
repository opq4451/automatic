package com.uf.automatic.controller;

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

@RestController
public class Controller {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@RequestMapping("/getUid")
	public String index(@RequestParam("user") String user, @RequestParam("pwd") String pwd) {
		String Step1 = "http://203.160.143.110/www_new/app/login/chk_data.php?active=newlogin&" + "username=" + user
				+ "" + "&passwd=" + pwd + "" + "&langx=zh-cn";
		try {
			String ret = httpClientGet(Step1);
			String uid = ret.substring(ret.indexOf("uid") + 4, ret.indexOf("uid") + 37);
			return uid;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "null";
	}

	public String httpClientGet(String uri) throws Exception {
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
}
