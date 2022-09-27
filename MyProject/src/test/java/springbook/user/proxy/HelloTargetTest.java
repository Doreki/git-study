package springbook.user.proxy;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class HelloTargetTest {

    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Toby"), is("Hello Toby"));
        assertThat(hello.sayHi("Toby"), is("Hi Toby"));
        assertThat(hello.sayThankyou("Toby"), is("Thank you Toby"));
    }

    @Test
    public void helloUppercase() {
        Hello proxiedHello = new HelloUppercase(new HelloTarget());
        assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
        assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
        assertThat(proxiedHello.sayThankyou("Toby"),is("THANK YOU TOBY"));

    }
}