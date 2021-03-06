package com.example.demo;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import xiaolei.gao.Image.ImageInterface;
import xiaolei.gao.file.FileInterface;
import xiaolei.gao.task.TaskInterface;

@RestController
public class ConsumerController {

	@Autowired
	private RestTemplate restTemplate;

	@Bean
	//@LoadBalanced
	public RestTemplate restTemplate() {
		// RestTemplate restTemplate = new RestTemplate();
		// MappingJackson2HttpMessageConverter converter = new
		// MappingJackson2HttpMessageConverter();
		//
		//
		// converter.setSupportedMediaTypes(
		// Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON,
		// MediaType.APPLICATION_OCTET_STREAM}));
		//
		// restTemplate.setMessageConverters(Arrays.asList(converter, new
		// FormHttpMessageConverter()));
		// return restTemplate;
		return new RestTemplate();
	}

	@Autowired
	private LoadBalancerClient loadBalancerClient;

	@RequestMapping("/consumer/getInterfaceInfo")
	public void getInterfaceInfo() {

		ServiceInstance choose = loadBalancerClient
				.choose("microservice-file-upload");
		System.out.println(choose.getPort());
	}

	@RequestMapping(value = "/consumer/hhh", method = RequestMethod.GET)
	public void test2(@RequestParam(name = "json", required = true) String json) {
		String result = null;
		result = TaskInterface.getTaskListByResponsible(restTemplate,json);
		System.out.println(result);
	}

//	@DeleteMapping("/deleteImage")
//	public void deleteImage(@RequestParam(name="hashCode") String hashCode,
//			@RequestParam(name="definePath",required=false) String definePath){
//		if(definePath==null){
//			new FileInterface(restTemplate).fileDelete(hashCode);
//		}else
//			new FileInterface(restTemplate).fileDelete(hashCode, definePath);
//	}
//	
//	@PostMapping("/uploadImage")
//	public void uploadImage(@RequestParam(name="file") MultipartFile file,
//			@RequestParam(name="fileUuid") String fileUuid,
//			@RequestParam(name="definePath",required=false) String definePath) throws IOException{
//		if(definePath==null){
//			new FileInterface(restTemplate).fileUpload(file,fileUuid);
//		}else
//			new FileInterface(restTemplate).fileUpload(file, fileUuid, definePath);
//	}
//	@GetMapping("/getImage")
//	public byte[] testImage(@RequestParam(name="hashCode") String hashCode,
//			@RequestParam(name="definePath",required=false) String definePath){
//		if(definePath==null){
//			return new FileInterface(restTemplate).getFile(hashCode);
//		}else
//			return new FileInterface(restTemplate).getFile(hashCode,definePath);
//	}
	@DeleteMapping("/deleteImage")
	public String deleteImage(@RequestParam(name="hashCode") String hashCode){
			return new ImageInterface(restTemplate).imageDelete(hashCode);
	}
	
	@PostMapping("/uploadImage")
	public String uploadImage(@RequestParam(name="file") MultipartFile file) throws IOException{
		return new ImageInterface(restTemplate).imageUpload(file);
	}
	@GetMapping("/getImage")
	public byte[] testImage(@RequestParam(name="hashCode") String hashCode){
		return new ImageInterface(restTemplate).getImage(hashCode);
	}
	
	@RequestMapping(value = "/consumer/getImage/{hashCade}", method = RequestMethod.GET)
	public byte[] getImage(@PathVariable("hashCode") String hashCode)
			throws IllegalStateException, IOException {
		String url = "http://microservice-file-upload/image/";
		return this.restTemplate.getForObject(url + hashCode, byte[].class);
	}

	@RequestMapping("/consumer/test")
	public String test() {
		return this.restTemplate.getForObject(
				"http://training-image-manage-service/provider/demo",
				String.class);
	}

	@RequestMapping("/consumer/demo")
	public String ConsumerDemo() {
		return this.restTemplate.getForObject(
				"http://microservice-file-upload/index.html", String.class);
	}

	/**
	 * 通过PrintWriter将响应数据写入response，ajax可以接受到这个数据
	 * 
	 * @param response
	 * @param data
	 * @throws IOException
	 */
	private void renderData(HttpServletResponse response, String data)
			throws IOException {
		PrintWriter printWriter = null;
		printWriter = response.getWriter();
		printWriter.print(data);
		if (null != printWriter) {
			printWriter.flush();
			printWriter.close();
		}
	}

	// @RequestMapping("/consumer/createRemote")
	// public void createRemote(@RequestParam(name = "remoteName", required =
	// true) String remoteName) {
	// new IGit(restTemplate).createRemote(remoteName);
	// }

	// @RequestMapping("/consumer/creatLocalGit")
	// public void creatLocalGit(@RequestParam(name = "filePath", required =
	// true) String filePath) {
	// new IGit(restTemplate).creatLocalGit(filePath);
	// }
	//
	// @RequestMapping("/consumer/getLog")
	// public void getLog(@RequestParam(name = "filePath", required = true)
	// String filePath) {
	// List<LogEnity> logs = new IGit(restTemplate).getLog(filePath);
	// for (LogEnity log : logs) {
	// System.out.println("id:" + log.getCommitId());
	// System.out.println("author:" + log.getCommitAuthor());
	// }
	// }
	//
	// @RequestMapping("/consumer/getStatus")
	// public void getStatus(@RequestParam(name = "filePath", required = true)
	// String filePath) {
	// StatusList s = new IGit(restTemplate).getStatus(filePath);
	// s.getAddFile().forEach(it -> System.out.println("AddFile:" + it));
	// s.getRemoveFile().forEach(it -> System.out.println("RemoveFile:" + it));
	// s.getModifiedFile().forEach(it -> System.out.println("ModifiedFile:" +
	// it));
	// s.getUntrackedFile().forEach(it -> System.out.println("UntrackedFile:" +
	// it));
	// s.getConfictingFile().forEach(it -> System.out.println("ConfictingFile:"
	// + it));
	// s.getMissingFile().forEach(it -> System.out.println("MissingFile:" +
	// it));
	// }
	//
	// @RequestMapping("/consumer/getAllBanch")
	// public void getAllBanch(@RequestParam(name = "filePath", required = true)
	// String filePath) {
	// List<String> bs = new IGit(restTemplate).showAllBranch(filePath);
	// for (String b : bs) {
	// System.out.println(b);
	// }
	// }
	//
	// @RequestMapping("/consumer/clone")
	// public void clone(@RequestParam(name = "filePath", required = true)
	// String filePath,
	// @RequestParam(name = "remoteName", required = true) String remoteName) {
	// new IGit(restTemplate).clone(remoteName, filePath);
	// }
	//
	// @RequestMapping("/consumer/createLocal")
	// public void createLocal(@RequestParam(name = "filePath", required = true)
	// String filePath) {
	// new IGit(restTemplate).creatLocalGit(filePath);
	// }
	//
	// @RequestMapping("/consumer/testException")
	// public void testException() {
	// try {
	// restTemplate.getForObject("http://demo/user/1",String.class);
	// } catch (RestClientResponseException e){
	// JSONObject pa=JSONObject.parseObject(e.getResponseBodyAsString());
	// System.out.println(pa.getString("message"));
	// //System.out.println("出现RestClient异常 :"+e.getResponseBodyAsString() );
	// e.printStackTrace();
	// } catch (Exception e){
	// System.out.println("出现Exception异常 :"+e.getMessage());
	// }
	// }
	//
	// @RequestMapping(value = "/consumer/testHash", method =
	// RequestMethod.POST)
	// public void testHash(@RequestParam(name = "hashFile", required = true)
	// MultipartFile hashFile) {
	// try {
	// System.out.println(new
	// HashFileInterface(restTemplate).hashFileUpload(hashFile));
	// } catch (IllegalStateException | IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// @RequestMapping(value = "/consumer/testImg", method = RequestMethod.POST)
	// public String testImg(@RequestParam(name = "picture", required = true)
	// MultipartFile picture) {
	// try {
	// return new ImageInterface(restTemplate).imageUpload(picture);
	// } catch (IllegalStateException | IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return "上传失败";
	// }
	//
	// @RequestMapping(value = "/consumer/testFileString", method =
	// RequestMethod.POST)
	// public String testFileString(@RequestParam(name = "fileString", required
	// = true) String fileString) {
	// try {
	// return new FileInterface(restTemplate).fileUpload(fileString);
	// } catch (IllegalStateException | IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return "上传失败";
	// }
	//
	// @RequestMapping(value = "/consumer/testFile", method =
	// RequestMethod.POST)
	// public String testFile(@RequestParam(name = "file", required = true)
	// MultipartFile file) {
	// try {
	// return new FileInterface(restTemplate).fileUpload(file);
	// } catch (IllegalStateException | IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return "上传失败";
	// }
	//
	// @RequestMapping(value = "/consumer/testHashGet", method =
	// RequestMethod.GET)
	// public byte[] testHashGet(@RequestParam(name = "fileHash", required =
	// true) String fileHash) {
	// return new HashFileInterface(restTemplate).getHashFile(fileHash);
	// }
	//
}
