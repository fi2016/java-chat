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
	private JList<Client> listLog = new JList<Client>();
	private JList<Client> listMember = new JList<Cient>();

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

	private void initialize()
	{
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
		contentPane.add(getListLog());
		contentPane.add(getListMember());
	}

	private void startServer()
	{
		InetAddress host = null;
		try
		{
			host.getLocalHost();
			//
		}
		catch (UnknownHostException e)
		{
			System.out.println();
		}
		server.startServer(this, Integer.valueOf(txtPort.getText()), "localhost");
		button_Start.setEnabled(false);
		btnStop.setEnabled(true);
		lbl_Message.setText("Server running on " + host + "/" + txtPort.getText());
	}

	private void closeServer()
	{
		//Kommentiere
		InetAddress host = null;
		try
		{
			host.getLocalHost();
		}
		catch (UnknownHostException e)
		{
			System.out.println();
		}
		server.closeServer();
		button_Start.setEnabled(true);
		btnStop.setEnabled(false);
		lbl_Message.setText("Server on " + host + "/" + txtPort.getText() + " closed.");
	}

	private JLabel getLbl_Message()
	{
		if (lbl_Message == null)
		{
			lbl_Message = new JLabel("0.0.0.0");
			lbl_Message.setBounds(25, 226, 287, 50);
		}
		return lbl_Message;
	}

	private JButton getButton_Start()
	{
		if (button_Start == null)
		{
			button_Start = new JButton("Start");
			button_Start.addActionListener(e -> startServer());
			button_Start.setBounds(90, 39, 66, 30);
		}
		return button_Start;
	}

	private JTextField getTxtPort()
	{
		if (txtPort == null)
		{
			txtPort = new JTextField();
			txtPort.setText("8008");
			txtPort.setColumns(10);
			txtPort.setBounds(25, 44, 40, 20);
		}
		return txtPort;
	}

	private JLabel getLblPortname()
	{
		if (lblPortname == null)
		{
			lblPortname = new JLabel("Portnr.");
			lblPortname.setFont(new Font("Tahoma", Font.BOLD, 18));
			lblPortname.setBounds(25, 11, 78, 22);
		}
		return lblPortname;
	}

	private JButton getBtnStop()
	{
		if (btnStop == null)
		{
			btnStop = new JButton("Stop");
			btnStop.setBackground(new Color(192, 192, 192));
			btnStop.addActionListener(e -> closeServer());
			btnStop.setBounds(166, 39, 66, 30);
		}
		return btnStop;
	}

	private JLabel getLblMembers()
	{
		if (lblMembers == null)
		{
			lblMembers = new JLabel("Members");
			lblMembers.setFont(new Font("Tahoma", Font.BOLD, 18));
			lblMembers.setBounds(414, 18, 110, 30);
		}
		return lblMembers;
	}

	private JList getListLog()
	{
		if (listLog == null)
		{
			listLog = new JList<Client>();
			listLog.setBounds(22, 96, 355, 135);
		}
		return listLog;
	}

	private JList getListMember()
	{
		if (listMember == null)
		{
			listMember = new JList<Client>();
			listMember.setBounds(403, 59, 110, 172);
		}
		return listMember;
	}
}
