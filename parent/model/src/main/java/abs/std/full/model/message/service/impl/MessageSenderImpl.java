package abs.std.full.model.message.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import abs.std.full.model.message.service.MessageSender;

@Service
public class MessageSenderImpl implements MessageSender {

	@Autowired
	private JmsTemplate jmsTemplate;

	public void send(String orderNumber) throws JMSException {

		jmsTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {

				TextMessage message = session.createTextMessage();
				message.setText("Сообщение");
				return message;
			}
		});

	}

}
