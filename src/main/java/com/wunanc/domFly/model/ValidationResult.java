package com.wunanc.domfly.model;

public final class ValidationResult {
    private final boolean valid;
    private final String key;

    private ValidationResult(boolean valid, String key) {
        this.valid = valid;
        this.key = key;
    }

    public static ValidationResult success() { return new ValidationResult(true, null); }
    public static ValidationResult failure(String key) { return new ValidationResult(false, key); }

    public boolean isValid() { return valid; }
    public String getKey() { return key; }
}
