package abs.std.full.model.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import abs.std.full.api.entity.Seller;
import abs.std.full.model.dao.SellerDao;

@Repository
public class SellerDaoImpl implements SellerDao {

	@PersistenceContext(unitName = "std.entityManagerFactory")
	private EntityManager em;

	public void save(Seller seller) {
		em.persist(seller);
		em.flush();
	}
}
