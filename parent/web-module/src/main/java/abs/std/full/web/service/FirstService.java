package abs.std.full.web.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import abs.std.full.ejb.controller.StdService;

@Service
public class FirstService {
	@Autowired
	StdService std;

	@PostConstruct
	public void init() {
		std.print();
	}
}
