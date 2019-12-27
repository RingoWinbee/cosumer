package xiaolei.gao.Image;

import java.io.IOException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
	 */
	public String imageUpload(MultipartFile picture) throws IOException{
		MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpHeaders pictureHeader = new HttpHeaders();
        pictureHeader.setContentType(MediaType.parseMediaType(picture.getContentType()));
        //如果是用spring 的MultipartFile接受，则加入下面这行， 去个随机文件名
        pictureHeader.setContentDispositionFormData("file", picture.getOriginalFilename());
        HttpEntity<byte[]> picturePart = new HttpEntity<>(
        		picture.getBytes(), pictureHeader);
        multipartRequest.add("file", picturePart);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>
                (multipartRequest, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(serviceUrl,requestEntity,String.class);
        return result.getBody();
	}

	/**
	 * 删除图片(其实只是数据库的引用值减1)
	 * @param String imageHash
	 * @return 文件hashCode
	 */
	public String imageDelete(String imageHash) {
		String wayUrl = serviceUrl.
				concat(imageHash);
		ResponseEntity<String> resp= restTemplate.exchange(wayUrl, HttpMethod.DELETE, null, String.class);
		return resp.getBody();
	}

	/**
	 * 获取图片
	 * @param String imageHash
	 * @return byte[] 图片二进制数据
	 */
	public byte[] getImage(String imageHash) {
		String wayUrl = serviceUrl.concat(imageHash);
		return restTemplate.getForObject(wayUrl, byte[].class);
	}
}
