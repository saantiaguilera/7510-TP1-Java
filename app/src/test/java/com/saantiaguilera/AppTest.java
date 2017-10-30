package com.saantiaguilera;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class AppTest {

    @Test
    public void test_AppMain_RunsArgs_AsQueries() throws Exception {
        KnowledgeBase base = Mockito.spy(new KnowledgeBase());

        Field field = App.class.getDeclaredField("base");
        field.setAccessible(true);
        field.set(null, base);

        String[] queries = new String[] {
                "varon(juan).",
                "varon(pepe).",
                "varon(hector).",
                "varon(roberto).",
                "varon(alejandro).",
                "mujer(maria)."
        };

        App.main(queries);

        Mockito.verify(base, Mockito.times(queries.length)).answer(Mockito.anyString());
    }

    @Test
    public void test_AppMain_DoesntUseKnowledgeBase_IfNoQueriesSupplied() throws Exception{
        KnowledgeBase base = Mockito.spy(new KnowledgeBase());

        Field field = App.class.getDeclaredField("base");
        field.setAccessible(true);
        field.set(null, base);

        String[] queries = new String[] {};

        App.main(queries);

        Mockito.verify(base, Mockito.times(0)).answer(Mockito.anyString());
    }

    @Test
    public void test_App_CantBeInstantiated() {
        try {
            Constructor<App> constructor = App.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
            Assert.fail("Should fail! This class cant be instantiated");
        } catch (Exception ex) {
            Assert.assertTrue(ex.getCause() instanceof IllegalAccessException);
            Assert.assertEquals("Cant instantiate this class", ex.getCause().getMessage());
        }
    }

}
