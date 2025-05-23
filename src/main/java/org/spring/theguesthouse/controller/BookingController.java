package org.spring.theguesthouse.controller;

import lombok.RequiredArgsConstructor;
import org.spring.theguesthouse.dto.BookingDTO;
import org.spring.theguesthouse.repository.BookingRepo;
import org.spring.theguesthouse.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(path ="/bookings")
@RequiredArgsConstructor
public class BookingController {



}
