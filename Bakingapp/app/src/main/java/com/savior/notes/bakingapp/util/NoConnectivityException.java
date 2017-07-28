package com.savior.notes.bakingapp.util;

import java.io.IOException;

/**
 * Created by 700000075 on 7/28/2017.
 */
public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No connectivity exception";
    }

}