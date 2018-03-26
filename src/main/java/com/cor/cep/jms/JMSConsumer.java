package com.cor.cep.jms;

import javax.jms.*;

import com.cor.cep.event.TemperatureEvent;
import com.cor.cep.handler.TemperatureEventHandler;
import com.cor.cep.util.RandomTemperatureEventGenerator;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消息的消费者（接受者）
 * @author liang
 *
 */

@Component
public class JMSConsumer {

    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;//默认连接用户名
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;//默认连接密码
    private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;//默认连接地址

    /** Logger */
    private static Logger LOG = LoggerFactory.getLogger(RandomTemperatureEventGenerator.class);

    /** The TemperatureEventHandler - wraps the Esper engine and processes the Events  */

    @Autowired
    private TemperatureEventHandler temperatureEventHandler;

    public void generateEvents(){
        ActiveMQConnectionFactory connectionFactory;//连接工厂
        Connection connection = null;//连接

        Session session;//会话 接受或者发送消息的线程
        Destination destination;//消息的目的地

        MessageConsumer messageConsumer;//消息的消费者

        //实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(JMSConsumer.USERNAME, JMSConsumer.PASSWORD, JMSConsumer.BROKEURL);
        connectionFactory.setTrustAllPackages(true);
        try {
            //通过连接工厂获取连接
            connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
            //创建session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //创建一个连接HelloWorld的消息队列
            destination = session.createQueue("HelloWorld");
            //创建消息消费者
            messageConsumer = session.createConsumer(destination);

            ExecutorService xrayExecutor = Executors.newSingleThreadExecutor();
            LOG.debug(getStartingMessage());

            //不断监听获取消息，并发送给Esper
            while (true) {
                /*TextMessage textMessage = (TextMessage) messageConsumer.receive();
                if(textMessage != null){
                    String receiveStr = textMessage.getText();
                    TemperatureEvent ve = new TemperatureEvent(Integer.valueOf(receiveStr), new Date());
                    temperatureEventHandler.handle(ve);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        LOG.error("Thread Interrupted", e);
                    }
                }else {
                    break;
                }*/

                ObjectMessage message=(ObjectMessage) messageConsumer.receive();

                if(message != null){

                    TemperatureEvent temperatureEvent= (TemperatureEvent) message.getObject();

                    temperatureEventHandler.handle(temperatureEvent);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        LOG.error("Thread Interrupted", e);
                    }
                }else {
                    break;
                }
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    void sendEvents(){
        ExecutorService xrayExecutor = Executors.newSingleThreadExecutor();
        LOG.debug(getStartingMessage());
    }


    private String getStartingMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n************************************************************");
        sb.append("\n* STARTING - ");
        sb.append("\n* PLEASE WAIT - TEMPERATURES ARE RANDOM SO MAY TAKE");
        sb.append("\n* A WHILE TO SEE WARNING AND CRITICAL EVENTS!");
        sb.append("\n************************************************************\n");
        return sb.toString();
    }
}