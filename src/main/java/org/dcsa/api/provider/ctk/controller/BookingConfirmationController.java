package org.dcsa.api.provider.ctk.controller;

import org.dcsa.api.provider.ctk.init.AppProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@Controller
public class BookingConfirmationController {
	private CompletableFuture<String> completableFuture;
	public CompletableFuture<String> getBookingIdAsync(String bookingId) {
		completableFuture = new CompletableFuture<>();
		Executors.newCachedThreadPool().submit(() -> {
			Thread.sleep(500);
			completableFuture.complete(bookingId);
			return null;
		});
		return completableFuture;
	}

	@PostMapping("/form")
	@ResponseBody
	public ResponseEntity<String> submitForm(@RequestParam("bookingId") String bookingId) throws ExecutionException, InterruptedException {
		System.out.println("Received bookingId: " + bookingId);
		completableFuture = getBookingIdAsync(bookingId);
		return ResponseEntity.ok("Booking id is set successfully "+completableFuture.get());
	}
	@GetMapping("/bookingView")
	public String  getData(Model model) throws ExecutionException, InterruptedException {
		if (completableFuture == null) {
			model.addAttribute("errorMessage1", "Booking ID is not submitted by official API yet! Refresh the page after the booking id is set by the official API.");
			model.addAttribute("errorMessage2", "Newman will set the booking ID when it runs the official booking API.");
			return "errorView";
		}
		// Wait for the form to be completed
		var bookingId = completableFuture.get();
		model.addAttribute("id", bookingId);
		int delayAmount = 3000; // Delay amount in milliseconds
		model.addAttribute("delayAmount", delayAmount);
		return "bookingView";
	}

	@GetMapping("/bookingId")
	@ResponseBody
	public String getBookingId() throws ExecutionException, InterruptedException {
		if(completableFuture == null){
			return  "Booking ID is not submitted by official API yet!";
		}else{
			return completableFuture.get();
		}
	}

	@GetMapping("/example")
	public String example() throws InterruptedException {
		// Simulate delay on the server-side
		Thread.sleep(AppProperty.BOOKING_DELAY);
		return "Delayed response after " + AppProperty.BOOKING_DELAY + " milliseconds";
	}

}
