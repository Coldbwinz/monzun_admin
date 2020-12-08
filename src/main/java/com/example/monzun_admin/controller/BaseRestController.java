package com.example.monzun_admin.controller;

import java.util.HashMap;
import java.util.Map;

public class BaseRestController {
    protected Map<String, Boolean> getFalseResponse() {
        Map<String, Boolean> successFalse = new HashMap<>();
        successFalse.put("success", false);

        return successFalse;
    }

    protected Map<String, Boolean> getTrueResponse() {
        Map<String, Boolean> successFalse = new HashMap<>();
        successFalse.put("success", true);

        return successFalse;
    }
}
