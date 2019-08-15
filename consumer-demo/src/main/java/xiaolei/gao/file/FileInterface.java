package xiaolei.gao.file;

import java.io.File;
import java.io.IOException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

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
		String tempFilePath = System.getProperty("java.io.tmpdir") + file.getOriginalFilename();
		File tempFile = new File(tempFilePath);
		file.transferTo(tempFile);
		String wayUrl = serviceUrl + "fileUpload";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("multipart/form-data;charset=UTF-8"));
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		FileSystemResource resource = new FileSystemResource(tempFilePath);
		param.add("file", resource);
		HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(param, headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(wayUrl, formEntity, String.class);
		tempFile.delete();
		return responseEntity.getBody();
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
	 *   获取文件
	 * 
	 * @param String fileUuid
	 * @return byte[] 文件二进制数据
	 */
	public byte[] getFile(String fileUuid) {
		String wayUrl = serviceUrl + "getFile?fileUuid="+fileUuid;
		return restTemplate.getForObject(wayUrl,byte[].class);
	}
}
