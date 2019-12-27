package xiaolei.gao.task;

import java.io.IOException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TaskInterface {

	private final static String serviceUrl = "http://training-taskmanage-service-web/";

	private TaskInterface() {
	}

	// 修改任务和新建任务都是用这个函数
	public static String addTask(RestTemplate restTemplate, String taskId,
			String taskName, String taskResponsible, int taskType,
			String taskDescription, int taskPriority, String receiver,
			int isGroup, int taskStatus, int createOrUpdate) {
		String wayUrl = serviceUrl + "addTask";
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>(10);
		param.add("taskId", taskId);
		param.add("taskName", taskName);
		param.add("taskResponsible", taskResponsible);
		param.add("taskType", taskType);
		param.add("taskDescription", taskDescription);
		param.add("taskPriority", taskPriority);
		param.add("receiver", receiver);
		param.add("isGroup", isGroup);
		param.add("taskStatus", taskStatus);
		param.add("createOrUpdate", createOrUpdate);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	// 更新任务状态
	public static String updateTaskStatus(RestTemplate restTemplate,
			int status, String taskId) {
		String wayUrl = serviceUrl + "updateTaskStatus";
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>(2);
		param.add("status", status);
		param.add("taskId", taskId);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	// 获取用户的接收任务列表
	public static String getTaskListByReceiver(RestTemplate restTemplate,
			String receiverId) {
		String wayUrl = serviceUrl + "getTaskListByReceiver" + "?receiverId="
				+ receiverId;
		return restTemplate.getForObject(wayUrl, String.class);
	}

	// 获取用户的发送任务列表
	public static String getTaskListByResponsible(RestTemplate restTemplate,
			String responsible) {
		String wayUrl = serviceUrl + "getTaskListByResponsible"
				+ "?responsible=" + responsible;
		return restTemplate.getForObject(wayUrl, String.class);
	}

	// 获取用户的接收任务列表的详细信息
	public static String getTaskByIdForReceiver(RestTemplate restTemplate,
			String id) {
		String wayUrl = serviceUrl + "getTaskByIdForReceiver" + "?id=" + id;
		return restTemplate.getForObject(wayUrl, String.class);
	}

	// 获取用户的发送任务列表的详细信息
	public static String getTaskByIdForResponsible(RestTemplate restTemplate,
			String id) {
		String wayUrl = serviceUrl + "getTaskByIdForResponsible" + "?id=" + id;
		return restTemplate.getForObject(wayUrl, String.class);
	}

	// 上传任务附件到文件管理
	public static String uploadAttached(RestTemplate restTemplate,
			MultipartFile file, int fileType) throws IOException {
		String wayUrl = serviceUrl + "uploadAttached";
		Gson gson = new Gson();
		String byteJsonString = gson.toJson(file.getBytes());
		JsonObject object = new JsonObject();
		object.addProperty("byteJsonString", byteJsonString);
		object.addProperty("fileType", fileType);
		if (fileType == 0) {
			String fileLastName = file.getOriginalFilename().substring(
					file.getOriginalFilename().lastIndexOf('.'));
			Long fileSize = file.getSize();
			object.addProperty("fileLastName", fileLastName);
			object.addProperty("fileSize", fileSize);
		}
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>(2);
		param.add("fileType", fileType);
		param.add("fileJson", gson.toJson(object));
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	// 获取任务附件
	public static String getAttached(RestTemplate restTemplate,
			String hashOrUuid, int fileType) throws IOException {
		String wayUrl = serviceUrl + "getAttached" + "?hashOrUuid="
				+ hashOrUuid + "&fileType=" + fileType;
		return restTemplate.getForObject(wayUrl, String.class);
	}

	// 删除任务附件
	public static String deleteAttached(RestTemplate restTemplate,
			String hashOrUuid, int fileType, String attachedId)
			throws IOException {
		String wayUrl = serviceUrl + "deleteAttached";
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>(3);
		param.add("hashOrUuid", hashOrUuid);
		param.add("fileType", fileType);
		param.add("attachedId", attachedId);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	// 添加任务附件
	public static String addAttached(RestTemplate restTemplate, String id,
			String name, String taskId, String fileUuid, int hashOrUuid)
			throws IOException {
		String wayUrl = serviceUrl + "addAttached";
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>(5);
		param.add("id", id);
		param.add("name", name);
		param.add("taskId", taskId);
		param.add("fileUuid", fileUuid);
		param.add("hashOrUuid", hashOrUuid);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	// 获取任务附件列表
	public static String getAttachedListByTaskId(RestTemplate restTemplate,
			String taskId) throws IOException {
		String wayUrl = serviceUrl + "getAttachedListByTaskId" + "?taskId="
				+ taskId;
		return restTemplate.getForObject(wayUrl, String.class);
	}

	// 根据任务Id获取所有链接信息(但只包括"TL_LINK_ID","TASK_TYPE_NAME","TL_LINK_ITEM_NAME")
	public static String getAllLinkByTaskId(RestTemplate restTemplate,
			String taskId) throws IOException {
		String wayUrl = serviceUrl + "getAllLinkByTaskId" + "?taskId=" + taskId;
		return restTemplate.getForObject(wayUrl, String.class);
	}

	// 保存单个链接信息
	public static String addLink(RestTemplate restTemplate, String id,
			String taskId, String itemId, int linkType, String itemName)
			throws IOException {
		String wayUrl = serviceUrl + "addLink";
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>(5);
		param.add("id", id);
		param.add("taskId", taskId);
		param.add("itemId", itemId);
		param.add("linkType", linkType);
		param.add("itemName", itemName);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	// 删除单个链接信息
	public static String deleteLink(RestTemplate restTemplate, String linkId)
			throws IOException {
		String wayUrl = serviceUrl + "deleteLink";
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>(1);
		param.add("linkId", linkId);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	// 获取任务的所有类型
	public static String getAllTaskType(RestTemplate restTemplate)
			throws IOException {
		String wayUrl = serviceUrl + "getAllTaskType";
		return restTemplate.getForObject(wayUrl, String.class);
	}

	// 获取任务的所有优先级
	public static String getAllTaskPriority(RestTemplate restTemplate)
			throws IOException {
		String wayUrl = serviceUrl + "getAllTaskPriority";
		return restTemplate.getForObject(wayUrl, String.class);
	}

	// 获取任务的所有状态
	public static String getAllTaskStatus(RestTemplate restTemplate)
			throws IOException {
		String wayUrl = serviceUrl + "getAllTaskStatus";
		return restTemplate.getForObject(wayUrl, String.class);
	}

	// // 获取用户的发送任务列表的详细信息
	// public String getTaskByIdForReceiver(String json) {
	// String wayUrl = serviceUrl + "getTaskByIdForReceiver";
	// MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
	// param.add("json", json);
	// return restTemplate.postForObject(wayUrl, param, String.class);
	// }

}
