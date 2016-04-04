package graphicUserInterface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import commandLineStep.DataProcessor;

public class MainFrame extends JFrame implements ActionListener {

	JTextField pathDisplay = new JTextField("�п�ܸ�Ƨ����|", 30);
	File subjectPath;
	String specifiedString;

	JTextField textSingleStopVoltage = new JTextField("", 40);
	JTextField textDoubleStopVoltage = new JTextField("", 40);
	JCheckBox checkSingleBackwardScan = new JCheckBox("Include Backward Scan");
	JCheckBox checkDoubleBackwardScan = new JCheckBox("Include Backward Scan");
	JButton btnStartComputeSingleTftData = new JButton("Start");
	JButton btnStartComputeDoubleTftData = new JButton("Start");
	JButton btnStartComputeRealTimeMeasurementData = new JButton("Start");

	JTextField textTargetVoltage = new JTextField("", 40);
	JRadioButton rbtnTargetColumnId1 = new JRadioButton("ID1");
	JRadioButton rbtnTargetColumnId2 = new JRadioButton("ID2");
	JButton btnGetCurrentAtSpecifiedVoltageFromTransferCurve = new JButton(
			"Start");

	public MainFrame() {
		super("4155 Microfluidic TFT Data Processor v3.2 - JJLab");
		System.out.println("LOG: v2.1 �ץ�sampling�q���ɤU��12�I���p����~");
		System.out.println("LOG: v2.2 �Nsampling output�ɦW��X���ؿ���Ƨ��̫᭱���r��");
		System.out.println("LOG: v3�[�J�s�\��G��q�qtransfer�^���S�wVg�U���q�y");
		System.out.println("LOG: v3.1�^���q�youtput��X�ɦW�[�W�Хܭ���ID");
		System.out.println("LOG: v3.2��transfer curve���^���i�H���w�q���W��");
		System.out.println("LOG: v3.3�[����Ƨ����|��e");
		pathDisplay.setEditable(false);
		JButton btnChoosePath = new JButton("��ܼƾڦs�񪺸�Ƨ�");
		btnChoosePath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				subjectPath = choosePath();
				if (subjectPath != null)
					pathDisplay.setText(subjectPath.toString());
			}
		});

		JTabbedPane tabbedPane = new JTabbedPane();

		/*
		 * JButton button = new JButton("1"); tabbedPane.add("Tab 1", button);
		 * button = new JButton("2"); tabbedPane.add("Tab 2", button);
		 */

		JLabel label1 = new JLabel("��qID��log, �}�ڸ�, ��Xtxt�ɡC",
				SwingConstants.CENTER);
		JLabel label2 = new JLabel("��qID��log, �}�ڸ�, ��Xtxt�ɡC",
				SwingConstants.CENTER);
		JLabel label12 = new JLabel("���w����q��(�w�]12V)�G");
		JLabel label22 = new JLabel("���w����q��(�w�]12V)�G");
		JLabel label3 = new JLabel("��q���XID��, ��Xtxt�ɡC", SwingConstants.CENTER);

		JPanel panelSingleTft = new JPanel();
		JPanel panelDoubleTft = new JPanel();
		JPanel panelRealTimeMeasurement = new JPanel();

		panelSingleTft.setLayout(new GridLayout(0, 1));
		panelSingleTft.add(label1, BorderLayout.NORTH);
		panelSingleTft.add(label12);
		panelSingleTft.add(textSingleStopVoltage);
		panelSingleTft.add(checkSingleBackwardScan);
		panelSingleTft.add(btnStartComputeSingleTftData);
		btnStartComputeSingleTftData.addActionListener(this);

		panelDoubleTft.setLayout(new GridLayout(0, 1));
		panelDoubleTft.add(label2, BorderLayout.NORTH);
		panelDoubleTft.add(label22);
		panelDoubleTft.add(textDoubleStopVoltage);
		panelDoubleTft.add(checkDoubleBackwardScan);
		panelDoubleTft.add(btnStartComputeDoubleTftData);
		btnStartComputeDoubleTftData.addActionListener(this);

		panelRealTimeMeasurement.setLayout(new GridLayout(0, 1));
		panelRealTimeMeasurement.add(label3, BorderLayout.NORTH);
		// panelRealTimeMeasurement.add(measurementInterval);
		panelRealTimeMeasurement.add(btnStartComputeRealTimeMeasurementData);
		btnStartComputeRealTimeMeasurementData.addActionListener(this);

		tabbedPane.addTab("��@TFT", null, panelSingleTft);
		tabbedPane.addTab("���kTFT", null, panelDoubleTft);
		tabbedPane.addTab("Sampling", null, panelRealTimeMeasurement);

		/* �o�O�@�ӧ��㪺 panel */
		JLabel label4 = new JLabel("�п�J���w��Vg�G");
		JPanel panelGetCurrentAtSpecifiedVoltageFromTransferCurve = new JPanel();
		panelGetCurrentAtSpecifiedVoltageFromTransferCurve
				.setLayout(new GridLayout(0, 1));
		tabbedPane.addTab("�S�wVg�q�y�^��", null,
				panelGetCurrentAtSpecifiedVoltageFromTransferCurve);

		panelGetCurrentAtSpecifiedVoltageFromTransferCurve.add(label4,
				BorderLayout.NORTH);
		panelGetCurrentAtSpecifiedVoltageFromTransferCurve
				.add(textTargetVoltage);
		panelGetCurrentAtSpecifiedVoltageFromTransferCurve
				.add(rbtnTargetColumnId1);
		panelGetCurrentAtSpecifiedVoltageFromTransferCurve
				.add(rbtnTargetColumnId2);
		ButtonGroup groupGetCurrentAtVoltage = new ButtonGroup();
		groupGetCurrentAtVoltage.add(rbtnTargetColumnId1);
		groupGetCurrentAtVoltage.add(rbtnTargetColumnId2);

		panelGetCurrentAtSpecifiedVoltageFromTransferCurve
				.add(btnGetCurrentAtSpecifiedVoltageFromTransferCurve);
		btnGetCurrentAtSpecifiedVoltageFromTransferCurve
				.addActionListener(this);
		/* ���Obutton���򪺫ŧi�b�̫e�� */

		setLayout(new FlowLayout());
		add(pathDisplay);
		add(btnChoosePath);
		tabbedPane.setBounds(0, 0, 500, 450);
		add(tabbedPane);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 260);
		setVisible(true);
	}

	private File choosePath() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = fileChooser.showOpenDialog(this);

		File fileName = fileChooser.getSelectedFile();
		return fileName;
	}

	protected void startComputing(Boolean includeBackwardScan, int mode)
			throws FileNotFoundException, IOException {
		DataProcessor d = new DataProcessor(subjectPath);
		switch (mode) {
		case 1:
			d.handleAllSingleScans(
					(textSingleStopVoltage.getText() != "") ? Double
							.parseDouble(textSingleStopVoltage.getText()) : 12,
					includeBackwardScan);
			break;

		case 2:
			d.handleAllDoubleScans(
					(textDoubleStopVoltage.getText() != "" ? Double
							.parseDouble(textDoubleStopVoltage.getText()) : 12),
					includeBackwardScan);
			break;

		case 3:
			d.handleAllRealTimeMeasurements();
			break;

		case 4:
			if (rbtnTargetColumnId1.isSelected())
				d.gettingCurrentAtSpecifiedVoltage("ID1",
						textTargetVoltage.getText());
			else if (rbtnTargetColumnId2.isSelected())
				d.gettingCurrentAtSpecifiedVoltage("ID2",
						textTargetVoltage.getText());
			break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!subjectPath.isDirectory()) {
			JOptionPane.showMessageDialog(null, "���|�����T�I");
		}

		if (e.getSource() == this.btnStartComputeSingleTftData) {
			if (checkSingleBackwardScan.isSelected())
				try {
					startComputing(true, 1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			else
				try {
					startComputing(false, 1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}

		else if (e.getSource() == this.btnStartComputeDoubleTftData) {
			if (checkDoubleBackwardScan.isSelected())
				try {
					startComputing(true, 2);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			else
				try {
					startComputing(false, 2);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}

		else if (e.getSource() == this.btnStartComputeRealTimeMeasurementData) {
			try {
				startComputing(false, 3);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		else if (e.getSource() == this.btnGetCurrentAtSpecifiedVoltageFromTransferCurve) {
			try {
				startComputing(false, 4);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
