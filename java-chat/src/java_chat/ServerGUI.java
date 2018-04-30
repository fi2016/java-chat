package java_chat;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JList;

public class ServerGUI extends JFrame
{

	private JPanel contentPane;
	private JLabel lbl_Message;
	private JButton button_Start;
	private JTextField txtPort;
	private JLabel lblPortname;
	private JButton btnStop;
	private Server server;
	private JLabel lblMembers;
	private JList list_log;
	private JList list_Members;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					ServerGUI frame = new ServerGUI();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerGUI()
	{
		setBackground(Color.DARK_GRAY);
		setTitle("Server");
		initialize();
		server = new Server();
	}
	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 325);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(112, 128, 144));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLbl_Message());
		contentPane.add(getButton_Start());
		contentPane.add(getTxtPort());
		contentPane.add(getLblPortname());
		contentPane.add(getBtnStop());
		contentPane.add(getLblMembers());
		contentPane.add(getList());
		contentPane.add(getList_1());
	}
	
	private void startServer()
	{
		InetAddress host = null;
		try
		{
			host.getLocalHost();
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server.startServer(this,Integer.valueOf(txtPort.getText()),"localhost");
		button_Start.setEnabled(false);
		btnStop.setEnabled(true);
		lbl_Message.setText("Server running on " + host + "/" +txtPort.getText());
	}
	
	private void closeServer() throws UnknownHostException
	{
		server.closeServer();
		button_Start.setEnabled(true);
		btnStop.setEnabled(false);
		lbl_Message.setText("Server on " + InetAddress.getLocalHost() + "/" +txtPort.getText() + " closed.");
	}
	
	private JLabel getLbl_Message() {
		if (lbl_Message == null) {
			lbl_Message = new JLabel("0.0.0.0");
			lbl_Message.setBounds(25, 226, 287, 50);
		}
		return lbl_Message;
	}
	private JButton getButton_Start() {
		if (button_Start == null) {
			button_Start = new JButton("Start");
			button_Start.addActionListener(e -> startServer());
			button_Start.setBounds(90, 39, 66, 30);
		}
		return button_Start;
	}

	private JTextField getTxtPort() {
		if (txtPort == null) {
			txtPort = new JTextField();
			txtPort.setText("8008");
			txtPort.setColumns(10);
			txtPort.setBounds(25, 44, 40, 20);
		}
		return txtPort;
	}
	private JLabel getLblPortname() {
		if (lblPortname == null) {
			lblPortname = new JLabel("Portnr.");
			lblPortname.setFont(new Font("Tahoma", Font.BOLD, 18));
			lblPortname.setBounds(25, 11, 78, 22);
		}
		return lblPortname;
	}
	private JButton getBtnStop() {
		if (btnStop == null) {
			btnStop = new JButton("Stop");
			btnStop.setBackground(new Color(192, 192, 192));
			btnStop.addActionListener(e->closeServer());
			btnStop.setBounds(166, 39, 66, 30);
		}
		return btnStop;
	}
	private JLabel getLblMembers() {
		if (lblMembers == null) {
			lblMembers = new JLabel("Members");
			lblMembers.setFont(new Font("Tahoma", Font.BOLD, 18));
			lblMembers.setBounds(414, 18, 110, 30);
		}
		return lblMembers;
	}
	private JList getList() {
		if (list == null) {
			list = new JList();
			list.setBounds(25, 106, 287, 120);
		}
		return list;
	}
	private JList getList_1() {
		if (list_1 == null) {
			list_1 = new JList();
			list_1.setBounds(402, 59, 122, 171);
		}
		return list_1;
	}
}
