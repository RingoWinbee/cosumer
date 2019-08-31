package xiaolei.gao.file;

import java.io.IOException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.Gson;

public class FileInterface {

	private String serviceUrl = "http://training-filemanage-service/";
	private RestTemplate restTemplate;

	public FileInterface(RestTemplate restTemplate) {
		super();
		this.restTemplate = restTemplate;
	}

	/**
	 * 上传文件保存
	 * 
	 * @param MultipartFile file(上传的文件数据)
	 * @return String 文件的uuid码
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public String fileUpload(MultipartFile file) throws IllegalStateException, IOException {
		String wayUrl = serviceUrl + "fileUpload";
		HttpHeaders headers = new HttpHeaders();
		// 告诉后台这是json格式,为了对应后台的@RequestBody
		headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
		// 将文件的二进制数据序列化成json字符串,不然直接传byte[]到后台的话就会大小不一致
		String byteJsonString = new Gson().toJson(file.getBytes());
		// 如果你要传多个参数到后台的话就才要用MultiValueMap<String, Object>,只有一个参数直接添加就好了
		HttpEntity<String> formEntity = new HttpEntity<String>(byteJsonString);
		return restTemplate.postForObject(wayUrl, formEntity, String.class);
	}

	/**
	 * 用于给前端直接将字符串保存到文件
	 * 
	 * @RequestParam(String fileString)
	 * @return String 文件的uuid码
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public String fileUpload(String fileString) throws IllegalStateException, IOException {
		String wayUrl = serviceUrl + "fileUpload";
		HttpHeaders headers = new HttpHeaders();
		// 告诉后台这是json格式,为了对应后台的@RequestBody
		headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
		//将字符串也序列化成json格式来传输就可以和普通文件保存共用一个controller方法
		String byteJsonString=new Gson().toJson(fileString.getBytes());
		// 如果你要传多个参数到后台的话就才要用MultiValueMap<String, Object>,只有一个参数直接添加就好了
		HttpEntity<String> formEntity = new HttpEntity<String>(byteJsonString);
		return restTemplate.postForObject(wayUrl, formEntity, String.class);
	}

	/**
	 * 删除文件
	 * 
	 * @param String fileUuid
	 * @return String "删除成功"或者"该文件不存在"
	 */
	public String fileDelete(String fileUuid) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("fileUuid", fileUuid);
		String wayUrl = serviceUrl + "deleteFile";
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 获取文件
	 * 
	 * @param String fileUuid
	 * @return byte[] 文件二进制数据
	 */
	public byte[] getFile(String fileUuid) {
		String wayUrl = serviceUrl + "getFile?fileUuid=" + fileUuid;
		return restTemplate.getForObject(wayUrl, byte[].class);
	}
}
