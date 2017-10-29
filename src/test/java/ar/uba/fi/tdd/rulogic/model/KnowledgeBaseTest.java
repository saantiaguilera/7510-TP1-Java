package ar.uba.fi.tdd.rulogic.model;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import javax.annotation.Nonnull;

public class KnowledgeBaseTest {

	@InjectMocks
	@Nonnull
	private KnowledgeBase knowledgeBase;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
	}

	@Test
	public void test() {
		Assert.assertTrue(this.knowledgeBase.answer("varon (javier)."));
	}

}
