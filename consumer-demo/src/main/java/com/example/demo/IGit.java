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
		String wayUrl = serviceUrl + "createRemote";
		// restTemplate.getForObject(wayUrl,String.class);
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
		// restTemplate.getForObject不知道为什么只能在url后面接参数来传参,postForObject就可以方法加参数,而且不管被调用的方法是post还是get都可以用postForObject
		String wayUrl = serviceUrl + "getStatus?filePath=" + filePath;
		return restTemplate.getForObject(wayUrl, StatusList.class);
	}

	/**
	 * 查看git提交日志(相当于git log)
	 * 
	 * @param LogEnity
	 * @param List
	 */
	public List<LogEnity> getLog(String filePath) {
		// restTemplate.getForObject不知道为什么只能在url后面接参数来传参,postForObject就可以方法加参数,而且不管被调用的方法是post还是get都可以用postForObject
		String wayUrl = serviceUrl + "getLog?filePath=" + filePath;
		LogEnity[] ls = restTemplate.getForObject(wayUrl, LogEnity[].class);
		List<LogEnity> logs = new ArrayList<LogEnity>();
		for (LogEnity l : ls) {
			logs.add(l);
		}
		return logs;
	}

	/**
	 * 回退到上一个版本(相当于git reset --hard HEAD^)
	 * 
	 */
	public void resetLastOne(String filePath) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("filePath", filePath);
		String wayUrl = serviceUrl + "resetLastOne";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 回退到指定版本(相当于git reset --hard 版本号)
	 * 
	 */
	public void reset(String filePath, String version) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("filePath", filePath);
		param.add("version", version);
		String wayUrl = serviceUrl + "reset";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 创建分支(相当于git branch <name> )
	 * 
	 */
	public void createBranch(String filePath, String bname) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("filePath", filePath);
		param.add("bname", bname);
		String wayUrl = serviceUrl + "createBranch";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 把某个分支合并到当前分支(相当于git merge <name>)
	 */
	public void mergeBranch(String filePath, String bname) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("filePath", filePath);
		param.add("bname", bname);
		String wayUrl = serviceUrl + "mergeBranch";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 删除分支(相当于git branch -d<name> )
	 * 
	 */
	public void deleteBranch(String filePath, String bname) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("filePath", filePath);
		param.add("bname", bname);
		String wayUrl = serviceUrl + "deleteBranch";
		restTemplate.postForObject(wayUrl, param, String.class);
	}
	
	/**
	 * 切换分支(相当于git checkout <name> 如果有文件没有commit就不会执行)
	 * 
	 */
	public void checkOutBranch(String filePath, String bname) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("filePath", filePath);
		param.add("bname", bname);
		String wayUrl = serviceUrl + "checkOutBranch";
		restTemplate.postForObject(wayUrl, param, String.class);
	}
	
	/**
	 * 把远程仓库clone到指定目录(相当于git clone )默认显示mastet分支,其他分支要自己切换
	 * 
	 */
	public void clone(String remoteName, String filePath) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("remoteName", remoteName);
		param.add("filePath", filePath);
		String wayUrl = serviceUrl + "clone";
		restTemplate.postForObject(wayUrl, param, String.class);
	}
	
	/**
	* 向远程仓库某个分支push((相当于git push)) 或者用于新建分支向远程仓库的推送
	 * 
	 */
	public void push(String remoteName, String bName,String filePath) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("remoteName", remoteName);
		param.add("bName", bName);
		param.add("filePath", filePath);
		String wayUrl = serviceUrl + "push";
		restTemplate.postForObject(wayUrl, param, String.class);
	}
	
	/**
	* 从远程仓库拉取特定分支((相当于git pull)) 或者用于新建分支向远程仓库的推送
	 * 
	 */
	public void pull(String remoteName, String bName,String filePath) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("remoteName", remoteName);
		param.add("bName", bName);
		param.add("filePath", filePath);
		String wayUrl = serviceUrl + "pull";
		restTemplate.postForObject(wayUrl, param, String.class);
	}
}
