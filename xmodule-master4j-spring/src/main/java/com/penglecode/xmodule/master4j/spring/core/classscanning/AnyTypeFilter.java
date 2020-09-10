package com.penglecode.xmodule.master4j.spring.core.classscanning;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * 任意类型的
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/6 23:29
 */
public class AnyTypeFilter implements TypeFilter {

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        return true;
    }

}
