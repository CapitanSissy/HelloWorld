package simple.number;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestOperations {

  @Test
  public void testAddition() {
    Operations operations = new Operations();
    assertEquals(12, operations.addition(2, 10));
  }

  @Test
  public void testDivision() {
    Operations operations = new Operations();
    assertEquals(2, operations.division(10, 5));
  }

  @Test
  public void testModulo() {
    Operations operations = new Operations();
    assertEquals(1, operations.modulo(10, 3));
  }

  @Test
  public void testMultiplication() {
    Operations operations = new Operations();
    assertEquals(1000, operations.multiplication(25, 40));
  }

  @Test
  public void testSubstraction() {
    Operations operations = new Operations();
    assertEquals(47, operations.subtraction(50, 3));
  }

}
