package com.civilization.bot.event.rule.impl;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FreezeGameMessageRuleTest {

    private FreezeGameMessageRule testInstance = new FreezeGameMessageRule();

    @Test
    public void shouldBeCorrectForValidMessage() {
        assertThat(testInstance.getRule().test("!freeze 58")).isTrue();
    }
}