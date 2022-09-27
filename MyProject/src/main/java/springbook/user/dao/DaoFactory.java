package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import springbook.user.service.DummyMailSender;
import springbook.user.service.UserService;
import springbook.user.service.UserServiceImpl;
import springbook.user.service.UserServiceTx;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {



    @Bean
    public UserDaoJdbc userDao() {
        UserDaoJdbc userDao = new UserDaoJdbc();
        userDao.setDataSource(dataSource());
        return userDao;
    }


    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(org.mariadb.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mariadb://127.0.0.1:3306/user");
        dataSource.setUsername("user");
        dataSource.setPassword("123456");

        return dataSource;
    }

    @Bean
    public UserService userService() {
            UserServiceTx service = new UserServiceTx();
            service.setTransactionManager(transactionManager());
            service.setUserService(userServiceImpl());
            return service;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public MailSender mailSender() {return new DummyMailSender();}

    @Bean
    public UserServiceImpl userServiceImpl() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        userServiceImpl.setUserDao(userDao());
        userServiceImpl.setMailSender(mailSender());

        return userServiceImpl;
    }
}
