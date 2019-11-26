package xiaolei.gao.file;

import java.io.IOException;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FileInterface {

	private String serviceUrl = "http://training-filemanage-service/";
	private RestTemplate restTemplate;

	public FileInterface(RestTemplate restTemplate) {
		super();
		this.restTemplate = restTemplate;
	}

	/**
	 * 上传文件到默认位置保存(直接传json)
	 * 
	 * @param String fileJosn(上传的文件数据Json)
	 * @return "保存成功"/"保存失败"
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public String fileJsonUpload(String json) throws IllegalStateException, IOException {
		String wayUrl = serviceUrl + "fileUpload";
		JsonObject object = new JsonParser().parse(json).getAsJsonObject();
		String byteJsonString=object.get("byteJsonString").getAsString();
		String fileUuid=object.get("fileUuid").getAsString();
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("byteJsonString", byteJsonString);
		param.add("fileUuid", fileUuid);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}
	/**
	 * 上传文件到默认位置保存
	 * 
	 * @param MultipartFile file(上传的文件数据)
	 * @param String fileUuid(文件保存的Uuid)
	 * @return "保存成功"/"保存失败"
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public String fileUpload(MultipartFile file,String fileUuid) throws IllegalStateException, IOException {
		String wayUrl = serviceUrl + "fileUpload";
		// 将文件的二进制数据序列化成json字符串,不然直接传byte[]到后台的话就会大小不一致
		String byteJsonString = new Gson().toJson(file.getBytes());
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("byteJsonString", byteJsonString);
		param.add("fileUuid", fileUuid);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}
	
	/**
	 * 上传文件到特定位置保存
	 * 
	 * @param MultipartFile file(上传的文件数据)
	 * @param String fileUuid(文件保存的Uuid)
	 * @param String definePath(自定义的文件位置储存位置(可选，用于选择保存在哪个git仓库的下面))
	 * @return "保存成功"/"保存失败"
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public String fileUpload(MultipartFile file,String fileUuid,String definePath) throws IllegalStateException, IOException {
		String wayUrl = serviceUrl + "fileUpload";
		// 将文件的二进制数据序列化成json字符串,不然直接传byte[]到后台的话就会大小不一致
		String byteJsonString = new Gson().toJson(file.getBytes());
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("byteJsonString", byteJsonString);
		param.add("fileUuid", fileUuid);
		param.add("definePath", definePath);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}


	/**
	 * 用于给前端直接将字符串保存到文件储存到默认位置
	 * 
	 * @Param(String fileString)
	 * @param String fileUuid(文件保存的Uuid) 
	 * @return "保存成功"/"保存失败"
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public String fileUpload(String fileString,String fileUuid) throws IllegalStateException, IOException {
		String wayUrl = serviceUrl + "fileUpload";
		//将字符串也序列化成json格式来传输就可以和普通文件保存共用一个controller方法
		String byteJsonString=new Gson().toJson(fileString.getBytes());
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("byteJsonString", byteJsonString);
		param.add("fileUuid", fileUuid);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}
	
	/**
	 * 用于给前端直接将字符串保存到文件储存到特定位置
	 * 
	 * @Param(String fileString)
	 * @param String fileUuid(文件保存的Uuid) 
	 * @param String definePath(自定义的文件位置储存位置(可选，用于选择保存在哪个git仓库的下面)) 
	 * @return "保存成功"/"保存失败"
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public String fileUpload(String fileString,String fileUuid,String definePath) throws IllegalStateException, IOException {
		String wayUrl = serviceUrl + "fileUpload";
		//将字符串也序列化成json格式来传输就可以和普通文件保存共用一个controller方法
		String byteJsonString=new Gson().toJson(fileString.getBytes());
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("byteJsonString", byteJsonString);
		param.add("fileUuid", fileUuid);
		param.add("definePath", definePath);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 删除默认位置的文件
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
	 * 删除特定位置的文件(用于删除某个git仓库下的文件)
	 * 
	 * @param String fileUuid
	 * @param String definePath(自定义的文件位置储存位置(可选，用于选择保存在哪个git仓库的下面))
	 * @return String "删除成功"或者"该文件不存在"
	 */
	public String fileDelete(String fileUuid,String definePath) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("definePath", definePath);
		param.add("fileUuid", fileUuid);
		String wayUrl = serviceUrl + "deleteFile";
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 获取默认位置的文件
	 * 
	 * @param String fileUuid
	 * @return byte[] 文件二进制数据
	 */
	public byte[] getFile(String fileUuid) {
		String wayUrl = serviceUrl + "getFile?fileUuid=" + fileUuid;
		return restTemplate.getForObject(wayUrl, byte[].class);
	}
	
	/**
	 * 获取特定位置的文件
	 * 
	 * @param String fileUuid
	 * @param String definePath(自定义的文件位置储存位置(可选，用于选择保存在哪个git仓库的下面)) 
	 * @return byte[] 文件二进制数据
	 */
	public byte[] getFile(String fileUuid,String definePath) {
		String wayUrl = serviceUrl + "getFile?fileUuid=" + fileUuid+"&definePath="+definePath;
		return restTemplate.getForObject(wayUrl, byte[].class);
	}
}
