package xiaolei.gao.task;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class TaskInterfaceTest {

	
	@Test
	public void test() {
		RestTemplate rs=new RestTemplate();
		Object o=rs.getForObject("http://192.168.32.254:8768/test1", String.class);
		System.out.println(o.getClass());
	}

}
