package java_chat;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JComboBox;
import javax.swing.JTabbedPane;

public class AdmintoolGUI extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblChatRoomsChats;
	private JList<Client> listMembers;
	private JLabel lblMember;
	private JButton btnKicken;
	private JButton btnBannen;
	private Admintool admintool;
	private JButton btnCloseChat;
	private JLabel lblServer;
	private JComboBox<String> comboBoxServerIDs;
	private JButton btnConnect;
	private JLabel lblNickname;
	private JTextField textFieldNickname;
	private JButton btnDisconnect;
	private JTextField textFieldMessage;
	private JButton btnSend;
	private DefaultListModel<Client> lmMembers = new DefaultListModel<Client>();
	private JComboBox<ChatRoom> comboBoxChatRooms;
	private JTabbedPane tabbedPane;
	private JPanel panel;

	/**
	 * Launch the application.
	**/
	public AdmintoolGUI(Server server)
	{
		initialize();
		try
		{
			admintool = new Admintool(server);
		}
		catch (UnknownHostException e)
		{
			JOptionPane.showMessageDialog(null, "Der angegebene Host ist nicht bekannt");
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "IO Exception AdmintoolGUI im Konstruktor");
		}
	}

	

	public void connectAdmintool()
	{
		try
		{
			admintool.connectServer(comboBoxServerIDs.getSelectedItem().toString());

		}
		catch (UnknownHostException e)
		{
			JOptionPane.showMessageDialog(null, "Der angegebene Host ist nicht bekannt");
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "IO Exception AdmintoolGUI beim connecten");
		}
	}

	public void sendMessage()
	{
		try
		{
			admintool.sendMessage(textFieldMessage.getText());
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "IO Exception AdmintoolGUI beim senden");
		}
	}

	private void initialize()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 912, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setTitle("Admintool");
		contentPane.setLayout(null);
		contentPane.add(getLblChatRoomsChats());
		contentPane.add(getListMembers());
		contentPane.add(getLblMember());
		contentPane.add(getBtnKicken());
		contentPane.add(getBtnBannen());
		contentPane.add(getBtnCloseChat());
		contentPane.add(getLblServer());
		contentPane.add(getComboBoxServerIDs());
		contentPane.add(getBtnConnect());
		contentPane.add(getBtnDisconnect());
		contentPane.add(getLblNickname());
		contentPane.add(getTextFieldNickname());
		contentPane.add(getBtnSend());
		contentPane.add(getTextFieldMessage());
		contentPane.add(getComboBoxChatRooms());
		contentPane.add(getTabbedPane());
	}

	private JLabel getLblChatRoomsChats()
	{
		if (lblChatRoomsChats == null)
		{
			lblChatRoomsChats = new JLabel("Chat Rooms");
			lblChatRoomsChats.setBounds(572, 193, 81, 14);
		}
		return lblChatRoomsChats;
	}

	private JList<Client> getListMembers()
	{
		if (listMembers == null)
		{
			listMembers = new JList<Client>(lmMembers);
			listMembers.setBounds(572, 44, 196, 138);
		}
		return listMembers;
	}

	private JLabel getLblMember()
	{
		if (lblMember == null)
		{
			lblMember = new JLabel("Members");
			lblMember.setBounds(572, 25, 46, 14);
		}
		return lblMember;
	}

	private JButton getBtnKicken()
	{
		if (btnKicken == null)
		{
			btnKicken = new JButton("Kicken");
			btnKicken.addActionListener(e -> admintool.kickClient(lmMembers.elementAt(listMembers.getSelectedIndex())));
			btnKicken.setBounds(778, 40, 110, 25);
		}
		return btnKicken;
	}

	private JButton getBtnBannen()
	{
		if (btnBannen == null)
		{
			btnBannen = new JButton("Bannen");
			btnBannen.addActionListener(e -> admintool.banClient(lmMembers.elementAt(listMembers.getSelectedIndex())));
			btnBannen.setBounds(780, 75, 110, 25);
		}
		return btnBannen;
	}

	private JButton getBtnCloseChat()
	{
		if (btnCloseChat == null)
		{
			btnCloseChat = new JButton("Chat Schliessen");
			btnCloseChat.addActionListener(e -> admintool.closeChatroom((ChatRoom)comboBoxChatRooms.getSelectedItem()));
			btnCloseChat.setBounds(572, 254, 120, 35);
		}
		return btnCloseChat;
	}

	private JLabel getLblServer()
	{
		if (lblServer == null)
		{
			lblServer = new JLabel("Server:");
			lblServer.setBounds(10, 25, 36, 14);
		}
		return lblServer;
	}

	private JComboBox<String> getComboBoxServerIDs()
	{
		if (comboBoxServerIDs == null)
		{
			comboBoxServerIDs = new JComboBox<String>();
			comboBoxServerIDs.setBounds(66, 22, 401, 20);
		}
		return comboBoxServerIDs;
	}

	private JButton getBtnConnect()
	{
		if (btnConnect == null)
		{
			btnConnect = new JButton("Connect");
			btnConnect.addActionListener(e -> connectAdmintool());
			btnConnect.setBounds(477, 21, 85, 23);
		}
		return btnConnect;
	}

	private JLabel getLblNickname()
	{
		if (lblNickname == null)
		{
			lblNickname = new JLabel("Nickname:");
			lblNickname.setBounds(10, 59, 49, 14);
		}
		return lblNickname;
	}

	private JTextField getTextFieldNickname()
	{
		if (textFieldNickname == null)
		{
			textFieldNickname = new JTextField();
			textFieldNickname.setBounds(66, 56, 401, 20);
			textFieldNickname.setColumns(10);
		}
		return textFieldNickname;
	}

	private JButton getBtnDisconnect()
	{
		if (btnDisconnect == null)
		{
			btnDisconnect = new JButton("Disconnect");
			btnDisconnect.setBounds(477, 55, 85, 23);
		}
		return btnDisconnect;
	}


	private JTextField getTextFieldMessage()
	{
		if (textFieldMessage == null)
		{
			textFieldMessage = new JTextField();
			textFieldMessage.setBounds(6, 338, 461, 20);
			textFieldMessage.setColumns(10);
		}
		return textFieldMessage;
	}

	private JButton getBtnSend()
	{
		if (btnSend == null)
		{
			btnSend = new JButton("Send");
			btnSend.addActionListener(e -> sendMessage());
			btnSend.setBounds(477, 338, 85, 23);
		}
		return btnSend;
	}
	private JComboBox<ChatRoom> getComboBoxChatRooms() {
		if (comboBoxChatRooms == null) {
			comboBoxChatRooms = new JComboBox<ChatRoom>();
			comboBoxChatRooms.setBounds(572, 218, 264, 25);
		}
		return comboBoxChatRooms;
	}
	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setBounds(10, 0, 886, 20);
			tabbedPane.addTab("New tab", null, getPanel(), null);
		}
		return tabbedPane;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
		}
		return panel;
	}
}
