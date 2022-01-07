package boot.apps.controller;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import boot.apps.config.AppConfig;
import boot.apps.model.Error;
import boot.apps.model.Metadata;
import boot.apps.model.ResponseHelper;
import lombok.extern.log4j2.Log4j2;

@RestController
@RequestMapping("/moneyops")
@Log4j2
public class MoneyOpsController {
	
	private AtomicInteger quarterAlloc = new AtomicInteger();
	private AtomicInteger dimesAlloc   = new AtomicInteger();
	private AtomicInteger nickelsAlloc = new AtomicInteger();
	private AtomicInteger penniesAlloc = new AtomicInteger();
	
	private AppConfig appConfig;
	
	public MoneyOpsController(AppConfig appConfig) {
		this.appConfig = appConfig;
		initCounters();
	}
	
	private void initCounters() {
		quarterAlloc.set(Integer.parseInt(appConfig.getProperty("app.config.maxalloc.quarters")));
		dimesAlloc.set(Integer.parseInt(appConfig.getProperty("app.config.maxalloc.dimes")));
		nickelsAlloc.set(Integer.parseInt(appConfig.getProperty("app.config.maxalloc.nickels")));
		penniesAlloc.set(Integer.parseInt(appConfig.getProperty("app.config.maxalloca.pennies")));
	}
	
	@RequestMapping(value="/health", method = RequestMethod.GET)
	public ResponseEntity<String> health() throws Exception {
		return new ResponseEntity<String>("Money Ops controller active", HttpStatus.OK);
	}
	
	@RequestMapping(value="/change", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseHelper> requestChange(@RequestParam("bill") double bill, HttpServletRequest request) {
		Instant requestTime = Instant.now();
		try {
			Metadata metadata = new Metadata("Response for change endpoint" , "/moneyops/change", 200, requestTime);
			
			performValidation(bill);
			
			String changeBreakdown = getChangeBreakdown(bill);
			
			ResponseHelper responseHelper = new ResponseHelper(changeBreakdown, metadata);
			
			return ResponseEntity.ok(responseHelper);
		} catch (Exception ex) {
			log.error("Error with change endpoint - moneyops ", ex);
			
			Metadata metadata = new Metadata("Response for requesting change - moneyops :: " + ex.getMessage(), "/moneyops/change", 500, requestTime);
			
			List<Error> lsErrors = new ArrayList<> ();
			
			Error error = new Error("500");
			lsErrors.add(error);
			
			ResponseHelper responseHelper = new ResponseHelper(null, metadata, null, lsErrors);
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(responseHelper);
		}
	}

	private void performValidation(double bill) throws Exception {
		
		// Check if input is valid
		if(!(bill == 1 || bill == 2 || bill == 5 || bill == 10 || bill == 20 || bill == 50 || bill == 100)) {
			throw new Exception("Unknown bill denomination. Available bills are 1, 2, 5, 10, 20, 50, 100");
		}
		
		// Check if we have enough coins
		int change = (int) Math.ceil(bill * 100);
		if( change > ((quarterAlloc.get() * 25) + (dimesAlloc.get() * 10) + (nickelsAlloc.get() * 5) + penniesAlloc.get() ) ) {
			throw new Exception ("There is not enough coins for change. Kindly reconfigure and try again. Thank you.");
		}

	}
	
	private String getChangeBreakdown(double changeDue) {
		int change = (int) Math.ceil(changeDue*100);
		
		int quarters  = (quarterAlloc.get() > 0) ? Math.round((int) change/25) : 0;
		if(quarterAlloc.get() > 0 && quarters > 0) {
			if(quarterAlloc.get() >= quarters) {
				change = change % 25;
				quarterAlloc.set(quarterAlloc.get() - quarters);
			} else {
				quarters = quarters - quarterAlloc.get();
				change = (quarters * 25);
				quarterAlloc.set(0);
			}
		}
		
		int dimes = (dimesAlloc.get() > 0) ? Math.round((int) change/10) : 0;
		if (dimes > 0 && dimesAlloc.get() > 0) {
			if(dimesAlloc.get() >= dimes) {
				change = change % 10;
				dimesAlloc.set(dimesAlloc.get() - dimes);
			} else {
				dimes = dimes - dimesAlloc.get();
				change = (dimes * 10);
				dimesAlloc.set(0);
			}
		}
		
		int nickels = (nickelsAlloc.get() > 0) ? Math.round((int) change/5) : 0;
		
		if(nickelsAlloc.get() > 0 && nickels > 0) {
			if(nickelsAlloc.get() >= nickels) {
				change = change % 5;
				nickelsAlloc.set(nickelsAlloc.get() - nickels); 
			} else {
				nickels = nickels - nickelsAlloc.get();
				change = (nickels * 5);
				nickelsAlloc.set(0);
			}
		}
		
		int pennies = Math.round((int) change/1);
		
		if(penniesAlloc.get() > 0 && pennies > 0) {
			if(penniesAlloc.get() >= pennies) {
				penniesAlloc.set(penniesAlloc.get() - pennies);
			} else {
				pennies = pennies - penniesAlloc.get();
				change = pennies;
				penniesAlloc.set(0);
			}
		}
		
		return getResponseString(quarters, dimes, nickels, pennies);	
	}
	
	
	private String getResponseString(int quarters, int dimes, int nickels, int pennies) {
		StringBuilder builder = new StringBuilder();
		builder.append("Coins for this change are ");
		builder.append(quarters + " Quarters , ");
		builder.append(dimes    + " Dimes , ");
		builder.append(nickels  + " Nickels, ");
		builder.append(pennies  + " Pennies, ");
		builder.append(":: Max Counters are ");
		builder.append(quarterAlloc.get() + " Max Quarters , ");
		builder.append(dimesAlloc.get()   + " Max Dimes ,");
		builder.append(nickelsAlloc.get() + " Max Nickels ,");
		builder.append(penniesAlloc.get() + " Max Pennies ,");
		return builder.toString();
	}

}
