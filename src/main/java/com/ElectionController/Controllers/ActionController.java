package com.ElectionController.Controllers;

import com.ElectionController.DatabaseConnector.Getter.H2Getter;
import com.ElectionController.DatabaseConnector.Putter.H2Putter;
import com.ElectionController.DatabaseConnector.Updater.H2Updater;
import org.springframework.beans.factory.annotation.Autowired;

public class ActionController {
    @Autowired
    protected H2Getter h2Getter;

    @Autowired
    protected H2Putter h2Putter;

    @Autowired
    protected H2Updater h2Updater;
}
