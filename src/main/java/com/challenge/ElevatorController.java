package com.challenge;

import com.challenge.elevator.CallRequest;
import com.challenge.elevator.CallSystem;
import com.challenge.elevator.Direction;
import com.challenge.elevator.Elevator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by david on 10/30/16.
 */
@RestController
public class ElevatorController {
    @Autowired
    private CallSystem callSystem;

    @RequestMapping("/snapshot")
    public List<String> snapshot() {
        final List<String> result = new ArrayList<>();
        callSystem.getElevators().forEach( elevator -> {
            result.add(elevator.toString());
        });

        return result;
    }

    @RequestMapping("/call")
    public Object call(@RequestParam(name = "level") int level, @RequestParam(name = "direction") Direction direction) {
        Map<String, Object> result = new LinkedHashMap<>();
        CallRequest cr = new CallRequest(direction, level);

        result.put("snapshot", snapshot());
        Elevator elevator = callSystem.request(cr);
        result.put("your_elevator", elevator.getNumber());
        return result;
    }

    @RequestMapping("/press")
    public Object press(@RequestParam(name = "level") int level, @RequestParam(name = "elevator") int elevator) {
        if (elevator < 1 || elevator > callSystem.getElevators().size()) {
            return "elevator number " + elevator + " is not available";
        }
        Elevator el = callSystem.getElevator(elevator);

        el.respond(level);

        return el.toString();
    }
}
