package boot.apps.helper;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MoneyChangerTests {
	
	@Test
	void validBillInput() {
		
		MoneyChangerHelper moneyChangerHelper = new MoneyChangerHelper();
		
		Assertions.assertTrue(moneyChangerHelper.isValidBill(1));
		Assertions.assertTrue(moneyChangerHelper.isValidBill(2));
		Assertions.assertTrue(moneyChangerHelper.isValidBill(5));
		Assertions.assertTrue(moneyChangerHelper.isValidBill(10));
		Assertions.assertTrue(moneyChangerHelper.isValidBill(20));
		Assertions.assertTrue(moneyChangerHelper.isValidBill(50));
		Assertions.assertTrue(moneyChangerHelper.isValidBill(100));
	}
	
	AtomicInteger quartersAlloc = new AtomicInteger(10);
	AtomicInteger dimesAlloc 	= new AtomicInteger(10);
	AtomicInteger nickelsAlloc 	= new AtomicInteger(10);
	AtomicInteger penniesAlloc 	= new AtomicInteger(10);
	
	@Test
	void validAllocation() {
		
		MoneyChangerHelper moneyChangerHelper = new MoneyChangerHelper();
		
		Assertions.assertTrue(moneyChangerHelper.isAllocationEnough(1, quartersAlloc, dimesAlloc, nickelsAlloc, penniesAlloc));
		Assertions.assertTrue(moneyChangerHelper.isAllocationEnough(2, quartersAlloc, dimesAlloc, nickelsAlloc, penniesAlloc));
		Assertions.assertTrue(moneyChangerHelper.isAllocationEnough(5, quartersAlloc, dimesAlloc, nickelsAlloc, penniesAlloc));
		Assertions.assertTrue(moneyChangerHelper.isAllocationEnough(10, quartersAlloc, dimesAlloc, nickelsAlloc, penniesAlloc));
		Assertions.assertTrue(moneyChangerHelper.isAllocationEnough(20, quartersAlloc, dimesAlloc, nickelsAlloc, penniesAlloc));
		Assertions.assertTrue(moneyChangerHelper.isAllocationEnough(50, quartersAlloc, dimesAlloc, nickelsAlloc, penniesAlloc));
		Assertions.assertTrue(moneyChangerHelper.isAllocationEnough(100, quartersAlloc, dimesAlloc, nickelsAlloc, penniesAlloc));
		
	}

}
