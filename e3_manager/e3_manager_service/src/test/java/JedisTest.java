import cn.e3mall.comment.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JedisTest {

   /* @Test
    public void test1(){
        //初始化Spring容器
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        //从容器中获取JedisClient对象
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        System.out.println(jedisClient);
        jedisClient.set("k1","v1");
        String k1 = jedisClient.get("k1");
        System.out.println(k1);

    }*/




}
