package me.dynmie.commands;

import lombok.Getter;

/**
 * @author dynmie
 */
public enum CommandResult {

    OK(true),

    NO_PERMISSIONS(false),

    INCORRECT_USAGE(false),

    PLAYER_NOT_EXIST(false),

    SILENT_FAIL(false);

    private final @Getter boolean success;

    CommandResult(boolean success) {
        this.success = success;
    }

}
