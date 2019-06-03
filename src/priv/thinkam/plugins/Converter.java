package priv.thinkam.plugins;

/**
 * 字符串转换器
 *
 * @author thinkam
 * @date 2019/6/3 22:10
 */
public interface Converter {
    /**
     * convert string
     *
     * @param originalString original string
     * @return converted string
     */
    String convert(String originalString);
}
