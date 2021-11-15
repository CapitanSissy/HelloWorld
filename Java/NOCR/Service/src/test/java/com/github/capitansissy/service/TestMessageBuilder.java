package com.github.capitansissy.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TestMessageBuilder {

  @Test
  public void testNameWorld() {
    MessageBuilder messageBuilder = new MessageBuilder();
    assertEquals("Hello World", messageBuilder.getMessage("World"));
  }

  @Test
  public void testNameEmpty() {
    MessageBuilder obj = new MessageBuilder();
    assertEquals("Please provide a name!", obj.getMessage(" "));
  }

  @Test
  public void testNameNull() {
    MessageBuilder obj = new MessageBuilder();
    assertEquals("Please provide a name!", obj.getMessage(null));
  }

}