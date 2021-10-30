package com.github.capitansissy.wrapper;

import com.github.capitansissy.constants.Defaults;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface JMXBeanOperation {
  String name() default Defaults.Slugs.None;

  String description() default Defaults.Slugs.None;

  String nameKey() default Defaults.Slugs.None;

  String descriptionKey() default Defaults.Slugs.None;

  IMPACT_TYPES impactType() default IMPACT_TYPES.UNKNOWN;

  enum IMPACT_TYPES {
    INFO, ACTION, ACTION_INFO, UNKNOWN
  }

  String sortValue() default Defaults.Slugs.None;
}
