package boot.apps.controller;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import boot.apps.config.AppConfig;
import boot.apps.model.Error;
import boot.apps.model.Metadata;
import boot.apps.model.ResponseHelper;
import lombok.extern.log4j2.Log4j2;

@RestController
@RequestMapping("/moneyops")
@Log4j2
public class MoneyOpsController {
	
	private volatile int quarterCounter;
	private volatile int dimesCounter;
	private volatile int nickelsCounter;
	private volatile int penniesCounter;
	
	private AppConfig appConfig;
	
	public MoneyOpsController(AppConfig appConfig) {
		this.appConfig = appConfig;
		initCounters();
	}
	
	private synchronized void initCounters() {
		quarterCounter = Integer.parseInt(appConfig.getProperty("app.config.maxalloc.quarters"));
		dimesCounter   = Integer.parseInt(appConfig.getProperty("app.config.maxalloc.dimes"));
		nickelsCounter = Integer.parseInt(appConfig.getProperty("app.config.maxalloc.nickels"));
		penniesCounter = Integer.parseInt(appConfig.getProperty("app.config.maxalloca.pennies"));
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
			log.error("Error with change endpoint - moneyops", ex);
			
			Metadata metadata = new Metadata("Response for requesting change - moneyops", "/moneyops/change", 500, requestTime);
			
			List<Error> lsErrors = new ArrayList<> ();
			
			Error error = new Error("500");
			lsErrors.add(error);
			
			ResponseHelper responseHelper = new ResponseHelper(null, metadata, null, lsErrors);
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(responseHelper);
		}
	}
	
	private synchronized String getChangeBreakdown(double changeDue) {
		int change = (int) Math.ceil(changeDue*100);
		
		int intMaxQuarters = quarterCounter;
		int quarters       = Math.round((int) change/25);
		
		if(intMaxQuarters - quarters >= 0) {
			change = change % 25;
			quarterCounter = intMaxQuarters - quarters;
		}
		
		int intMaxDimes = dimesCounter;
		int dimes       = Math.round((int) change/10);
		if(intMaxDimes - dimes >= 0) {
			change = change % 10;
			dimesCounter = intMaxDimes - dimes;
		}
		
		int intMaxNickels = nickelsCounter;
		int nickels = Math.round((int) change/5);
		
		if(intMaxNickels - nickels >= 0) {
			change = change % 5;
			nickelsCounter = intMaxNickels - nickels; 
		}
		
		int intMaxPennies = penniesCounter;
		int pennies = Math.round((int) change/1);
		
		if(intMaxPennies - pennies >= 0) {
			penniesCounter = intMaxPennies - pennies;
		}
		
		return getResponseString(quarters, dimes, nickels, pennies);	
	}
	
	private void performValidation(double bill) throws Exception {
		if(!(bill == 1 || bill == 2 || bill == 5 || bill == 10 || bill == 20 || bill == 50 || bill == 100)) {
			throw new Exception("Unknown bill denomination. Available bills are 1, 2, 5, 10, 20, 50, 100");
		}
	}
	
	private String getResponseString(int quarters, int dimes, int nickels, int pennies) {
		StringBuilder builder = new StringBuilder();
		builder.append("Change breakdown is ");
		builder.append(quarters + " Quarters , ");
		builder.append(dimes    + " Dimes , ");
		builder.append(nickels  + " Nickels, ");
		builder.append(pennies  + " Pennies, ");
		builder.append("Counters are ");
		builder.append(quarterCounter + " Max Quarters , ");
		builder.append(dimesCounter   + " Max Dimes ,");
		builder.append(nickelsCounter + " Max Nickels ,");
		builder.append(penniesCounter + " Max Pennies ,");
		return builder.toString();
	}

}
