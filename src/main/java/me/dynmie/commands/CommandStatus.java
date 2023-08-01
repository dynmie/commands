package me.dynmie.commands;

import lombok.Getter;

/**
 * @author dynmie
 */
public enum CommandStatus {

    OK(true),

    NO_PERMISSIONS(false),

    INCORRECT_USAGE(false),

    PLAYER_NOT_EXIST(false),

    SILENT_FAIL(false);

    private final @Getter boolean success;

    CommandStatus(boolean success) {
        this.success = success;
    }

}
