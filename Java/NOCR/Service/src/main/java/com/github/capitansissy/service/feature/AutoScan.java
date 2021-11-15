package com.github.capitansissy.service.feature;

import com.github.capitansissy.Logger;
import com.github.capitansissy.constants.Defaults;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.Populator;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ClasspathDescriptorFileFinder;
import org.glassfish.hk2.utilities.DuplicatePostProcessor;

public class AutoScan implements Feature {
  private static Logger logger = new Logger();

  @Inject
  private ServiceLocator serviceLocator;

  @Override
  public boolean configure(FeatureContext context) {
    DynamicConfigurationService dcs = serviceLocator.getService(DynamicConfigurationService.class);
    Populator populator = dcs.getPopulator();
    try {
      populator.populate(
        new ClasspathDescriptorFileFinder(this.getClass().getClassLoader()),
        new DuplicatePostProcessor());
    } catch (Exception e) {
      logger.setLog(e.getMessage(), Defaults.Log4J.DEBUG);
    }
    return true;
  }
}