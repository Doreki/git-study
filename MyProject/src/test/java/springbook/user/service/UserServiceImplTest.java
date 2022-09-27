package springbook.user.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static springbook.user.service.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoFactory.class,loader = AnnotationConfigContextLoader.class)
public class UserServiceImplTest {
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    UserDao userDao;

    @Autowired
    MailSender mailSender;
    List<User> users;

    @Autowired
    DataSource dataSource;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("gyumee", "박상철", "springno1", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER-1,0),
                new User("leegw", "이길원", "springno1", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER, 0),
                 new User("gyumee1", "박상철1", "springno1", Level.SILVER,60,MIN_RECCOMEND_FOR_GOLD-1),
                new User("leegw1", "이길원1", "springno1", Level.SILVER,60, MIN_RECCOMEND_FOR_GOLD),
                 new User("ldh1", "임도현1", "springno3",Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    public void upgradeLevels() throws Exception{
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size(), is(2));
        checkUserAndLevel(updated.get(0),"joytouch", Level.SILVER);
        checkUserAndLevel(updated.get(1),"madnite1", Level.GOLD);

        List<String> request = mockMailSender.getRequests();
        assertThat(request.size(), is(2));
        assertThat(request.get(0), is(users.get(1).getEmail()));
        assertThat(request.get(1), is(users.get(2).getEmail()));

    }

//    @Test
//    public void mockUpgradeLevels() throws Exception {
//        UserServiceImpl userServiceImpl = new UserServiceImpl();
//
//        UserDao mockUserDao = mock(UserDao.class);
//        when
//    }

    private void checkLevelUpgradede(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if(upgraded) {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
        else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }
    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithOutLevel = users.get(0);
        userWithOutLevel.setLevel(null);

        userServiceImpl.add(userWithLevel);
        userServiceImpl.add(userWithOutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithOutLevelRead = userDao.get(userWithOutLevel.getId());

        assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
        assertThat(userWithOutLevelRead.getLevel(), is(Level.BASIC));
    }
    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel(), is(expectedLevel));
    }

    static class TestUserServiceImpl extends UserServiceImpl {
    private String id;

        public TestUserServiceImpl(String id) {
            this.id = id;
        }

        @Override
        public void upgradeLevel(User user) {
            if(user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }
    static class TestUserServiceException extends RuntimeException {

    }

    @Test
    public void upgradeAllOrNothing() throws Exception{
        TestUserServiceImpl testUserService = new TestUserServiceImpl(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setMailSender(mailSender);

        UserServiceTx userServiceTx = new UserServiceTx();
        userServiceTx.setTransactionManager(transactionManager);
        userServiceTx.setUserService(testUserService);

        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        try{
            userServiceTx.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch(TestUserServiceException e) {

        }

        checkLevelUpgradede(users.get(1), false);
        userServiceImpl.setMailSender(mailSender);
    }

    static class MockMailSender implements MailSender {
        private List<String> requests = new ArrayList<String>();

        public List<String> getRequests() {
            return requests;
        }
        @Override
        public void send(SimpleMailMessage mailMessage) throws MailException {
            requests.add(mailMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage[] mailMessages) throws MailException {

        }
    }

    static class MockUserDao implements UserDao {
        private List<User> users;
        private List<User> updated = new ArrayList();

        private MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return updated;
        }

        public List<User> getAll() {
            return this.users;
        }

        public void update(User user) {
            updated.add(user);
        }

        public void add(User user) {throw new UnsupportedOperationException();}
        public void deleteAll( ) {throw new UnsupportedOperationException();}
        public User get(String id) {throw new UnsupportedOperationException();}
        public int getCount() {throw new UnsupportedOperationException();}
    }

    private  void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertThat(updated.getId(), is(expectedId));
        assertThat(updated.getLevel(), is(expectedLevel));
    }
}
