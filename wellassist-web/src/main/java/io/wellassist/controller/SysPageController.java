package io.wellassist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统页面视图
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月24日 下午11:05:27
 */
@Controller
public class SysPageController {
	
	@RequestMapping("sys/{url}.html")
	public String page(@PathVariable("url") String url){
		return "sys/" + url + ".html";
	}

	@RequestMapping("generator/{url}.html")
	public String generator(@PathVariable("url") String url){
		return "generator/" + url + ".html";
	}
//	@RequestMapping("{url}.vm")
//	public String welcomePage(@PathVariable("url") String url){
//		return "views/front/welcome.html";
//	}

	@RequestMapping("platform/{url}.html")
	public String platformPage(@PathVariable("url") String url){
		return "platform/" + url + ".html";
	}




}
