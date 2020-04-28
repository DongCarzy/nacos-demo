package discovery;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author carzy
 */
@RestController
@RequestMapping("server/b")
public class TestController {

    private RestTemplate restTemplate = new RestTemplate();

    @NacosInjected
    private NamingService namingService;
    @Value("${nacos.config.group}")
    private String groupName;
    @Value("${server.port}")
    private int port;

    @GetMapping(value = "/get")
    public List<Instance> get(@RequestParam String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName, groupName);
    }

    @GetMapping(value = "/test")
    public String test() {
        return "this is server b: " + port;
    }

    @GetMapping(value = "/a")
    public String a() throws NacosException {
        Instance instance = namingService.selectOneHealthyInstance("nacos-server-a", groupName);
        String url = String.format("http://%s:%d/server/a/test", instance.getIp(), instance.getPort());
        return this.restTemplate.getForObject(url, String.class);
    }
}
