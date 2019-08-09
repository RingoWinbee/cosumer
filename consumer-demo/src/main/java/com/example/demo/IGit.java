package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class IGit {

	private String serviceUrl = "http://Training-Git-Service/";
	private RestTemplate restTemplate;

	public IGit(RestTemplate restTemplate) {
		super();
		this.restTemplate = restTemplate;
	}

	/**
	 * 在linux的gitroot目录建立远程仓库
	 */
	public void createRemote(String remoteName) {
		// HttpHeaders headers = new HttpHeaders();
		// headers.setContentType(MediaType.ALL);
		 MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("remoteName", remoteName);
		// HttpEntity<MultiValueMap<String, String>> formEntity = new
		// HttpEntity<>(param);
		String wayUrl = serviceUrl+"createRemote";
		//restTemplate.getForObject(wayUrl,String.class);
		restTemplate.postForObject(wayUrl, param, String.class);
		// System.out.println(a);
		// URI location =
		// restTemplate.postForLocation("http://Training-Git-Service/createRemote",
		// param);
		// return
		// this.restTemplate.getForObject("http://microservice-file-upload/provider/demo",
		// String.class);
	}

	/**
	 * 用于创建Git文件目录,如果没有此目录会自动创建
	 */
	public void creatLocalGit(String filePath) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("filePath", filePath);
		String wayUrl = serviceUrl + "creatLocalGit";
		restTemplate.postForObject(wayUrl, param, String.class);
	}
	
	/**
	 * 用于Git-add文件到缓冲区
	 */
	public void add(String filePath, String fileName) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("filePath", filePath);
		param.add("fileName", fileName);
		String wayUrl = serviceUrl + "add";
		restTemplate.postForObject(wayUrl, param, String.class);
	}
	
	/**
	 * 用于Git-rm -f将文件移出缓冲区和工作区,没有add直接rm会无效,有add但没有commit直接rm会git status看不到记录,
	 */
	public void rm(String filePath, String fileName) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("filePath", filePath);
		param.add("fileName", fileName);
		String wayUrl = serviceUrl + "rm";
		restTemplate.postForObject(wayUrl, param, String.class);
	}
	
	/**
	 * 用于Git-commit文件到区
	 */
	public void commit(String filePath, String massage) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("filePath", filePath);
		param.add("massage", massage);
		String wayUrl = serviceUrl + "commit";
		restTemplate.postForObject(wayUrl, param, String.class);
	}
	
	/**
	 * 查看git文件夹状态(相当于git status)
	 */
	public StatusList getStatus(String filePath) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("filePath", filePath);
		String wayUrl = serviceUrl + "getStatus";
		return restTemplate.getForObject(wayUrl, StatusList.class,param);
	}
	
	/**
	 * 查看git提交日志(相当于git log)
	 * @param LogEnity 
	 * @param List 
	 */
	public List<LogEnity> getLog(String filePath){
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("filePath", filePath);
		String wayUrl = serviceUrl + "getLog";
		LogEnity[] ls=restTemplate.getForObject(wayUrl, LogEnity[].class,param);
		List<LogEnity> logs=new ArrayList<LogEnity>();
		for(LogEnity l:ls) {
			logs.add(l);
		}
		return logs;
	}
}
