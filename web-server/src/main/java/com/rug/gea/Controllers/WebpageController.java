package com.rug.gea.Controllers;

import com.rug.gea.Collections.ClientsRepository;
import com.rug.gea.Collections.DataRepository;
import com.rug.gea.DataModels.Client;
import com.rug.gea.DataModels.Data;
import com.rug.gea.DataModels.Information;
import com.rug.gea.DataModels.PredictWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.TimeoutException;

@Controller
public class WebpageController {
    @Autowired
    private ClientsRepository clients;
    @Autowired
    private DataRepository data;

    private void addAttributes(Model model,double gas,double electricity) {
        model.addAttribute("expectedElectricity", String.format("%.2f kWh per day",gas));
        model.addAttribute("expectedGas", String.format("%.2f kWh per day",electricity));
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
        try {
            MessageController.sendMessage(client.zip, new Information(Information.Request.Create, client));
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        addAttributes(model,0,0);
        return "index";
    }

    @RequestMapping(value = "predict", method = RequestMethod.POST)
    public String predict(Model model,@ModelAttribute PredictWrapper metersAndType){
        List<Data> readings = data.findByType(metersAndType.getType());
        double gas = 0;
        double electricity = 0;
        OptionalDouble optionalGas = readings.stream().mapToDouble(Data::getGas).average();
        if (optionalGas.isPresent()) {
            gas = optionalGas.getAsDouble() * metersAndType.getSqm();
        }
        OptionalDouble optionalElectricity = readings.stream().mapToDouble(Data::getElectricity).average();
        if (optionalElectricity.isPresent()){
            electricity = optionalElectricity.getAsDouble() * metersAndType.getSqm();
        }
        addAttributes(model,gas,electricity);
        return "index";
    }

}
