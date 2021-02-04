package org.garry.netty.channel;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChannelPipelineCoverage {

    String ALL = "all";
    String ONE = "one";

    String value();
}
