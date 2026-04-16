package io.wangk.peekaboo.common.utils;


import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import io.wangk.peekaboo.common.constants.CommonConstants;
import io.wangk.peekaboo.common.exception.StatusException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.*;
import java.sql.Clob;
import java.util.*;
import java.util.stream.Collectors;

import static io.wangk.peekaboo.common.constants.CommonConstants.SINGLE_QUOTES;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils extends org.apache.commons.lang3.StringUtils {


    /**
     * <p>Checks if a CharSequence is empty ("") or null.</p>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is empty or null
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * <p>Checks if a CharSequence is not empty ("") and not null.</p>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty and not null
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    public static String wrapperSingleQuotes(String value) {
        return SINGLE_QUOTES + value + SINGLE_QUOTES;
    }

    public static String escapeJava(String str) {
        return escapeJavaStyleString(str, false, false);
    }

    private static String escapeJavaStyleString(String str, boolean escapeSingleQuotes, boolean escapeForwardSlash) {
        if (str == null) {
            return null;
        }
        try {
            StringWriter writer = new StringWriter(str.length() * 2);
            escapeJavaStyleString(writer, str, escapeSingleQuotes, escapeForwardSlash);
            return writer.toString();
        } catch (IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            throw new RuntimeException(ioe);
        }
    }

    private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote,
                                              boolean escapeForwardSlash) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (str == null) {
            return;
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // handle unicode
            if (ch > 0xfff) {
                out.write("\\u" + hex(ch));
            } else if (ch > 0xff) {
                out.write("\\u0" + hex(ch));
            } else if (ch > 0x7f) {
                out.write("\\u00" + hex(ch));
            } else if (ch < 32) {
                switch (ch) {
                    case '\b' :
                        out.write('\\');
                        out.write('b');
                        break;
                    case '\n' :
                        out.write('\\');
                        out.write('n');
                        break;
                    case '\t' :
                        out.write('\\');
                        out.write('t');
                        break;
                    case '\f' :
                        out.write('\\');
                        out.write('f');
                        break;
                    case '\r' :
                        out.write('\\');
                        out.write('r');
                        break;
                    default :
                        if (ch > 0xf) {
                            out.write("\\u00" + hex(ch));
                        } else {
                            out.write("\\u000" + hex(ch));
                        }
                        break;
                }
            } else {
                switch (ch) {
                    case '\'' :
                        if (escapeSingleQuote) {
                            out.write('\\');
                        }
                        out.write('\'');
                        break;
                    case '"' :
                        out.write('\\');
                        out.write('"');
                        break;
                    case '\\' :
                        out.write('\\');
                        out.write('\\');
                        break;
                    case '/' :
                        if (escapeForwardSlash) {
                            out.write('\\');
                        }
                        out.write('/');
                        break;
                    default :
                        out.write(ch);
                        break;
                }
            }
        }
    }

    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
    }

    public static String replaceDoubleBrackets(String mainParameter) {
        mainParameter = mainParameter
                .replace(CommonConstants.DOUBLE_BRACKETS_LEFT, CommonConstants.DOUBLE_BRACKETS_LEFT_SPACE)
                .replace(CommonConstants.DOUBLE_BRACKETS_RIGHT, CommonConstants.DOUBLE_BRACKETS_RIGHT_SPACE);
        if (mainParameter.contains(CommonConstants.DOUBLE_BRACKETS_LEFT) || mainParameter.contains(CommonConstants.DOUBLE_BRACKETS_RIGHT)) {
            return replaceDoubleBrackets(mainParameter);
        } else {
            return  mainParameter;
        }
    }

    public static String removeSingeQuotes(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }

        return str.replaceAll("'","");
    }

    public static boolean isEmptyOrNullStr(String str) {
        return isEmpty(str) || "null".equalsIgnoreCase(str);
    }

    public static List<Integer> splitToInteger(String value) {
        return splitToInteger(value, CommonConstants.COMMA);
    }

    public static List<Integer> splitToInteger(String value, CharSequence separator) {
        int[] integers = StrUtil.splitToInt(value, separator);
        return Arrays.stream(integers).boxed().collect(Collectors.toList());
    }

    public static List<Long> splitToLong(String value) {
        return splitToLong(value, CommonConstants.COMMA);
    }

    public static List<Long> splitToLong(String value, CharSequence separator) {
        long[] longs = StrUtil.splitToLong(value, separator);
        return Arrays.stream(longs).boxed().collect(Collectors.toList());
    }

    public static String maxLength(CharSequence str, int maxLength) {
        return StrUtil.maxLength(str, maxLength - 3); // -3 的原因，是该方法会补充 ... 恰好
    }

    /**
     * 给定字符串是否以任何一个字符串开始
     * 给定字符串和数组为空都返回 false
     *
     * @param str      给定字符串
     * @param prefixes 需要检测的开始字符串
     * @since 3.0.6
     */
    public static boolean startWithAny(String str, Collection<String> prefixes) {
        if (StrUtil.isEmpty(str) || ArrayUtil.isEmpty(prefixes)) {
            return false;
        }

        for (CharSequence suffix : prefixes) {
            if (StrUtil.startWith(str, suffix, false)) {
                return true;
            }
        }
        return false;
    }

    public static Set<Long> splitToLongSet(String value) {
        return splitToLongSet(value, StrPool.COMMA);
    }

    public static Set<Long> splitToLongSet(String value, CharSequence separator) {
        long[] longs = StrUtil.splitToLong(value, separator);
        return Arrays.stream(longs).boxed().collect(Collectors.toSet());
    }

    /**
     * 移除字符串中，包含指定字符串的行
     *
     * @param content 字符串
     * @param sequence 包含的字符串
     * @return 移除后的字符串
     */
    public static String removeLineContains(String content, String sequence) {
        if (StrUtil.isEmpty(content) || StrUtil.isEmpty(sequence)) {
            return content;
        }
        return Arrays.stream(content.split("\n"))
                .filter(line -> !line.contains(sequence))
                .collect(Collectors.joining("\n"));
    }


    public static String toString(Object obj) {
        if (obj instanceof InputStream) {
            return IoUtil.readUtf8((InputStream) obj);
        }
        if (obj instanceof byte[]) {
            return new String((byte[]) obj);
        }
        if (obj instanceof Clob) {
            Clob clob = (Clob) obj;
            try {
                Reader is = clob.getCharacterStream();
                BufferedReader br = new BufferedReader(is);
                String line = br.readLine();
                StringBuffer sb = new StringBuffer();
                while (line != null) {
                    sb.append(line);
                    line = br.readLine();
                }
                return sb.toString();
            } catch (Exception e) {
                throw new StatusException("Clob转String异常！", e);
            }
        }
        return obj == null ? "" : String.valueOf(obj);
    }
}
