package com.relicum.dual.Commands.Exception;

/**
 * Name: IncompleteSelection.java Created: 11 November 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class IncompleteSelection extends Exception {

    public IncompleteSelection() {
        super();
    }

    public IncompleteSelection(String message) {

        super(message);
    }

    public IncompleteSelection(String message, Throwable throwable) {

        super(message, throwable);
    }

}
