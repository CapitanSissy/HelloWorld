package com.github.capitansissy.wrapper;

import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.security.AES;
import org.jetbrains.annotations.NotNull;

import javax.management.*;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class JMXBeanWrapper implements DynamicMBean {
  public static final String BEAN_OPERATION_SORT = "com.github.capitansissy.wrapper.BEAN_OPERATION_SORT";
  private MBeanInfo beanInfo = null;
  private Object bean = null;
  private Map<String, BeanAttribute> beanAttributes = new HashMap<String, BeanAttribute>();
  private Map<String, String> operationMapping = new HashMap<String, String>();
  private ResourceBundle resourceBundle = null;
  private boolean sorted = false;

  private class BeanAttribute {
    private Method getter;
    private Method setter;
    private String description;
    private String sortValue;

    public BeanAttribute(Method getter, Method setter, String description,
                         String sortValue) {
      setGetter(getter);
      setSetter(setter);
      setDescription(description);
      setSortValue(sortValue);
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public Method getGetter() {
      return getter;
    }

    public Method getSetter() {
      return setter;
    }

    public void setGetter(Method method) {
      this.getter = method;
    }

    public void setSetter(Method method) {
      this.setter = method;
    }

    public String getSortValue() {
      return sortValue;
    }

    public void setSortValue(String sortValue) {
      this.sortValue = sortValue;
    }
  }

  public JMXBeanWrapper(@NotNull Object bean) throws SecurityException, IntrospectionException, UnsupportedEncodingException {
    this.bean = bean;
    Class<?> beanClass = bean.getClass();

    JMXBean jmxBean = beanClass.getAnnotation(JMXBean.class);
    if (jmxBean == null) {
      throw new IllegalArgumentException(beanClass.getName()
        + " not a JMXBean annotated class.");
    }

    String beanName = jmxBean.className().equals(Defaults.Slugs.None) ? beanClass.getName()
      : jmxBean.className();
    String beanDescription = jmxBean.description();

    if (!jmxBean.resourceBundleName().equals(Defaults.Slugs.None)) {
      this.resourceBundle = ResourceBundle.getBundle(jmxBean
        .resourceBundleName());
      if (resourceBundle != null) {
        if (resourceBundle.containsKey(jmxBean.descriptionKey()))
          beanDescription = resourceBundle.getString(jmxBean
            .descriptionKey());
      }
    }

    this.sorted = jmxBean.sorted();

    List<MBeanAttributeInfo> attributes = getBeanAttributeInfos(bean);

    if (sorted) {
      Collections.sort(attributes, new Comparator<MBeanAttributeInfo>() {
        @Override
        public int compare(MBeanAttributeInfo o1, MBeanAttributeInfo o2) {
          BeanAttribute a1 = beanAttributes.get(o1.getName());
          BeanAttribute a2 = beanAttributes.get(o2.getName());
          String s1 = a1.getSortValue().equals(Defaults.Slugs.None) ? o1.getName()
            : a1.getSortValue();
          String s2 = a2.getSortValue().equals(Defaults.Slugs.None) ? o2.getName()
            : a2.getSortValue();
          return s1.compareTo(s2);
        }
      });
    }

    List<MBeanOperationInfo> operations = getBeanOperationInfos(bean);

    if (sorted) {
      Collections.sort(operations, new Comparator<MBeanOperationInfo>() {
        @Override
        public int compare(MBeanOperationInfo o1, MBeanOperationInfo o2) {
          String s1 = (String) o1.getDescriptor().getFieldValue(
            BEAN_OPERATION_SORT);
          String s2 = (String) o2.getDescriptor().getFieldValue(
            BEAN_OPERATION_SORT);
          if (Defaults.Slugs.None.equals(s1)) {
            s1 = o1.getName();
          }
          if (Defaults.Slugs.None.equals(s2)) {
            s2 = o2.getName();
          }
          return s1.compareTo(s2);
        }
      });
    }

    this.beanInfo = new MBeanInfo(beanName, beanDescription,
      attributes.toArray(new MBeanAttributeInfo[0]), null,
      operations.toArray(new MBeanOperationInfo[0]), null);

  }

  @Override
  public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
    BeanAttribute att = beanAttributes.get(attribute);
    if (att == null) {
      throw new AttributeNotFoundException(attribute);
    }
    try {
      return att.getGetter().invoke(bean);
    } catch (Exception e) {
      throw new ReflectionException(e);
    }
  }

  @Override
  public void setAttribute(@NotNull Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
    BeanAttribute att = beanAttributes.get(attribute.getName());
    if (att == null) {
      throw new AttributeNotFoundException(attribute.getName());
    }
    try {
      att.getSetter().invoke(bean, attribute.getValue());
    } catch (Exception e) {
      throw new ReflectionException(e);
    }
  }

  @Override
  public AttributeList getAttributes(@NotNull String[] attributes) {
    AttributeList result = new AttributeList();

    for (String name : attributes) {
      try {
        Object value = getAttribute(name);
        result.add(new Attribute(name, value));
      } catch (Exception ex) {

      }
    }
    return result;
  }

  @Override
  public AttributeList setAttributes(@NotNull AttributeList attributes) {
    AttributeList result = new AttributeList();

    Iterator<Object> iterator = attributes.iterator();
    while (iterator.hasNext()) {
      Attribute att = (Attribute) iterator.next();
      try {
        setAttribute(att);
        Attribute res = new Attribute(att.getName(),
          getAttribute(att.getName()));
        result.add(res);
      } catch (Exception e) {
      }
    }

    return result;
  }

  private boolean signatureMatches(String[] signature, Method method) {
    if (signature != null
      && method.getParameterTypes().length != signature.length) {
      return false;
    }

    int i = 0;
    for (Class<?> clazz : method.getParameterTypes()) {
      if (!clazz.getName().equals(signature[i++]))
        return false;
    }
    return true;
  }

  @Override
  public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
    String methodName = operationMapping.get(actionName);
    if (methodName != null) {
      try {
        for (Method method : bean.getClass().getMethods()) {
          if (method.getName().equals(methodName)
            && signatureMatches(signature, method))
            return method.invoke(bean, params);
        }
      } catch (Exception ex) {
        throw new ReflectionException(ex,
          "Can't convert signature for operation " + actionName);
      }
    }
    throw new MBeanException(new IllegalArgumentException(
      "Operation not found: " + actionName + "(" + signature + ")"));
  }

  @Override
  public MBeanInfo getMBeanInfo() {
    return beanInfo;
  }

  private List<MBeanOperationInfo> getBeanOperationInfos(@NotNull Object bean) throws UnsupportedEncodingException {
    Class<?> beanClass = bean.getClass();
    List<MBeanOperationInfo> operations = new ArrayList<MBeanOperationInfo>();
    for (Method method : beanClass.getMethods()) {
      JMXBeanOperation jmxBeanOperation = method
        .getAnnotation(JMXBeanOperation.class);

      if (jmxBeanOperation == null) {
        continue;
      }
      String name = jmxBeanOperation.name();
      String description = jmxBeanOperation.description();
      String sortValue = jmxBeanOperation.sortValue();
      if (resourceBundle != null) {
        if (!jmxBeanOperation.nameKey().equals(Defaults.Slugs.None)) {
          if (resourceBundle.containsKey(jmxBeanOperation.nameKey())) {
            name = new String(Objects.requireNonNull(AES.decrypt(resourceBundle.getString(jmxBeanOperation.nameKey()), Defaults.INTERNAL_SECURITY_KEY)).getBytes(Defaults.CHARSET), Defaults.CHARSET);
          }
        }
        if (!jmxBeanOperation.descriptionKey().equals(Defaults.Slugs.None)) {
          if (resourceBundle.containsKey(jmxBeanOperation.descriptionKey())) {
            description = new String(Objects.requireNonNull(AES.decrypt(resourceBundle.getString(jmxBeanOperation.descriptionKey()), Defaults.INTERNAL_SECURITY_KEY)).getBytes(Defaults.CHARSET), Defaults.CHARSET);
          }
        }
      }
      if (Defaults.Slugs.None.equals(name)) {
        name = method.getName();
      }
      int impact = MBeanOperationInfo.UNKNOWN;
      switch (jmxBeanOperation.impactType()) {
        case INFO:
          impact = MBeanOperationInfo.INFO;
          break;
        case ACTION:
          impact = MBeanOperationInfo.ACTION;
          break;
        case ACTION_INFO:
          impact = MBeanOperationInfo.ACTION_INFO;
          break;
        default:
          impact = MBeanOperationInfo.UNKNOWN;
          break;
      }
      int counter = 0;
      ArrayList<MBeanParameterInfo> pInfos = new ArrayList<MBeanParameterInfo>();
      Class<?>[] classes = method.getParameterTypes();
      Annotation[][] paramAnnotations = method.getParameterAnnotations();
      for (Class<?> clazz : classes) {
        String paramName = "param" + ++counter;
        String paramType = clazz.getName();
        String paramDescription = Defaults.Slugs.None;
        if (paramAnnotations[counter - 1].length > 0) {
          for (Annotation a : paramAnnotations[counter - 1]) {
            if (a instanceof JMXBeanParameter) {
              JMXBeanParameter jmxBeanParameter = (JMXBeanParameter) a;
              paramDescription = jmxBeanParameter.description();
              if (!Defaults.Slugs.None.equals(jmxBeanParameter.name()))
                paramName = jmxBeanParameter.name();
              if (resourceBundle != null) {
                if (!jmxBeanParameter.nameKey().equals(Defaults.Slugs.None)) {
                  if (resourceBundle.containsKey(jmxBeanParameter.nameKey())) {
                    paramName = new String(Objects.requireNonNull(AES.decrypt(resourceBundle.getString(jmxBeanParameter.nameKey()), Defaults.INTERNAL_SECURITY_KEY)).getBytes(Defaults.CHARSET), Defaults.CHARSET);
                  }
                }
                if (!jmxBeanParameter.descriptionKey().equals(Defaults.Slugs.None)) {
                  if (resourceBundle.containsKey(jmxBeanParameter.descriptionKey())) {
                    paramDescription = new String(Objects.requireNonNull(AES.decrypt(resourceBundle.getString(jmxBeanParameter.descriptionKey()), Defaults.INTERNAL_SECURITY_KEY)).getBytes(Defaults.CHARSET), Defaults.CHARSET);
                  }
                }
              }
              continue;
            }
          }
        }
        MBeanParameterInfo pInfo = new MBeanParameterInfo(paramName,
          paramType, paramDescription);
        pInfos.add(pInfo);
      }

      Map<String, String> descriptorValues = new HashMap<String, String>();
      descriptorValues.put(BEAN_OPERATION_SORT, sortValue);
      ImmutableDescriptor desc = new ImmutableDescriptor(descriptorValues);

      MBeanOperationInfo info = new MBeanOperationInfo(name, description,
        pInfos.toArray(new MBeanParameterInfo[0]), method
        .getReturnType().getName(), impact, desc);
      operationMapping.put(name, method.getName());
      operations.add(info);
    }
    return operations;
  }

  private List<MBeanAttributeInfo> getBeanAttributeInfos(@NotNull Object bean) throws IntrospectionException, UnsupportedEncodingException {

    Class<?> beanClass = bean.getClass();
    for (Method method : beanClass.getMethods()) {
      JMXBeanAttribute jmxBeanAttribute = method
        .getAnnotation(JMXBeanAttribute.class);

      if (jmxBeanAttribute == null) {
        continue;
      }
      String description = jmxBeanAttribute.description();
      String name = jmxBeanAttribute.name();
      String sortValue = jmxBeanAttribute.sortValue();
      if (resourceBundle != null) {
        if (!jmxBeanAttribute.nameKey().equals(Defaults.Slugs.None)) {
          if (resourceBundle.containsKey(jmxBeanAttribute.nameKey())) {
            name = new String(Objects.requireNonNull(AES.decrypt(resourceBundle.getString(jmxBeanAttribute.nameKey()), Defaults.INTERNAL_SECURITY_KEY)).getBytes(Defaults.CHARSET), Defaults.CHARSET);
          }
        }
        if (!jmxBeanAttribute.descriptionKey().equals(Defaults.Slugs.None)) {
          if (resourceBundle.containsKey(jmxBeanAttribute.descriptionKey())) {
            description = new String(Objects.requireNonNull(AES.decrypt(resourceBundle.getString(jmxBeanAttribute.descriptionKey()), Defaults.INTERNAL_SECURITY_KEY)).getBytes(Defaults.CHARSET), Defaults.CHARSET);
          }
        }
      }

      if (method.getName().startsWith("get")
        || method.getName().startsWith("is")) {
        if (Defaults.Slugs.None.equals(name)) {
          if (method.getName().startsWith("get")) {
            name = method.getName().substring(3);
          } else {
            name = method.getName().substring(2);
          }
          name = Character.toLowerCase(name.charAt(0))
            + name.substring(1);
        }
        BeanAttribute att = beanAttributes.get(name);
        if (att == null) {
          beanAttributes.put(name, new BeanAttribute(method, null,
            description, sortValue));
        } else {
          att.setGetter(method);
          if (Defaults.Slugs.None.equals(att.getDescription())) {
            att.setDescription(description);
          }
        }
      } else if (method.getName().startsWith("set")) {
        if (Defaults.Slugs.None.equals(name)) {
          name = method.getName().substring(3);
          name = Character.toLowerCase(name.charAt(0))
            + name.substring(1);
        }
        BeanAttribute att = beanAttributes.get(name);
        if (att == null) {
          beanAttributes.put(name, new BeanAttribute(null, method,
            description, sortValue));
        } else {
          att.setSetter(method);
          if (Defaults.Slugs.None.equals(att.getDescription())) {
            att.setDescription(description);
          }
        }
      } else {
        continue;
      }
    }

    List<MBeanAttributeInfo> attributes = new ArrayList<MBeanAttributeInfo>();
    for (Map.Entry<String, BeanAttribute> entry : beanAttributes.entrySet()) {
      MBeanAttributeInfo info = new MBeanAttributeInfo(entry.getKey(),
        entry.getValue().getDescription(), entry.getValue()
        .getGetter(), entry.getValue().getSetter());
      attributes.add(info);
    }
    return attributes;
  }

  @Override
  public String toString() {
    return bean.toString() + ":" + beanInfo.toString();
  }

}