package com.example.CarRental.controller;

import com.example.CarRental.model.Renter;
import com.example.CarRental.model.Transaction;
import com.example.CarRental.repository.TransactionRepository;
import com.example.CarRental.request.ProfileEditRequest;
import com.example.CarRental.request.RentRequest;
import com.example.CarRental.request.ReviewRequest;
import com.example.CarRental.response.CarResp;
import com.example.CarRental.response.ReviewResponse;
import com.example.CarRental.service.CarService;
import com.example.CarRental.service.RenterService;
import com.example.CarRental.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/api/renter")
@AllArgsConstructor
public class RenterController {

    private final RenterService renterService;
    private final ReviewService reviewService;
    private final CarService carService;
    private final TransactionRepository transactionRepository;


    @GetMapping("/showProfile")
    public String showProfile(Model model){
        model.addAttribute("renter", new ProfileEditRequest());
        return "rentersProfile";
    }

    @PostMapping("/editProfile")
    public String editProfile(@ModelAttribute("renter") ProfileEditRequest request, Principal principal){
        String response;
        response = renterService.editProfile(request,principal.getName());
        return "redirect:/api/renter/showProfile?"+ response;
    }

    @GetMapping("/getProfile")
    @ResponseBody
    public Renter getProfile(Principal principal) {
        return renterService.getRenter(principal.getName());
    }

    @PostMapping("/rents/{carId}")
    public String rentCar(Principal principal, @PathVariable("carId") Long carId, @ModelAttribute("rent")RentRequest rentRequest){
        String response = renterService.sendCarRentalRequest(principal.getName(), carId, rentRequest);
        if(response.equals("successful"))
            return "redirect:/api/renter/cars?"+ response;
        else
            return "redirect:/api/renter/getCar/" + carId + "?" + response;
    }

    @GetMapping("/cars")
    public String getAvailableCars(Model model){
        List<CarResp> cars = carService.getRenterCars();
        model.addAttribute("cars",cars);
        return "cars";
    }

    @GetMapping("getCar/{Id}")
    public String getCar(Model model, @PathVariable("Id") Long id){
        CarResp car = carService.getOne(id);
        model.addAttribute("car", car);
        model.addAttribute("rent", new RentRequest());
        return "rentalForm";
    }

    @GetMapping("/rental")
    public String getRental(Principal principal, Model model){
        List<Transaction> transactions = renterService.getRentals(principal.getName());
        model.addAttribute("transactions", transactions);
        return "rental";
    }

    @PostMapping("/createReview/{id}")
    public String createReview(ReviewRequest reviewRequest, @PathVariable("id") Long id) {
        String response = reviewService.createReview(reviewRequest,id,false);
        return "redirect:/api/renter/rental?" +response;
    }

    @GetMapping("/seeReview")
    @ResponseBody
    public ReviewResponse seeReview(Long trId){
        return reviewService.seeRentersFromOwnerReview(trId);
    }

}
