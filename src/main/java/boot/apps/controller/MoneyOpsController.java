package boot.apps.controller;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
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
import boot.apps.helper.MoneyChangerHelper;
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
	private MoneyChangerHelper moneyChangerHelper;
	
	public MoneyOpsController(AppConfig appConfig, MoneyChangerHelper moneyChangerHelper) {
		this.appConfig 			= appConfig;
		this.moneyChangerHelper = moneyChangerHelper;
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
			
			moneyChangerHelper.performValidation(bill, quarterAlloc, dimesAlloc, nickelsAlloc, penniesAlloc);
			
			HashMap<String, Integer> mapValues = moneyChangerHelper.getChangeBreakdown(bill, quarterAlloc, dimesAlloc, nickelsAlloc, penniesAlloc);
			String changeBreakdown = moneyChangerHelper.getResponseString(mapValues,quarterAlloc, dimesAlloc, nickelsAlloc, penniesAlloc);
			
			ResponseHelper responseHelper = new ResponseHelper(changeBreakdown, metadata);
			
			return ResponseEntity.ok(responseHelper);
		} catch (Exception ex) {
			log.error("Error with change endpoint - moneyops ", ex);
			
			Metadata metadata = new Metadata("Response for requesting change - moneyops :: " + ex.getMessage(), "/moneyops/change", 500, requestTime);
			
			List<Error> lsErrors = new ArrayList<> ();
			
			Error error = new Error("500");
			lsErrors.add(error);
			
			ResponseHelper responseHelper = new ResponseHelper(metadata, lsErrors);
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(responseHelper);
		}
	}


}
