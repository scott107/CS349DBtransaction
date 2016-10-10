// This code gives the layout for the UI and
//   demonstrates two ways of updating the data
//   in a JTable.
// Another option to consider when using JTable is
//   creating your own data model by overriding
//   AbstractTableModel. You might use this option
//   if data for table was coming from say a DB.
//   One example: http://www.java2s.com/Code/Java/Swing-JFC/CreatingsimpleJTableusingAbstractTableModel.htm

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.ResultSetMetaData;

public class AccountTransactionLayout extends JFrame {
	private Manager manage;
	private JTable table;
	private DefaultTableModel dtm;
	
	
	private String[] columnNames = {"Account ID",
            "Account Name",
            "Balance"};
	private String[][] data;
	
	public AccountTransactionLayout() {
		try {
			manage = new Manager();
			refreshTable(manage.refreshTable());
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, e1.toString(), "Database connection failed.", JOptionPane.ERROR_MESSAGE);
		}
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());

		dtm = new DefaultTableModel(data,columnNames);
		table = new JTable(dtm);
		// The default size of a JTable is something like
		// 450 X 400.
		Dimension smallerSize = new Dimension(450, 50);
		table.setPreferredScrollableViewportSize(smallerSize );
		
		JScrollPane scrollPaneForTable = new JScrollPane(table);
				
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.fill = GridBagConstraints.BOTH;

		contentPane.add(scrollPaneForTable,constraints);
		
		constraints.gridx = 0;
//		constraints.gridy = 1;
		constraints.weighty = 0;
		constraints.gridy = GridBagConstraints.RELATIVE;
		constraints.insets = new Insets(2, 4, 2, 4);
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		JLabel toLabel = new JLabel("From:");
		contentPane.add(toLabel,constraints);
		
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		JTextField fromField = new JTextField("",8);
		// Workaround, because of: http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4247013
		fromField.setMinimumSize(fromField.getPreferredSize());
		contentPane.add(fromField,constraints);
		
		constraints.gridx = 0;
//		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		JLabel fromLabel = new JLabel("To:");
		contentPane.add(fromLabel,constraints);
		
		constraints.gridx = 1;
//		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		JTextField toField = new JTextField("",8);
		toField.setMinimumSize(toField.getPreferredSize());
		contentPane.add(toField,constraints);

		constraints.gridx = 0;
//		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		JLabel amountLabel = new JLabel("Amount:");
		contentPane.add(amountLabel,constraints);
		
		constraints.gridx = 1;
//		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		JTextField amountField = new JTextField("",8);
		amountField.setMinimumSize(amountField.getPreferredSize());
		contentPane.add(amountField,constraints);

		constraints.gridx = 0;
//		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		JButton clearButton = new JButton("Clear");
		contentPane.add(clearButton,constraints);

		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fromField.setText("");
				toField.setText("");
				amountField.setText("");
			}
		});
		
		constraints.gridx = 1;
//		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		JButton transferButton = new JButton("Transfer");
		transferButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try{
				manage.transfer(Integer.parseInt(fromField.getText()), Integer.parseInt(toField.getText()), Double.parseDouble(amountField.getText()));
				//table.setModel(new DefaultTableModel(newData,columnNames));
				refreshTable(manage.refreshTable());
				reallyrefreshthetable();
				}
				// catch data-type errors
				catch(NumberFormatException err){
					JOptionPane.showMessageDialog(null, err.toString(), "Input type error.", JOptionPane.ERROR_MESSAGE);
				}		
			}
			
		});
		contentPane.add(transferButton,constraints);
	}
	
	public void refreshTable(ArrayList tabledata){

		data = new String[3][3];
		int j = 0;
		for (int i = 0; i < tabledata.size(); i++) {
			data[j][i%3] = (String) tabledata.get(i);
			if (i % 3 == 2)
				j++;
		}
	}
	
	public void reallyrefreshthetable(){
		dtm.setDataVector(data, columnNames);
	}

	public static void main(String[] args) {

		JFrame frame = new AccountTransactionLayout();
		frame.pack();
		frame.setVisible(true);
	    
	}
}