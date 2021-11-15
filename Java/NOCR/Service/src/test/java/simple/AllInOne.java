package simple;

import com.github.capitansissy.service.TestGeneral;
import com.github.capitansissy.service.TestMessageBuilder;
import junit.framework.JUnit4TestAdapter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import simple.number.TestOperations;
import simple.string.TestHelloWorld;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  TestHelloWorld.class,
  TestOperations.class,
  TestMessageBuilder.class,
  TestGeneral.class
})

public class AllInOne {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  @NotNull
  @Contract(" -> new")
  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(AllInOne.class);
  }
}