package boot.apps.helper;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
@Log4j2
public class MoneyChangerHelper {
	
	public void performValidation(double bill, AtomicInteger quarterAlloc, AtomicInteger dimesAlloc, AtomicInteger nickelsAlloc, AtomicInteger penniesAlloc) throws Exception {
		
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
	
	public HashMap<String, Integer> getChangeBreakdown(double changeDue, AtomicInteger quarterAlloc, AtomicInteger dimesAlloc, AtomicInteger nickelsAlloc, AtomicInteger penniesAlloc) {
		HashMap<String, Integer> mapValues = new HashMap<String, Integer>();
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
		
		mapValues.put("quarters", quarters);
		mapValues.put("dimes"	, dimes);
		mapValues.put("nickels"	, nickels);
		mapValues.put("pennies"	, pennies);
		
		return mapValues;	
	}
	
	
	public String getResponseString(HashMap<String, Integer> mapValues,AtomicInteger quarterAlloc, AtomicInteger dimesAlloc, AtomicInteger nickelsAlloc, AtomicInteger penniesAlloc) {
		StringBuilder builder = new StringBuilder();
		builder.append("Coins for this change are ");
		builder.append(mapValues.get("quarters") + " Quarters , ");
		builder.append(mapValues.get("dimes")    + " Dimes , ");
		builder.append(mapValues.get("nickels")  + " Nickels, ");
		builder.append(mapValues.get("pennies")  + " Pennies, ");
		builder.append(":: Max Counters are ");
		builder.append(quarterAlloc.get() + " Max Quarters , ");
		builder.append(dimesAlloc.get()   + " Max Dimes ,");
		builder.append(nickelsAlloc.get() + " Max Nickels ,");
		builder.append(penniesAlloc.get() + " Max Pennies ,");
		return builder.toString();
	}
	

}
