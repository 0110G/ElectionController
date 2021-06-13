package com.electionController.controllers.electionController;

import com.electionController.exceptions.InvalidParameterException;

public  class ElectionController {

    // TODO: Add support for genenrating election id

    public static void ValidateNotNull(final Object obj) {
        if (obj == null) {
            throw new InvalidParameterException("Invalid Parameter");
        }
    }

    public static Object getValueOrDefault(final Object value, final Object defaultVal) {
        if (value == null) {return defaultVal;}
        return value;
    }
}
