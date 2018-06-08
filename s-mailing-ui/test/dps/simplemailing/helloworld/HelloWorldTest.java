package dps.simplemailing.helloworld;

import org.junit.Assert;
import org.junit.Test;

public class HelloWorldTest {

    @Test
    public void helloTest()
    {
        HelloWorld helloWorld = new HelloWorld();
        Assert.assertEquals("Hello",helloWorld.getHello());
    }

}
