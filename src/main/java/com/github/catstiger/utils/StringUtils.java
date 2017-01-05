package com.github.catstiger.utils;

import java.util.Random;

public final class StringUtils {

  private static final Random RANDOM = new Random();
  public static final String EMPTY = "";

  private StringUtils() {

  }
  
  /**
   * <code>Null</code> safe equals method.
   */
  public static boolean equals(String a, String b) {
    if (a == null) {
      return b == null;
    }
    return a.equals(b);
  }

  /**
   * <code>Null</code> safe equals method.
   */
  public static boolean equalsIgnoreCase(String a, String b) {
    if (a == null) {
      return b == null;
    }
    return a.equalsIgnoreCase(b);
  }

  /**
   * <p>
   * Checks if a CharSequence is empty ("") or null.
   * </p>
   *
   * <pre>
   * StringUtils.isEmpty(null)      = true
   * StringUtils.isEmpty("")        = true
   * StringUtils.isEmpty(" ")       = false
   * StringUtils.isEmpty("bob")     = false
   * StringUtils.isEmpty("  bob  ") = false
   * </pre>
   *
   * <p>
   * NOTE: This method changed in Lang version 2.0. It no longer trims the
   * CharSequence. That functionality is available in isBlank().
   * </p>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if the CharSequence is empty or null
   * @since 3.0 Changed signature from isEmpty(String) to isEmpty(CharSequence)
   */
  public static boolean isEmpty(CharSequence cs) {
    return cs == null || cs.length() == 0;
  }

  /**
   * <p>
   * Checks if a CharSequence is not empty ("") and not null.
   * </p>
   *
   * <pre>
   * StringUtils.isNotEmpty(null)      = false
   * StringUtils.isNotEmpty("")        = false
   * StringUtils.isNotEmpty(" ")       = true
   * StringUtils.isNotEmpty("bob")     = true
   * StringUtils.isNotEmpty("  bob  ") = true
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if the CharSequence is not empty and not null
   * @since 3.0 Changed signature from isNotEmpty(String) to
   *        isNotEmpty(CharSequence)
   */
  public static boolean isNotEmpty(CharSequence cs) {
    return !StringUtils.isEmpty(cs);
  }

  /**
   * <p>
   * Checks whether the String a valid Java number.
   * </p>
   *
   * <p>
   * Valid numbers include hexadecimal marked with the <code>0x</code>
   * qualifier, scientific notation and numbers marked with a type qualifier
   * (e.g. 123L).
   * </p>
   *
   * <p>
   * <code>Null</code> and empty String will return <code>false</code>.
   * </p>
   *
   * @param str the <code>String</code> to check
   * @return <code>true</code> if the string is a correctly formatted number
   */
  public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
      return false;
    }
    char[] chars = str.toCharArray();
    int sz = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean allowSigns = false;
    boolean foundDigit = false;
    // deal with any possible sign up front
    int start = (chars[0] == '-') ? 1 : 0;
    if (sz > start + 1 && chars[start] == '0' && chars[start + 1] == 'x') {
      int i = start + 2;
      if (i == sz) {
        return false; // str == "0x"
      }
      // checking hex (it can't be anything else)
      for (; i < chars.length; i++) {
        if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
          return false;
        }
      }
      return true;
    }
    sz--; // don't want to loop to the last char, check it afterwords
          // for type qualifiers
    int i = start;
    // loop to the next to last char or to the last char if we need another
    // digit to
    // make a valid number (e.g. chars[0..5] = "1234E")
    while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
      if (chars[i] >= '0' && chars[i] <= '9') {
        foundDigit = true;
        allowSigns = false;

      } else if (chars[i] == '.') {
        if (hasDecPoint || hasExp) {
          // two decimal points or dec in exponent
          return false;
        }
        hasDecPoint = true;
      } else if (chars[i] == 'e' || chars[i] == 'E') {
        // we've already taken care of hex.
        if (hasExp) {
          // two E's
          return false;
        }
        if (!foundDigit) {
          return false;
        }
        hasExp = true;
        allowSigns = true;
      } else if (chars[i] == '+' || chars[i] == '-') {
        if (!allowSigns) {
          return false;
        }
        allowSigns = false;
        foundDigit = false; // we need a digit after the E
      } else {
        return false;
      }
      i++;
    }
    if (i < chars.length) {
      if (chars[i] >= '0' && chars[i] <= '9') {
        // no type qualifier, OK
        return true;
      }
      if (chars[i] == 'e' || chars[i] == 'E') {
        // can't have an E at the last byte
        return false;
      }
      if (chars[i] == '.') {
        if (hasDecPoint || hasExp) {
          // two decimal points or dec in exponent
          return false;
        }
        // single trailing decimal point after non-exponent is ok
        return foundDigit;
      }
      if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
        return foundDigit;
      }
      if (chars[i] == 'l' || chars[i] == 'L') {
        // not allowing L with an exponent or decimal point
        return foundDigit && !hasExp && !hasDecPoint;
      }
      // last character is illegal
      return false;
    }
    // allowSigns is true iff the val ends in 'E'
    // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
    return !allowSigns && foundDigit;
  }

  /**
   * <p>
   * Checks if a CharSequence is whitespace, empty ("") or null.
   * </p>
   *
   * <pre>
   * StringUtils.isBlank(null)      = true
   * StringUtils.isBlank("")        = true
   * StringUtils.isBlank(" ")       = true
   * StringUtils.isBlank("bob")     = false
   * StringUtils.isBlank("  bob  ") = false
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if the CharSequence is null, empty or whitespace
   * @since 2.0
   * @since 3.0 Changed signature from isBlank(String) to isBlank(CharSequence)
   */
  public static boolean isBlank(CharSequence cs) {
    int strLen;
    if (cs == null || (strLen = cs.length()) == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if (Character.isWhitespace(cs.charAt(i)) == false) {
        return false;
      }
    }
    return true;
  }

  /**
   * <p>
   * Checks if a CharSequence is not empty (""), not null and not whitespace
   * only.
   * </p>
   *
   * <pre>
   * StringUtils.isNotBlank(null)      = false
   * StringUtils.isNotBlank("")        = false
   * StringUtils.isNotBlank(" ")       = false
   * StringUtils.isNotBlank("bob")     = true
   * StringUtils.isNotBlank("  bob  ") = true
   * </pre>
   *
   * @param cs the CharSequence to check, may be null
   * @return {@code true} if the CharSequence is not empty and not null and not
   *         whitespace
   * @since 2.0
   * @since 3.0 Changed signature from isNotBlank(String) to
   *        isNotBlank(CharSequence)
   */
  public static boolean isNotBlank(CharSequence cs) {
    return !StringUtils.isBlank(cs);
  }

  /**
   * <p>
   * Removes control characters (char &lt;= 32) from both ends of this String,
   * handling {@code null} by returning {@code null}.
   * </p>
   *
   * <p>
   * The String is trimmed using {@link String#trim()}. Trim removes start and
   * end characters &lt;= 32. To strip whitespace use {@link #strip(String)}.
   * </p>
   *
   * <p>
   * To trim your choice of characters, use the {@link #strip(String, String)}
   * methods.
   * </p>
   *
   * <pre>
   * StringUtils.trim(null)          = null
   * StringUtils.trim("")            = ""
   * StringUtils.trim("     ")       = ""
   * StringUtils.trim("abc")         = "abc"
   * StringUtils.trim("    abc    ") = "abc"
   * </pre>
   *
   * @param str the String to be trimmed, may be null
   * @return the trimmed string, {@code null} if null String input
   */
  public static String trim(String str) {
    return str == null ? null : str.trim();
  }

  /**
   * <p>
   * Removes control characters (char &lt;= 32) from both ends of this String
   * returning {@code null} if the String is empty ("") after the trim or if it
   * is {@code null}.
   *
   * <p>
   * The String is trimmed using {@link String#trim()}. Trim removes start and
   * end characters &lt;= 32. To strip whitespace use
   * {@link #stripToNull(String)}.
   * </p>
   *
   * <pre>
   * StringUtils.trimToNull(null)          = null
   * StringUtils.trimToNull("")            = null
   * StringUtils.trimToNull("     ")       = null
   * StringUtils.trimToNull("abc")         = "abc"
   * StringUtils.trimToNull("    abc    ") = "abc"
   * </pre>
   *
   * @param str the String to be trimmed, may be null
   * @return the trimmed String, {@code null} if only chars &lt;= 32, empty or
   *         null String input
   * @since 2.0
   */
  public static String trimToNull(String str) {
    String ts = trim(str);
    return isEmpty(ts) ? null : ts;
  }

  /**
   * <p>
   * Removes control characters (char &lt;= 32) from both ends of this String
   * returning an empty String ("") if the String is empty ("") after the trim
   * or if it is {@code null}.
   *
   * <p>
   * The String is trimmed using {@link String#trim()}. Trim removes start and
   * end characters &lt;= 32. To strip whitespace use
   * {@link #stripToEmpty(String)}.
   * </p>
   *
   * <pre>
   * StringUtils.trimToEmpty(null)          = ""
   * StringUtils.trimToEmpty("")            = ""
   * StringUtils.trimToEmpty("     ")       = ""
   * StringUtils.trimToEmpty("abc")         = "abc"
   * StringUtils.trimToEmpty("    abc    ") = "abc"
   * </pre>
   *
   * @param str the String to be trimmed, may be null
   * @return the trimmed String, or an empty String if {@code null} input
   * @since 2.0
   */
  public static String trimToEmpty(String str) {
    return str == null ? EMPTY : str.trim();
  }

  /**
   * <p>
   * Creates a random string whose length is the number of characters specified.
   * </p>
   *
   * <p>
   * Characters will be chosen from the set of all characters.
   * </p>
   *
   * @param count the length of random string to create
   * @return the random string
   */
  public static String random(int count) {
    return random(count, false, false);
  }

  /**
   * <p>
   * Creates a random string whose length is the number of characters specified.
   * </p>
   *
   * <p>
   * Characters will be chosen from the set of characters whose ASCII value is
   * between {@code 32} and {@code 126} (inclusive).
   * </p>
   *
   * @param count the length of random string to create
   * @return the random string
   */
  public static String randomAscii(int count) {
    return random(count, 32, 127, false, false);
  }

  /**
   * <p>
   * Creates a random string whose length is the number of characters specified.
   * </p>
   *
   * <p>
   * Characters will be chosen from the set of alpha-numeric characters.
   * </p>
   *
   * @param count the length of random string to create
   * @return the random string
   */
  public static String randomAlphanumeric(int count) {
    return random(count, true, true);
  }

  /**
   * <p>
   * Creates a random string whose length is the number of characters specified.
   * </p>
   *
   * <p>
   * Characters will be chosen from the set of numeric characters.
   * </p>
   *
   * @param count the length of random string to create
   * @return the random string
   */
  public static String randomNumeric(int count) {
    return random(count, false, true);
  }

  /**
   * <p>
   * Creates a random string whose length is the number of characters specified.
   * </p>
   *
   * <p>
   * Characters will be chosen from the set of alpha-numeric characters as
   * indicated by the arguments.
   * </p>
   *
   * @param count the length of random string to create
   * @param letters if {@code true}, generated string will include alphabetic
   *          characters
   * @param numbers if {@code true}, generated string will include numeric
   *          characters
   * @return the random string
   */
  private static String random(int count, boolean letters, boolean numbers) {
    return random(count, 0, 0, letters, numbers);
  }

  private static String random(int count, int start, int end, boolean letters, boolean numbers) {
    return random(count, start, end, letters, numbers, null, RANDOM);
  }

  private static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
    if (count == 0) {
      return "";
    } else if (count < 0) {
      throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }
    if (start == 0 && end == 0) {
      end = 'z' + 1;
      start = ' ';
      if (!letters && !numbers) {
        start = 0;
        end = Integer.MAX_VALUE;
      }
    }

    char[] buffer = new char[count];
    int gap = end - start;

    while (count-- != 0) {
      char ch;
      if (chars == null) {
        ch = (char) (random.nextInt(gap) + start);
      } else {
        ch = chars[random.nextInt(gap) + start];
      }
      if (letters && Character.isLetter(ch) || numbers && Character.isDigit(ch) || !letters && !numbers) {
        if (ch >= 56320 && ch <= 57343) {
          if (count == 0) {
            count++;
          } else {
            // low surrogate, insert high surrogate after putting it in
            buffer[count] = ch;
            count--;
            buffer[count] = (char) (55296 + random.nextInt(128));
          }
        } else if (ch >= 55296 && ch <= 56191) {
          if (count == 0) {
            count++;
          } else {
            // high surrogate, insert low surrogate before putting it in
            buffer[count] = (char) (56320 + random.nextInt(128));
            count--;
            buffer[count] = ch;
          }
        } else if (ch >= 56192 && ch <= 56319) {
          // private high surrogate, no effing clue, so skip it
          count++;
        } else {
          buffer[count] = ch;
        }
      } else {
        count++;
      }
    }
    return new String(buffer);
  }

  /**
   * Returns a new String with the prefix removed, if present. This is case
   * sensitive.
   *
   * @param value The input String
   * @param prefix String to remove on left
   * @return The String without prefix
   */
  public static String removeLeft(final String value, final String prefix) {
    return removeLeft(value, prefix, true);
  }

  /**
   * Returns a new String with the prefix removed, if present.
   *
   * @param value The input String
   * @param prefix String to remove on left
   * @param caseSensitive ensure case sensitivity
   * @return The String without prefix
   */
  public static String removeLeft(final String value, final String prefix, final boolean caseSensitive) {
    if (value == null || prefix == null) {
      throw new RuntimeException("Value or prefix must not be null.");
    }

    if (caseSensitive) {
      return value.startsWith(prefix) ? value.substring(prefix.length()) : value;
    }
    return value.toLowerCase().startsWith(prefix.toLowerCase()) ? value.substring(prefix.length()) : value;
  }

  /**
   * Returns a new string with the 'suffix' removed, if present. Search is case
   * sensitive.
   *
   * @param value The input String
   * @param suffix The suffix to remove
   * @return The String without suffix!
   */
  public static String removeRight(final String value, final String suffix) {
    return removeRight(value, suffix, true);
  }

  /**
   * Returns a new string with the 'suffix' removed, if present.
   *
   * @param value The input String
   * @param suffix The suffix to remove
   * @param caseSensitive whether search should be case sensitive or not
   * @return The String without suffix!
   */
  public static String removeRight(final String value, final String suffix, final boolean caseSensitive) {
    if (value == null || suffix == null) {
      throw new RuntimeException("Value or prefix must not be null.");
    }

    return endsWith(value, suffix, caseSensitive) ? value.substring(0, value.toLowerCase().lastIndexOf(suffix.toLowerCase())) : value;
  }

  /**
   * Test if value ends with search. The search is case sensitive.
   *
   * @param value input string
   * @param search string to search
   * @return true or false
   */
  public static boolean endsWith(final String value, final String search) {
    if (value == null) {
      throw new RuntimeException("Value must not be null.");
    }
    return endsWith(value, search, value.length(), true);
  }

  /**
   * Test if value ends with search.
   *
   * @param value input string
   * @param search string to search
   * @param caseSensitive true or false
   * @return true or false
   */
  public static boolean endsWith(final String value, final String search, final boolean caseSensitive) {
    if (value == null) {
      throw new RuntimeException("Value must not be null.");
    }
    return endsWith(value, search, value.length(), caseSensitive);
  }

  /**
   * Test if value ends with search.
   *
   * @param value input string
   * @param search string to search
   * @param position position till which you want to search.
   * @param caseSensitive true or false
   * @return true or false
   */
  public static boolean endsWith(final String value, final String search, final int position, final boolean caseSensitive) {
    if (value == null) {
      throw new RuntimeException("Value must not be null.");
    }
    int remainingLength = position - search.length();
    if (caseSensitive) {
      return value.indexOf(search, remainingLength) > -1;
    }
    return value.toLowerCase().indexOf(search.toLowerCase(), remainingLength) > -1;
  }

  /**
   * Replace consecutive whitespace characters with a single space.
   *
   * @param value input String
   * @return collapsed String
   */
  public static String collapseWhitespace(final String value) {
    if (value == null) {
      throw new RuntimeException("Value must not be null.");
    }
    return value.trim().replaceAll("\\s\\s+", " ");
  }

  /**
   * Transform to camelCase
   *
   * @param value The input String
   * @return String in camelCase.
   */
  public static String toCamelCase(final String value) {
    if (value == null) {
      throw new RuntimeException("Value must not be null.");
    }
    String str = toStudlyCase(value);
    return str.substring(0, 1).toLowerCase() + str.substring(1);
  }

  /**
   * Converts the first character of string to upper case.
   *
   * @param input The string to convert.
   * @return Returns the converted string.
   */
  public static String upperFirst(final String value) {
    if (value == null) {
      throw new RuntimeException("Value must not be null.");
    }

    if (value.length() <= 1) {
      return value.toUpperCase();
    }
    return value.substring(0, 1).toUpperCase() + value.substring(1);
  }

  /**
   * Converts the first character of string to lower case.
   *
   * @param input The string to convert.
   * @return Returns the converted string.
   */
  public static String lowerFirst(final String value) {
    if (value == null) {
      throw new RuntimeException("Value must not be null.");
    }
    if (value.length() <= 1) {
      return value.toLowerCase();
    }

    return value.substring(0, 1).toLowerCase() + value.substring(1);
  }

  /**
   * Transform to StudlyCaps.
   *
   * @param value The input String
   * @return String in StudlyCaps.
   */
  public static String toStudlyCase(final String value) {
    if (value == null) {
      throw new RuntimeException("Value must not be null.");
    }

    String[] words = collapseWhitespace(value.trim()).split("\\s*(_|-|\\s)\\s*");
    StringBuilder buf = new StringBuilder(value.length());

    if (words != null && words.length > 0) {
      for (int i = 0; i < words.length; i++) {
        buf.append(upperFirst(words[i]));
      }
    }
    return buf.toString();
  }

  /**
   * Decamelize String
   *
   * @param value The input String
   * @param chr string to use
   * @return String decamelized.
   */
  public static String toDecamelize(final String value, final String chr) {
    String camelCasedString = toCamelCase(value);
    String[] words = camelCasedString.split("(?=\\p{Upper})");
    StringBuilder buf = new StringBuilder(value.length());

    if (words != null && words.length > 0) {
      for (int i = 0; i < words.length; i++) {
        if (words[i] != null) {
          buf.append(words[i].toLowerCase());
          if (i < words.length - 1) {
            buf.append(chr);
          }
        }
      }
    }
    return buf.toString();
  }

  /**
   * Transform to snake_case.
   *
   * @param value The input String
   * @return String in snake_case.
   */
  public static String toSnakeCase(final String value) {
    return toDecamelize(value, "_");
  }

}
