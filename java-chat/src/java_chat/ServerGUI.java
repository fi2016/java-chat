package java_chat;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Font;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.Color;
import javax.swing.JList;
import java.io.IOException;

public class ServerGUI extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbl_Message;
	private JButton button_Start;
	private JTextField txtPort;
	private JLabel lblPortname;
	private JButton btnStop;
	private Server server;
	private JLabel lblMembers;
	private InetAddress host;
	private JList<String> listLog = new JList<String>();
	private JList<Client> listMember = new JList<Client>();
	private Thread listenThread;
	private JButton btnAdmintool;

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
		try
		{
			host = InetAddress.getLocalHost();
		}
		catch (UnknownHostException e)
		{
			lbl_Message.setText("Localhost was not found");
		}
	}
	
	private void openAdmintool()
	{
		try
		{
			server.openAdmintool();
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initialize()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 325);
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
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
		contentPane.add(getBtnAdmintool());
	}

	private void startServer()
	{
		server = new Server(this, Integer.valueOf(txtPort.getText()), "localhost");
		listenThread = new Thread(server);
		listenThread.start();
		button_Start.setEnabled(false);
		btnStop.setEnabled(true);
		lbl_Message.setText("Server running on " + host + "/" + txtPort.getText());
	}

	private void closeServer()
	{
		listenThread.interrupt();
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
			btnStop.setEnabled(false);
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

	private JList<String> getListLog()
	{
		if (listLog == null)
		{
			listLog = new JList<String>();
			listLog.setForeground(Color.WHITE);
			listLog.setBounds(22, 96, 355, 135);
		}
		return listLog;
	}

	private JList<Client> getListMember()
	{
		if (listMember == null)
		{
			listMember = new JList<Client>();
			listMember.setForeground(Color.WHITE);
			listMember.setBounds(403, 59, 110, 172);
		}
		return listMember;
	}
	private JButton getBtnAdmintool() {
		if (btnAdmintool == null) {
			btnAdmintool = new JButton("Admintool");
			btnAdmintool.addActionListener(e -> openAdmintool());
			btnAdmintool.setBounds(242, 39, 135, 30);
		}
		return btnAdmintool;
	}
}
