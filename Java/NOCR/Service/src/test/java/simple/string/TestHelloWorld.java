package simple.string;

import org.junit.*;

import static org.junit.Assert.assertEquals;


public class TestHelloWorld {
  private HelloWorld h;

  @BeforeClass
  public static void beforeClass() {
  }

  @Before
  public void setUp() throws Exception {
    h = new HelloWorld();
  }

  @Test
  public void testHelloEmpty() {
    assertEquals(h.getName(), "");
    assertEquals(h.getMessage(), "Hello!");
  }

  @Test
  public void testHelloWorld() {
    h.setName("World");
    assertEquals(h.getName(), "World");
    assertEquals(h.getMessage(), "Hello World!");
  }

  @After
  public void after() {
  }

  @AfterClass
  public static void afterClass() {
  }

}