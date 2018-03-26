package com.example.demo.Controllers;

import com.example.demo.Collections.ClientsRepository;
import com.example.demo.Collections.DataRepository;
import com.example.demo.DataModels.Client;
import com.example.demo.DataModels.PredictWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebpageController {
    @Autowired
    private ClientsRepository clients;
    @Autowired
    private DataRepository data;

    private void addAttributes(Model model,int gas,int electricity) {
        model.addAttribute("expectedElectricity", gas);
        model.addAttribute("expectedGas",electricity);
        model.addAttribute("client", new Client()); // adding in model
        model.addAttribute("metersAndType", new PredictWrapper());
    }

    @GetMapping("/")
    String index(Model model){
        addAttributes(model,0,0);
        return "index";
    }

    @RequestMapping(value = "client", method = RequestMethod.POST)
    public String addClient(Model model,@ModelAttribute Client client){
        clients.save(client);
        addAttributes(model,0,0);
        return "index";
    }

    @RequestMapping(value = "predict", method = RequestMethod.POST)
    public String predict(Model model,@ModelAttribute PredictWrapper metersAndType){
        System.out.println(data.findByType(metersAndType.getType()));
        addAttributes(model,0,0);
        return "index";
    }

}