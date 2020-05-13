package com.civilization.configuration.custom.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface RatingGamesChannelEventAnnotation {
    String channels() default "discord.channel.rating-games.name";
}
