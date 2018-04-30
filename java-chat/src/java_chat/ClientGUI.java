package java_chat;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ClientGUI extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6803153492488659745L;
	private JPanel contentPane;

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
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	protected void connectClient()
	{
		
	}
	
	protected void closeClient()
	{
		
	}
	
	protected void handoverMessage()
	{
		
	}
	
	protected void recieveMessage()
	{
		
	}
	
	protected void setNickname()
	{
		
	}

	/**
	 * Create the frame.
	 */
	public ClientGUI()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}
