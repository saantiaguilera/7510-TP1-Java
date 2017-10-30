package com.saantiaguilera;

import org.junit.Assert;
import org.junit.Test;

public final class KnowledgeBaseTest {

	@Test
	public void test_WrongFactName_ReturnsFalse() {
		Assert.assertFalse(new KnowledgeBase().answer("noexiste(hector)."));
	}

	@Test
	public void test_WrongFactParam_ReturnsFalse() {
		Assert.assertFalse(new KnowledgeBase().answer("hermano(nicolas, hector)."));
	}

	@Test
	public void test_SwitchedFactParam_ReturnsFalse() {
		Assert.assertFalse(new KnowledgeBase().answer("padre(pepe, juan)."));
	}

	@Test
	public void test_GoodFact_ReturnsTrue() {
		Assert.assertTrue(new KnowledgeBase().answer("padre(juan, pepe)."));
	}

	@Test
	public void test_GoodFact_WithSalts_ReturnTrue() {
		Assert.assertTrue(new KnowledgeBase().answer("   p a d    r  e  ( j u   an ,    pe   p   e   )    .   "));
	}

	@Test
	public void test_WrongRuleParams_ReturnsFalse() {
		Assert.assertFalse(new KnowledgeBase().answer("hijo(juan, pepe)."));
	}

	@Test
	public void test_MalformedRuleParams_ReturnsFalse() {
		Assert.assertFalse(new KnowledgeBase().answer("hijo(juan, pepe, maria)."));
	}

	@Test
	public void test_GoodRule_ReturnsTrue() {
		Assert.assertFalse(new KnowledgeBase().answer("hijo(pepe, juan)."));
	}

	@Test
	public void test_GoodRule_WithSalts_ReturnTrue() {
		Assert.assertFalse(new KnowledgeBase().answer("    h i j o   (    pepe   ,    juan  )   .  "));
	}

}
