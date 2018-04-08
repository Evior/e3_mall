import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;

public class activeMQ {

    /**
     * 点到点
     */
    @Test
    public void test_Product() throws JMSException {

        //1创建一个连接工厂的对象，需要指定服务的ip和端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
        //2使用一个工厂创建一个Connection对象
        Connection connection = connectionFactory.createConnection();
        //3开启连接
        connection.start();
        //4创建session
        //第一个参数是否开启事务
        //第二个参数是(不开启事务才有意义)应答模式 ：自动 Session.AUTO_ACKNOWLEDGE 手动Session.CLIENT_ACKNOWLEDGE
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5使用一个session创建一个Destination
        Queue queue = session.createQueue("test_Queue");
        //6使用一个session创建一个Production
        MessageProducer producer = session.createProducer(queue);
        //7创建一个Message 可以使用TextMessage
        //TextMessage text = new ActiveMQTextMessage();
        // text.setText("hello activeMQ");
        TextMessage textMessage = session.createTextMessage("hello activeMQ");
        //8发送信息
        producer.send(textMessage);
        //9关闭资源
        producer.close();
        session.close();
        connection.close();
    }


    @Test
    public void test_Consumer() throws Exception {

        //1创建一个连接工厂的对象，需要指定服务的ip和端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
        //2使用一个工厂创建一个Connection对象
        Connection connection = connectionFactory.createConnection();
        //3开启连接
        connection.start();
        //4创建session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5使用一个session创建一个Destination
        Queue q = session.createQueue("spring-queue");
        //6使用一个session创建一个Consumer
        MessageConsumer consumer = session.createConsumer(q);
        //8接受消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage message1= (TextMessage) message;
                try {
                    //9打印结果
                    String text = message1.getText();
                    System.out.println(text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //10等待接收消息
        System.in.read();
        //11关闭资源
        consumer.close();
        session.close();
        connection.close();
    }


    @Test
    public void test2_Product() throws Exception {
        //1创建一个连接工厂的对象，需要指定服务的ip和端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
        //2使用一个工厂创建一个Connection对象
        Connection connection = connectionFactory.createConnection();
        //3开启连接
        connection.start();
        //4创建session
        //第一个参数是否开启事务
        //第二个参数是(不开启事务才有意义)应答模式 ：自动 Session.AUTO_ACKNOWLEDGE 手动Session.CLIENT_ACKNOWLEDGE
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5使用一个session创建一个Destination
        Topic topic = session.createTopic("test_Topic");
        //6使用一个session创建一个Production
        MessageProducer producer = session.createProducer(topic);
        //7创建一个Message 可以使用TextMessage
        //TextMessage text = new ActiveMQTextMessage();
        // text.setText("hello activeMQ");
        TextMessage textMessage = session.createTextMessage("hello activeMQ topic");
        //8发送信息
        producer.send(textMessage);
        //9关闭资源
        producer.close();
        session.close();
        connection.close();




    }


    @Test
    public void  test3_Consumer() throws Exception{

//1创建一个连接工厂的对象，需要指定服务的ip和端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
        //2使用一个工厂创建一个Connection对象
        Connection connection = connectionFactory.createConnection();
        //3开启连接
        connection.start();
        //4创建session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5使用一个session创建一个Destination
        Topic topic = session.createTopic("test_Topic");
        //6使用一个session创建一个Consumer
        MessageConsumer consumer = session.createConsumer(topic);
        //8接受消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage message1= (TextMessage) message;
                try {
                    //9打印结果
                    String text = message1.getText();
                    System.out.println(text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //10等待接收消息
        System.in.read();
        //11关闭资源
        consumer.close();
        session.close();
        connection.close();

    }



    @Test
    public void  test4_Consumer() throws Exception{

//1创建一个连接工厂的对象，需要指定服务的ip和端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
        //2使用一个工厂创建一个Connection对象
        Connection connection = connectionFactory.createConnection();
        //3开启连接
        connection.start();
        //4创建session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5使用一个session创建一个Destination
        Topic topic = session.createTopic("test_Topic");
        //6使用一个session创建一个Consumer
        MessageConsumer consumer = session.createConsumer(topic);
        //8接受消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage message1= (TextMessage) message;
                try {
                    //9打印结果
                    String text = message1.getText();
                    System.out.println(text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //10等待接收消息
        System.in.read();
        //11关闭资源
        consumer.close();
        session.close();
        connection.close();

    }
}
