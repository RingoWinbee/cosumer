package xiaolei.gao.hashFile;

import java.io.IOException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.Gson;

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
		String wayUrl = serviceUrl + "hashFileUpload";
		// 将文件的二进制数据序列化成json字符串,不然直接传byte[]到后台的话就会大小不一致
		String byteJsonString = new Gson().toJson(hashFile.getBytes());
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("byteJsonString", byteJsonString);
		param.add("fileLastName",//文件的后缀名
				hashFile.getOriginalFilename().substring(hashFile.getOriginalFilename().lastIndexOf('.')));
		param.add("fileSize", hashFile.getSize());//文件的大小
		return restTemplate.postForObject(wayUrl, param, String.class);
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
