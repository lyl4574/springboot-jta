package cn.forever.demo.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

import cn.forever.demo.datasource.Test2DBConfig;

@Configuration
@MapperScan(basePackages = "cn.forever.demo.dao.test2", sqlSessionTemplateRef = "test2SqlSessionTemplate")
public class Test2MyBatisConfig {
	
	@Bean(name = "test2DataSource")
	public DataSource test2DataSource(Test2DBConfig test2Config) throws SQLException {
		MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
		mysqlXaDataSource.setUrl(test2Config.getUrl());
		mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
		mysqlXaDataSource.setPassword(test2Config.getPassword());
		mysqlXaDataSource.setUser(test2Config.getUsername());
		mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

		AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
		xaDataSource.setXaDataSource(mysqlXaDataSource);
		xaDataSource.setUniqueResourceName("test2DataSource");

		xaDataSource.setMinPoolSize(test2Config.getMinPoolSize());
		xaDataSource.setMaxPoolSize(test2Config.getMaxPoolSize());
		xaDataSource.setMaxLifetime(test2Config.getMaxLifetime());
		xaDataSource.setBorrowConnectionTimeout(test2Config.getBorrowConnectionTimeout());
		xaDataSource.setLoginTimeout(test2Config.getLoginTimeout());
		xaDataSource.setMaintenanceInterval(test2Config.getMaintenanceInterval());
		xaDataSource.setMaxIdleTime(test2Config.getMaxIdleTime());
		xaDataSource.setTestQuery(test2Config.getTestQuery());
		return xaDataSource;
	}

	@Bean(name = "test2SqlSessionFactory")
	public SqlSessionFactory testSqlSessionFactory(@Qualifier("test2DataSource") DataSource dataSource)
			throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath:/mybatis/test2/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "test2SqlSessionTemplate")
	public SqlSessionTemplate testSqlSessionTemplate(
			@Qualifier("test2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
