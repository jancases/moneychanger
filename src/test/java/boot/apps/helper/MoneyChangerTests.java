package boot.apps.helper;

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

}
