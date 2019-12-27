package xiaolei.gao.file;

import java.io.IOException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

public class FileInterface {

	private final String serviceUrl = "http://training-filemanage-service/";
	private RestTemplate restTemplate;

	public FileInterface(RestTemplate restTemplate) {
		super();
		this.restTemplate = restTemplate;
	}

	/**
	 * 上传文件到默认位置保存
	 * 
	 * @param MultipartFile file(上传的文件数据)
	 * @param String fileUuid(文件保存的Uuid)
	 * @return 保存的路径
	 * @throws IOException
	 */
	public String fileUpload(MultipartFile file, String fileUuid)
			throws IOException {
		MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>(1);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpHeaders fileHeader = new HttpHeaders();
		fileHeader.setContentType(MediaType.parseMediaType(file
				.getContentType()));
		// 如果是用spring 的MultipartFile接受，则加入下面这行， 去个随机文件名
		fileHeader.setContentDispositionFormData("file", fileUuid);
		HttpEntity<byte[]> filePart = new HttpEntity<>(file.getBytes(),
				fileHeader);
		multipartRequest.add("file", filePart);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(
				multipartRequest, headers);
		ResponseEntity<String> result = restTemplate.postForEntity(serviceUrl,
				requestEntity, String.class);
		return result.getBody();
	}

	/**
	 * 上传文件到特定位置保存
	 * 
	 * @param MultipartFile file(上传的文件数据)
	 * @param String fileUuid(文件保存的Uuid)
	 *            
	 * @param String definePath(自定义的文件位置储存位置(可选，用于选择保存在哪个git仓库的下面))
	 * @return 保存的路径
	 * @throws IOException
	 */
	public String fileUpload(MultipartFile file, String fileUuid,
			String definePath) throws IOException {
		MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>(2);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpHeaders fileHeader = new HttpHeaders();
		fileHeader.setContentType(MediaType.parseMediaType(file
				.getContentType()));
		// 如果是用spring 的MultipartFile接受，则加入下面这行， 去个随机文件名
		fileHeader.setContentDispositionFormData("file", fileUuid);
		HttpEntity<byte[]> filePart = new HttpEntity<>(file.getBytes(),
				fileHeader);
		multipartRequest.add("file", filePart);
		multipartRequest.add("definePath", definePath);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(
				multipartRequest, headers);
		ResponseEntity<String> result = restTemplate.postForEntity(serviceUrl,
				requestEntity, String.class);
		return result.getBody();
	}

	/**
	 * 用于给前端直接将字符串保存到文件储存到默认位置
	 * 
	 * @Param(String fileString)
	 * @param String fileUuid(文件保存的Uuid)
	 * @return 保存的路径
	 */
	public String fileUpload(String fileString, String fileUuid) {
		MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>(1);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpHeaders fileHeader = new HttpHeaders();
		fileHeader.setContentType(MediaType.TEXT_PLAIN);
		// 如果是用spring 的MultipartFile接受，则加入下面这行， 去个随机文件名
		fileHeader.setContentDispositionFormData("file", fileUuid);
		HttpEntity<byte[]> filePart = new HttpEntity<>(fileString.getBytes(),
				fileHeader);
		multipartRequest.add("file", filePart);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(
				multipartRequest, headers);
		ResponseEntity<String> result = restTemplate.postForEntity(serviceUrl,
				requestEntity, String.class);
		return result.getBody();
	}

	/**
	 * 用于给前端直接将字符串保存到文件储存到特定位置
	 * 
	 * @Param(String fileString)
	 * @param String fileUuid(文件保存的Uuid)
	 * @param String definePath(自定义的文件位置储存位置(可选，用于选择保存在哪个git仓库的下面))
	 * @return 保存的路径
	 */
	public String fileUpload(String fileString, String fileUuid,
			String definePath) {
		MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>(2);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpHeaders fileHeader = new HttpHeaders();
		fileHeader.setContentType(MediaType.TEXT_PLAIN);
		// 如果是用spring 的MultipartFile接受，则加入下面这行， 去个随机文件名
		fileHeader.setContentDispositionFormData("file", fileUuid);
		HttpEntity<byte[]> filePart = new HttpEntity<>(fileString.getBytes(),
				fileHeader);
		multipartRequest.add("file", filePart);
		multipartRequest.add("definePath", definePath);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(
				multipartRequest, headers);
		ResponseEntity<String> result = restTemplate.postForEntity(serviceUrl,
				requestEntity, String.class);
		return result.getBody();
	}

	/**
	 * 删除默认位置的文件
	 * 
	 * @param String fileUuid
	 * @return 文件uuid
	 */
	public String fileDelete(String fileUuid) {
		String wayUrl = serviceUrl.
				concat(fileUuid);
		ResponseEntity<String> resp= restTemplate.exchange(wayUrl, HttpMethod.DELETE, null, String.class);
		return resp.getBody();
	}

	/**
	 * 删除特定位置的文件(用于删除某个git仓库下的文件)
	 * 
	 * @param String fileUuid
	 * @param String definePath(自定义的文件位置储存位置(可选，用于选择保存在哪个git仓库的下面))
	 * @return definePath/fileUuid
	 */
	public String fileDelete(String fileUuid, String definePath) {
		String wayUrl = serviceUrl.
				concat(definePath).
				concat("/").
				concat(fileUuid);
		ResponseEntity<String> resp= restTemplate.exchange(wayUrl, HttpMethod.DELETE, null, String.class);
		return resp.getBody();
	}

	/**
	 * 获取默认位置的文件
	 * 
	 * @param String
	 *            fileUuid
	 * @return byte[] 文件二进制数据
	 */
	public byte[] getFile(String fileUuid) {
		String wayUrl = serviceUrl.
				concat(fileUuid);
		return restTemplate.getForObject(wayUrl, byte[].class);
	}

	/**
	 * 获取特定位置的文件
	 * 
	 * @param String fileUuid
	 * @param String definePath(自定义的文件位置储存位置(可选，用于选择保存在哪个git仓库的下面))
	 * @return byte[] 文件二进制数据
	 */
	public byte[] getFile(String fileUuid, String definePath) {
		String wayUrl = serviceUrl.
				concat(definePath).
				concat("/").
				concat(fileUuid);
		return restTemplate.getForObject(wayUrl, byte[].class);
	}
}
