package abs.std.full.model.message.service;

import javax.jms.JMSException;

public interface MessageSender {
	public void send(String orderNumber) throws JMSException;
}
