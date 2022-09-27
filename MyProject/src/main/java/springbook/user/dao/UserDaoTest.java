package springbook.user.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.domain.User;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserDaoTest {
        private UserDao dao;

        @Before
        public void setUp() {
            ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//            this.dao = context.getBean("userDao", UserDao.class); //UserDao 객체를 생성해서 반환해줌
//                dao.deleteAll();
        }

        @Test
        public void addANdGet() throws SQLException,ClassNotFoundException {

        User user = new User();
        user.setId("2");
        user.setName("조보미");
        user.setPassword("1234");

        dao.add(user);

        System.out.println(user.getId() + "등록성공");

        User user2 = dao.get(user.getId());

        assertThat(user2.getName(), is(user.getName()));
        assertThat(user2.getPassword(), is(user.getPassword()));
         }


}
