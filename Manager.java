import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Manager {
	
    private Connection con;
    private Statement stmt;
    
    public Manager() throws SQLException{
        String url = "jdbc:mysql://kc-sce-appdb01.kc.umkc.edu/slnz8b";
        String userID = "slnz8b";
        String password = "tZrFLVzffV";
   
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(java.lang.ClassNotFoundException e) {
            System.out.println(e);
            System.exit(0);
        }
       
        con = DriverManager.getConnection(url,userID,password);
        stmt = con.createStatement();
    }
    
	
	public void transfer (int fromAccnt, int toAccount, int amount){
		try {
			con.setAutoCommit(false);
			int debitor = (getBalance(fromAccnt) - amount);
			int creditor = (getBalance(toAccount) + amount);

			String debit = SQLthings.modifyBalance(fromAccnt, debitor);
			String credit = SQLthings.modifyBalance(toAccount, creditor);

			stmt.executeUpdate(debit);
			stmt.executeUpdate(credit);
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public int getBalance(int accntNum){
		int balance = 100;
		ResultSet temp;
		String tableBalance = SQLthings.getBalance(accntNum);
		try {
			temp = stmt.executeQuery(tableBalance);
			temp.next();
			balance = temp.getInt(1);
		} catch (SQLException err) {
		    JOptionPane.showMessageDialog(null, err.toString(), "Database query contained an error.", JOptionPane.ERROR_MESSAGE);
		}
		return balance;
	}

	public ArrayList refreshTable() {
		ResultSet tempRS = null;
		String refresh = SQLthings.populateTable();
		ArrayList arr = new ArrayList();
		try {
			tempRS = stmt.executeQuery(refresh);


			java.sql.ResultSetMetaData rsmd = tempRS.getMetaData();
			int numberColumns = rsmd.getColumnCount();
			boolean next = tempRS.next();

			while (next) {
				int j = 1;
				for (int i = 1; i <= numberColumns; i++) {
					String tempString = tempRS.getString(i);

					// integer columns
					if (i == 1 || i == 3) {

						arr.add((tempString));
					} else
						arr.add((tempString));
				}
				j++;
				next = tempRS.next();
			}

		} catch (SQLException err) {
			JOptionPane.showMessageDialog(null, err.toString(), "Database error.", JOptionPane.ERROR_MESSAGE);
		}
		return arr;
	}
}
