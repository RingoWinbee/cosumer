
package com.example.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import xiaolei.gao.Image.ImageInterface;
import xiaolei.gao.git.IGit;
import xiaolei.gao.git.LogEnity;
import xiaolei.gao.git.StatusList;
import xiaolei.gao.hashFile.HashFileInterface;

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
	public void test(@RequestParam(name = "picture", required = true) MultipartFile picture)
			throws IllegalStateException, IOException {
//		//String fileName = picture.getOriginalFilename();
//		String tempFilePath = System.getProperty("java.io.tmpdir") + picture.getOriginalFilename();
//		File tempFile = new File(tempFilePath);
//		picture.transferTo(tempFile);
//		String url = "http://training-image-manage-service/imageUpload";
//		HttpHeaders headers = new HttpHeaders();
//		//headers.add("Accept", MediaType.APPLICATION_JSON.toString());
//		headers.setContentType(MediaType.parseMediaType("multipart/form-data;charset=UTF-8"));
//		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
//		FileSystemResource resource = new FileSystemResource(tempFilePath);
//		param.add("picture", resource);
//		//System.out.println("fileName:   " + fileName);
//		HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(param, headers);
//		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, formEntity, String.class);
//		String str = responseEntity.getBody();
//		System.out.println(str);
//		tempFile.delete();
		new ImageInterface(restTemplate).imageUpload(picture);
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

	@RequestMapping("/consumer/getLog")
	public void getLog(@RequestParam(name = "filePath", required = true) String filePath) {
		List<LogEnity> logs = new IGit(restTemplate).getLog(filePath);
		for (LogEnity log : logs) {
			System.out.println("id:" + log.getCommitId());
			System.out.println("author:" + log.getCommitAuthor());
		}
	}

	@RequestMapping("/consumer/getStatus")
	public void getStatus(@RequestParam(name = "filePath", required = true) String filePath) {
		StatusList s = new IGit(restTemplate).getStatus(filePath);
		s.getAddFile().forEach(it -> System.out.println("AddFile:" + it));
		s.getRemoveFile().forEach(it -> System.out.println("RemoveFile:" + it));
		s.getModifiedFile().forEach(it -> System.out.println("ModifiedFile:" + it));
		s.getUntrackedFile().forEach(it -> System.out.println("UntrackedFile:" + it));
		s.getConfictingFile().forEach(it -> System.out.println("ConfictingFile:" + it));
		s.getMissingFile().forEach(it -> System.out.println("MissingFile:" + it));
	}

	@RequestMapping("/consumer/getAllBanch")
	public void getAllBanch(@RequestParam(name = "filePath", required = true) String filePath) {
		List<String> bs = new IGit(restTemplate).showAllBranch(filePath);
		for (String b : bs) {
			System.out.println(b);
		}
	}

	@RequestMapping("/consumer/clone")
	public void clone(@RequestParam(name = "filePath", required = true) String filePath,
			@RequestParam(name = "remoteName", required = true) String remoteName) {
		new IGit(restTemplate).clone(remoteName, filePath);
	}

	@RequestMapping("/consumer/createLocal")
	public void createLocal(@RequestParam(name = "filePath", required = true) String filePath) {
		new IGit(restTemplate).creatLocalGit(filePath);
	}

	@RequestMapping("/consumer/testException")
	public void testException() {
		try {
			restTemplate.getForObject("http://demo/user/1",String.class);
        } catch (RestClientResponseException e){
        	JSONObject pa=JSONObject.parseObject(e.getResponseBodyAsString());
        	System.out.println(pa.getString("message"));
            //System.out.println("出现RestClient异常 :"+e.getResponseBodyAsString() );
        	e.printStackTrace();
        } catch (Exception e){
        	System.out.println("出现Exception异常 :"+e.getMessage());
        }
	}
	
	@RequestMapping(value = "/consumer/testHash", method = RequestMethod.POST)
	public void testHash(@RequestParam(name = "hashFile", required = true) MultipartFile hashFile) {
		try {
			new HashFileInterface(restTemplate).hashFileUpload(hashFile);
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
