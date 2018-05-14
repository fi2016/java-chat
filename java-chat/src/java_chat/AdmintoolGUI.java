package java_chat;

import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AdmintoolGUI extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblChatRoomsChats;
	private JList<Client> listAllMembers;
	private JLabel lblMember;
	private JButton btnKick;
	private JButton btnBan;
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
	private JButton btnKickClientRoom;
	private JButton btnBanClientRoom;
	private JLabel lblChatroomMitglieder;
	private JList<ClientProxy> listRoomMembers;
	private JList<String> listChatRoom;
	private DefaultListModel<String> listModelPublicChat = new DefaultListModel<String>();
	private DefaultListModel<ClientProxy> listModelPrivateMember = new DefaultListModel<ClientProxy>();	
	private Admintool admintool;
	

	/**
	 * Launch the application.
	**/
	public AdmintoolGUI(Server server)
	{
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				closeWindow();
			}
		});
		initialize();
		comboBoxServerIDs.addItem("localhost");
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
	
	private void closeWindow()
	{
		dispose();
		admintool.closeClient();
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
	
	protected void addChatRoomMember()
	{
		listModelPrivateMember.clear();

		ChatRoom cr = (ChatRoom)comboBoxChatRooms.getSelectedItem();
		for (ClientProxy cp : cr.getClientProxyList())
		{
			listModelPrivateMember.addElement(cp);
		}
	}
	
	protected void deleteChatroomMember()
	{
		int index = listRoomMembers.getSelectedIndex();
		listModelPrivateMember.remove(index);
		addChatRoomMember();
	}
	
	private void refreshChatRoomBox()
	{
		admintool.closeChatroom((ChatRoom)comboBoxChatRooms.getSelectedItem());
		comboBoxChatRooms.remove(comboBoxChatRooms.getSelectedIndex());
	}
	
	/* TO DO
	private void refreshMemberList()
	{
		lmMembers.clear();
		for (ClientProxy cp : )
		{
			listModelPrivateMember.addElement(cp);
		}
	}*/ 

	private void initialize()
	{
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 1000, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setTitle("Admintool");
		contentPane.setLayout(null);
		contentPane.add(getLblChatRoomsChats());
		contentPane.add(getListAllMembers());
		contentPane.add(getLblMember());
		contentPane.add(getBtnKick());
		contentPane.add(getBtnBan());
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
		contentPane.add(getBtnKickClientRoom());
		contentPane.add(getBtnBanClientRoom());
		contentPane.add(getLblChatroomMitglieder());
		contentPane.add(getListRoomMembers());
		contentPane.add(getListChatRoom());
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

	private JList<Client> getListAllMembers()
	{
		if (listAllMembers == null)
		{
			listAllMembers = new JList<Client>(lmMembers);
			listAllMembers.setBounds(572, 44, 196, 138);
		}
		return listAllMembers;
	}

	private JLabel getLblMember()
	{
		if (lblMember == null)
		{
			lblMember = new JLabel("Members");
			lblMember.setBounds(572, 25, 100, 14);
		}
		return lblMember;
	}

	private JButton getBtnKick()
	{
		if (btnKick == null)
		{
			btnKick = new JButton("Kicken");
			btnKick.addActionListener(e -> admintool.kickClient(lmMembers.elementAt(listAllMembers.getSelectedIndex())));
			btnKick.setBounds(778, 40, 92, 25);
		}
		return btnKick;
	}

	private JButton getBtnBan()
	{
		if (btnBan == null)
		{
			btnBan = new JButton("Bannen");
			btnBan.addActionListener(e -> admintool.banClient(lmMembers.elementAt(listAllMembers.getSelectedIndex())));
			btnBan.setBounds(780, 75, 92, 25);
		}
		return btnBan;
	}

	private JButton getBtnCloseChat()
	{
		if (btnCloseChat == null)
		{
			btnCloseChat = new JButton("Chat Schliessen");
			btnCloseChat.addActionListener(e -> refreshChatRoomBox());
			btnCloseChat.setBounds(682, 254, 138, 35);
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
			comboBoxChatRooms.addActionListener(e -> addChatRoomMember());
			comboBoxChatRooms.setBounds(572, 218, 320, 25);
		}
		return comboBoxChatRooms;
	}

	private JButton getBtnKickClientRoom() {
		if (btnKickClientRoom == null) {
			btnKickClientRoom = new JButton("Kick Client");
			btnKickClientRoom.setBounds(830, 254, 100, 35);
		}
		return btnKickClientRoom;
	}
	private JButton getBtnBanClientRoom() {
		if (btnBanClientRoom == null) {
			btnBanClientRoom = new JButton("Ban Client");
			btnBanClientRoom.setBounds(572, 254, 100, 35);
		}
		return btnBanClientRoom;
	}
	private JLabel getLblChatroomMitglieder() {
		if (lblChatroomMitglieder == null) {
			lblChatroomMitglieder = new JLabel("Chatroom Mitglieder");
			lblChatroomMitglieder.setBounds(572, 300, 160, 14);
		}
		return lblChatroomMitglieder;
	}
	private JList<ClientProxy> getListRoomMembers() {
		if (listRoomMembers == null) {
			listRoomMembers = new JList<ClientProxy>(listModelPrivateMember);
			listRoomMembers.setBounds(572, 325, 160, 100);
		}
		return listRoomMembers;
	}
	private JList<String> getListChatRoom() {
		if (listChatRoom == null) {
			listChatRoom = new JList<String>(listModelPublicChat);
			listChatRoom.setBounds(10, 89, 550, 240);
		}
		return listChatRoom;
	}
}
