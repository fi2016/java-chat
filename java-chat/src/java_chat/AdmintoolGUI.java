package java_chat;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JButton;

public class AdmintoolGUI extends JFrame
{

	private JPanel contentPane;
	private JList listPublicChat;
	private JLabel lblffentlicherChat;
	private JList listPrivateChats;
	private JLabel lblPrivateChats;
	private JList listMembers;
	private JLabel lblMember;
	private JButton btnKicken;
	private JButton btnBannen;

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
					AdmintoolGUI frame = new AdmintoolGUI();
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
	public AdmintoolGUI()
	{
		initialize();
	}
	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setTitle("Admintool");
		contentPane.setLayout(null);
		contentPane.add(getListPublicChat());
		contentPane.add(getLblffentlicherChat());
		contentPane.add(getListPrivateChats());
		contentPane.add(getLblPrivateChats());
		contentPane.add(getListMembers());
		contentPane.add(getLblMember());
		contentPane.add(getBtnKicken());
		contentPane.add(getBtnBannen());
	}

	private JList getListPublicChat() {
		if (listPublicChat == null) {
			listPublicChat = new JList();
			listPublicChat.setBounds(10, 215, 440, 236);
		}
		return listPublicChat;
	}
	private JLabel getLblffentlicherChat() {
		if (lblffentlicherChat == null) {
			lblffentlicherChat = new JLabel("\u00D6ffentlicher Chat");
			lblffentlicherChat.setBounds(10, 190, 91, 14);
		}
		return lblffentlicherChat;
	}
	private JList getListPrivateChats() {
		if (listPrivateChats == null) {
			listPrivateChats = new JList();
			listPrivateChats.setBounds(478, 215, 196, 236);
		}
		return listPrivateChats;
	}
	private JLabel getLblPrivateChats() {
		if (lblPrivateChats == null) {
			lblPrivateChats = new JLabel("Private Chats");
			lblPrivateChats.setBounds(478, 190, 81, 14);
		}
		return lblPrivateChats;
	}
	private JList getListMembers() {
		if (listMembers == null) {
			listMembers = new JList();
			listMembers.setBounds(478, 41, 196, 138);
		}
		return listMembers;
	}
	private JLabel getLblMember() {
		if (lblMember == null) {
			lblMember = new JLabel("Members");
			lblMember.setBounds(478, 16, 46, 14);
		}
		return lblMember;
	}
	private JButton getBtnKicken() {
		if (btnKicken == null) {
			btnKicken = new JButton("Kicken");
			btnKicken.setBounds(361, 41, 89, 23);
		}
		return btnKicken;
	}
	private JButton getBtnBannen() {
		if (btnBannen == null) {
			btnBannen = new JButton("Bannen");
			btnBannen.setBounds(361, 75, 89, 23);
		}
		return btnBannen;
	}
}
