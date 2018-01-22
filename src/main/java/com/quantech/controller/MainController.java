package com.quantech.controller;

import com.quantech.facade.FacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    @Autowired
    private FacadeService dbs;

    // TODO: I'm using this admin as a placeholder for a lot of these mappings - separate out later.
    // TODO: Decide what URLS + mappings we need.
    // TODO: Login.

    @GetMapping("/")
    public String viewHome() {
        return "quantech";
    }

    // Create new team.

    // Add doctors to existing team.

    // Remove doctors from existing team.

    // Delete team.

    // Deactivate/delete account.
}
