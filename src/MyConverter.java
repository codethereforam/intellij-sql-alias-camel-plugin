import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterators;
import com.google.common.collect.Streams;

import java.util.stream.Collectors;

/**
 * @author thinkam
 * @date 2019/6/2 11:56
 */
public class MyConverter implements Converter {
    @Override
    public String convert(String origin)  {
        return Streams.stream(Splitter.on(",").trimResults()
                .omitEmptyStrings()
                .split(origin))
                .map(s -> {
                    if (s.contains(" ")) {
                        return s;
                    }
                    return s + " " + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,
                            Iterators.getLast(Splitter.on(".").split(s).iterator()));
                })
                .collect(Collectors.joining(", "));
    }
}
