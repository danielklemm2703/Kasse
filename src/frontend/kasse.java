package frontend;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;


public class kasse {

	private JFrame _frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					kasse window = new kasse();
					window._frame.setVisible(true);

					// // open db
					// Connection c = null;
					// try {
					// Class.forName("org.sqlite.JDBC");
					// c = DriverManager
					// .getConnection("jdbc:sqlite:sqlite/kasse.db");
					// } catch (Exception e) {
					// System.err.println(e.getClass().getName() + ": "
					// + e.getMessage());
					// System.exit(0);
					// }
					// System.out.println("Opened database successfully");

					// create table
					Connection c = null;
					 Statement stmt = null;
					 try {
					 Class.forName("org.sqlite.JDBC");
					 c = DriverManager
					 .getConnection("jdbc:sqlite:sqlite/kasse.db");
					 System.out.println("Opened database successfully");
					
					 stmt = c.createStatement();
					 String sql = "CREATE TABLE COMPANY "
					 + "(ID INT PRIMARY KEY     NOT NULL,"
					 + " NAME           TEXT    NOT NULL, "
					 + " AGE            INT     NOT NULL, "
					 + " ADDRESS        CHAR(50), "
					 + " SALARY         REAL)";
					 stmt.executeUpdate(sql);
					 stmt.close();
					 c.close();
					 } catch (Exception e) {
					 System.err.println(e.getClass().getName() + ": "
					 + e.getMessage());
						// System.exit(0);
					 }
					 System.out.println("Table created successfully");

					// inserts
					c = null;
					stmt = null;
					try {
						Class.forName("org.sqlite.JDBC");
						c = DriverManager
								.getConnection("jdbc:sqlite:sqlite/kasse.db");
						c.setAutoCommit(false);
						System.out.println("Opened database successfully");

						stmt = c.createStatement();
						String sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
								+ "VALUES (1, 'Paul', 32, 'California', 20000.00 );";
						stmt.executeUpdate(sql);

						sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
								+ "VALUES (2, 'Allen', 25, 'Texas', 15000.00 );";
						stmt.executeUpdate(sql);

						sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
								+ "VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );";
						stmt.executeUpdate(sql);

						sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
								+ "VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );";
						stmt.executeUpdate(sql);

						stmt.close();
						c.commit();
						c.close();
					} catch (Exception e) {
						System.err.println(e.getClass().getName() + ": "
								+ e.getMessage());
						// System.exit(0);
					}
					System.out.println("Records created successfully");

					// select
					c = null;
					stmt = null;
					try {
						Class.forName("org.sqlite.JDBC");
						c = DriverManager
								.getConnection("jdbc:sqlite:sqlite/kasse.db");
						c.setAutoCommit(false);
						System.out.println("Opened database successfully");

						stmt = c.createStatement();
						ResultSet rs = stmt
								.executeQuery("SELECT * FROM COMPANY;");
						while (rs.next()) {
							int id = rs.getInt("id");
							String name = rs.getString("name");
							int age = rs.getInt("age");
							String address = rs.getString("address");
							float salary = rs.getFloat("salary");
							System.out.println("ID = " + id);
							System.out.println("NAME = " + name);
							System.out.println("AGE = " + age);
							System.out.println("ADDRESS = " + address);
							System.out.println("SALARY = " + salary);
							System.out.println();
						}
						rs.close();
						stmt.close();
						c.close();
					} catch (Exception e) {
						System.err.println(e.getClass().getName() + ": "
								+ e.getMessage());
						System.exit(0);
					}
					System.out.println("Operation done successfully");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public kasse() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		_frame = new JFrame();
		_frame.setBounds(100, 100, 450, 300);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
