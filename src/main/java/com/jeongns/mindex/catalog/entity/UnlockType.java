package com.jeongns.mindex.catalog.entity;

import java.util.Locale;

public enum UnlockType {
    ITEM;

    public static UnlockType fromConfig(String rawValue) {
        if (rawValue == null) {
            throw new IllegalArgumentException("필수 문자열 누락: entries.unlockType");
        }

        String normalized = rawValue.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("필수 문자열 누락: entries.unlockType");
        }

        try {
            return UnlockType.valueOf(normalized.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 unlockType: " + rawValue, e);
        }
    }
}
