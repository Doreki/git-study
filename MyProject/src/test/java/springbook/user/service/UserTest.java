package springbook.user.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import static org.hamcrest.Matchers.is;

public class UserTest {
    User user;

    @Before
    public void setUp() {
        user = new User();
    }

    @Test()
    public void upgradeLevel() {
        Level[] levels = Level.values();
        for(Level level : levels) {
            if(level.nextLevel()==null) continue;
            user.setLevel(level);
            user.upgradeLevel();
            Assert.assertThat(user.getLevel(), is(level.nextLevel()));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void cannotUpgeradeLevel() {
        Level[] levels = Level.values();
        for(Level level: levels) {
            if(level.nextLevel() !=null) continue;
            user.setLevel(level);
            user.upgradeLevel();
        }
    }
}
