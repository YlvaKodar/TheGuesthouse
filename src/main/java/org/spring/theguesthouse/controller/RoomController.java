package org.spring.theguesthouse.controller;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.RoomDto;
import org.spring.theguesthouse.entity.Room;
import org.spring.theguesthouse.repository.RoomRepo;
import org.spring.theguesthouse.service.RoomService;
import org.spring.theguesthouse.service.impl.RoomServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RequestMapping((path ="/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomRepo roomRepo;
    private final RoomService roomService;

    @RequestMapping("/available")
    public String showAvailableRooms(Model model) {
        List<RoomDto> roomsList = roomService.getAllRooms();
        model.addAttribute("roomsTitle", "Rooms");
        model.addAttribute("allRooms", roomsList);
        model.addAttribute("ID", "id");
        model.addAttribute("displayRooms", "true");
        return "showAvailableRooms";

    }

}
