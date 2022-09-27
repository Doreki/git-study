package springbook.user.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoFactory.class,loader = AnnotationConfigContextLoader.class)
public class UserDaoTest {

//    @Autowired
//    private ApplicationContext context;
    @Autowired
    private UserDao dao;
    @Autowired
    DataSource dataSource;
    private User user1;
    private User user2;
    private User user3;



    @Before
    public void setUp() {
        this.user1 = new User("gyumee", "박상철", "springno1", Level.BASIC,1,0);
        this.user2 = new User("leegw", "이길원", "springno1", Level.SILVER,55, 10);
        this.user3 = new User("ldh", "임도현", "springno3",Level.GOLD, 100, 30);
    }


    @Test
    public void addAndGet(){

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));
//        User user = new User();
//        user.setId("2");
//        user.setName("조보미");
//        user.setPassword("1234");

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User userget1 = dao.get(user1.getId());
        checkSameUser(userget1, user1);

        User userget2 = dao.get(user2.getId());
        checkSameUser(userget2, user2);
    }

    @Test
    public void count() throws SQLException, ClassNotFoundException {


        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(),is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure(){
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id");
    }

    @Test
    public void getAll() {
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assertThat(users0.size(), is(0));

        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertThat(users1.size(), is(1));
        checkSameUser(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size(), is(2));
        checkSameUser(user1, users1.get(0));
        checkSameUser(user2, users1.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size(), is(3));
        checkSameUser(user3, users1.get(0));
        checkSameUser(user1, users1.get(1));
        checkSameUser(user2, users1.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getLogin(), is(user2.getLogin()));
        assertThat(user1.getRecommend(), is(user2.getRecommend()));
    }

    @Test
    public void update() {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user2);

        user1.setName("오민규");
        user1.setPassword("springno6");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1, user1update);
        User user2same = dao.get(user2.getId());
        checkSameUser(user2, user2same);
    }
//    @Test(expected = DataAccessException.class)
//    public void duplicateKey() {
//        dao.add(user1);
//        dao.add(user1);
//    }
//    @Test
//    public void sqlExceptionTranslate() {
//        dao.deleteAll();
//
//        try{
//            dao.add(user1);
//            dao.add(user1);
//        }
//        catch (DuplicateKeyException ex) {
//            SQLException sqlEX = (SQLException) ex.getRootCause();
//            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
//
//            assertThat(set.translate(null, null, sqlEX)),is(DuplicateKeyException.class);
//        }
//    }
}
