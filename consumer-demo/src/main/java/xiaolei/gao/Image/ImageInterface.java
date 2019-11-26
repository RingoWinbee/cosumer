package xiaolei.gao.Image;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
	 * @param String
	 *            json(上传的图片json数据)
	 * @return String 图片的hash码
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public String imageUpload(String json) throws IllegalStateException,
			IOException {
		String wayUrl = serviceUrl + "imageUpload";
		JsonObject taskObject = new JsonParser().parse(json).getAsJsonObject();
		String byteJsonString = taskObject.get("byteJsonString").getAsString();
		String fileLastName = taskObject.get("fileLastName").getAsString();
		String fileSize = taskObject.get("fileSize").getAsString();
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("byteJsonString", byteJsonString);
		param.add("fileLastName",// 文件的后缀名
				fileLastName);
		param.add("fileSize", fileSize);// 文件的大小
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 上传图片保存
	 * 
	 * @param MultipartFile
	 *            picture(上传的图片数据)
	 * @return String 图片的hash码
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public String imageUpload(MultipartFile picture)
			throws IllegalStateException, IOException {
		String wayUrl = serviceUrl + "imageUpload";
		// 将文件的二进制数据序列化成json字符串,不然直接传byte[]到后台的话就会大小不一致
		String byteJsonString = new Gson().toJson(picture.getBytes());
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("byteJsonString", byteJsonString);
		param.add(
				"fileLastName",// 文件的后缀名
				picture.getOriginalFilename().substring(
						picture.getOriginalFilename().lastIndexOf('.')));
		param.add("fileSize", picture.getSize());// 文件的大小
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 删除图片(其实只是数据库的引用值减1)
	 * 
	 * @param String
	 *            imageHash
	 * @return String "删除成功"或者"该图片不存在"
	 */
	public String imageDelete(String imageHash) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("hashCode", imageHash);
		String wayUrl = serviceUrl + "deleteImage";
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 获取图片
	 * 
	 * @param String
	 *            imageHash
	 * @return byte[] 图片二进制数据
	 */
	public byte[] getImage(String imageHash) {
		// restTemplate.getForObject不知道为什么只能在url后面接参数来传参,postForObject就可以方法加参数,而且不管被调用的方法是post还是get都可以用postForObject
		String wayUrl = serviceUrl + "getImage?imageHash=" + imageHash;
		return restTemplate.getForObject(wayUrl, byte[].class);
	}
}
