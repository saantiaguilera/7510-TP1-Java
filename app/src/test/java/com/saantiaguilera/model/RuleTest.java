package com.saantiaguilera.model;

import com.saantiaguilera.ResourcesUtil;
import com.saantiaguilera.model.contracts.Bindable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ResourcesUtil.class})
public class RuleTest {

    @Test
    public void test_Binding_CanOnlyHappenOnce() {
        Rule rule = new Rule();
        rule.bind("varon(a,b) := j(b), d(a,b)");

        try {
            rule.bind("varon(a,b) := j(b), d(a,b)");
            Assert.fail("Already bound");
        } catch (Bindable.BoundException ex) {
            Assert.assertEquals("Already bound.", ex.getMessage());
        }
    }

    @Test
    public void test_Binding_BindsFact() {
        Rule rule = new Rule();
        rule.bind("varon(a,b) := j(b), d(a,b)");

        Fact fact = new Fact();
        fact.bind("varon(a,b).");

        Assert.assertTrue(fact.matches(rule.name()));
    }

    @Test
    public void test_Binding_BindsParams() {
        Rule rule = new Rule();
        rule.bind("varon(a,b) := j(b), d(b,a)");

        Fact j = new Fact();
        j.bind("j(b).");

        Fact d = new Fact();
        d.bind("d(b,a).");

        Assert.assertTrue(j.matches(rule.params().get(0)));
        Assert.assertTrue(d.matches(rule.params().get(1)));
    }

    @Test
    public void test_Name_CantBeNull() {
        Rule rule = new Rule();

        try {
            rule.name();
            Assert.fail("Should throw illegalstateex");
        } catch (IllegalStateException ex) {
            Assert.assertEquals("Name not found. Forgot to bind?", ex.getMessage());
        }
    }

    @Test
    public void test_Params_CantBeNull() {
        Rule rule = new Rule();

        try {
            rule.params();
            Assert.fail("Should throw illegalstateex");
        } catch (IllegalStateException ex) {
            Assert.assertEquals("Params not found. Forgot to bind?", ex.getMessage());
        }
    }

    @Test
    public void test_ZipMapWorks() {
        Rule rule = new Rule();
        rule.bind("asd(a,b,c,d) := q(a,b), j(d,c), p(d,b,c,a)");

        Fact fact = new Fact();
        fact.bind("varon(arjona, brian, carlos, delfina).");

        HashMap<String, String> zipmap = rule.zipmap(fact);

        Assert.assertEquals("arjona", zipmap.get("a"));
        Assert.assertEquals("brian", zipmap.get("b"));
        Assert.assertEquals("carlos", zipmap.get("c"));
        Assert.assertEquals("delfina", zipmap.get("d"));
    }

    @Test
    public void test_ZipMapFails_IfDefinedRuleHasDifferentParamSize() {

        Rule rule = new Rule();
        rule.bind("asd(a,b,c,d) := q(a,b), j(d,c), p(d,b,c,a)");

        Fact fact = new Fact();
        fact.bind("varon(arjona, brian).");

        try {
            rule.zipmap(fact);
            Assert.fail("Should fail because of different sizes");
        } catch (IllegalStateException ex) {
            Assert.assertEquals("Rule has same name, but defined params dont have same size as query", ex.getMessage());
        }
    }

    @Test
    public void test_Matches_ReturnsFalse_IfNameIsWrong() {
        Rule rule = new Rule();
        rule.bind("papa(a, b) := varon(a), varon(b)");

        Fact fact = new Fact();
        fact.bind("mama(lucia, laura).");

        Assert.assertFalse(rule.matches(fact));
    }

    @Test
    public void test_Matches_ReturnsFalse_IfParamsAreWrong() {
        Rule rule = new Rule();
        rule.bind("hijo(X, Y) :- varon(X), padre(Y, X).");

        Fact fact = new Fact();
        fact.bind("hijo(pepe, maria).");

        Assert.assertFalse(rule.matches(fact));
    }

    @Test
    public void test_Matches_ReturnsTrue_IfItsCorrect() {
        Rule rule = new Rule();
        rule.bind("hijo(X, Y) :- varon(X), padre(Y, X).");

        Fact fact = new Fact();
        fact.bind("hijo(pepe, juan).");

        Assert.assertFalse(rule.matches(fact));
    }

    @Test
    public void test_Matches_ReturnsFalse_IfAnErrorOccurs() throws Exception {
        PowerMockito.mockStatic(ResourcesUtil.class);

        PowerMockito.when(ResourcesUtil.getResource(Mockito.anyString())).thenThrow(NullPointerException.class);

        Rule rule = new Rule();
        rule.bind("hijo(X, Y) :- varon(X), padre(Y, X).");

        Fact fact = new Fact();
        fact.bind("hijo(pepe, juan).");

        // Which should be technically true, but there was an error obtaining the db
        Assert.assertFalse(rule.matches(fact));
    }

}
