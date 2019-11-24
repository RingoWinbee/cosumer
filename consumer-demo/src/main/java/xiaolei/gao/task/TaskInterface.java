package xiaolei.gao.task;

import java.io.IOException;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TaskInterface {

	private String serviceUrl = "http://training-taskmanage-service/";

	private RestTemplate restTemplate;

	public TaskInterface(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public TaskInterface() {
	}

	// 修改任务和新建任务都是用这个函数
	public void addTask(String json) {
		String wayUrl = serviceUrl + "addTask";
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("json", json);
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	// 更新任务状态
	public void updateTaskStatus(String json) {
		String wayUrl = serviceUrl + "updateTaskStatus";
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("json", json);
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	// 获取用户的接收任务列表
	public String getTaskListByReceiver(String json) {
		String wayUrl = serviceUrl + "getTaskListByReceiver";
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("json", json);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	// 获取用户的发送任务列表
	public String getTaskListByResponsible(String json) {
		String wayUrl = serviceUrl + "getTaskListByResponsible";
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("json", json);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	// 获取用户的接收任务列表的详细信息
	public String getTaskByIdForReceiver(String json) {
		String wayUrl = serviceUrl + "getTaskByIdForReceiver";
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("json", json);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	// 获取用户的发送任务列表的详细信息
	public String getTaskByIdForResponsible(String json) {
		String wayUrl = serviceUrl + "getTaskByIdForResponsible";
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("json", json);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	// 上传任务附件
	public String uploadAttached(MultipartFile file, int fileType) throws IOException {
		String wayUrl = serviceUrl + "uploadAttached";
		// 将文件的二进制数据序列化成json字符串,不然直接传byte[]到后台的话就会大小不一致
		String fileJson = new Gson().toJson(file.getBytes());
		JsonObject jsonObject=new JsonObject();
		jsonObject.addProperty("fileJson", fileJson);
		jsonObject.addProperty("fileType", fileType);
		String json=new Gson().toJson(jsonObject);
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("json", json);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

//	// 获取用户的发送任务列表的详细信息
//	public String getTaskByIdForReceiver(String json) {
//		String wayUrl = serviceUrl + "getTaskByIdForReceiver";
//		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
//		param.add("json", json);
//		return restTemplate.postForObject(wayUrl, param, String.class);
//	}

}
