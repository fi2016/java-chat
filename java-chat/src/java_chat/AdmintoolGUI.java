package java_chat;

import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JScrollPane;

public class AdmintoolGUI extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblChatRoomsChats;
	private JList<ClientProxy> listAllMembers;
	private JLabel lblMember;
	private JButton btnKick;
	private JButton btnBan;
	private JButton btnCloseChat;
	private DefaultListModel<ClientProxy> lmMembers = new DefaultListModel<ClientProxy>();
	private JComboBox<ChatRoom> comboBoxChatRooms;
	private JButton btnKickClientRoom;
	private JButton btnBanClientRoom;
	private JLabel lblChatroomMember;
	private JList<ClientProxy> listRoomMembers;
	private DefaultListModel<ClientProxy> listModelPrivateMember = new DefaultListModel<ClientProxy>();
	private Admintool admintool;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	/**
	 * Launch the application.
	 * @param roomList 
	 * @param clientList 
	 **/
	public AdmintoolGUI(Server server, ArrayList<ClientProxy> clientList, ArrayList<ChatRoom> roomList)
	{
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent arg0)
			{
				closeWindow();
			}
		});
		initialize();
		admintool = new Admintool(server,clientList,roomList);
		
	}

	private void closeWindow()
	{
		dispose();
	}
	protected void addChatRoomMember()
	{
		refreshRooms();
		listModelPrivateMember.clear();
		
		ChatRoom cr = (ChatRoom) comboBoxChatRooms.getSelectedItem();
		for (ClientProxy cp : cr.getClientProxyList())
		{
			listModelPrivateMember.addElement(cp);
		}
	}

	private void refreshRooms()
	{
		comboBoxChatRooms.removeAll();
		ArrayList<ChatRoom> roomlist = admintool.getRoomList();
		for (ChatRoom room : roomlist)
		{
			comboBoxChatRooms.addItem(room);
		}
	}

	protected void deleteChatroomMember()
	{
		int index = listRoomMembers.getSelectedIndex();
		listModelPrivateMember.remove(index);
		addChatRoomMember();
	}

	private void closeChatRoom()
	{
		admintool.closeChatroom((ChatRoom) comboBoxChatRooms.getSelectedItem());
		comboBoxChatRooms.remove(comboBoxChatRooms.getSelectedIndex());
		refreshRooms();
	}

	protected void createTab(Room r)
	{
		JPanel p = new JPanel();
		JList<String> listHistory = new JList<String>();

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

		p.add(listHistory, gbc_listHistory);

		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		p.add(new Label(r.getName()), gbc_lblNewLabel);
	}
	
	private void initialize()
	{
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 372, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setTitle("Admintool");
		contentPane.setLayout(null);
		contentPane.add(getLblChatRoomsChats());
		contentPane.add(getLblMember());
		contentPane.add(getBtnKick());
		contentPane.add(getBtnBan());
		contentPane.add(getBtnCloseChat());
		contentPane.add(getComboBoxChatRooms());
		contentPane.add(getBtnKickClientRoom());
		contentPane.add(getBtnBanClientRoom());
		contentPane.add(getLblChatroomMember());
		contentPane.add(getScrollPane_2());
		contentPane.add(getScrollPane_1_1());
	}

	private JLabel getLblChatRoomsChats()
	{
		if (lblChatRoomsChats == null)
		{
			lblChatRoomsChats = new JLabel("Chat Rooms");
			lblChatRoomsChats.setBounds(10, 196, 81, 14);
		}
		return lblChatRoomsChats;
	}

	private JList<ClientProxy> getListAllMembers()
	{
		if (listAllMembers == null)
		{
			listAllMembers = new JList<ClientProxy>(lmMembers);
		}
		return listAllMembers;
	}

	private JLabel getLblMember()
	{
		if (lblMember == null)
		{
			lblMember = new JLabel("Members");
			lblMember.setBounds(10, 21, 100, 14);
		}
		return lblMember;
	}

	private JButton getBtnKick()
	{
		if (btnKick == null)
		{
			btnKick = new JButton("Kicken");
			btnKick.addActionListener(
					e -> admintool.kickClient(lmMembers.elementAt(listAllMembers.getSelectedIndex())));
			btnKick.setBounds(214, 44, 92, 25);
		}
		return btnKick;
	}

	private JButton getBtnBan()
	{
		if (btnBan == null)
		{
			btnBan = new JButton("Bannen");
			btnBan.addActionListener(e -> admintool.banClient(lmMembers.elementAt(listAllMembers.getSelectedIndex())));
			btnBan.setBounds(214, 80, 92, 25);
		}
		return btnBan;
	}

	private JButton getBtnCloseChat()
	{
		if (btnCloseChat == null)
		{
			btnCloseChat = new JButton("Close Chat");
			btnCloseChat.addActionListener(e -> closeChatRoom());
			btnCloseChat.setBounds(116, 254, 106, 35);
		}
		return btnCloseChat;
	}

	private JComboBox<ChatRoom> getComboBoxChatRooms()
	{
		if (comboBoxChatRooms == null)
		{
			comboBoxChatRooms = new JComboBox<ChatRoom>();
			comboBoxChatRooms.addActionListener(e -> addChatRoomMember());
			comboBoxChatRooms.setBounds(10, 221, 320, 25);
		}
		return comboBoxChatRooms;
	}

	private JButton getBtnKickClientRoom()
	{
		if (btnKickClientRoom == null)
		{
			btnKickClientRoom = new JButton("Kick Client");
			btnKickClientRoom.setBounds(232, 254, 100, 35);
		}
		return btnKickClientRoom;
	}

	private JButton getBtnBanClientRoom()
	{
		if (btnBanClientRoom == null)
		{
			btnBanClientRoom = new JButton("Ban Client");
			btnBanClientRoom.setBounds(10, 254, 100, 35);
		}
		return btnBanClientRoom;
	}

	private JLabel getLblChatroomMember()
	{
		if (lblChatroomMember == null)
		{
			lblChatroomMember = new JLabel("Chatroom Member");
			lblChatroomMember.setBounds(10, 300, 160, 14);
		}
		return lblChatroomMember;
	}

	private JList<ClientProxy> getListRoomMembers()
	{
		if (listRoomMembers == null)
		{
			listRoomMembers = new JList<ClientProxy>(listModelPrivateMember);
		}
		return listRoomMembers;
	}
	private JScrollPane getScrollPane_2() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 46, 194, 136);
			scrollPane.setViewportView(getListAllMembers());
		}
		return scrollPane;
	}
	private JScrollPane getScrollPane_1_1() {
		if (scrollPane_1 == null) {
			scrollPane_1 = new JScrollPane();
			scrollPane_1.setBounds(10, 326, 158, 98);
			scrollPane_1.setViewportView(getListRoomMembers());
		}
		return scrollPane_1;
	}
}
