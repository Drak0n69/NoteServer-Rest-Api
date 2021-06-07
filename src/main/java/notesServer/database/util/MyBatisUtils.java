package notesServer.database.util;

import notesServer.database.mappers.AccountMapper;
import notesServer.database.mappers.NoteMapper;
import notesServer.database.mappers.SectionMapper;
import notesServer.database.mappers.SessionMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

public class MyBatisUtils {

    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        return dataSourceBuilder.build();
    }

    private SqlSessionFactory getFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        return factoryBean.getObject();
    }

    @Bean
    public AccountMapper accountMapper() throws Exception {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(getFactory());
        return sqlSessionTemplate.getMapper(AccountMapper.class);
    }

    @Bean
    public SessionMapper sessionMapper() throws Exception {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(getFactory());
        return sqlSessionTemplate.getMapper(SessionMapper.class);
    }

    @Bean
    public SectionMapper sectionMapper() throws Exception {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(getFactory());
        return sqlSessionTemplate.getMapper(SectionMapper.class);
    }

    @Bean
    public NoteMapper noteMapper() throws Exception {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(getFactory());
        return sqlSessionTemplate.getMapper(NoteMapper.class);
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
