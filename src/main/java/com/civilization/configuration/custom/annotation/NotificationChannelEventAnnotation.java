package com.civilization.configuration.custom.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface NotificationChannelEventAnnotation {
    String channels() default "discord.channel.notification.name";
}
