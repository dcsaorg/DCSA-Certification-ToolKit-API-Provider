package org.dcsa.api.provider.ctk.controller;

import org.dcsa.api.provider.ctk.init.AppProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.ExecutionException;

@Controller
public class BookingConfirmationController {

	public static int REQUESTED_DELAY;

	private String officialBookingId;
	@PostMapping("/officialBookingId")
	@ResponseBody
	public ResponseEntity<String> submitForm(@RequestParam("bookingId") String bookingId) throws ExecutionException, InterruptedException {
		System.out.println("Received bookingId: " + bookingId);
		officialBookingId = bookingId;
		return ResponseEntity.ok("Booking id:" +officialBookingId+ "is set successfully ");
	}
	@GetMapping("/bookingView")
	public String  getVide(Model model) {
		if (officialBookingId == null) {
			model.addAttribute("errorMessage1", "Booking ID is not submitted by official API yet!");
			model.addAttribute("errorMessage2", "Please wait for the Newman until it runs the official booking API. It will show the booking ID here.");
			return "errorView";
		}
		model.addAttribute("id", officialBookingId);
		int delayAmount = AppProperty.BOOKING_DELAY; // Delay amount in milliseconds
		model.addAttribute("delayAmount", delayAmount);
		return "bookingView";
	}

	@GetMapping("/bookingId")
	@ResponseBody
	public String getBookingId() {
		if(officialBookingId == null){
			return  "Booking ID is not submitted by official API yet!";
		}else{
			return officialBookingId;
		}
	}

	@PostMapping("/submitForm")
	@ResponseBody
	public ResponseEntity<String> submittedBookingId(@RequestParam("id") String id) {
		System.out.println("Submitted ID: " + id);
		return ResponseEntity.ok("Booking ID: <b>"+id+"</b> submitted.");
	}

	@PostMapping("/submitDelay")
	@ResponseBody
	public ResponseEntity<String> submitDelay(@RequestParam("delay") String delay) throws InterruptedException {
		REQUESTED_DELAY = Integer.parseInt(delay);
		System.out.println("Delay for : " + REQUESTED_DELAY + " milliseconds");
		Thread.sleep(REQUESTED_DELAY);
		return ResponseEntity.ok("Delayed response after " + REQUESTED_DELAY/1000 + " seconds");
	}
}
