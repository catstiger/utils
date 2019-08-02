package com.github.catstiger.common.util;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.apache.commons.text.CharacterPredicate;
import org.apache.commons.text.RandomStringGenerator;

public final class RandomUtil {
  private static UniformRandomProvider rng = RandomSource.create(RandomSource.JDK);

  protected static RandomStringGenerator numberGenerator = new RandomStringGenerator.Builder().withinRange('0', '9').usingRandom(rng::nextInt).build();

  protected static RandomStringGenerator upperWordGenerator = new RandomStringGenerator.Builder().withinRange('A', 'Z').usingRandom(rng::nextInt).build();

  protected static RandomStringGenerator lowerWordGenerator = new RandomStringGenerator.Builder().withinRange('a', 'z').usingRandom(rng::nextInt).build();

  protected static RandomStringGenerator wordGenerator = new RandomStringGenerator.Builder().withinRange('A', 'z').usingRandom(rng::nextInt)
      .filteredBy(new CharacterPredicate() {
        @Override
        public boolean test(int codePoint) {
          return codePoint < '[' || codePoint > '`';
        }
      }).build();
  protected static RandomStringGenerator stringGenerator = new RandomStringGenerator.Builder().withinRange('0', 'z').usingRandom(rng::nextInt)
      .filteredBy(new CharacterPredicate() {
        @Override
        public boolean test(int codePoint) {
          return (codePoint < '[' || codePoint > '`') && (codePoint < ':' || codePoint > '@');
        }
      }).build();

  /**
   * 生成指定长度的随机数字
   * 
   * @param length 指定随机数字的长度
   * @return 生成的数字字符串
   */
  public static String nextNumber(int length) {
    return numberGenerator.generate(length);
  }

  /**
   * 生成指定长度的大写字符串，只包括A~Z的字符
   * 
   * @param length 指定随机字符串的长度
   * @return 生成的大写字符串
   */
  public static String nextUpper(int length) {
    return upperWordGenerator.generate(length);
  }

  /**
   * 生成指定长度的小写字符串，只包括a~z的字符
   * 
   * @param length 指定随机字符串的长度
   * @return 生成的小写字符串
   */
  public static String nextLower(int length) {
    return lowerWordGenerator.generate(length);
  }

  /**
   * 生成指定长度的字母字符串，只包括a~z、A~Z的字母
   * 
   * @param length 指定随机字母的长度
   * @return 生成的字母
   */
  public static String nextWord(int length) {
    return wordGenerator.generate(length);
  }

  /**
   * 生成指定长度的字符串，只包括a~z、A~Z，0~9的字符
   * 
   * @param length 指定随机字符串的长度
   * @return 生成的字符串
   */
  public static String nextString(int length) {
    return stringGenerator.generate(length);
  }

}
