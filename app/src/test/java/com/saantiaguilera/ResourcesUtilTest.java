package com.saantiaguilera;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ResourcesUtilTest {

    @Test
    public void test_CantInstantiateClass() throws Exception {
        try {
            Constructor<ResourcesUtil> constructor = ResourcesUtil.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
            Assert.fail("Cant create class even with reflection");
        } catch (InvocationTargetException ex) {
            Assert.assertTrue(ex.getTargetException() instanceof IllegalAccessException);
            Assert.assertEquals("Shouldnt instantiate this", ex.getTargetException().getMessage());
        }
    }

    @Test
    public void test_GetResources_GivesResource() throws Exception {
        Assert.assertNotNull(ResourcesUtil.getResource("rules.db"));
    }

    @Test
    public void test_GetResources_ThrowsFileNotFound_IfNoResource() throws Exception {
        try {
            ResourcesUtil.getResource("asdfasd");
            Assert.fail("File shouldnt exist");
        } catch (FileNotFoundException ex) {
            Assert.assertNotNull(ex);
        }
    }

}