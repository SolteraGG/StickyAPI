package com.ristexsoftware.knappy.bukkit.command;

public enum ExitCode {
    ERROR,
    INVALID_SYNTAX,
    PERMISSION_DENIED,
    SUCCESS,
    OUT_OF_RANGE;

    public static ExitCode fromOrdinal(int ordinal) {
        switch (ordinal) {
            case 0:
                return ERROR;
            case 1:
                return INVALID_SYNTAX;
            case 2:
                return PERMISSION_DENIED;
            case 3:
                return SUCCESS;
            default:
                return OUT_OF_RANGE;
        }
    }

}