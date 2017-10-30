package com.saantiaguilera.model;

import org.junit.Assert;
import org.junit.Test;

public class MutableFactTest {

    @Test
    public void test_IfParamsLengthDiffer_Fails() {
        MutableFact fact = new MutableFact();
        fact.bind("varon(juan).");

        try {
            fact.mutate("juan", "pepe");
            Assert.fail("2 params vs binding 1");
        } catch (IncompatibleClassChangeError ex) {
            Assert.assertEquals("Param sizes to mutate differ.", ex.getMessage());
        }
    }

    @Test
    public void test_Immutability() {
        MutableFact fact = new MutableFact();
        fact.bind("varon(juan).");

        Assert.assertNotEquals(fact, fact.mutate("pepe"));
        Assert.assertNotEquals(fact.mutate("pepe"), fact.mutate("lolo"));
    }

    @Test
    public void test_ParamsChange() {
        MutableFact fact = new MutableFact();
        fact.bind("varon(juan).");
        Fact immutableFact = fact.mutate("pepe");

        Assert.assertEquals("pepe", immutableFact.params().get(0));
    }

    @Test
    public void test_NameStaysSame() {
        MutableFact fact = new MutableFact();
        fact.bind("varon(juan).");
        Fact immutableFact = fact.mutate("pepe");

        Assert.assertEquals("varon", immutableFact.name());
    }

}
