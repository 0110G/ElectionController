package com.ElectionController.Controllers.ElectionControllerEndPoints;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Controllers.ActionController;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Exceptions.InvalidParameterException;
import com.ElectionController.Structures.Voter;

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
