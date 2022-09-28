package defaultSS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import list.SolarSystem;
import list.Planet;

public class SolarSystemDisplayDriver {

	JFrame frmLab;
	private JPanel canvas;
	private SolarSystem list = new SolarSystem();
	private Object lock = new Object();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SolarSystemDisplayDriver window = new SolarSystemDisplayDriver();
					window.frmLab.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SolarSystemDisplayDriver() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize() {
		list.add(new Planet("Mercury", 0.39, 4222.6, 0));
		list.add(new Planet("Venus", 0.723, 2802.0, 0));
		list.add(new Planet("Earth", 1, 24, 1));
		list.add(new Planet("Mars", 1.524, 24.7, 2));
		list.add(new Planet("Jupiter", 5.203, 9.9, 79));
		list.add(new Planet("Saturn", 9.539, 10.7, 62));
		list.add(new Planet("Uranus", 19.18, 17.2, 27));
		list.add(new Planet("Neptune", 30.06, 16.1, 14));

		frmLab = new JFrame();
		frmLab.setTitle("Lab 5: Solar System");
		frmLab.setBounds(10, 200, 1905, 290);
		frmLab.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLab.getContentPane().setLayout(null);

		JPanel currentPanel = new JPanel();
		currentPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Solar System",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		currentPanel.setBounds(6, 10, 1870, 220);
		frmLab.getContentPane().add(currentPanel);
		currentPanel.setLayout(null);

		canvas = list.makeCanvas();
		canvas.setBounds(16, 50, 1835, 150);
		currentPanel.add(canvas);
		canvas.setBackground(Color.WHITE);

		JLabel lblSortBy = new JLabel("Sort By:");
		lblSortBy.setBounds(10, 22, 46, 14);
		currentPanel.add(lblSortBy);

		final JComboBox dropSort = new JComboBox();
		dropSort.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				synchronized (lock) {
					if (dropSort.getSelectedIndex() == 2)
						list.sortByDayLength();
					else if (dropSort.getSelectedIndex() == 1)
						list.sortByNumMoons();
					else
						list.sortByDistance();
					canvas.repaint();
				}
			}
		});
		dropSort.setBounds(62, 19, 150, 20);
		currentPanel.add(dropSort);
		dropSort.setModel(
				new DefaultComboBoxModel(new String[] { "Distance From Sun", "Number of Moons", "Day Length" }));
		dropSort.setSelectedIndex(0);

		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dropSort.setSelectedIndex(0);
				list.sortByDistance();
				canvas.repaint();
			}
		});
		btnReset.setBounds(250, 25, 100, 23);
		frmLab.getContentPane().add(btnReset);

	}
}
