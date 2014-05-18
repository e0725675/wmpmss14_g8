package at.tuwien.sentimentanalyzer.beans;

import at.tuwien.sentimentanalyzer.entities.Foo;

/**
 * Sample Bean that generates and process Foo.
 */
public class FooBean {

    private static int counter = 0;

    public Foo generateFoo() {
    	counter++;
    	
        Foo answer = new Foo();
        answer.setDescription("Foo "+counter);
        return answer;
    }

    public String processFoo(Foo foo) {
        return "Processed Foo id " + foo.getId() + " desc " + foo.getDescription();
    }
}
