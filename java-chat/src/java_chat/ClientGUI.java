package java_chat;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Label;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;

public class ClientGUI extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6803153492488659745L;
	private JPanel contentPane;
	private JLabel lblServer;
	private JLabel lblNickname;
	protected JTextField textFieldNickname;
	private JButton btnConnect;
	private JButton btnDisconnect;
	protected JTextField textFieldMessage;
	private JButton btnSend;
	protected JComboBox<String> comboBoxServerIDs;
	protected Socket clientSocket;
	private Client client;
	private DefaultListModel<String> text = new DefaultListModel<String>();
	private DefaultListModel<String> user = new DefaultListModel<String>();
	private ArrayList<String> userList = new ArrayList<String>();
	private JList<String> listUser;
	private JTabbedPane tabsHistory;
	private DefaultListModel<String> history = new DefaultListModel<String>();

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
					ClientGUI frame = new ClientGUI();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	protected void connectClient()
	{
		String hostName = comboBoxServerIDs.getSelectedItem().toString();
		if(textFieldNickname.getText().equals("") || textFieldNickname.getText().equals(" "))
		{
			JOptionPane.showMessageDialog(null, "Please insert Nickname first");
		}
		else
		{
			try
			{
				client = new Client();
				client.connectServer(hostName);
				btnSend.setEnabled(false);
				btnDisconnect.setEnabled(true);
			}
			catch (UnknownHostException e)
			{
				JOptionPane.showMessageDialog(null, "Der Host " + hostName + " ist unbekannt");
				System.exit(1);
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "Bekomme keine I/O für die Verbindung zu " + hostName);
				System.exit(1);
			}
			setNickname("add");
		}
	}

	protected void closeClient()
	{
		btnSend.setEnabled(true);
		btnDisconnect.setEnabled(false);
		client.closeClient();
		
	}

	protected void handoverMessage() // Message to client
	{
		String message = textFieldMessage.getText();
		String chn = tabsHistory.getSelectedComponent().getName();

		try
		{
			client.sendMessage(message, chn);
			textFieldMessage.setText(""); // foo
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}// pushtry

	protected void setNickname(String command)
	{
		String nick = getTextFieldNickname().getText();
		
		String message = "CMD" + command + "\u1001ePAM" + nick;

		client.sendCommand(message);
	}

	protected void showNotification(String message)
	{
		JOptionPane.showMessageDialog(null, message);
	}

	protected void serverListeAbrufen()
	{
		String daniel = "172.16.102.2";
		comboBoxServerIDs.addItem(daniel);

		String stefan = "172.16.102.3";
		comboBoxServerIDs.addItem(stefan);

		String thomas = "172.16.102.4";
		comboBoxServerIDs.addItem(thomas);

		String carina = "172.16.102.5";
		comboBoxServerIDs.addItem(carina);

		String herrGeis = "172.16.102.6";
		comboBoxServerIDs.addItem(herrGeis);

		String lukas = "172.16.102.9";
		comboBoxServerIDs.addItem(lukas);

		String frank = "172.16.102.8";
		comboBoxServerIDs.addItem(frank);

		String marc = "172.16.102.7";
		comboBoxServerIDs.addItem(marc);

		String sebastian = "172.16.102.14";
		comboBoxServerIDs.addItem(sebastian);

		String lars = "172.16.102.15";
		comboBoxServerIDs.addItem(lars);

		String herrWolf = "172.16.102.1";
		comboBoxServerIDs.addItem(herrWolf);

	}

	protected void createTab(Room r)
	{
		JPanel p = new JPanel();
		JList<String> listHistory = new JList<String>();
		tabsHistory.addTab(r.getName(), p);

		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]
		{ 557, 0, 0 };
		gbl_panel.rowHeights = new int[]
		{ 287, 0 };
		gbl_panel.columnWeights = new double[]
		{ 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[]
		{ 0.0, Double.MIN_VALUE };
		p.setLayout(gbl_panel);

		GridBagConstraints gbc_listHistory = new GridBagConstraints();
		gbc_listHistory.insets = new Insets(0, 0, 0, 5);
		gbc_listHistory.fill = GridBagConstraints.BOTH;
		gbc_listHistory.gridx = 0;
		gbc_listHistory.gridy = 0;

		listHistory.setModel(history);
		p.add(listHistory, gbc_listHistory);

		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		p.add(new Label(r.getName()), gbc_lblNewLabel);
	}

	protected void updateHistory(String message) // aufrufen, wenn Pane offen
	{
		JPanel panel = (JPanel) tabsHistory.getSelectedComponent();

		for (Room r : client.getRoomList())
		{
			if (r.getName().equals(panel.getName()))
			{
				history.addElement(message);
			}
		}
	}

	protected void showHistory() // AUfrufen, wenn Pane geändert wird
	{
		JPanel panel = (JPanel) tabsHistory.getSelectedComponent();

		for (Room r : client.getRoomList())
		{
			if (r.getName().equals(panel.getName()))
			{
				for (String message : r.getHistory())
				{
					history.addElement(message);
				}
			}
		}
	}

	/**
	 * Create the frame.
	 */
	public ClientGUI()
	{
		initialize();
		serverListeAbrufen();

		// Test von Räumen
		createTab(new Room("public"));
		// createTab("test");
	}

	private void initialize()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 601, 372);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]
		{ 74, 411, 124, 0 };
		gbl_contentPane.rowHeights = new int[]
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[]
		{ 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[]
		{ 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);
		GridBagConstraints gbc_lblServer = new GridBagConstraints();
		gbc_lblServer.insets = new Insets(0, 0, 5, 5);
		gbc_lblServer.gridx = 0;
		gbc_lblServer.gridy = 0;
		contentPane.add(getLblServer(), gbc_lblServer);
		GridBagConstraints gbc_comboBoxServerIDs = new GridBagConstraints();
		gbc_comboBoxServerIDs.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxServerIDs.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxServerIDs.gridx = 1;
		gbc_comboBoxServerIDs.gridy = 0;
		contentPane.add(getComboBoxServerIDs(), gbc_comboBoxServerIDs);
		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.insets = new Insets(0, 0, 5, 0);
		gbc_btnConnect.gridx = 2;
		gbc_btnConnect.gridy = 0;
		contentPane.add(getBtnConnect(), gbc_btnConnect);
		GridBagConstraints gbc_lblNickname = new GridBagConstraints();
		gbc_lblNickname.insets = new Insets(0, 0, 5, 5);
		gbc_lblNickname.gridx = 0;
		gbc_lblNickname.gridy = 1;
		contentPane.add(getLblNickname(), gbc_lblNickname);
		GridBagConstraints gbc_textFieldNickname = new GridBagConstraints();
		gbc_textFieldNickname.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldNickname.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldNickname.gridx = 1;
		gbc_textFieldNickname.gridy = 1;
		contentPane.add(getTextFieldNickname(), gbc_textFieldNickname);
		GridBagConstraints gbc_btnDisconnect = new GridBagConstraints();
		gbc_btnDisconnect.insets = new Insets(0, 0, 5, 0);
		gbc_btnDisconnect.gridx = 2;
		gbc_btnDisconnect.gridy = 1;
		contentPane.add(getBtnDisconnect(), gbc_btnDisconnect);
		GridBagConstraints gbc_tabsHistory = new GridBagConstraints();
		gbc_tabsHistory.gridwidth = 2;
		gbc_tabsHistory.gridheight = 8;
		gbc_tabsHistory.insets = new Insets(0, 0, 5, 5);
		gbc_tabsHistory.fill = GridBagConstraints.BOTH;
		gbc_tabsHistory.gridx = 0;
		gbc_tabsHistory.gridy = 2;
		contentPane.add(getTabsHistory(), gbc_tabsHistory);
		GridBagConstraints gbc_listUser = new GridBagConstraints();
		gbc_listUser.gridheight = 8;
		gbc_listUser.insets = new Insets(0, 0, 5, 0);
		gbc_listUser.fill = GridBagConstraints.BOTH;
		gbc_listUser.gridx = 2;
		gbc_listUser.gridy = 2;
		contentPane.add(getListUser(), gbc_listUser);
		GridBagConstraints gbc_textFieldMessage = new GridBagConstraints();
		gbc_textFieldMessage.gridwidth = 2;
		gbc_textFieldMessage.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldMessage.gridx = 0;
		gbc_textFieldMessage.gridy = 10;
		contentPane.add(getTextFieldMessage(), gbc_textFieldMessage);
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 10;
		contentPane.add(getBtnSend(), gbc_btnSend);
	}

	private JLabel getLblServer()
	{
		if (lblServer == null)
		{
			lblServer = new JLabel("Server:");
		}
		return lblServer;
	}

	private JLabel getLblNickname()
	{
		if (lblNickname == null)
		{
			lblNickname = new JLabel("Nickname:");
		}
		return lblNickname;
	}

	private JTextField getTextFieldNickname()
	{
		if (textFieldNickname == null)
		{
			textFieldNickname = new JTextField();
			textFieldNickname.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					setNickname("alt");
				}
			});
			textFieldNickname.setColumns(10);
		}
		return textFieldNickname;
	}

	private JButton getBtnConnect()
	{
		if (btnConnect == null)
		{
			btnConnect = new JButton("Connect");
			btnConnect.addActionListener(e -> connectClient());
		}
		return btnConnect;
	}

	private JButton getBtnDisconnect()
	{
		if (btnDisconnect == null)
		{
			btnDisconnect = new JButton("Disconnect");
			btnDisconnect.setEnabled(false);
			btnDisconnect.addActionListener(e -> closeClient());
		}
		return btnDisconnect;
	}

	private JTextField getTextFieldMessage()
	{
		if (textFieldMessage == null)
		{
			textFieldMessage = new JTextField();
			textFieldMessage.addActionListener(e -> handoverMessage());
			textFieldMessage.setColumns(10);
		}
		return textFieldMessage;
	}

	private JButton getBtnSend()
	{
		if (btnSend == null)
		{
			btnSend = new JButton("Send");
			btnSend.addActionListener(e -> handoverMessage());
		}
		return btnSend;
	}

	private JComboBox<String> getComboBoxServerIDs()
	{
		if (comboBoxServerIDs == null)
		{
			comboBoxServerIDs = new JComboBox<String>();
		}
		return comboBoxServerIDs;
	}

	private JList<String> getListUser()
	{
		if (listUser == null)
		{
			listUser = new JList<String>(user);
		}
		return listUser;
	}

	private JTabbedPane getTabsHistory()
	{
		if (tabsHistory == null)
		{
			tabsHistory = new JTabbedPane(JTabbedPane.TOP);
		}
		return tabsHistory;
	}
}
