
package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ConsumerController {

	@Autowired
	private RestTemplate restTemplate;
	
	@Bean 
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	@Autowired
	private LoadBalancerClient loadBalancerClient;

	@RequestMapping("/consumer/getInterfaceInfo")
	public void getInterfaceInfo() {

		ServiceInstance choose = loadBalancerClient.choose("microservice-file-upload");
		System.out.println(choose.getPort());
	}

	@RequestMapping(value = "/consumer/hhh", method = RequestMethod.POST)
	public void test(@RequestParam(name = "picture", required = true)MultipartFile picture) throws IllegalStateException, IOException {
		String fileName = picture.getOriginalFilename();
		String tempFilePath = System.getProperty("java.io.tmpdir") + picture.getOriginalFilename();
		File tempFile = new File(tempFilePath);
		picture.transferTo(tempFile);
		String url = "http://training-image_manage-service/imageUpload";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		headers.setContentType(MediaType.parseMediaType("multipart/form-data;charset=UTF-8"));
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		FileSystemResource resource = new FileSystemResource(tempFilePath);
		param.add("picture", resource);
		System.out.println("fileName:   " + fileName);
		HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(param, headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, formEntity, String.class);
		String str = responseEntity.getBody();
		System.out.println(str);
		tempFile.delete();
	}

	@RequestMapping(value = "/consumer/getImage/{hashCade}", method = RequestMethod.GET)
	public byte[] getImage(@PathVariable("hashCode") String hashCode) throws IllegalStateException, IOException {
		String url = "http://microservice-file-upload/image/";
		return this.restTemplate.getForObject(url + hashCode, byte[].class);
	}

	@RequestMapping("/consumer/test")
	public String test() {
		return this.restTemplate.getForObject("http://training-image-manage-service/provider/demo", String.class);
	}

	@RequestMapping("/consumer/demo")
	public String ConsumerDemo() {
		return this.restTemplate.getForObject("http://microservice-file-upload/index.html", String.class);
	}

	/**
	 * 通过PrintWriter将响应数据写入response，ajax可以接受到这个数据
	 * 
	 * @param response
	 * @param data
	 * @throws IOException
	 */
	private void renderData(HttpServletResponse response, String data) throws IOException {
		PrintWriter printWriter = null;
		printWriter = response.getWriter();
		printWriter.print(data);
		if (null != printWriter) {
			printWriter.flush();
			printWriter.close();
		}
	}

	@RequestMapping("/consumer/createRemote")
	public void createRemote(@RequestParam(name = "remoteName", required = true) String remoteName) {
		new IGit(restTemplate).createRemote(remoteName);
	}
	
	@RequestMapping("/consumer/creatLocalGit")
	public void creatLocalGit(@RequestParam(name = "filePath", required = true) String filePath) {
		new IGit(restTemplate).creatLocalGit(filePath);
	}
}
