
public class SQLthings {
	public static String modifyBalance(int fromAccount, double amount){
		return ("UPDATE ACCOUNT SET account_balance = " + amount + " WHERE account_id = " + fromAccount + ";");
		
	}
	public static String getBalance(int accntNum){
		return ("SELECT account_balance FROM account WHERE account_id = " + accntNum + ";");
	}
	public static String populateTable(){
		return ("SELECT * FROM account;");
	}
}
