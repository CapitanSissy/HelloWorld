import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.security.AES;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

public class Converter {
  private static final String PLAIN_TEXT_ENTITY = "/src/main/resources/plain-text/%1$s_plain-text.properties";
  private static final String ENCRYPTED_ENTITY = "/src/main/resources/%1$s.properties";
  private static Properties properties = new Properties() {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Override
    public Set<Object> keySet() {
      return Collections.unmodifiableSet(new TreeSet<>(super.keySet()));
    }

    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {
      Set<Map.Entry<Object, Object>> set1 = super.entrySet();
      Set<Map.Entry<Object, Object>> set2 = new LinkedHashSet<>(set1.size());
      Iterator<Map.Entry<Object, Object>> iterator = set1.stream().sorted(Comparator.comparing(o -> o.getKey().toString())).iterator();
      while (iterator.hasNext())
        set2.add(iterator.next());
      return set2;
    }

    @NotNull
    @Override
    public synchronized Enumeration<Object> keys() {
      return Collections.enumeration(new TreeSet<>(super.keySet()));
    }
  };

  private static void encryptProperties(String fileName) throws IOException {
    String PLAIN_TEXT = new File(new File(Converter.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getParentFile().getPath().concat(PLAIN_TEXT_ENTITY)).getPath();
    String ENCRYPTED = new File(new File(Converter.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getParentFile().getPath().concat(ENCRYPTED_ENTITY)).getPath();

    try (InputStream fileInputStream = new FileInputStream(String.format(PLAIN_TEXT, fileName))) {
      Properties sourceProperties = new Properties();
      properties.clear();

      sourceProperties.load(fileInputStream);
      Map<String, String> sortedMap = new TreeMap(sourceProperties);

      for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
        switch (entry.getKey()) {
          case "app.name":
          case "version.number":
          case "release.candidate":
            properties.setProperty(entry.getKey(), entry.getValue());
            break;
          default:
            properties.setProperty(entry.getKey(), AES.encrypt(entry.getValue(), Defaults.INTERNAL_SECURITY_KEY));
        }
      }

      try (OutputStream fileOutputStream = new FileOutputStream(String.format(ENCRYPTED, fileName))) {
        properties.store(fileOutputStream, Defaults.Slugs.None);
      }
    }
  }

  public static void main(String[] args) {
    try {
      encryptProperties("db");
      encryptProperties("structure");
      encryptProperties("messages");
      encryptProperties("messages_ar_AE");
      encryptProperties("messages_fa_IR");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
