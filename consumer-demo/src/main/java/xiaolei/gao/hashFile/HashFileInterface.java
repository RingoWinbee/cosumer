package xiaolei.gao.hashFile;

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

public class HashFileInterface {

	private String serviceUrl = "http://training-hashfile-manage-service/";
	private RestTemplate restTemplate;

	public HashFileInterface(RestTemplate restTemplate) {
		super();
		this.restTemplate = restTemplate;
	}

	/**
	 * 上传文件保存
	 * 
	 * @param MultipartFile hashFile(上传的文件数据)
	 * @return String 文件的hash码
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public String hashFileUpload(MultipartFile hashFile) throws IllegalStateException, IOException {
		String tempFilePath = System.getProperty("java.io.tmpdir") + hashFile.getOriginalFilename();
		File tempFile = new File(tempFilePath);
		hashFile.transferTo(tempFile);
		String wayUrl = serviceUrl + "hashFileUpload";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("multipart/form-data;charset=UTF-8"));
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		FileSystemResource resource = new FileSystemResource(tempFilePath);
		param.add("hashFile", resource);
		HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(param, headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(wayUrl, formEntity, String.class);
		tempFile.delete();
		return responseEntity.getBody();
	}

	/**
	 * 删除文件(其实只是数据库的引用值减1)
	 * 
	 * @param String fileHash
	 * @return String "删除成功"或者"该文件不存在"
	 */
	public String hashFileDelete(String fileHash) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("fileHash", fileHash);
		String wayUrl = serviceUrl + "deleteHashFile";
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 *  获取文件
	 * 
	 * @param String fileHash
	 * @return byte[] 文件二进制数据
	 */
	public byte[] getHashFile(String fileHash) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("fileHash", fileHash);
		String wayUrl = serviceUrl + "getHashFile";
		return restTemplate.postForObject(wayUrl, param, byte[].class);
	}
}
