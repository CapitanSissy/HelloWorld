package com.github.capitansissy.wrapper;

import com.github.capitansissy.constants.Defaults;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface JMXBean {
  String className() default Defaults.Slugs.None;

  String description() default Defaults.Slugs.None;

  String descriptionKey() default Defaults.Slugs.None;

  String resourceBundleName() default Defaults.Slugs.None;

  boolean sorted() default false;
}