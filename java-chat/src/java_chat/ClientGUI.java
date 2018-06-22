package java_chat;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ClientGUI extends JFrame
{
	private Client client;
	protected Socket clientSocket;

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
	private DefaultListModel<String> text = new DefaultListModel<String>();
	private DefaultListModel<String> user = new DefaultListModel<String>();
	private ArrayList<String> userList = new ArrayList<String>();
	private JList<String> listUser;
	private JTabbedPane tabsHistory;
	private DefaultListModel<String> history = new DefaultListModel<String>();
	private JList<String> listRoom;
	private JButton btnJoinRoom;
	private String oldNick;
	private JButton btnRooms;
	private JPopupMenu popupMenu;
	private JMenuItem mntmNewRoom;
	private JMenuItem mntmJoinRoom;

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
		if (textFieldNickname.getText().equals("") || textFieldNickname.getText().equals(" "))
		{
			JOptionPane.showMessageDialog(null, "Please insert Nickname first");
		}
		else
		{
			try
			{
				client = new Client(this);

				client.connectServer(hostName);
				btnSend.setEnabled(true);
				btnConnect.setEnabled(false);
				btnDisconnect.setEnabled(true);

				client.createRoom("public");

				showHistory();

			}
			catch (UnknownHostException e)
			{
				JOptionPane.showMessageDialog(null, "Der Host " + hostName + " ist unbekannt");
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "Bekomme keine I/O für die Verbindung zu " + hostName);
			}
			setNickname("add");
		}
	}

	protected void closeClient()
	{
		btnSend.setEnabled(false);
		btnDisconnect.setEnabled(false);
		client.closeClient();
	}

	protected void handoverMessage() // Message to client
	{
		String message = textFieldMessage.getText();
		int index = tabsHistory.getSelectedIndex();
		String chn = tabsHistory.getTitleAt(index);

		try
		{
			client.sendMessage(message, chn);
			textFieldMessage.setText("");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	protected void setNickname(String command)
	{
		String nick = getTextFieldNickname().getText();

		client.sendCommand(command, nick);
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
		p.setName(r.getName());

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

		showHistory();
	}

	protected void updateHistory(String message) // Methode aufrufen, wenn Pane
													// geöffnet ist
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

	protected void showHistory() // Methode aufrufen, wenn Pane geändert wird
	{
		JPanel panel = (JPanel) tabsHistory.getSelectedComponent();

		history.clear();

		if (client.getRoomList() == null)
		{
		}
		else
		{
			for (Room r : client.getRoomList())
			{
				if (r.getName().equals(panel.getName()))
				{
					ArrayList<String> al = r.getHistory();
					for (String message : al)
					{
						history.addElement(message);
					}
				}
			}
		}

	}

	protected void createNewRoom()
	{
		String input = null;
		input = JOptionPane.showInputDialog("New Room");
		
		if(input != null)
		{
			if(input.equals("") == false)
			{
				client.sendCommand("cnr", input);
			}	
			else
			{
				JOptionPane.showMessageDialog(null, "Bitte einen Namen eingeben");
			}
		}
	}

	/**
	 * Create the frame.
	 */
	public ClientGUI()
	{
		initialize();
		btnSend.setEnabled(false);
		serverListeAbrufen();
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
		{ 74, 411, 0, 124, 0 };
		gbl_contentPane.rowHeights = new int[]
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[]
		{ 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[]
		{ 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
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
		addPopup(getBtnRooms(), getPopupMenu());
		GridBagConstraints gbc_btnRooms = new GridBagConstraints();
		gbc_btnRooms.insets = new Insets(0, 0, 5, 5);
		gbc_btnRooms.gridx = 2;
		gbc_btnRooms.gridy = 0;
		contentPane.add(getBtnRooms(), gbc_btnRooms);
		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.fill = GridBagConstraints.BOTH;
		gbc_btnConnect.insets = new Insets(0, 0, 5, 0);
		gbc_btnConnect.gridx = 3;
		gbc_btnConnect.gridy = 0;
		contentPane.add(getBtnConnect(), gbc_btnConnect);
		GridBagConstraints gbc_lblNickname = new GridBagConstraints();
		gbc_lblNickname.insets = new Insets(0, 0, 5, 5);
		gbc_lblNickname.gridx = 0;
		gbc_lblNickname.gridy = 1;
		contentPane.add(getLblNickname(), gbc_lblNickname);
		GridBagConstraints gbc_textFieldNickname = new GridBagConstraints();
		gbc_textFieldNickname.gridwidth = 2;
		gbc_textFieldNickname.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldNickname.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldNickname.gridx = 1;
		gbc_textFieldNickname.gridy = 1;
		contentPane.add(getTextFieldNickname(), gbc_textFieldNickname);
		GridBagConstraints gbc_btnDisconnect = new GridBagConstraints();
		gbc_btnDisconnect.fill = GridBagConstraints.BOTH;
		gbc_btnDisconnect.insets = new Insets(0, 0, 5, 0);
		gbc_btnDisconnect.gridx = 3;
		gbc_btnDisconnect.gridy = 1;
		contentPane.add(getBtnDisconnect(), gbc_btnDisconnect);
		GridBagConstraints gbc_tabsHistory = new GridBagConstraints();
		gbc_tabsHistory.gridwidth = 3;
		gbc_tabsHistory.gridheight = 8;
		gbc_tabsHistory.insets = new Insets(0, 0, 5, 5);
		gbc_tabsHistory.fill = GridBagConstraints.BOTH;
		gbc_tabsHistory.gridx = 0;
		gbc_tabsHistory.gridy = 2;
		contentPane.add(getTabsHistory(), gbc_tabsHistory);
		GridBagConstraints gbc_listRoom = new GridBagConstraints();
		gbc_listRoom.gridheight = 3;
		gbc_listRoom.insets = new Insets(0, 0, 5, 0);
		gbc_listRoom.fill = GridBagConstraints.BOTH;
		gbc_listRoom.gridx = 3;
		gbc_listRoom.gridy = 2;
		contentPane.add(getListRoom(), gbc_listRoom);
		GridBagConstraints gbc_btnJoinRoom = new GridBagConstraints();
		gbc_btnJoinRoom.anchor = GridBagConstraints.NORTH;
		gbc_btnJoinRoom.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnJoinRoom.insets = new Insets(0, 0, 5, 0);
		gbc_btnJoinRoom.gridx = 3;
		gbc_btnJoinRoom.gridy = 5;
		contentPane.add(getBtnJoinRoom(), gbc_btnJoinRoom);
		GridBagConstraints gbc_listUser = new GridBagConstraints();
		gbc_listUser.gridheight = 4;
		gbc_listUser.insets = new Insets(0, 0, 5, 0);
		gbc_listUser.fill = GridBagConstraints.BOTH;
		gbc_listUser.gridx = 3;
		gbc_listUser.gridy = 6;
		contentPane.add(getListUser(), gbc_listUser);
		GridBagConstraints gbc_textFieldMessage = new GridBagConstraints();
		gbc_textFieldMessage.gridwidth = 3;
		gbc_textFieldMessage.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldMessage.gridx = 0;
		gbc_textFieldMessage.gridy = 10;
		contentPane.add(getTextFieldMessage(), gbc_textFieldMessage);
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSend.gridx = 3;
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
			tabsHistory.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent arg0)
				{
					showHistory();

				}
			});
		}
		return tabsHistory;
	}

	private JList<String> getListRoom()
	{
		if (listRoom == null)
		{
			listRoom = new JList<String>();
		}
		return listRoom;
	}

	private JButton getBtnJoinRoom()
	{
		if (btnJoinRoom == null)
		{
			btnJoinRoom = new JButton("Join Room");
		}
		return btnJoinRoom;
	}

	private JButton getBtnRooms()
	{
		if (btnRooms == null)
		{
			btnRooms = new JButton("Rooms");
		}
		return btnRooms;
	}

	private JPopupMenu getPopupMenu()
	{
		if (popupMenu == null)
		{
			popupMenu = new JPopupMenu();
			popupMenu.add(getMntmNewRoom());
			popupMenu.add(getMntmJoinRoom());
		}
		return popupMenu;
	}

	private static void addPopup(Component component, final JPopupMenu popup)
	{
		component.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON1)
				{
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e)
			{
				popup.show(e.getComponent(), 0, component.getHeight());
			}
		});
	}

	private JMenuItem getMntmNewRoom()
	{
		if (mntmNewRoom == null)
		{
			mntmNewRoom = new JMenuItem("New Room");
			mntmNewRoom.addActionListener(e -> createNewRoom());
		}
		return mntmNewRoom;
	}

	public String getOldNick()
	{
		return oldNick;
	}

	public void setOldNick(String oldNick)
	{
		this.oldNick = oldNick;
	}
	private JMenuItem getMntmJoinRoom() {
		if (mntmJoinRoom == null) {
			mntmJoinRoom = new JMenuItem("Join Room");
		}
		return mntmJoinRoom;
	}
}
