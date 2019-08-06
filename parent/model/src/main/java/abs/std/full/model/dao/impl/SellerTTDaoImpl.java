package abs.std.full.model.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import abs.std.full.api.entity.Seller;
import abs.std.full.model.dao.SellerDao;

@Service
public class SellerTTDaoImpl {
	@Autowired
	private SellerDao sellerDao;
	
	@Autowired
	private TransactionTemplate tt; 

	public void save(final Seller seller) {
		tt.execute((transactionStatus) -> {
			sellerDao.save(seller);
			return 1;
		});
	}

	public void setSellerDao(SellerDao sellerDao) {
		this.sellerDao = sellerDao;
	}

	public void setTt(TransactionTemplate tt) {
		this.tt = tt;
	}

}
