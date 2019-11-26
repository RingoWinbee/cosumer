package xiaolei.gao.task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class TaskInterface {

	private final static String serviceUrl = "http://training-taskmanage-service-web/";

	private TaskInterface() {
	}

	// 修改任务和新建任务都是用这个函数
	public static void addTask(RestTemplate restTemplate, String json) {
		String wayUrl = serviceUrl + "addTask";
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("json", json);
		restTemplate.postForObject(wayUrl, param, Object.class);
	}

	// 更新任务状态
	public static void updateTaskStatus(RestTemplate restTemplate, String json) {
		String wayUrl = serviceUrl + "updateTaskStatus";
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("json", json);
		restTemplate.postForObject(wayUrl, param, Object.class);
	}

	// 获取用户的接收任务列表
	public static String getTaskListByReceiver(RestTemplate restTemplate,
			String json) {
		String wayUrl = serviceUrl + "getTaskListByReceiver" + "?json={json}";
		Map<String, String> param = new HashMap<String, String>(1);
		param.put("json", json);
		return restTemplate.getForObject(wayUrl, String.class, param);
	}

	// 获取用户的发送任务列表
	public static String getTaskListByResponsible(RestTemplate restTemplate,
			String json) {
		String wayUrl = serviceUrl + "getTaskListByResponsible"
				+ "?json={json}";
		Map<String, String> param = new HashMap<String, String>(1);
		param.put("json", json);
		return restTemplate.getForObject(wayUrl, String.class, param);
	}

	// 获取用户的接收任务列表的详细信息
	public static String getTaskByIdForReceiver(RestTemplate restTemplate,
			String json) {
		String wayUrl = serviceUrl + "getTaskByIdForReceiver" + "?json={json}";
		Map<String, String> param = new HashMap<String, String>(1);
		param.put("json", json);
		return restTemplate.getForObject(wayUrl, String.class, param);
	}

	// 获取用户的发送任务列表的详细信息
	public static String getTaskByIdForResponsible(RestTemplate restTemplate,
			String json) {
		String wayUrl = serviceUrl + "getTaskByIdForResponsible"
				+ "?json={json}";
		Map<String, String> param = new HashMap<String, String>(1);
		param.put("json", json);
		return restTemplate.getForObject(wayUrl, String.class, param);
	}

	// 上传任务附件到文件管理
	public static String uploadAttached(RestTemplate restTemplate, String json)
			throws IOException {
		String wayUrl = serviceUrl + "uploadAttached";
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("json", json);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	// 获取任务附件
	public static byte[] getAttached(RestTemplate restTemplate, String json)
			throws IOException {
		String wayUrl = serviceUrl + "getAttached" + "?json={json}";
		Map<String, String> param = new HashMap<String, String>(1);
		param.put("json", json);
		return restTemplate.getForObject(wayUrl, byte[].class, param);
	}

	// 删除任务附件
	public static String deleteAttached(RestTemplate restTemplate, String json)
			throws IOException {
		String wayUrl = serviceUrl + "deleteAttached";
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("json", json);
		return restTemplate.postForObject(wayUrl, param, String.class);
	}

	// 添加任务附件
	public static void addAttached(RestTemplate restTemplate, String json)
			throws IOException {
		String wayUrl = serviceUrl + "addAttached";
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("json", json);
		restTemplate.postForObject(wayUrl, param, Object.class);
	}

	// 获取任务附件列表
	public static String getAttachedListByTaskId(RestTemplate restTemplate,
			String json) throws IOException {
		String wayUrl = serviceUrl + "getAttachedListByTaskId" + "?json={json}";
		Map<String, String> param = new HashMap<String, String>(1);
		param.put("json", json);
		return restTemplate.getForObject(wayUrl, String.class, param);
	}

	// 根据任务Id获取所有链接信息(但只包括"TL_LINK_ID","TASK_TYPE_NAME","TL_LINK_ITEM_NAME")
	public static String getAllLinkByTaskId(RestTemplate restTemplate,
			String json) throws IOException {
		String wayUrl = serviceUrl + "getAllLinkByTaskId" + "?json={json}";
		Map<String, String> param = new HashMap<String, String>(1);
		param.put("json", json);
		return restTemplate.getForObject(wayUrl, String.class, param);
	}

	// 保存单个链接信息
	public static void addLink(RestTemplate restTemplate, String json)
			throws IOException {
		String wayUrl = serviceUrl + "addLink";
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("json", json);
		restTemplate.postForObject(wayUrl, param, Object.class);
	}

	// 删除单个链接信息
	public static void deleteLink(RestTemplate restTemplate, String json)
			throws IOException {
		String wayUrl = serviceUrl + "deleteLink";
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("json", json);
		restTemplate.postForObject(wayUrl, param, Object.class);
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
