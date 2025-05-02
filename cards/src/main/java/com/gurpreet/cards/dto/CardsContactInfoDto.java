package com.gurpreet.cards.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/*
    Commenting this record class as with
    records class, all the variables become final
    and cannot change the values at runtime.
    To run the values at runtime, we will define
    a new class and use spring boot actuator to refresh the
    properties at runtime.
@ConfigurationProperties(prefix = "cards")
public record CardsContactInfoDto(String message, Map<String,String> contactDetails,
                                  List<String> onCallSupport) {
}
 */

@ConfigurationProperties(prefix = "cards")
@Getter
@Setter
public class CardsContactInfoDto{
    private String message;
    private Map<String,String> contactDetails;
    private List<String> onCallSupport;
}