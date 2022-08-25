package com.example.CarRental.controller;

import com.example.CarRental.model.Owner;
import com.example.CarRental.model.Transaction;
import com.example.CarRental.repository.CarRepository;
import com.example.CarRental.repository.TransactionRepository;
import com.example.CarRental.request.CarRequest;
import com.example.CarRental.request.ProfileEditRequest;
import com.example.CarRental.request.ReviewRequest;
import com.example.CarRental.response.CarEditResp;
import com.example.CarRental.response.CarResp;
import com.example.CarRental.response.ReviewResponse;
import com.example.CarRental.service.CarService;
import com.example.CarRental.service.ImageService;
import com.example.CarRental.service.OwnerService;
import com.example.CarRental.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/api/owner")
@AllArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;
    private final CarService carService;
    private final ReviewService reviewService;
    private final ImageService imageService;
    private final CarRepository carRepository;
    private final TransactionRepository transactionRepository;


    @GetMapping("/showProfile")
    public String showProfile(Model model){
        model.addAttribute("owner", new ProfileEditRequest());
        return "ownersProfile";
    }

    @PostMapping("/editProfile")
    public String editProfile(@ModelAttribute("owner") ProfileEditRequest request, Principal principal){
        String response;
        response = ownerService.editProfile(request, principal.getName());
        return "redirect:/api/owner/showProfile?"+ response;
    }

    @GetMapping("/getProfile")
    @ResponseBody
    public Owner getProfile(Principal principal){
        return ownerService.getOwner(principal.getName());
    }

    @GetMapping("/showCars")
    public String showCars(Principal principal, Model model){
        model.addAttribute("newcar",new CarRequest());
        List<CarResp> carResponse = carService.getOwnersCars(principal.getName());
        model.addAttribute("cars", carResponse);
        return "ownersCars";
    }

    @GetMapping("getCarImage/{carId}")
    public String getCarImage(@PathVariable("carId") Long carId, Model model){
        List<String> image = imageService.getImageById(carId);
        model.addAttribute("img", image);
        return "images";
    }

    @RequestMapping(value = "/cars/delete", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteCar(Long carId){
        carService.deleteCarById(carId);
        return "redirect:/api/owner/showCars";
    }

    @RequestMapping(value = "/cars/edit", method = {RequestMethod.PUT, RequestMethod.GET})
    public String editCar(CarEditResp car){
        carService.editCar(car);
        return "redirect:/api/owner/showCars";
    }

    @PostMapping("/cars/new")
    public String addCar(CarRequest request, Principal principal) throws IOException {
        String username = principal.getName();
        Long id = carService.addCar(request, username);
        imageService.uploadImage(request.getImages(),id);
        return "redirect:/api/owner/showCars";
    }

    @GetMapping("/transactions")
    public String getTransactions(Principal principal, Model model){
        List<Transaction> transactions = ownerService.getTransactions(principal.getName());
        model.addAttribute("transactions", transactions);
        model.addAttribute("reviews", new ReviewRequest());
        return "transactions";
    }

    @PostMapping("/completeTransaction/{id}")
    public String completeTr(@PathVariable("id") Long id) {
        String response =ownerService.completeTransaction(id);
        return "redirect:/api/owner/transactions?"+ response;
    }

    @PostMapping("/acceptRentalRequest/{transactionId}")
    public String acceptRentalRequest(@PathVariable("transactionId") Long transactionId) {
        String response = ownerService.acceptTransaction(transactionId);
        return "redirect:/api/owner/transactions?" + response;
    }

    @PostMapping("/createReview/{id}")
    public String createReview(ReviewRequest reviewRequest, @PathVariable("id") Long id) {
        String response = reviewService.createReview(reviewRequest,id,true);
        return "redirect:/api/owner/transactions?" + response;
    }

    @GetMapping("/seeReview")
    @ResponseBody
    public ReviewResponse seeReview(Long trId){
        return reviewService.seeOwnersFromRenterReview(trId);
    }
}
