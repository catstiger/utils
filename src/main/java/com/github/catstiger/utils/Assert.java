package com.github.catstiger.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public final class Assert {
  /**
   * 验证一个boolean表达式，如果测试结果为false,则抛出{@code IllegalArgumentException}
   * @param expression 表达式
   * @param message the exception message to use if the assertion fails
   * @throws IllegalArgumentException if expression is {@code false}
   */
  public static void isTrue(boolean expression, String message) {
    if (!expression) {
      throw new IllegalArgumentException(message);
    }
  }
  
  public static void isTrue(boolean expression) {
    if (!expression) {
      throw new IllegalArgumentException("表达式结果必须为true");
    }
  }
  
  /**
   * 验证给定的String不是空的，也就是说，既不是{@code null}, 也不是空字符串。
   * 
   * @param text the String to check
   * @param message the exception message to use if the assertion fails
   * @throws IllegalArgumentException if the text is empty
   */
  public static void hasLength(String text, String message) {
    if (text != null && text.length() > 0) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * @see #hasLength
   */
  public static void hasLength(String text) {
    hasLength(text,
        "给定内容必须包含至少一个字符");
  }

  /**
   * 测试给定字符串是否包含内容，也就是说，该字符串必须不为{@code null}，而且必须至少包含一个非空格字符。
   * @param text the String to check
   * @param message the exception message to use if the assertion fails
   * @see StringUtils#hasText
   * @throws IllegalArgumentException if the text does not contain valid text content
   */
  public static void hasText(String text, String message) {
    if (StringUtils.isBlank(text)) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * @see #hasText
   */
  public static void hasText(String text) {
    hasText(text,
        "给定字符串必须不为null, 而且至少包含一个非空格字符。");
  }
  
  /**
   * 测试一个collection不为空，它必须不是{@code null}，而且至少包含一个element。
   * @param collection the collection to check
   * @param message the exception message to use if the assertion fails
   * @throws IllegalArgumentException if the collection is {@code null} or has no elements
   */
  public static void notEmpty(Collection<?> collection, String message) {
    if (CollectionUtils.isEmpty(collection)) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void notEmpty(Collection<?> collection) {
    notEmpty(collection,
        "集合必须不为null，而且至少包含一个元素。");
  }

  /**
   * 测试给定的Map不为{@code null},而且至少包含一个entry.
   * @param map the map to check
   * @param message the exception message to use if the assertion fails
   * @throws IllegalArgumentException if the map is {@code null} or has no entries
   */
  public static void notEmpty(Map<?, ?> map, String message) {
    if (CollectionUtils.isEmpty(map)) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void notEmpty(Map<?, ?> map) {
    notEmpty(map, "改Map必须不为null, 而且至少有一个entry.");
  }
  
  /**
   * 测试给定的数组不为空，也就是说，该数组必须不为null,而且至少包含一个元素。
   * @param array the array to check
   * @param message the exception message to use if the assertion fails
   * @throws IllegalArgumentException if the object array is {@code null} or has no elements
   */
  public static void notEmpty(Object[] array, String message) {
    if (ObjectUtils.isEmpty(array)) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void notEmpty(Object[] array) {
    notEmpty(array, "数组必须不为null, 而且至少包含一个元素。");
  }


  /**
   * Assert that the provided object is an instance of the provided class.
   * <pre class="code">Assert.instanceOf(Foo.class, foo);</pre>
   * @param clazz the required class
   * @param obj the object to check
   * @throws IllegalArgumentException if the object is not an instance of clazz
   * @see Class#isInstance
   */
  public static void isInstanceOf(Class<?> clazz, Object obj) {
    isInstanceOf(clazz, obj, "");
  }

  
  public static void isInstanceOf(Class<?> type, Object obj, String message) {
    Objects.requireNonNull(type, "Type to check against must not be null");
    if (!type.isInstance(obj)) {
      throw new IllegalArgumentException(
          (StringUtils.isEmpty(message) ? message + " " : "") +
          "Object of class [" + (obj != null ? obj.getClass().getName() : "null") +
          "] must be an instance of " + type);
    }
  }
  
  


  
  private Assert() {
    
  }
}
