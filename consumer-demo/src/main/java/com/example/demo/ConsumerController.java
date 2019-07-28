
package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
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

	@Bean
	@LoadBalanced//使其具有负载均衡的能力
	public RestTemplate resTemplate() {
		return new RestTemplateBuilder().build();
	}
	
	@Autowired
	private	RestTemplate resTemplate;
	
	 @Autowired
	 private LoadBalancerClient loadBalancerClient;

	 @RequestMapping("/consumer/getInterfaceInfo")
	 public void getInterfaceInfo(){
		 
	     ServiceInstance choose = loadBalancerClient.choose("microservice-file-upload");
	     System.out.println(choose.getPort());
	 }
	
	 @RequestMapping(value="/consumer/hhh",method = RequestMethod.POST)
	 public void test(MultipartFile picture) throws IllegalStateException, IOException{
		 String fileName = picture.getOriginalFilename();
		 String tempFilePath = System.getProperty("java.io.tmpdir") + picture.getOriginalFilename();
	        File tempFile = new File(tempFilePath);
	        picture.transferTo(tempFile);
	        String url = "http://microservice-file-upload/imageUpload";
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Accept",MediaType.APPLICATION_JSON.toString());
	        headers.setContentType(MediaType.parseMediaType("multipart/form-data;charset=UTF-8"));
	        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
	        FileSystemResource resource = new FileSystemResource(tempFilePath);
	        param.add("picture",resource);
	        System.out.println("fileName:   "+fileName);
	        HttpEntity<MultiValueMap<String,Object>> formEntity = new HttpEntity<>(param,headers);
	        ResponseEntity<String> responseEntity = resTemplate.postForEntity(url,formEntity, String.class);
	        String str=responseEntity.getBody();
	        System.out.println(str);
	        tempFile.delete();
	 }
	 
	 @RequestMapping(value="/consumer/getImage/{hashCade}",method = RequestMethod.GET)
	 public byte[] getImage(@PathVariable("hashCode") String hashCode) throws IllegalStateException, IOException{
		 String url = "http://microservice-file-upload/image/";
		 return this.resTemplate.getForObject(url+hashCode, byte[].class);
	 }
	 @RequestMapping("/consumer/get")
	 public String testGetFile(){
		 String base64=this.resTemplate.getForObject("http://microservice-file-upload/getImageByHashCode", String.class);
		 return base64;
//		 byte[] bytes=this.resTemplate.getForObject("http://microservice-file-upload/getImageByHashCode", byte[].class);
//		 String imgFilePath = "d:\\222.jpg";// 新生成的图片  
//         OutputStream out=null;
//		try {
//			out = new FileOutputStream(imgFilePath);
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}  
//         try {
//			out.write(bytes);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
//         try {
//			out.flush();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
//         try {
//			out.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
	 }
	 
	 @RequestMapping("/consumer/test")
	 public String test(){
		 return this.resTemplate.getForObject("http://microservice-file-upload/provider/demo", String.class);
	 }
	@RequestMapping("/consumer/demo")
	public String ConsumerDemo(){
		return this.resTemplate.getForObject("http://microservice-file-upload/index.html", String.class);
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
}