package me.dynmie.commands;

import lombok.Getter;

/**
 * @author dynmie
 */
public enum CommanResult {

    OK(true),

    NO_PERMISSIONS(false),

    INCORRECT_USAGE(false),

    PLAYER_NOT_EXIST(false),

    SILENT_FAIL(false);

    private final @Getter boolean success;

    CommanResult(boolean success) {
        this.success = success;
    }

}
