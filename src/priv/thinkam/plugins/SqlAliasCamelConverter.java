package priv.thinkam.plugins;

import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterators;
import com.google.common.collect.Streams;

import java.util.stream.Collectors;

/**
 * @author thinkam
 * @date 2019/6/3 22:15
 */
public class SqlAliasCamelConverter implements Converter {
    /**
     * " "
     */
    private static final String SPACE_STRING = " ";

    /**
     * 字符串转换(e.g. “user_name, b.user_age” 被转换成 "user_name userName, b.user_age userAge”)
     *
     * @param originalString 原来的文本
     * @return 转换后的字符串
     * @author thinkam
     * @date 2019/5/30 22:48
     */
    @Override
    public String convert(String originalString) {
        return Streams.stream(Splitter.on(",").trimResults()
                .omitEmptyStrings()
                .split(originalString))
                .map(this::addCamelBehind)
                .collect(Collectors.joining(", "));
    }

    private String addCamelBehind(String s) {
        if (s.contains(SPACE_STRING)) {
            return s;
        }
        return s + " " + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,
                Iterators.getLast(Splitter.on(".").split(s).iterator()));
    }
}
