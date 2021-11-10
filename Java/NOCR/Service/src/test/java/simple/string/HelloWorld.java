package simple.string;

public class HelloWorld {
  private String name = "";

  String getName() {
    return name;
  }

  String getMessage() {
    if (name.equals("")) {
      return "Hello!";
    } else {
      return "Hello " + name + "!";
    }
  }

  void setName(String name) {
    this.name = name;
  }

}