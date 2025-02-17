//package com.ict.vita.config;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import jakarta.persistence.EntityManagerFactory;
//
//@Configuration
//@EnableJpaRepositories( //JPA 가 사용될 위치를 지정
//		//basePackages에 리포지토리 인터페이스가 있는 패키지 지정
//        basePackages = "com.ict.vita.repository.postgres", 
//        entityManagerFactoryRef = "postgresEntityManagerFactory",
//        transactionManagerRef = "postgresTransactionManager"
//)
////[포스트그레 DB 설정]
//public class PostgresDatabaseConfig {
//	//포스트그레 DB는 보조 데이터베이스라 @Primary 어노테이션 제외한다
//	
//	@Bean(name = "postgresDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().build();
//    }
//	
//    @Bean(name = "postgresEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//    	LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource());
//        em.setPackagesToScan(new String[] {""});
//        
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        vendorAdapter.setShowSql(true);
//        vendorAdapter.setGenerateDdl(true);
//        em.setJpaVendorAdapter(vendorAdapter);
//        
//        return em;
//    }
//
//    @Bean(name = "postgresTransactionManager")
//    public PlatformTransactionManager transactionManager() {
//    	JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
//        return transactionManager;
//    }
//
//}
