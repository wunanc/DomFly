package com.wunanc.DomFly.exception;

import java.util.Map;

public class FlightCommandException extends RuntimeException {
    private final String messageKey;
    private final Map<String, String> params;

    public FlightCommandException(String messageKey) {
        this(messageKey, Map.of());
    }
    public FlightCommandException(String messageKey, Map<String, String> params) {
        super("Command execution failed: " + messageKey);
        this.messageKey = messageKey;
        this.params = params;
    }
    public String getMessageKey() {
        return messageKey;
    }
    public Map<String, String> getParams() {
        return params;
    }
}
