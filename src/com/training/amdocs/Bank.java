package com.training.amdocs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Bank {
	Connection con;

	public void estconnection() throws SQLException {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@//localhost:1521/orcl.iiht.tech";
			String userName = "scott";
			String password = "tiger";
			con = DriverManager.getConnection(url, userName, password);

		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
			System.err.println("driver class not found");

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Connection failed");
		}
	}

	public void openingAnAccount(String fullName, String dob, String address, String mobileNo, String emailId,
			String panNo, long aadharNo) throws SQLException {

		estconnection();
		float initial_deposit = 0;
		if (con != null) {
			try {

				PreparedStatement pst;
				pst = con.prepareStatement("insert into Banking values(banking_auto_incr.nextval,?,?,?,?,?,?,?,?)");
				pst.setString(1, fullName);
				pst.setString(2, dob);
				pst.setString(3, address);
				pst.setString(4, mobileNo);
				pst.setString(5, emailId);
				pst.setString(6, panNo);
				pst.setLong(7, aadharNo);
				pst.setFloat(8, initial_deposit);
				int i = pst.executeUpdate();
				if (i == 1) {
					PreparedStatement pst1;
					pst1 = con.prepareStatement("select * from Banking where panno=?");
					pst1.setString(1, panNo);
					ResultSet res;
					res = pst1.executeQuery();
					if (res.next()) {
						System.out.println(
								"Your Account Created Successfully and Your Accont Number is: " + res.getLong(1));
					}
				} else
					System.out.println("Something went wrong while creating an account");

			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				con.close();
			}
		}

	}

	public void checkBalance(long accountNumber) throws SQLException {
		estconnection();
		if (con != null) {
			try {
				PreparedStatement pst;
				pst = con.prepareStatement("select * from Banking where accountno=?");
				pst.setLong(1, accountNumber);
				ResultSet res;
				res = pst.executeQuery();
				if (res.next()) {
					System.out.println("Your Accont Balance is: " + res.getFloat(9));
				}

				else
					System.out.println("Invalid Account number");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				con.close();
			}
		}
	}

	public void deposit(long accountNumber, float amount) throws SQLException {
		estconnection();
		if (con != null) {
			try {
				PreparedStatement pst;
				pst = con.prepareStatement("select * from Banking where accountno=?");
				pst.setLong(1, accountNumber);
				ResultSet res;
				res = pst.executeQuery();
				if (res.next()) {
					float balamount = res.getFloat(9);
					System.out.println("Your balance before depositing: " + balamount);
					Float newbal = balamount + amount;
					PreparedStatement pst1 = con.prepareStatement("update Banking set balance=? where accountno=? ");
					pst1.setFloat(1, newbal);
					pst1.setLong(2, accountNumber);
					int i = pst1.executeUpdate();
					if (i == 1)
						System.out.println("Amount Deposited successfully");
					else
						System.out.println("Deposit Unsuccessful");

					PreparedStatement pst2 = con.prepareStatement("select * from Banking where accountno=?");
					pst2.setLong(1, accountNumber);
					ResultSet res1 = pst2.executeQuery();
					if (res1.next()) {
						System.out.println("Your balance after depositing: " + res1.getFloat(9));
					}
				}

				else
					System.out.println("Invalid Account number");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				con.close();
			}
		}
	}

	public void withdraw(long accountNumber, float amount) throws SQLException {
		estconnection();
		if (con != null) {
			try {
				PreparedStatement pst;
				pst = con.prepareStatement("select * from Banking where accountno=?");
				pst.setLong(1, accountNumber);
				ResultSet res;
				res = pst.executeQuery();
				if (res.next()) {
					float balamount = res.getFloat(9);
					System.out.println("Your balance before withdrawal: " + balamount);
					if (balamount < amount) {
						System.out.println("Sorry! you have insufficient balance");
					} else {
						Float newbal = balamount - amount;
						PreparedStatement pst1 = con
								.prepareStatement("update Banking set balance=? where accountno=? ");
						pst1.setFloat(1, newbal);
						pst1.setLong(2, accountNumber);
						int i = pst1.executeUpdate();
						if (i == 1)
							System.out.println("withdrawn successfully");
						else
							System.out.println("withdraw Unsuccessful");

						PreparedStatement pst2 = con.prepareStatement("select * from Banking where accountno=?");
						pst2.setLong(1, accountNumber);
						ResultSet res1 = pst2.executeQuery();
						if (res1.next()) {
							System.out.println("Your balance after withdrawal: " + res1.getFloat(9));
						}
					}
				} else
					System.out.println("Invalid Account number");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				con.close();
			}
		}
	}

	public static void main(String[] args) throws SQLException, InterruptedException {
		Bank bk = new Bank();
		bk.estconnection();
		String fullName, dob, address, mobileNo, emailId, panNo;
		long aadharNo;
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("***************************************************************************");
			System.out.println("\n 1. Opening an Account \n " + "2. Check Balance \n " + "3. Deposit \n "
					+ "4. Withdraw \n " + "5. Exit \n");
			System.out.println("***************************************************************************");
			System.out.println("Select your option:");
			switch (sc.nextInt()) {
			case 1:
				System.out.println("\nEnter your Fullname: ");
				fullName = sc.next();
				System.out.println("\nEnter your Date Of Birth: ");
				dob = sc.next();
				Thread.sleep(100);
				System.out.println("\nEnter your Address: ");
				address = sc.next();
				System.out.println("\nEnter your Mobile Number: ");
				mobileNo = sc.next();
				System.out.println("\nEnter your EmailId: ");
				emailId = sc.next();
				System.out.println("\nEnter your Pan number: ");
				panNo = sc.next();
				System.out.println("\nEnter your Aadhar Number: ");
				aadharNo = sc.nextLong();
				bk.openingAnAccount(fullName, dob, address, mobileNo, emailId, panNo, aadharNo);
				break;

			case 2:
				System.out.println("\nPlease Enter your account number to check your balance: ");
				long accountno = sc.nextLong();
				bk.checkBalance(accountno);
				break;

			case 3:
				System.out.println("\nPlease Enter your account number to deposit: ");
				long daccno = sc.nextLong();
				System.out.println("\nEnter the amount to be deposited: ");
				float amt = sc.nextFloat();
				bk.deposit(daccno, amt);
				break;

			case 4:
				System.out.println("\nPlease Enter your account number to withdraw: ");
				long waccno = sc.nextLong();
				System.out.println("\nEnter the withdrawal amount: ");
				float wamt = sc.nextFloat();
				bk.withdraw(waccno, wamt);
				break;

			case 5:
				System.exit(0);
			default:
				System.out.println("\nInvalid Option ");

			}
		}

	}

}
