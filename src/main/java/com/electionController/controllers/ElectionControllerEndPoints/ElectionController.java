package com.electionController.controllers.ElectionControllerEndPoints;

import com.electionController.controllers.ActionController;
import com.electionController.exceptions.InvalidParameterException;

import java.util.List;
import java.util.stream.Collectors;

public class ElectionController extends ActionController {

    protected static void ValidateNotNull(final Object obj) {
        if (obj == null) {
            throw new InvalidParameterException("Invalid Parameter");
        }
    }

    protected static List<String> getUniqueEntities(final List<String> list) {
        if (list == null) {
            return list;
        }
        return list.stream().distinct().collect(Collectors.toList());
    }

    protected static Object getValueOrDefault(final Object value, final Object defaultVal) {
        if (value == null) {return defaultVal;}
        return value;
    }


}
