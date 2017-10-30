package com.saantiaguilera.model;

import com.saantiaguilera.model.contracts.Bindable;
import org.junit.Assert;
import org.junit.Test;

public class FactTest {

    @Test
    public void test_FactParamsReturnsParams() {
        Fact fact = new Fact();
        fact.bind("varon(juan, maria, p).");
        Assert.assertEquals("juan", fact.params().get(0));
        Assert.assertEquals("maria", fact.params().get(1));
        Assert.assertEquals("p", fact.params().get(2));
    }

    @Test
    public void test_BindingCanOnlyHappenOnce() {
        Fact fact = new Fact();
        fact.bind("varon(juan).");

        try {
            fact.bind("mujer(maria).");
            Assert.fail("Already bound");
        } catch (Bindable.BoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("Already bound to varon"));
        }
    }

    @Test
    public void test_BindingParsesCorrectly() {
        Fact fact = new Fact();
        fact.bind("varon        (       juan , pepe )     .            ");

        Assert.assertEquals("varon", fact.name());
        Assert.assertEquals("juan", fact.params().get(0));
        Assert.assertEquals("pepe", fact.params().get(1));
    }

    @Test
    public void test_NameCantBeNull() {
        Fact fact = new Fact();

        try {
            fact.name();
            Assert.fail("Should have thrown illegalstateexception");
        } catch (IllegalStateException ex) {
            Assert.assertTrue(ex.getMessage().contains("Forgot to bind"));
        }
    }

    @Test
    public void test_ParamsCantBeNull() {
        Fact fact = new Fact();

        try {
            fact.params();
            Assert.fail("Should have thrown illegalstateexception");
        } catch (IllegalStateException ex) {
            Assert.assertTrue(ex.getMessage().contains("Forgot to bind"));
        }
    }

    @Test
    public void test_FactNameReturnsName() {
        Fact fact = new Fact();
        fact.bind("varon(juan).");
        Assert.assertEquals("varon", fact.name());
    }

    @Test
    public void test_Matches_WithWrongNameFact() {
        Fact fact1 = new Fact();
        fact1.bind("varon(juan).");

        Fact fact2 = new Fact();
        fact2.bind("mujer(juan).");

        Assert.assertFalse(fact2.matches(fact1));
    }

    @Test
    public void test_Matches_WithWrongParamFact() {
        Fact fact1 = new Fact();
        fact1.bind("varon(juan, pepe).");

        Fact fact2 = new Fact();
        fact2.bind("varon(juan, maria).");

        Assert.assertFalse(fact2.matches(fact1));
    }

    @Test
    public void test_Equals_IsCloseToSameAsMatch() {
        Fact fact1 = new Fact();
        fact1.bind("varon(juan).");

        Fact fact2 = new Fact();
        fact2.bind("varon(juan).");

        Assert.assertTrue(fact2.equals(fact1));
        Assert.assertFalse(fact2.equals("varon(juan)."));
    }

    @Test
    public void test_HashCode_TakesParamsIntoAccount() throws Exception {
        Fact fact1 = new Fact();
        fact1.bind("varon(juan).");

        int hashcode = fact1.hashCode();

        Fact.class.getDeclaredField("name").set(fact1, "mujer");

        Assert.assertNotEquals(hashcode, fact1.hashCode());
    }

    @Test
    public void test_Matches_WithSameFact() {
        Fact fact1 = new Fact();
        fact1.bind("varon(juan, pepe, c, d, e).");

        Fact fact2 = new Fact();
        fact2.bind(" v a r o    n      (      j u  an     , pe pe,      c , d  ,e   )    . ");

        Assert.assertTrue(fact2.matches(fact1));
    }

}
