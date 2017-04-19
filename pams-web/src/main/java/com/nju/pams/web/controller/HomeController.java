package com.nju.pams.web.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nju.pams.util.NetworkUtil;
import com.nju.pams.util.ResultUtil;
import com.nju.pams.util.constant.ResultEnum;
import net.sf.json.JSONObject;


@Controller
@RequestMapping(value = "/web")
public class HomeController {
	
	/**
	 * 首页
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("home");
		//返回 home.jsp
		return modelAndView;
	}
	
	//浏览器重定向到登录页面
    @RequestMapping(value = "/test")
    public String doLogout(HttpServletRequest request, Model model) {
        return "test";
    }
    
    //测试json返回值
    @ResponseBody
	@RequestMapping(value = "/json", method = RequestMethod.GET)
	public String getDateCanRedeem(@RequestParam("a") final Integer a,
								   @RequestParam("b") final Integer b) {
		final JSONObject result = new JSONObject();

		if(null == a || null == b) {
			ResultUtil.addResult(result, ResultEnum.NullParameter);
			return result.toString();
		}
		result.put("hello", "你好");
		result.put("a", a);
		result.put("b", b);
		ResultUtil.addSuccess(result);
		return result.toString();
	}
    
	/**
	 * ajax测试页面
	 */
	@RequestMapping(value = "/ajax", method = RequestMethod.GET)
	public ModelAndView ajax() {
		ModelAndView modelAndView = new ModelAndView("ajax");
		//返回 ajax.jsp
		return modelAndView;
	}
	
	//测试json返回值
    @ResponseBody
	@RequestMapping(value = "/getAjaxJson1", method = RequestMethod.GET)
	public String getAjaxJson1(@RequestParam("a") final Integer a,
							   @RequestParam("b") final Integer b) {
		final JSONObject result = new JSONObject();
		if(null == a || null == b) {
			ResultUtil.addResult(result, ResultEnum.NullParameter);
			return result.toString();
		}
		result.put("a", a);
		result.put("b", b);
		ResultUtil.addSuccess(result);
		return result.toString();
	}
    
    //测试json返回值
    @ResponseBody
	@RequestMapping(value = "/getAjaxJson2", method = RequestMethod.GET)
	public String getAjaxJson2(@RequestParam("a") final String a,
							   @RequestParam("b") final String b) {
		final JSONObject result = new JSONObject();
		if(null == a || null == b) {
			ResultUtil.addResult(result, ResultEnum.NullParameter);
			return result.toString();
		}
		result.put("a", a);
		result.put("b", b);
		ResultUtil.addSuccess(result);
		return result.toString();
	}
    
    //测试json返回值
    @ResponseBody
	@RequestMapping(value = "getAjaxJson3", method = RequestMethod.GET)
	public String getAjaxJson3() {
		final JSONObject result = new JSONObject();
		ResultUtil.addSuccess(result);
		return result.toString();
	}
    
    //根据基金关键词进行模糊查询
  	@RequestMapping(value = "/testUrl")
  	@ResponseBody
  	public String search(){
  		StringBuffer strBuf = new StringBuffer();
  		for(int i = 601000; i <= 601999; i++) {
  			strBuf.append("0").append(i).append(",");
  		}
  		String allList = strBuf.toString();
  		System.out.println(allList);
  		String localUrl = "http://api.money.126.net/data/feed/" + allList + ",money.api";	
  		String result = null;
        HttpClient client = new HttpClient();
        HttpMethod httpMethod = new GetMethod(localUrl);
        try {
        	int httpCode = client.executeMethod(httpMethod);
        	if (httpCode == 200) {
        		result = httpMethod.getResponseBodyAsString();
        	}
        } catch (IOException e) {
        	e.printStackTrace();
        	return null;
        }
  		return result;
  	}

    @ResponseBody
	@RequestMapping(value = "/getIpAddress", method = RequestMethod.GET)
	public String getIpAddress(HttpServletRequest request) {
		final JSONObject result = new JSONObject();
		String ip = NetworkUtil.getIpAddress(request);
		result.put("ip", ip);
		ResultUtil.addSuccess(result);
		return result.toString();
	}
}
