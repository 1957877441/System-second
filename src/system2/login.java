package system2;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * ��¼����
 * 
 * @author LB
 * @create
 */
public class login extends JFrame implements ActionListener {
	JLabel user, password;
	JTextField username;
	JPasswordField passwordField;
	JButton loginButton;
	CardLayout cardLayout = new CardLayout();
	JPanel card;
	JPanel cardPanel;
	JTabbedPane jTabbedPane;
	int type = 1;
	Users users;
	static String str;
	static int b;

	public login() {
		init();
	}

	private void init() {
		setTitle("������Ϣ����ϵͳ");
		setLayout(new BorderLayout());
		user = new JLabel("�û���");
		password = new JLabel("����");

		card = new JPanel(cardLayout);

		JPanel panel1 = new JPanel(new BorderLayout());

		username = new JTextField();
		passwordField = new JPasswordField();
		loginButton = new JButton("��¼");
		loginButton.addActionListener(this);

		JPanel titlepanel = new JPanel(new FlowLayout());// �������
		JLabel title = new JLabel("������Ϣ����ϵͳ");
		titlepanel.add(title);

		JPanel loginpanel = new JPanel();// ��¼���
		loginpanel.setLayout(null);

		user.setBounds(50, 20, 50, 20);
		password.setBounds(50, 60, 50, 20);
		username.setBounds(110, 20, 120, 20);
		passwordField.setBounds(110, 60, 120, 20);
		loginpanel.add(user);
		loginpanel.add(password);
		loginpanel.add(username);
		loginpanel.add(passwordField);

		panel1.add(titlepanel, BorderLayout.NORTH);
		panel1.add(loginpanel, BorderLayout.CENTER);
		panel1.add(loginButton, BorderLayout.SOUTH);

		card.add(panel1, "login");
		// card.add(cardPanel, "info");

		add(card);
		setBounds(600, 200, 900, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// ��������
	static void playMusic() {// �������ֲ���
		try {
			java.net.URL cb;
			File f = new File("src/images/Taylor+Swift+-+Ours.wav"); // ����������������ļ����ڵ�·��
			cb = f.toURL();
			AudioClip aau;
			aau = Applet.newAudioClip(cb);

			aau.play();
			aau.loop();// ѭ������
			System.out.println("���Բ���");
			// ѭ������ aau.play()
			// ���� aau.stop()ֹͣ����

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new login();
		String z = JOptionPane.showInputDialog("����:");
		Runnable runnable = new Runnable() {
			// ���� run ����
			public void run() {
				// ��ȡ����ʱ��
				java.util.Date utildate = new java.util.Date();
				str = DateFormat.getTimeInstance().format(utildate);
				System.out.println(str);
				System.out.println("Hello��");
				b = str.compareTo(z);
				if (b == 0) {
					System.out.println("1��");
					playMusic();
				}
			}
		};
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		// �ڶ�������Ϊ�״�ִ�е���ʱʱ�䣬����������Ϊ��ʱִ�еļ��ʱ��
		// 10���� 1����
		// ��һ��ִ�е�ʱ��Ϊ10�룬Ȼ��ÿ��һ��ִ��һ��
		service.scheduleAtFixedRate(runnable, 10, 1, TimeUnit.SECONDS);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean flag = false;// ������־�û��Ƿ���ȷ

		if (e.getSource() == loginButton) {
			ArrayList<Users> list = new CheckUsers().getUsers();// ��������û���Ϣ
			for (int i = 0; i < list.size(); i++) {// ���������û���Ϣ���Դ����ж��������Ϣ�Ƿ���ȷ
				users = list.get(i);
				String passwordStr = new String(passwordField.getPassword());
				if (username.getText().equals(users.getName()) && passwordStr.equals(users.getPassword())) {
					if (users.getType() == 1) {// ѧ��
						type = users.getType();
						JOptionPane.showMessageDialog(null, "��ӭ��¼(ѧ��)", "������Ϣ����ϵͳ", JOptionPane.PLAIN_MESSAGE);
					}
					if (users.getType() == 5) {// ��ְ��
						type = users.getType();
						JOptionPane.showMessageDialog(null, "��ӭ��¼(��ְ��)", "������Ϣ����ϵͳ", JOptionPane.PLAIN_MESSAGE);
					}
					if (users.getType() == 2) {// �������߲���
						type = users.getType();
						JOptionPane.showMessageDialog(null, "��ӭ��¼(�������߲���)", "������Ϣ����ϵͳ", JOptionPane.PLAIN_MESSAGE);
					}
					if (users.getType() == 3) {// �������߲��Ÿ�����
						type = users.getType();
						System.out.println(type);
						JOptionPane.showMessageDialog(null, "��ӭ��¼(�������߲��Ÿ�����)", "������Ϣ����ϵͳ", JOptionPane.PLAIN_MESSAGE);
					}
					if (users.getType() == 4) {// ���߰츺����
						type = users.getType();
						System.out.println(type);
						JOptionPane.showMessageDialog(null, "��ӭ��¼(���ذ�)", "������Ϣ����ϵͳ", JOptionPane.PLAIN_MESSAGE);
					}
					flag = true;
					break;// �����Ϣ��ȷ���˳����������Ч��
				}
			}
			if (!flag) {// ��Ϣ����ȷ����������
				JOptionPane.showMessageDialog(null, "��������ȷ���û���������", "����", JOptionPane.WARNING_MESSAGE);
				username.setText("");
				passwordField.setText("");
			} else {
				// ���������Ϣ��ȷʱ���Ϳ�ʼ����ѡ����棬����ѡ�������뵽��Ƭ��������
				DormitoryInfo dormitoryInfo = new DormitoryInfo(users, type);// ������Ϣ
				cardPanel = new JPanel();
				jTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
				jTabbedPane.add("������Ϣ", dormitoryInfo);
				cardPanel.add(jTabbedPane);
				card.add(cardPanel, "info");
				cardLayout.show(card, "info");// ������Ϣ��ȷ����ʾ�������棬��������������ȷ��Ϣ
			}
		}
	}
}
