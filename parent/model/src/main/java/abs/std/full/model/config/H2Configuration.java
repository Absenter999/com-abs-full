package abs.std.full.model.config;

import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.dialect.H2Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import abs.std.full.model.mdb.QueueListener;

@Configuration
public class H2Configuration {
	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		// See: application.properties
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
		dataSource.setUsername("sa");
		// dataSource.setPassword("spring.datasource.password");
		System.out.println("## getDataSource: " + dataSource);
		return dataSource;
	}

	@Bean
	@Autowired
	public TransactionTemplate createTransactionTemplate(PlatformTransactionManager transactionManager) {
		TransactionTemplate tt = new TransactionTemplate();
		tt.setIsolationLevelName("ISOLATION_READ_UNCOMMITTED");
		tt.setTimeout(30);
		tt.setTransactionManager(transactionManager);
		tt.afterPropertiesSet();
		return tt;
	}
//	JtaTransactionManager
//	LocalContainerEntityManagerFactoryBean
//	DataSourceTransactionManager

	@Bean
	@Autowired
	public JpaTransactionManager createTransactionManager(	EntityManagerFactory emf) {
		JpaTransactionManager jtm = new JpaTransactionManager();
		jtm.setEntityManagerFactory(emf);
		jtm.afterPropertiesSet();
		return jtm;
	}

	@Bean
	@Autowired
	public EntityManagerFactory entityManagerFactory(DataSource dataSource) {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabase(Database.H2);
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(false);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan(new String[] { "abs.std.full.api.entity", "abs.std.full.model.dao.impl" });
		factory.setDataSource(dataSource);
		factory.setJpaDialect(new HibernateJpaDialect());
		factory.setPersistenceUnitName("std.entityManagerFactory");
		Map<String, String> jpaProperties = new HashMap<String, String>();
		jpaProperties.put("hibernate.dialect", H2Dialect.class.getName());
		jpaProperties.put("hibernate.cache.use_second_level_cache", "true");
		jpaProperties.put("hibernate.cache.use_query_cache", "true");
		jpaProperties.put("hibernate.cache.use_minimal_puts", "true");
		jpaProperties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
		factory.setJpaPropertyMap(jpaProperties);
		factory.afterPropertiesSet();

		return factory.getObject();
	}
	
	@Bean
	public JndiObjectFactoryBean destination() {
	    JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
//	    jndiObjectFactoryBean.setJndiTemplate(jndiTemplate());
	    jndiObjectFactoryBean.setJndiName("jms/TestQueue");
	    return jndiObjectFactoryBean;
	}
	
	@Bean
	public JmsTemplate jmsTemplate() {
	    JmsTemplate jmsTemplate = new JmsTemplate();
	    jmsTemplate.setDefaultDestination((Destination)destination().getObject());
//	    jmsTemplate.setPubSubDomain(true);  //may be necessary if using topic
//	    jmsTemplate.setDefaultDestinationName("jms/TestQueue");
	    jmsTemplate.setConnectionFactory(connectionFactory());
	    return jmsTemplate;
	}
	@Bean
	public ConnectionFactory connectionFactory() {
		try {
			JndiObjectFactoryBean jndiFactory = new JndiObjectFactoryBean();
			jndiFactory.setJndiName("jms/__defaultConnectionFactory");
			jndiFactory.setResourceRef(true); // adds java:comp/env/ prefix
			jndiFactory.afterPropertiesSet(); // very important, actually causes the object to be loaded

			return (ConnectionFactory) jndiFactory.getObject();

		} catch (IllegalArgumentException | NamingException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Bean
	@Autowired
	public DefaultMessageListenerContainer queueSimpleMessageListenerContainer(QueueListener messageListener) {
	    DefaultMessageListenerContainer listenerContainer = new DefaultMessageListenerContainer();
	    listenerContainer.setConnectionFactory(connectionFactory());
	    listenerContainer.setDestination((Destination) destination().getObject());
	    listenerContainer.setMessageListener(messageListener);
//	    listenerContainer.setSessionTransacted(true);
//	    listenerContainer.setTransactionManager(jtaTransactionManager());
	    return listenerContainer;
	}
	
}
