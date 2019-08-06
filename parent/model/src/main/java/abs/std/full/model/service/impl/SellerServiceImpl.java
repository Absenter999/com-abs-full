package abs.std.full.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import abs.std.full.api.entity.Seller;
import abs.std.full.model.dao.SellerDao;
import abs.std.full.model.service.SellerService;

@Service
@EnableTransactionManagement
public class SellerServiceImpl implements SellerService {
	@Autowired
	private SellerDao sellerDao;

	@Transactional
	public void save(Seller seller) {
		sellerDao.save(seller);
	}


}
