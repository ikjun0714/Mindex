package com.jeongns.mindex.service.registration;

import lombok.NonNull;

public record RegistrationResult(
        @NonNull RegistrationStatus status,
        String entryId,
        String entryName
) {
    public static RegistrationResult entryNotFound(@NonNull String entryId) {
        return new RegistrationResult(RegistrationStatus.ENTRY_NOT_FOUND, entryId, null);
    }

    public static RegistrationResult requirementNotMet(@NonNull String entryId, @NonNull String entryName) {
        return new RegistrationResult(RegistrationStatus.REQUIREMENT_NOT_MET, entryId, entryName);
    }

    public static RegistrationResult alreadyRegistered(@NonNull String entryId, @NonNull String entryName) {
        return new RegistrationResult(RegistrationStatus.ALREADY_REGISTERED, entryId, entryName);
    }

    public static RegistrationResult success(@NonNull String entryId, @NonNull String entryName) {
        return new RegistrationResult(RegistrationStatus.SUCCESS, entryId, entryName);
    }
}
