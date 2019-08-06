package abs.std.full.web.rest.controller;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import abs.std.full.api.entity.Seller;
import abs.std.full.model.dao.impl.SellerTTDaoImpl;
import abs.std.full.model.message.service.MessageSender;
import abs.std.full.model.service.SellerService;

@Controller("stdRestController")
@RequestMapping(value = "/rest")
public class StdRestController {
	@Autowired
	private SellerService sellerService;

	@Autowired
	private SellerTTDaoImpl sellerTTDao;

	@Autowired
	private MessageSender messageSender;

	@RequestMapping("/info")
	public @ResponseBody String getBook() {
		return "Информация о сервисе";
	}

	@RequestMapping("/save")
	public @ResponseBody String save() {
		Seller seller = new Seller();
		seller.setName("Seller-" + System.currentTimeMillis());
		seller.setAddress("Address");
		sellerService.save(seller);
		return "save";
	}

	@RequestMapping("/ttsave")
	public @ResponseBody String saveWithTT() {
		Seller seller = new Seller();
		seller.setName("Seller-" + System.currentTimeMillis());
		seller.setAddress("Address");
		sellerTTDao.save(seller);
		return "save";
	}

	@RequestMapping("/send")
	public @ResponseBody String send() {
		try {
			messageSender.send("");
		} catch (JMSException e) {
			e.printStackTrace();
			return "error";
		}
		return "send";
	}
}
