package com.github.catstiger.utils;

import org.junit.Test;

import junit.framework.TestCase;


public class StringUtilsTest {
  @Test
  public void testIsNumber() {
    TestCase.assertTrue(StringUtils.isNumber("9987744232"));
    TestCase.assertTrue(StringUtils.isNumber("-98787544332"));
    TestCase.assertTrue(StringUtils.isNumber("34.9995834"));
    TestCase.assertTrue(StringUtils.isNumber("-45.9954"));
    TestCase.assertTrue(StringUtils.isNumber("23245.8"));
    TestCase.assertTrue(StringUtils.isNumber("0x0085"));
    TestCase.assertTrue(!StringUtils.isNumber("79.34.45"));
    TestCase.assertTrue(!StringUtils.isNumber("99,685,434,343"));
    TestCase.assertTrue(!StringUtils.isNumber("--4454"));
    
  }
}
