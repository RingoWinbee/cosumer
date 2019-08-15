package xiaolei.gao.Image;

import java.io.File;
import java.io.IOException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageInterface {
	private String serviceUrl = "http://training-image-manage-service/";
	private RestTemplate restTemplate;

	public ImageInterface(RestTemplate restTemplate) {
		super();
		this.restTemplate = restTemplate;
	}

	/**
	 * 上传图片保存
	 * 
	 * @param MultipartFile picture(上传的图片数据)
	 * @return String 图片的hash码
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public String imageUpload(MultipartFile picture) throws IllegalStateException, IOException {
		String tempFilePath = System.getProperty("java.io.tmpdir") + picture.getOriginalFilename();
		File tempFile = new File(tempFilePath);
		picture.transferTo(tempFile);
		String wayUrl = serviceUrl + "imageUpload";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("multipart/form-data;charset=UTF-8"));
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		FileSystemResource resource = new FileSystemResource(tempFilePath);
		param.add("picture", resource);
		HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(param, headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(wayUrl, formEntity, String.class);
		tempFile.delete();
		return responseEntity.getBody();
	}

	/**
	 * 删除图片(其实只是数据库的引用值减1)
	 * 
	 * @param String imageHash
	 * @return String "删除成功"或者"该图片不存在"
	 */
	public String imageDelete(String imageHash) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("imageHash", imageHash);
		String wayUrl = serviceUrl + "deleteImage";
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 *  获取图片
	 * 
	 * @param String imageHash
	 * @return byte[] 图片二进制数据
	 */
	public byte[] getImage(String imageHash) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("imageHash", imageHash);
		String wayUrl = serviceUrl + "getImage";
		return restTemplate.postForObject(wayUrl, param, byte[].class);
	}
}
