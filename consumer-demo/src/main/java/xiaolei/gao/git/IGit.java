package xiaolei.gao.git;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class IGit {

	private String serviceUrl = "http://Training-Git-Service/";
	private RestTemplate restTemplate;

	public IGit(RestTemplate restTemplate) {
		super();
		this.restTemplate = restTemplate;
	}

	/**
	 * 在linux的gitroot目录建立远程仓库
	 * 
	 * @param remoteName(远程仓库名)
	 */
	public void createRemote(String remoteName) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("remoteName", remoteName);
		String wayUrl = serviceUrl + "createRemote";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 在本机的目录建立本地远程仓库
	 * 
	 * @param gitName(本地远程仓库名字，不要与本地仓库名冲突)
	 * @throws MyGitExcepition
	 */
	public void createLocalRemote(String gitName) throws MyGitExcepition {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("gitName", gitName);
		String wayUrl = serviceUrl + "createLocalRemote";
		int result = restTemplate.postForObject(wayUrl, param, int.class);
		if (result == 0)
			throw new MyGitExcepition("仓库名重复!");
	}

	/**
	 * 用于创建Git文件目录,如果没有此目录会自动创建
	 * 
	 * @param gitName(本地仓库名字，不要与本地仓库名冲突)
	 * @throws MyGitExcepition
	 */
	public void creatLocalGit(String gitName) throws MyGitExcepition {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("gitName", gitName);
		String wayUrl = serviceUrl + "creatLocalGit";
		int result = restTemplate.postForObject(wayUrl, param, int.class);
		if (result == 0)
			throw new MyGitExcepition("仓库名重复!");
	}

	/**
	 * 用于Git-add文件到缓冲区
	 * 
	 * @param gitName(本地仓库名字)
	 * @param fileName(文件名,所有的话用*)
	 */
	public void add(String gitName, String fileName) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("gitName", gitName);
		param.add("fileName", fileName);
		String wayUrl = serviceUrl + "add";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 用于Git-rm -f将文件移出缓冲区和工作区,没有add直接rm会无效,有add但没有commit直接rm会git status看不到记录,
	 * 
	 * @param gitName(本地仓库名字)
	 * @param fileName(文件名,所有的话用*)
	 */
	public void rm(String gitName, String fileName) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("gitName", gitName);
		param.add("fileName", fileName);
		String wayUrl = serviceUrl + "rm";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 用于Git-commit文件到区
	 * 
	 * @param gitName(本地仓库名字)
	 * @param massage(提交信息)
	 */
	public void commit(String gitName, String massage) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("gitName", gitName);
		param.add("massage", massage);
		String wayUrl = serviceUrl + "commit";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 查看git文件夹状态(相当于git status)
	 * 
	 * @param gitName(本地仓库名字)
	 */
	public StatusList getStatus(String gitName) {
		// restTemplate.getForObject不知道为什么只能在url后面接参数来传参,postForObject就可以方法加参数,而且不管被调用的方法是post还是get都可以用postForObject
		String wayUrl = serviceUrl + "getStatus?gitName=" + gitName;
		return restTemplate.getForObject(wayUrl, StatusList.class);
	}

	/**
	 * 查看git提交日志(相当于git log)
	 * 
	 * @param gitName(本地仓库名字)
	 */
	public List<LogEnity> getLog(String gitName) {
		// restTemplate.getForObject不知道为什么只能在url后面接参数来传参,postForObject就可以方法加参数,而且不管被调用的方法是post还是get都可以用postForObject
		String wayUrl = serviceUrl + "getLog?gitName=" + gitName;
		LogEnity[] ls = restTemplate.getForObject(wayUrl, LogEnity[].class);
		List<LogEnity> logs = new ArrayList<LogEnity>();
		for (LogEnity l : ls) {
			logs.add(l);
		}
		return logs;
	}

	/**
	 * 回退到上一个版本(相当于git reset --hard HEAD^)
	 * 
	 * @param gitName(本地仓库名字)
	 */
	public void resetLastOne(String gitName) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("gitName", gitName);
		String wayUrl = serviceUrl + "resetLastOne";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 回退到指定版本(相当于git reset --hard 版本号)
	 * 
	 * @param gitName(本地仓库名字)
	 * @param version(版本号)
	 */
	public void reset(String gitName, String version) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("gitName", gitName);
		param.add("version", version);
		String wayUrl = serviceUrl + "reset";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 创建分支(相当于git branch <name> )
	 * 
	 * @param gitName(本地仓库名字)
	 * @param bname(分支名)
	 */
	public void createBranch(String gitName, String bname) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("gitName", gitName);
		param.add("bname", bname);
		String wayUrl = serviceUrl + "createBranch";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 把某个分支合并到当前分支(相当于git merge <name>)
	 * 
	 * @param gitName(本地仓库名字)
	 * @param bname(分支名)
	 */
	public void mergeBranch(String gitName, String bname) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("gitName", gitName);
		param.add("bname", bname);
		String wayUrl = serviceUrl + "mergeBranch";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 删除分支(相当于git branch -d<name> )
	 * 
	 * @param gitName(本地仓库名字)
	 * @param bname(分支名)
	 */
	public void deleteBranch(String gitName, String bname) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("gitName", gitName);
		param.add("bname", bname);
		String wayUrl = serviceUrl + "deleteBranch";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 切换分支(相当于git checkout <name> 如果有文件没有commit就不会执行)
	 * 
	 * @param gitName(本地仓库名字)
	 * @param bname(分支名)
	 */
	public void checkOutBranch(String gitName, String bname) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("gitName", gitName);
		param.add("bname", bname);
		String wayUrl = serviceUrl + "checkOutBranch";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 把获取当前仓库的所有分支名(相当于git branch )
	 * 
	 * @param gitName(本地仓库名字)
	 */
	public List<String> showAllBranch(String gitName) {
		// restTemplate.getForObject不知道为什么只能在url后面接参数来传参,postForObject就可以方法加参数,而且不管被调用的方法是post还是get都可以用postForObject
		String wayUrl = serviceUrl + "showAllBranch?gitName=" + gitName;
		String[] bs = restTemplate.getForObject(wayUrl, String[].class);
		List<String> lbs = new ArrayList<String>();
		for (String b : bs) {
			lbs.add(b);
		}
		return lbs;
	}

	/**
	 * 把远程仓库clone到指定目录(相当于git clone )默认显示mastet分支,其他分支要自己切换
	 * 
	 * @param remoteName(远程仓库的名字)
	 * @param gitName(本地仓库名字)
	 * @throws MyGitExcepition
	 */
	public void clone(String remoteName, String gitName) throws MyGitExcepition {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("remoteName", remoteName);
		param.add("gitName", gitName);
		String wayUrl = serviceUrl + "clone";
		int result = restTemplate.postForObject(wayUrl, param, int.class);
		if (result == 0)
			throw new MyGitExcepition("仓库名重复!");
	}

	/**
	 * 把本地远程仓库clone到本地指定目录(相当于git clone )默认显示mastet分支,其他分支要自己切换
	 * 
	 * @param localRemotePath(本地远程仓库的路径)
	 * @param gitName(本地仓库名字)
	 * @throws MyGitExcepition
	 */
	public void cloneLocalRemote(String localRemotePath, String gitName) throws MyGitExcepition {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("localRemotePath", localRemotePath);
		param.add("gitName", gitName);
		String wayUrl = serviceUrl + "cloneLocalRemote";
		int result = restTemplate.postForObject(wayUrl, param, int.class);
		if (result == 0)
			throw new MyGitExcepition("仓库名重复!");
	}

	/**
	 * 向远程仓库某个分支push((相当于git push)) 或者用于新建分支向远程仓库的推送
	 * 
	 * @param remoteName(远程仓库的名字)
	 * @param bName(分支名)
	 * @param gitName(本地仓库名字)
	 */
	public void push(String remoteName, String bName, String gitName) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("remoteName", remoteName);
		param.add("bName", bName);
		param.add("gitName", gitName);
		String wayUrl = serviceUrl + "push";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 向本地远程仓库某个分支push((相当于git push)) 或者用于新建分支向远程仓库的推送
	 * 
	 * @param localRemotePath(本地远程仓库的名字)
	 * @param bName(分支名)
	 * @param gitName(本地仓库名字)
	 */
	public void pushLocalRemote(String localRemotePath, String bName, String gitName) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("localRemotePath", localRemotePath);
		param.add("bName", bName);
		param.add("gitName", gitName);
		String wayUrl = serviceUrl + "pushLocalRemote";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 从远程仓库拉取特定分支((相当于git pull))
	 * 
	 * @param remoteName(远程仓库的名字)
	 * @param bName(分支名)
	 * @param gitName(本地仓库名字)
	 */
	public void pull(String remoteName, String bName, String gitName) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("remoteName", remoteName);
		param.add("bName", bName);
		param.add("gitName", gitName);
		String wayUrl = serviceUrl + "pull";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 从本地远程仓库拉取特定分支((相当于git pull))
	 * 
	 * @param localRemotePath(本地远程仓库的名字)
	 * @param bName(分支名)
	 * @param gitName(本地仓库名字)
	 */
	public void pullLocalRemote(String localRemotePath, String bName, String gitName) {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("localRemotePath", localRemotePath);
		param.add("bName", bName);
		param.add("gitName", gitName);
		String wayUrl = serviceUrl + "pullLocalRemote";
		restTemplate.postForObject(wayUrl, param, String.class);
	}

	/**
	 * 判断本地是否有这个git仓库
	 * 
	 * @param gitName 仓库名
	 */
	public boolean isHave(String gitName) {
		String wayUrl = serviceUrl + "isHave?gitName="+gitName;
		return restTemplate.getForObject(wayUrl, boolean.class);
	}
	
	/**
	 * 获取某个git仓库下的某个commit下的某个文件内容
	 * @param gitName 文件所在的仓库名
	 * @param commitId 提交版本号
	 * @param path 文件在仓库的全路径
	 */
	public byte[] readFileFromCommit(String gitName,String commitId,String path) {
		String wayUrl = serviceUrl + "readFileFromCommit?gitName="+gitName+"&commitId="+commitId+"&path="+path;
		return restTemplate.getForObject(wayUrl, byte[].class);
	}
	
	/**
	 * 获取某个git仓库下的所有commitId(由新到旧)
	 * @param gitName 仓库名
	 */
	public String listCommitId(String gitName) {
		String wayUrl = serviceUrl + "listCommitId?gitName="+gitName;
		return restTemplate.getForObject(wayUrl, String.class);
	}
	
	/**
	 * 获取某个git仓库下的某次提交的某个文件夹下的文件列表
	 * @param gitName 文件所在的仓库名
	 * @param commitId 提交版本号
	 * @param path 要查看的文件夹(如想查看fff文件夹下的文件列表就输入fff)
	 */
	public String listFilesOfCommit(String gitName,String commitId,String path) {
		String wayUrl = serviceUrl + "listFilesOfCommit?gitName="+gitName+"&commitId="+commitId+"&path="+path;
		return restTemplate.getForObject(wayUrl, String.class);
	}
	
	/**
	 * 获取某个git仓库下的某次提交的根路径下的文件列表
	 * @param gitName 文件所在的仓库名
	 * @param commitId 提交版本号
	 */
	public String listFilesOfCommit(String gitName,String commitId) {
		String wayUrl = serviceUrl + "listFilesOfCommit?gitName="+gitName+"&commitId="+commitId;
		return restTemplate.getForObject(wayUrl, String.class);
	}
}
