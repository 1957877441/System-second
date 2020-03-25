package system2;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * ������Ϣ(ѧ��)
 */
public class DormitoryInfo extends JPanel implements ActionListener {
	Connection connection = new GetConnection().GetConnection();
	Users users;// ��ǰ�û�
	int type;// �û�����
	String Phone = "";// �����
	JTable table = new JTable();
	String[] col = { "ѧ��", "����", "���޸�Ⱦ", "ѧԺ", "��ϵ�绰", "�Ǽ�ʱ��" };
	DefaultTableModel mm = new DefaultTableModel(col, 0); // ����һ�����ģ��
	JLabel sno, name, Sdept, suse, es, time;
	JTextField snoText, nameText, SdeptText, suseText, esText, timeText;
	JButton add;
	JButton delete;
	JButton submit;
	JButton dao, dao1, dao2, dao3, dao4, dao5;
	JButton show, show1;
	JButton jchart;

	JPanel suguan;

	static ChartPanel frame1;
	static int a1, a2;
	static int x;// �ж������������ݵ�ͳ��ͼ
	static JFreeChart chart;
	static String[] data0;

	private DormitoryInfo() {
		if (x == 1) {
			CategoryDataset dataset = getDataSet();// ����õ����ݴ��ݸ�CategoryDataset��Ķ���
			chart = ChartFactory.createBarChart3D("��Ⱦ����ͳ��", // ͼ�����
					"���޸�Ⱦ", // Ŀ¼�����ʾ��ǩ
					"����/��", // ��ֵ�����ʾ��ǩ
					dataset, // ���ݼ�
					PlotOrientation.VERTICAL, // ͼ����ˮƽ����ֱ
					true, // �Ƿ���ʾͼ��(���ڼ򵥵���״ͼ������false)
					false, // �Ƿ����ɹ���
					false // �Ƿ�����URL����
			);
		}
		if (x == 2) {
			CategoryDataset dataset = getDataSet1();// ����õ����ݴ��ݸ�CategoryDataset��Ķ���
			chart = ChartFactory.createBarChart3D("��д����ͳ��", // ͼ�����
					"������д", // Ŀ¼�����ʾ��ǩ
					"����/��", // ��ֵ�����ʾ��ǩ
					dataset, // ���ݼ�
					PlotOrientation.VERTICAL, // ͼ����ˮƽ����ֱ
					true, // �Ƿ���ʾͼ��(���ڼ򵥵���״ͼ������false)
					false, // �Ƿ����ɹ���
					false // �Ƿ�����URL����
			);
		}
		CategoryPlot plot = chart.getCategoryPlot();// ��ȡͼ���������
		CategoryAxis domainAxis = plot.getDomainAxis(); // ˮƽ�ײ��б�
		domainAxis.setLabelFont(new Font("����", Font.BOLD, 14)); // ˮƽ�ײ�����
		domainAxis.setTickLabelFont(new Font("����", Font.BOLD, 12)); // ��ֱ����
		ValueAxis rangeAxis = plot.getRangeAxis();// ��ȡ��״
		rangeAxis.setLabelFont(new Font("����", Font.BOLD, 15));
		chart.getLegend().setItemFont(new Font("����", Font.BOLD, 15));
		chart.getTitle().setFont(new Font("����", Font.BOLD, 20));// ���ñ�������

		frame1 = new ChartPanel(chart, true); // ����Ҳ������chartFrame,����ֱ������һ��������Frame

	}

	private static CategoryDataset getDataSet() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(a1, "��Ⱦ", "��Ⱦ");// �ɼ�1
		dataset.addValue(a2, "δ��Ⱦ", "δ��Ⱦ");// �ɼ�2
		return dataset;
	}

	private static CategoryDataset getDataSet1() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(a1, "��д", "��д");// �ɼ�1
		dataset.addValue(a2 - a1, "δ��д", "δ��д");// �ɼ�2
		return dataset;
	}

	public ChartPanel getChartPanel() {
		return frame1;

	}

	public DormitoryInfo(Users users, int type) {// �ӵ�¼���洫�أ��û������û�����
		this.type = type;
		this.users = users;
		setLayout(new FlowLayout());

		table.setModel(mm);
		table.setRowSorter(new TableRowSorter<>(mm));// ����
		JPanel jPanel = new JPanel(new FlowLayout());
		JScrollPane js = new JScrollPane(table);
		jPanel.add(js);

		add(jPanel);
		search();
	}

	private void search() {
		PreparedStatement state;
		ResultSet resultSet;
		if (type == 1) {// ѧ��
			try {
				inquire();
				String select = "select Phone from ES where Sname" + "=" + "'" + users.getName() + "'";
				state = connection.prepareStatement(select);
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					Phone = resultSet.getString("Phone");
				}
				System.out.println(users.getName() + users.getName().length());
				select = "select*from ES where Phone" + "=" + "'" + Phone + "'";
				state = connection.prepareStatement(select);
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String Dno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, Dno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (type == 2) {// �������߲���
			try {
				inquire1();
				String select = "select Phone from ES where Sname" + "=" + "'" + users.getName() + "'";
				state = connection.prepareStatement(select);
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					Phone = resultSet.getString("Phone");
				}
				System.out.println(users.getName() + users.getName().length());
				select = "select*from ES where Phone" + "=" + "'" + Phone + "'";
				state = connection.prepareStatement(select);
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String Dno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, Dno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (type == 3) {// �������߲��Ÿ�����
			try {
				xiugai();
				int n = 2;
				state = connection.prepareStatement("select *from ES where Utype" + "=" + "'" + n + "'");
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String Dno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, Dno, Scheckin };
					mm.addRow(data);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (type == 4) {// ���ذ�
			try {
				xiugai1();
				state = connection.prepareStatement("select *from ES");
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String Dno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, Dno, Scheckin };
					mm.addRow(data);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (type == 5) {// ��ְ��
			try {
				inquire2();
				String select = "select Phone from ES where Sname" + "=" + "'" + users.getName() + "'";
				state = connection.prepareStatement(select);
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					Phone = resultSet.getString("Phone");
				}
				System.out.println(users.getName() + users.getName().length());
				select = "select*from ES where Phone" + "=" + "'" + Phone + "'";
				state = connection.prepareStatement(select);
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String Dno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, Dno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void inquire() {// ѧ��
		sno = new JLabel("ѧ��");
		Sdept = new JLabel("ѧԺ");
		suse = new JLabel("�绰");
		es = new JLabel("��Ⱦ���");
		time = new JLabel("�Ǽ�ʱ��");
		snoText = new JTextField(10);
		SdeptText = new JTextField(10);
		suseText = new JTextField(10);
		esText = new JTextField(10);
		timeText = new JTextField(10);
		name = new JLabel("����");
		nameText = new JTextField(10);
		suguan = new JPanel(new GridLayout(7, 2));
		add = new JButton("�ύ");
		add.addActionListener(this);
		sno.setFont(new Font("����", Font.PLAIN, 18));
		name.setFont(new Font("����", Font.PLAIN, 18));
		Sdept.setFont(new Font("����", Font.PLAIN, 18));
		suse.setFont(new Font("����", Font.PLAIN, 18));
		es.setFont(new Font("����", Font.PLAIN, 18));
		time.setFont(new Font("����", Font.PLAIN, 18));
		snoText.setBounds(0, 0, 240, 40);
		nameText.setBounds(0, 0, 240, 40);
		SdeptText.setBounds(0, 0, 240, 40);
		suseText.setBounds(0, 0, 240, 40);
		esText.setBounds(0, 0, 240, 40);
		timeText.setBounds(0, 0, 240, 40);
		suguan.add(sno);
		suguan.add(snoText);
		suguan.add(name);
		suguan.add(nameText);
		suguan.add(es);
		suguan.add(esText);
		suguan.add(Sdept);
		suguan.add(SdeptText);
		suguan.add(suse);
		suguan.add(suseText);
		suguan.add(time);
		suguan.add(timeText);
		add(suguan);
		suguan.add(add);
	}

	private void inquire1() {// �������߲���
		sno = new JLabel("ѧ��");
		Sdept = new JLabel("��������");
		suse = new JLabel("�绰");
		es = new JLabel("��Ⱦ���");
		time = new JLabel("�Ǽ�ʱ��");
		snoText = new JTextField(10);
		SdeptText = new JTextField(10);
		suseText = new JTextField(10);
		esText = new JTextField(10);
		timeText = new JTextField(10);
		name = new JLabel("����");
		nameText = new JTextField(10);
		suguan = new JPanel(new GridLayout(7, 2));
		add = new JButton("�ύ");
		add.addActionListener(this);
		sno.setFont(new Font("����", Font.PLAIN, 18));
		name.setFont(new Font("����", Font.PLAIN, 18));
		Sdept.setFont(new Font("����", Font.PLAIN, 18));
		suse.setFont(new Font("����", Font.PLAIN, 18));
		es.setFont(new Font("����", Font.PLAIN, 18));
		time.setFont(new Font("����", Font.PLAIN, 18));
		snoText.setBounds(0, 0, 240, 40);
		nameText.setBounds(0, 0, 240, 40);
		SdeptText.setBounds(0, 0, 240, 40);
		suseText.setBounds(0, 0, 240, 40);
		esText.setBounds(0, 0, 240, 40);
		timeText.setBounds(0, 0, 240, 40);
		suguan.add(sno);
		suguan.add(snoText);
		suguan.add(name);
		suguan.add(nameText);
		suguan.add(es);
		suguan.add(esText);
		suguan.add(Sdept);
		suguan.add(SdeptText);
		suguan.add(suse);
		suguan.add(suseText);
		suguan.add(time);
		suguan.add(timeText);
		add(suguan);
		suguan.add(add);
	}

	private void inquire2() {// ��ְ��
		sno = new JLabel("ѧ��");
		Sdept = new JLabel("��������");
		suse = new JLabel("�绰");
		es = new JLabel("��Ⱦ���");
		time = new JLabel("�Ǽ�ʱ��");
		snoText = new JTextField(10);
		SdeptText = new JTextField(10);
		suseText = new JTextField(10);
		esText = new JTextField(10);
		timeText = new JTextField(10);
		name = new JLabel("����");
		nameText = new JTextField(10);
		suguan = new JPanel(new GridLayout(7, 2));
		add = new JButton("�ύ");
		add.addActionListener(this);
		sno.setFont(new Font("����", Font.PLAIN, 18));
		name.setFont(new Font("����", Font.PLAIN, 18));
		Sdept.setFont(new Font("����", Font.PLAIN, 18));
		suse.setFont(new Font("����", Font.PLAIN, 18));
		es.setFont(new Font("����", Font.PLAIN, 18));
		time.setFont(new Font("����", Font.PLAIN, 18));
		snoText.setBounds(0, 0, 240, 40);
		nameText.setBounds(0, 0, 240, 40);
		SdeptText.setBounds(0, 0, 240, 40);
		suseText.setBounds(0, 0, 240, 40);
		esText.setBounds(0, 0, 240, 40);
		timeText.setBounds(0, 0, 240, 40);
		suguan.add(sno);
		suguan.add(snoText);
		suguan.add(name);
		suguan.add(nameText);
		suguan.add(es);
		suguan.add(esText);
		suguan.add(Sdept);
		suguan.add(SdeptText);
		suguan.add(suse);
		suguan.add(suseText);
		suguan.add(time);
		suguan.add(timeText);
		add(suguan);
		suguan.add(add);
	}

	private void xiugai() {// �������߲��Ÿ�����
		sno = new JLabel("ѧ��");
		Sdept = new JLabel("��������");
		suse = new JLabel("�绰");
		es = new JLabel("��Ⱦ���");
		time = new JLabel("�Ǽ�ʱ��");
		snoText = new JTextField(10);
		SdeptText = new JTextField(10);
		suseText = new JTextField(10);
		esText = new JTextField(10);
		timeText = new JTextField(10);
		name = new JLabel("����");
		nameText = new JTextField(10);
		suguan = new JPanel(new GridLayout(10, 2));
		add = new JButton("����");
		add.addActionListener(this);
		delete = new JButton("ɾ��(����)");
		delete.addActionListener(this);
		submit = new JButton("�޸�");
		submit.addActionListener(this);
		dao = new JButton("ѧ��׼ȷ��ѯ");
		dao.addActionListener(this);
		dao1 = new JButton("����ģ����ѯ");
		dao1.addActionListener(this);
		dao2 = new JButton("ʱ��׼ȷ��ѯ");
		dao2.addActionListener(this);
		dao3 = new JButton("��Ⱦ�����ѯ");
		dao3.addActionListener(this);
		jchart = new JButton("��Ⱦ���ͳ��ͼ");
		jchart.addActionListener(this);
		sno.setFont(new Font("����", Font.PLAIN, 18));
		name.setFont(new Font("����", Font.PLAIN, 18));
		Sdept.setFont(new Font("����", Font.PLAIN, 18));
		suse.setFont(new Font("����", Font.PLAIN, 18));
		es.setFont(new Font("����", Font.PLAIN, 18));
		time.setFont(new Font("����", Font.PLAIN, 18));
		snoText.setBounds(0, 0, 240, 40);
		nameText.setBounds(0, 0, 240, 40);
		SdeptText.setBounds(0, 0, 240, 40);
		suseText.setBounds(0, 0, 240, 40);
		esText.setBounds(0, 0, 240, 40);
		timeText.setBounds(0, 0, 240, 40);
		suguan.add(sno);
		suguan.add(snoText);
		suguan.add(name);
		suguan.add(nameText);
		suguan.add(es);
		suguan.add(esText);
		suguan.add(Sdept);
		suguan.add(SdeptText);
		suguan.add(suse);
		suguan.add(suseText);
		suguan.add(time);
		suguan.add(timeText);
		add(suguan);
		suguan.add(add);
		suguan.add(delete);
		suguan.add(submit);
		suguan.add(dao);
		suguan.add(dao1);
		suguan.add(dao2);
		suguan.add(dao3);
		suguan.add(jchart);
	}

	private void xiugai1() {// ���ذ�
		sno = new JLabel("ѧ��");
		Sdept = new JLabel("������λ");
		suse = new JLabel("�绰");
		es = new JLabel("��Ⱦ���");
		time = new JLabel("�Ǽ�ʱ��");
		snoText = new JTextField(10);
		SdeptText = new JTextField(10);
		suseText = new JTextField(10);
		esText = new JTextField(10);
		timeText = new JTextField(10);
		name = new JLabel("����");
		nameText = new JTextField(10);
		suguan = new JPanel(new GridLayout(12, 2));
		add = new JButton("����");
		add.addActionListener(this);
		delete = new JButton("ɾ��(����)");
		delete.addActionListener(this);
		submit = new JButton("�޸�");
		submit.addActionListener(this);
		dao = new JButton("ѧ��׼ȷ��ѯ");
		dao.addActionListener(this);
		dao1 = new JButton("����ģ����ѯ");
		dao1.addActionListener(this);
		dao2 = new JButton("ʱ��׼ȷ��ѯ");
		dao2.addActionListener(this);
		dao3 = new JButton("��Ⱦ�����ѯ");
		dao3.addActionListener(this);
		jchart = new JButton("��Ⱦ���ͳ��ͼ");
		jchart.addActionListener(this);
		dao4 = new JButton("��ѯĳ��ĳ�����");
		dao4.addActionListener(this);
		dao5 = new JButton("����ͳ��ͼ");
		dao5.addActionListener(this);
		show = new JButton("ѧ��ͳ����Ϣ����");
		show.addActionListener(this);
		show1 = new JButton("��ְ��ͳ����Ϣ����");
		show1.addActionListener(this);
		sno.setFont(new Font("����", Font.PLAIN, 18));
		name.setFont(new Font("����", Font.PLAIN, 18));
		Sdept.setFont(new Font("����", Font.PLAIN, 18));
		suse.setFont(new Font("����", Font.PLAIN, 18));
		es.setFont(new Font("����", Font.PLAIN, 18));
		time.setFont(new Font("����", Font.PLAIN, 18));
		snoText.setBounds(0, 0, 240, 40);
		nameText.setBounds(0, 0, 240, 40);
		SdeptText.setBounds(0, 0, 240, 40);
		suseText.setBounds(0, 0, 240, 40);
		esText.setBounds(0, 0, 240, 40);
		timeText.setBounds(0, 0, 240, 40);
		suguan.add(sno);
		suguan.add(snoText);
		suguan.add(name);
		suguan.add(nameText);
		suguan.add(es);
		suguan.add(esText);
		suguan.add(Sdept);
		suguan.add(SdeptText);
		suguan.add(suse);
		suguan.add(suseText);
		suguan.add(time);
		suguan.add(timeText);
		add(suguan);
		suguan.add(add);
		suguan.add(delete);
		suguan.add(submit);
		suguan.add(dao);
		suguan.add(dao1);
		suguan.add(dao2);
		suguan.add(dao3);
		suguan.add(jchart);
		suguan.add(dao4);
		suguan.add(dao5);
		suguan.add(show);
		suguan.add(show1);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// *** �������ذ츺���˲��� ***
		if (e.getSource() == add && type == 3) {// ����
			JOptionPane.showMessageDialog(null, "�����ѳɹ���", "����", JOptionPane.WARNING_MESSAGE);
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("insert into dbo.ES values(?,?,?,?,?,?,?)");
			} catch (SQLException e1) {
				System.out.println("����ʧ�ܣ�\n");
				e1.printStackTrace();
			}
			try {
				statement.setString(1, snoText.getText());
			} catch (SQLException e2) {
				System.out.println("ʧ�ܣ�\n");
				e2.printStackTrace();
			}
			try {
				statement.setString(2, nameText.getText());
			} catch (SQLException e3) {
				System.out.println("ʧ�ܣ�\n");
				e3.printStackTrace();
			}
			try {
				statement.setString(3, esText.getText());
			} catch (SQLException e4) {
				System.out.println("ʧ�ܣ�\n");
				e4.printStackTrace();
			}
			try {
				statement.setString(4, SdeptText.getText());
			} catch (SQLException e5) {
				System.out.println("ʧ�ܣ�\n");
				e5.printStackTrace();
			}
			try {
				statement.setString(5, suseText.getText());
			} catch (SQLException e6) {
				System.out.println("ʧ�ܣ�\n");
				e6.printStackTrace();
			}
			try {
				statement.setString(6, timeText.getText());
			} catch (SQLException e7) {
				System.out.println("ʧ�ܣ�\n");
				e7.printStackTrace();
			}
			try {
				int c = 2;
				statement.setLong(7, c);
			} catch (SQLException e7) {
				System.out.println("ʧ�ܣ�\n");
				e7.printStackTrace();
			}

			try {
				statement.executeUpdate();
			} catch (SQLException e1) {
				System.out.println("ʧ�ܣ�\n");
				e1.printStackTrace();
			}
			try {
				PreparedStatement state;
				ResultSet resultSet;
				int n = 2;
				state = connection.prepareStatement("select *from ES where Utype" + "=" + "'" + n + "'");
				resultSet = state.executeQuery();
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		}
		if (e.getSource() == delete && type == 3) {// ɾ��
			JOptionPane.showMessageDialog(null, "ɾ���ѳɹ���", "����", JOptionPane.WARNING_MESSAGE);
			Statement statement;
			try {
				statement = connection.createStatement();

				String sql = "delete from ES where Sname =" + "'" + nameText.getText() + "'" + "and Sno=" + "'"
						+ snoText.getText() + "'" + "and Phone=" + "'" + suseText.getText() + "'" + "and Province="
						+ "'" + SdeptText.getText() + "'" + "and Es=" + "'" + esText.getText() + "'" + "and Scheckin="
						+ "'" + timeText.getText() + "'";
				statement.executeUpdate(sql);
				PreparedStatement state;
				ResultSet resultSet;
				int n = 2;
				state = connection.prepareStatement("select *from ES where Utype" + "=" + "'" + n + "'");
				resultSet = state.executeQuery();
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		if (e.getSource() == submit && type == 3) {// �޸�
			try {
				if (esText.getText().length() > 0 && SdeptText.getText().length() == 0
						&& timeText.getText().length() > 0) {// ֻ�޸ĸ�Ⱦ���

					Statement statement = connection.createStatement();
					String sql = "update ES set Es=" + "'" + esText.getText() + "'" + "where Sno" + "=" + "'"
							+ snoText.getText() + "'" + "and Scheckin=" + "'" + timeText.getText() + "'";
					statement.executeUpdate(sql);
					PreparedStatement state;
					ResultSet resultSet;
					int n = 2;
					state = connection.prepareStatement("select *from ES where Utype" + "=" + "'" + n + "'");
					resultSet = state.executeQuery();
					while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
						// System.out.println(model.getRowCount());
						mm.removeRow(mm.getRowCount() - 1);
					}
					while (resultSet.next()) {// �Ѹ��º������������ʾ������У���ͬ
						String Sno = resultSet.getString(1);
						String Sname = resultSet.getString(2);
						String Ssex = resultSet.getString(3);
						String Sdept = resultSet.getString(4);
						String DDno = resultSet.getString(5);
						String Scheckin = resultSet.getString(6);
						String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
						mm.addRow(data);
					}
				}
				if (esText.getText().length() == 0 && SdeptText.getText().length() > 0
						&& timeText.getText().length() > 0) {// ֻ�޸�����ʡ��
					Statement statement = connection.createStatement();
					String sql = "update ES set Province=" + "'" + SdeptText.getText() + "'" + "where Sno" + "=" + "'"
							+ snoText.getText() + "'" + "and Scheckin=" + "'" + timeText.getText() + "'";
					statement.executeUpdate(sql);
					PreparedStatement state;
					ResultSet resultSet;
					int n = 2;
					state = connection.prepareStatement("select *from ES where Utype" + "=" + "'" + n + "'");
					resultSet = state.executeQuery();
					while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
						// System.out.println(model.getRowCount());
						mm.removeRow(mm.getRowCount() - 1);
					}
					while (resultSet.next()) {
						String Sno = resultSet.getString(1);
						String Sname = resultSet.getString(2);
						String Ssex = resultSet.getString(3);
						String Sdept = resultSet.getString(4);
						String DDno = resultSet.getString(5);
						String Scheckin = resultSet.getString(6);
						String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
						mm.addRow(data);
					}
				}
				if (esText.getText().length() > 0 && SdeptText.getText().length() > 0
						&& timeText.getText().length() > 0) {// ͬʱ�޸�
					Statement statement = connection.createStatement();
					String sql = "update ES set Province=" + "'" + SdeptText.getText() + "'" + ", Es=" + "'"
							+ esText.getText() + "'" + "where Sno" + "=" + "'" + snoText.getText() + "'"
							+ "and Scheckin=" + "'" + timeText.getText() + "'";
					statement.executeUpdate(sql);
					PreparedStatement state;
					ResultSet resultSet;
					int n = 2;
					state = connection.prepareStatement("select *from ES where Utype" + "=" + "'" + n + "'");
					resultSet = state.executeQuery();
					while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
						// System.out.println(model.getRowCount());
						mm.removeRow(mm.getRowCount() - 1);
					}
					while (resultSet.next()) {
						String Sno = resultSet.getString(1);
						String Sname = resultSet.getString(2);
						String Ssex = resultSet.getString(3);
						String Sdept = resultSet.getString(4);
						String DDno = resultSet.getString(5);
						String Scheckin = resultSet.getString(6);
						String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
						mm.addRow(data);
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao && type == 3) {// ����ѧ�Ž���׼ȷ��ѯ
			Statement statement;
			try {
				statement = connection.createStatement();

				String sql = "select *from ES where Sno =" + "'" + snoText.getText() + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao1 && type == 3) {// ��������ģ����ѯ
			Statement statement;
			try {
				statement = connection.createStatement();

				int n = 2;
				String sql = "select *from ES where Sname like'%" + nameText.getText() + "%'" + "and Utype" + "=" + "'"
						+ n + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao2 && type == 3) {// ʱ��׼ȷ��ѯ
			Statement statement;
			try {
				statement = connection.createStatement();

				int n = 2;
				String sql = "select *from ES where Scheckin =" + "'" + timeText.getText() + "'" + "and Utype" + "="
						+ "'" + n + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao3 && type == 3) {// ��Ⱦ��ѯ
			Statement statement;
			try {
				statement = connection.createStatement();

				int n = 2;
				String sql = "select *from ES where Es =" + "'" + esText.getText() + "'" + "and Utype" + "=" + "'" + n
						+ "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					System.out.println(Sno + "," + Sname + "," + Ssex + "," + Sdept + "," + DDno + "," + Scheckin);

					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == jchart && type == 3) {// ����ͳ��ͼ
			a1 = 0;
			a2 = 0;
			x = 1;
			int n = 2;
			try {
				PreparedStatement state;
				ResultSet resultSet;
				state = connection.prepareStatement("select *from ES where Scheckin =" + "'" + timeText.getText() + "'"
						+ "and Utype =" + "'" + n + "'");
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					System.out.println(Sno + "," + Sname + "," + Ssex + "," + Sdept + "," + DDno + "," + Scheckin);
					if (Ssex.equals("��")) {
						a1++;
					} else if (Ssex.equals("��")) {
						a2++;
					}
				}
				JFrame frame = new JFrame("��Ⱦ����ͳ��ͼ");
				frame.setLayout(new GridLayout(1, 1, 5, 5));
				frame.add(new DormitoryInfo().getChartPanel()); // �������ͼ
				frame.setBounds(0, 0, 500, 600);
				frame.setVisible(true);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		//////////////////////////////////////////////
		if (e.getSource() == add && type == 4) {// ����
			String a = JOptionPane.showInputDialog("��������Ҫ�������Ա���ͣ�1ѧ��2ѧУ�������߲��ų�Ա3ѧУ�������߲��Ÿ�����4ѧУ���ذ죩:");
			JOptionPane.showMessageDialog(null, "�����ѳɹ���", "����", JOptionPane.WARNING_MESSAGE);
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("insert into dbo.ES values(?,?,?,?,?,?,?)");
			} catch (SQLException e1) {
				System.out.println("����ʧ�ܣ�\n");
				e1.printStackTrace();
			}
			try {
				statement.setString(1, snoText.getText());
			} catch (SQLException e2) {
				System.out.println("ʧ�ܣ�\n");
				e2.printStackTrace();
			}
			try {
				statement.setString(2, nameText.getText());
			} catch (SQLException e3) {
				System.out.println("ʧ�ܣ�\n");
				e3.printStackTrace();
			}
			try {
				statement.setString(3, esText.getText());
			} catch (SQLException e4) {
				System.out.println("ʧ�ܣ�\n");
				e4.printStackTrace();
			}
			try {
				statement.setString(4, SdeptText.getText());
			} catch (SQLException e5) {
				System.out.println("ʧ�ܣ�\n");
				e5.printStackTrace();
			}
			try {
				statement.setString(5, suseText.getText());
			} catch (SQLException e6) {
				System.out.println("ʧ�ܣ�\n");
				e6.printStackTrace();
			}
			try {
				statement.setString(6, timeText.getText());
			} catch (SQLException e7) {
				System.out.println("ʧ�ܣ�\n");
				e7.printStackTrace();
			}
			try {
				statement.setString(7, a);
			} catch (SQLException e7) {
				System.out.println("ʧ�ܣ�\n");
				e7.printStackTrace();
			}

			try {
				statement.executeUpdate();
			} catch (SQLException e1) {
				System.out.println("ʧ�ܣ�\n");
				e1.printStackTrace();
			}
			try {
				PreparedStatement state;
				ResultSet resultSet;
				state = connection.prepareStatement("select *from ES");
				resultSet = state.executeQuery();
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		}
		if (e.getSource() == delete && type == 4) {// ɾ��
			JOptionPane.showMessageDialog(null, "ɾ���ѳɹ���", "����", JOptionPane.WARNING_MESSAGE);
			Statement statement;
			try {
				statement = connection.createStatement();

				String sql = "delete from ES where Sname =" + "'" + nameText.getText() + "'" + "and Sno=" + "'"
						+ snoText.getText() + "'" + "and Phone=" + "'" + suseText.getText() + "'" + "and Province="
						+ "'" + SdeptText.getText() + "'" + "and Es=" + "'" + esText.getText() + "'" + "and Scheckin="
						+ "'" + timeText.getText() + "'";
				statement.executeUpdate(sql);
				PreparedStatement state;
				ResultSet resultSet;
				state = connection.prepareStatement("select *from ES");
				resultSet = state.executeQuery();
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		if (e.getSource() == submit && type == 4) {// �޸�
			try {
				if (esText.getText().length() > 0 && SdeptText.getText().length() == 0
						&& timeText.getText().length() > 0) {// ֻ�޸ĸ�Ⱦ���

					Statement statement = connection.createStatement();
					String sql = "update ES set Es=" + "'" + esText.getText() + "'" + "where Sno" + "=" + "'"
							+ snoText.getText() + "'" + "and Scheckin=" + "'" + timeText.getText() + "'";
					statement.executeUpdate(sql);
					PreparedStatement state;
					ResultSet resultSet;
					state = connection.prepareStatement("select *from ES");
					resultSet = state.executeQuery();
					while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
						// System.out.println(model.getRowCount());
						mm.removeRow(mm.getRowCount() - 1);
					}
					while (resultSet.next()) {// �Ѹ��º������������ʾ������У���ͬ
						String Sno = resultSet.getString(1);
						String Sname = resultSet.getString(2);
						String Ssex = resultSet.getString(3);
						String Sdept = resultSet.getString(4);
						String DDno = resultSet.getString(5);
						String Scheckin = resultSet.getString(6);
						String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
						mm.addRow(data);
					}
				}
				if (esText.getText().length() == 0 && SdeptText.getText().length() > 0
						&& timeText.getText().length() > 0) {// ֻ�޸�����ʡ��
					Statement statement = connection.createStatement();
					String sql = "update ES set Province=" + "'" + SdeptText.getText() + "'" + "where Sno" + "=" + "'"
							+ snoText.getText() + "'" + "and Scheckin=" + "'" + timeText.getText() + "'";
					statement.executeUpdate(sql);
					PreparedStatement state;
					ResultSet resultSet;
					state = connection.prepareStatement("select *from ES");
					resultSet = state.executeQuery();
					while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
						// System.out.println(model.getRowCount());
						mm.removeRow(mm.getRowCount() - 1);
					}
					while (resultSet.next()) {
						String Sno = resultSet.getString(1);
						String Sname = resultSet.getString(2);
						String Ssex = resultSet.getString(3);
						String Sdept = resultSet.getString(4);
						String DDno = resultSet.getString(5);
						String Scheckin = resultSet.getString(6);
						String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
						mm.addRow(data);
					}
				}
				if (esText.getText().length() > 0 && SdeptText.getText().length() > 0
						&& timeText.getText().length() > 0) {// ͬʱ�޸�
					Statement statement = connection.createStatement();
					String sql = "update ES set Province=" + "'" + SdeptText.getText() + "'" + ", Es=" + "'"
							+ esText.getText() + "'" + "where Sno" + "=" + "'" + snoText.getText() + "'"
							+ "and Scheckin=" + "'" + timeText.getText() + "'";
					statement.executeUpdate(sql);
					PreparedStatement state;
					ResultSet resultSet;
					state = connection.prepareStatement("select *from ES");
					resultSet = state.executeQuery();
					while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
						// System.out.println(model.getRowCount());
						mm.removeRow(mm.getRowCount() - 1);
					}
					while (resultSet.next()) {
						String Sno = resultSet.getString(1);
						String Sname = resultSet.getString(2);
						String Ssex = resultSet.getString(3);
						String Sdept = resultSet.getString(4);
						String DDno = resultSet.getString(5);
						String Scheckin = resultSet.getString(6);
						String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
						mm.addRow(data);
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao && type == 4) {// ����ѧ�Ž���׼ȷ��ѯ
			Statement statement;
			try {
				statement = connection.createStatement();

				String sql = "select *from ES where Sno =" + "'" + snoText.getText() + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao1 && type == 4) {// ��������ģ����ѯ
			Statement statement;
			try {
				statement = connection.createStatement();

				String sql = "select *from ES where Sname like'%" + nameText.getText() + "%'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao2 && type == 4) {// ʱ��׼ȷ��ѯ
			Statement statement;
			try {
				statement = connection.createStatement();

				String sql = "select *from ES where Scheckin =" + "'" + timeText.getText() + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao3 && type == 4) {// ��Ⱦ��ѯ
			Statement statement;
			try {
				statement = connection.createStatement();

				String sql = "select *from ES where Es =" + "'" + esText.getText() + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					System.out.println(Sno + "," + Sname + "," + Ssex + "," + Sdept + "," + DDno + "," + Scheckin);

					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao4 && type == 4) {// ĳ��ĳ�������ѯ
			Statement statement;
			try {
				statement = connection.createStatement();

				String sql = "select *from ES where Scheckin =" + "'" + timeText.getText() + "'" + "and Sno =" + "'"
						+ snoText.getText() + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					System.out.println(Sno + "," + Sname + "," + Ssex + "," + Sdept + "," + DDno + "," + Scheckin);

					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == jchart && type == 4) {// ����ͳ��ͼ
			a1 = 0;
			a2 = 0;
			x = 1;
			try {
				PreparedStatement state;
				ResultSet resultSet;
				state = connection
						.prepareStatement("select *from ES where Scheckin =" + "'" + timeText.getText() + "'");
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					System.out.println(Sno + "," + Sname + "," + Ssex + "," + Sdept + "," + DDno + "," + Scheckin);
					if (Ssex.equals("��")) {
						a1++;
					} else if (Ssex.equals("��")) {
						a2++;
					}
				}
				JFrame frame = new JFrame("��Ⱦ����ͳ��ͼ");
				frame.setLayout(new GridLayout(1, 1, 5, 5));
				frame.add(new DormitoryInfo().getChartPanel()); // �������ͼ
				frame.setBounds(0, 0, 500, 600);
				frame.setVisible(true);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao5 && type == 4) {// ����ͳ��ͼ1
			a1 = 0;
			String a = JOptionPane.showInputDialog("�������ѧԺ��������:");
			a2 = Integer.parseInt(a);

			x = 2;

			ArrayList<String> list = new ArrayList<String>();
			try {
				PreparedStatement state;
				ResultSet resultSet;
				state = connection.prepareStatement("select *from ES where Scheckin =" + "'" + timeText.getText() + "'"
						+ "and Province =" + "'" + SdeptText.getText() + "'");
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					list.add(resultSet.getString(1));
					list.add(resultSet.getString(2));
					list.add(resultSet.getString(3));
					list.add(resultSet.getString(4));
					list.add(resultSet.getString(5));
					list.add(resultSet.getString(6));
					System.out.println(Sno + "," + Sname + "," + Ssex + "," + Sdept + "," + DDno + "," + Scheckin);
					a1++;
				}

				if (list != null && list.size() > 0) {// ���list�д��������ݣ�ת��Ϊ����
					data0 = new String[list.size()];// ����һ����list����һ��������
					for (int i = 0; i < list.size(); i++) {
						data0[i] = list.get(i);// ����
					}
				}

				JFrame frame = new JFrame("��д����ͳ��ͼ");
				frame.setLayout(new GridLayout(1, 1, 5, 5));
				frame.add(new DormitoryInfo().getChartPanel()); // �������ͼ
				frame.setBounds(0, 0, 500, 600);
				frame.setVisible(true);
				String b = JOptionPane.showInputDialog("�������Ƿ񵼳�excel:");
				if (b.equals("��")) {
					try {
						// ���ļ�
						WritableWorkbook book = Workbook.createWorkbook(new File("C:\\Users\\ASUS\\Desktop\\x.xls"));
						// ������Ϊ����һҳ���Ĺ���������0��ʾ���ǵ�һҳ
						WritableSheet sheet = book.createSheet("��һҳ", 0);
						System.out.println((data0.length) / 6);
						for (int i = 0; i < data0.length; i++) {
							Label label = new Label(i, 0, data0[i]);
							sheet.addCell(label);
						}
						// д�����ݲ��ر��ļ�
						book.write();
						book.close();
					} catch (Exception e1) {
						System.out.println(e1);
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == show && type == 4) {// ѧ��ͳ����Ϣ����
			Statement statement;
			try {
				statement = connection.createStatement();
				int z = 1;
				String sql = "select *from ES where Utype =" + "'" + z + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };

					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == show1 && type == 4) {// ��ְ��ͳ����Ϣ����
			Statement statement;
			try {
				statement = connection.createStatement();
				int z = 5;
				String sql = "select *from ES where Utype =" + "'" + z + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };

					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		// ***ѧ��***
		if (e.getSource() == add && type == 1) {// ѧ��
			JOptionPane.showMessageDialog(null, "�����ѳɹ���", "����", JOptionPane.WARNING_MESSAGE);
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("insert into dbo.ES values(?,?,?,?,?,?,?)");
			} catch (SQLException e1) {
				System.out.println("����ʧ�ܣ�\n");
				e1.printStackTrace();
			}
			try {
				statement.setString(1, snoText.getText());
			} catch (SQLException e2) {
				System.out.println("ʧ�ܣ�\n");
				e2.printStackTrace();
			}
			try {
				statement.setString(2, nameText.getText());
			} catch (SQLException e3) {
				System.out.println("ʧ�ܣ�\n");
				e3.printStackTrace();
			}
			try {
				statement.setString(3, esText.getText());
			} catch (SQLException e4) {
				System.out.println("ʧ�ܣ�\n");
				e4.printStackTrace();
			}
			try {
				statement.setString(4, SdeptText.getText());
			} catch (SQLException e5) {
				System.out.println("ʧ�ܣ�\n");
				e5.printStackTrace();
			}
			try {
				statement.setString(5, suseText.getText());
			} catch (SQLException e6) {
				System.out.println("ʧ�ܣ�\n");
				e6.printStackTrace();
			}
			try {
				statement.setString(6, timeText.getText());
			} catch (SQLException e7) {
				System.out.println("ʧ�ܣ�\n");
				e7.printStackTrace();
			}
			try {
				int c = 1;
				statement.setLong(7, c);
			} catch (SQLException e7) {
				System.out.println("ʧ�ܣ�\n");
				e7.printStackTrace();
			}

			try {
				statement.executeUpdate();
			} catch (SQLException e1) {
				System.out.println("ʧ�ܣ�\n");
				e1.printStackTrace();
			}
			try {
				PreparedStatement state;
				ResultSet resultSet;
				state = connection
						.prepareStatement("select *from ES " + "where Sname" + "=" + "'" + users.getName() + "'");
				resultSet = state.executeQuery();
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		// ***�������߲���***
		if (e.getSource() == add && type == 2) {// �������߲���
			JOptionPane.showMessageDialog(null, "�����ѳɹ���", "����", JOptionPane.WARNING_MESSAGE);
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("insert into dbo.ES values(?,?,?,?,?,?,?)");
			} catch (SQLException e1) {
				System.out.println("����ʧ�ܣ�\n");
				e1.printStackTrace();
			}
			try {
				statement.setString(1, snoText.getText());
			} catch (SQLException e2) {
				System.out.println("ʧ�ܣ�\n");
				e2.printStackTrace();
			}
			try {
				statement.setString(2, nameText.getText());
			} catch (SQLException e3) {
				System.out.println("ʧ�ܣ�\n");
				e3.printStackTrace();
			}
			try {
				statement.setString(3, esText.getText());
			} catch (SQLException e4) {
				System.out.println("ʧ�ܣ�\n");
				e4.printStackTrace();
			}
			try {
				statement.setString(4, SdeptText.getText());
			} catch (SQLException e5) {
				System.out.println("ʧ�ܣ�\n");
				e5.printStackTrace();
			}
			try {
				statement.setString(5, suseText.getText());
			} catch (SQLException e6) {
				System.out.println("ʧ�ܣ�\n");
				e6.printStackTrace();
			}
			try {
				statement.setString(6, timeText.getText());
			} catch (SQLException e7) {
				System.out.println("ʧ�ܣ�\n");
				e7.printStackTrace();
			}
			try {
				int c = 2;
				statement.setLong(7, c);
			} catch (SQLException e7) {
				System.out.println("ʧ�ܣ�\n");
				e7.printStackTrace();
			}

			try {
				statement.executeUpdate();
			} catch (SQLException e1) {
				System.out.println("ʧ�ܣ�\n");
				e1.printStackTrace();
			}
			try {
				PreparedStatement state;
				ResultSet resultSet;
				state = connection
						.prepareStatement("select *from ES " + "where Sname" + "=" + "'" + users.getName() + "'");
				resultSet = state.executeQuery();
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		// *** ��ְ�� ***
		if (e.getSource() == add && type == 5) {// ��ְ��
			JOptionPane.showMessageDialog(null, "�����ѳɹ���", "����", JOptionPane.WARNING_MESSAGE);
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("insert into dbo.ES values(?,?,?,?,?,?,?)");
			} catch (SQLException e1) {
				System.out.println("����ʧ�ܣ�\n");
				e1.printStackTrace();
			}
			try {
				statement.setString(1, snoText.getText());
			} catch (SQLException e2) {
				System.out.println("ʧ�ܣ�\n");
				e2.printStackTrace();
			}
			try {
				statement.setString(2, nameText.getText());
			} catch (SQLException e3) {
				System.out.println("ʧ�ܣ�\n");
				e3.printStackTrace();
			}
			try {
				statement.setString(3, esText.getText());
			} catch (SQLException e4) {
				System.out.println("ʧ�ܣ�\n");
				e4.printStackTrace();
			}
			try {
				statement.setString(4, SdeptText.getText());
			} catch (SQLException e5) {
				System.out.println("ʧ�ܣ�\n");
				e5.printStackTrace();
			}
			try {
				statement.setString(5, suseText.getText());
			} catch (SQLException e6) {
				System.out.println("ʧ�ܣ�\n");
				e6.printStackTrace();
			}
			try {
				statement.setString(6, timeText.getText());
			} catch (SQLException e7) {
				System.out.println("ʧ�ܣ�\n");
				e7.printStackTrace();
			}
			try {
				int c = 2;
				statement.setLong(7, c);
			} catch (SQLException e7) {
				System.out.println("ʧ�ܣ�\n");
				e7.printStackTrace();
			}

			try {
				statement.executeUpdate();
			} catch (SQLException e1) {
				System.out.println("ʧ�ܣ�\n");
				e1.printStackTrace();
			}
			try {
				PreparedStatement state;
				ResultSet resultSet;
				state = connection
						.prepareStatement("select *from ES " + "where Sname" + "=" + "'" + users.getName() + "'");
				resultSet = state.executeQuery();
				while (mm.getRowCount() > 0) {// �ѱ�����ˢ�£��´���ʾ��ʱ����ͷ��ʼ��ʾ
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

	}

}