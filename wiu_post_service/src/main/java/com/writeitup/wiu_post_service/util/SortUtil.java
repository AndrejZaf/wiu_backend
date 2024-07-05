package com.writeitup.wiu_post_service.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

@UtilityClass
public class SortUtil {

    public static Sort.Order parseSortParams(final String sortParams) {
        final String[] split = sortParams.split(";");
        return new Sort.Order(Sort.Direction.fromString(split[1]), split[0]);
    }
}
