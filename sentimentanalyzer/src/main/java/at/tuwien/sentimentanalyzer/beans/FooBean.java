package at.tuwien.sentimentanalyzer.beans;

import java.util.Random;

import at.tuwien.sentimentanalyzer.entities.Foo;

/**
 * Sample Bean that generates and process Foo.
 */
public class FooBean {

    private int counter;
    private Random ran = new Random();

    public Foo generateFoo() {
    	counter++;
    	
        Foo answer = new Foo();
        answer.setDescription(counter % 2 == 0 ? "Camel in Action" : "ActiveMQ in Action "+ran.nextInt());
        return answer;
    }

    public String processFoo(Foo order) {
        return "Processed Foo id " + order.getId() + " desc " + order.getDescription();
    }
}
