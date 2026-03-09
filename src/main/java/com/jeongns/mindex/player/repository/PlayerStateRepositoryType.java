package com.jeongns.mindex.player.repository;

import lombok.NonNull;

import java.util.Locale;

public enum PlayerStateRepositoryType {
    FILE,
    IN_MEMORY,
    POSTGRESQL,
    MYSQL;

    public static PlayerStateRepositoryType fromConfig(@NonNull String value) {
        try {
            return PlayerStateRepositoryType.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("지원하지 않는 player-state-storage 값입니다: " + value, e);
        }
    }
}
