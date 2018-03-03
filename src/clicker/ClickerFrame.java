package clicker;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

public class ClickerFrame extends JFrame
{
	
	private JTabbedPane tabPane;
	private int numberOfRobots = 0;
	public ClickerFrame()
	{
		
		setTitle("ClickerNS");
		setPreferredSize(new Dimension(500,700));
		setLayout(new BorderLayout());
		setResizable(false);
		

		tabPane = new JTabbedPane();
		
		QueuePanel kolejka = new QueuePanel(this);
		JPanel myszPanel = new MyszPanel(new GridLayout(8,1), this, kolejka);
		JPanel klawiaturaPanel = new KlawiaturaPanel(new GridLayout(6,1), kolejka, this);
		
		
		tabPane.addTab("Mysz", myszPanel);
		tabPane.addTab("Klawiatura", klawiaturaPanel);
		tabPane.addTab("Szablony", kolejka);
		add(tabPane);
		pack();
		
		addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent e) 
			{
				kolejka.saveProperties();
				System.exit(0);
			}
		});
	}

	
}

class MyszPanel extends JPanel
{
	public boolean clicked = false;
	
	public MyszPanel(LayoutManager mg, ClickerFrame currentFrame, QueuePanel k)
	{
		setLayout(mg);
		
		//Przysiski myszy lpm / ppm
		JPanel przyciskMyszy = new JPanel();
		JLabel pm = new JLabel("Przycisk Myszy");
		ButtonGroup bgMysz = new ButtonGroup();
		JRadioButton lewaMysz = new JRadioButton("Lewy");
		lewaMysz.setSelected(true);
		JRadioButton prawaMysz = new JRadioButton("Prawy");
		bgMysz.add(lewaMysz);
		bgMysz.add(prawaMysz);
		przyciskMyszy.add(pm);
		przyciskMyszy.add(lewaMysz);
		przyciskMyszy.add(prawaMysz);
		add(przyciskMyszy);
		
		
		//Czy ma byæ podwójne klikniêcie
		JPanel dbClick = new JPanel();
		JLabel dbKlik = new JLabel("Podwójne klikniêcie");
		ButtonGroup dbGroup = new ButtonGroup();
		JRadioButton dbFalse = new JRadioButton("Nie");
		dbFalse.setSelected(true);
		JRadioButton dbTrue = new JRadioButton("Tak");
		dbGroup.add(dbFalse);
		dbGroup.add(dbTrue);
		dbClick.add(dbKlik);
		dbClick.add(dbFalse);
		dbClick.add(dbTrue);
		add(dbClick);
		
		
		//Okreœlenie wspó³rzêdnych kursora
		JPanel cursorCoords = new JPanel();
		JLabel wKursora = new JLabel("Wspó³rzêdne kursora ");
		JLabel xLabel = new JLabel("X:");
		JLabel yLabel = new JLabel("Y:");
		JTextField xText = new JTextField();
		xText.setPreferredSize(new Dimension(50, 20));
		JTextField yText = new JTextField();
		yText.setPreferredSize(new Dimension(50, 20));
		JButton coords = new JButton("Zaznacz");
		coords.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				currentFrame.setVisible(false);
				clicked = false;
				ScreenFrame frame = new ScreenFrame();
				frame.setVisible(true);
				frame.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent event) 
					{
						xText.setText(new Double(event.getPoint().getX()).toString());
						yText.setText(new Double(event.getPoint().getY()).toString());
						clicked = true;
						removeMouseListener(this);
						frame.dispose();
						currentFrame.setVisible(true);
					}
				});
				
			}
		});
		cursorCoords.add(wKursora);
		cursorCoords.add(xLabel);
		cursorCoords.add(xText);
		cursorCoords.add(yLabel);
		cursorCoords.add(yText);
		cursorCoords.add(coords);
		add(cursorCoords);
		
		
		CzasWykonania czasWykonania = new CzasWykonania();
		RepeatTruePanel repeatTrue = new RepeatTruePanel(false);
		PowtorzPanel powtarzanie = new PowtorzPanel(repeatTrue);
		
		add(czasWykonania);
		add(powtarzanie);
		add(repeatTrue);
		
		JPanel akcept = new JPanel();
		JButton start = new JButton("Start");
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			double x = Double.parseDouble(xText.getText());
			double y = Double.parseDouble(yText.getText());
			ClickerObject objekt = new ClickerObject(lewaMysz.isSelected(), dbTrue.isSelected(), new Integer((int) x),new Integer((int) y),czasWykonania.getTimeToExecute(), powtarzanie.selectionState(), repeatTrue.getTimeBetween(), repeatTrue.getHowManyTimesToRepeat(), repeatTrue.isInfinite());
			objekt.startRobot(true);
			}
		});
		JButton kolej = new JButton("Dodaj do szablonów");
		kolej.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				double x = Double.parseDouble(xText.getText());
				double y = Double.parseDouble(yText.getText());
				ClickerObject objekt = new ClickerObject(lewaMysz.isSelected(), dbTrue.isSelected(), new Integer((int) x),new Integer((int) y),czasWykonania.getTimeToExecute(),powtarzanie.selectionState(), repeatTrue.getTimeBetween(), repeatTrue.getHowManyTimesToRepeat(), repeatTrue.isInfinite());
				k.addElementToList(objekt, true);
			}
		});
		akcept.add(start);
		akcept.add(kolej);
		add(akcept);
		JPanel copyR = new JPanel();
		JLabel copy = new JLabel("© Nikodem Strawa");
		copyR.add(copy);
		add(copyR);
	}
	
}

class KlawiaturaPanel extends JPanel
{
	public KlawiaturaPanel(LayoutManager mg, QueuePanel k, ClickerFrame currentFrame)
	{
		setLayout(mg);
		
		JPanel chooseButton = new JPanel();
		JLabel butt = new JLabel("Przysisk");
		JTextField przycisk = new JTextField();
		przycisk.setPreferredSize(new Dimension(50,20));
		przycisk.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				int shift = KeyEvent.VK_SHIFT;
				if (e.getKeyCode() == shift && przycisk.getText().equals(""))
					przycisk.setText("SHIFT");
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				int space = KeyEvent.VK_SPACE;
				int ctrl = KeyEvent.VK_CONTROL;
				int alt = KeyEvent.VK_ALT;
				int delete = KeyEvent.VK_DELETE;
				int esc = KeyEvent.VK_ESCAPE;
				int win = KeyEvent.VK_WINDOWS;
				if (e.getKeyCode() == space)
					przycisk.setText("Space");
				else if (e.getKeyCode() == ctrl)
					przycisk.setText("Ctrl");
				else if (e.getKeyCode() == alt)
					przycisk.setText("ALT");
				
				else if (e.getKeyCode() == esc)
					przycisk.setText("ESc");
				else if (e.getKeyCode() == delete)
					przycisk.setText("DEL");
				else if (e.getKeyCode() == win)
					przycisk.setText("WIn");
			}
		});
		chooseButton.add(butt);
		chooseButton.add(przycisk);
		add(chooseButton);
		
		CzasWykonania wykonaniaCzas = new CzasWykonania();
		RepeatTruePanel powtorzeniaTrue = new RepeatTruePanel(false);
		PowtorzPanel powtorzenia = new PowtorzPanel(powtorzeniaTrue);
		
		add(wykonaniaCzas);
		add(powtorzenia);
		add(powtorzeniaTrue);
		
		JPanel accept = new JPanel();
		JButton st = new JButton("Start");
		st.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ClickerObject obiekt = new ClickerObject(przycisk.getText(), wykonaniaCzas.getTimeToExecute(), powtorzenia.selectionState(), powtorzeniaTrue.getTimeBetween(), powtorzeniaTrue.getHowManyTimesToRepeat(), powtorzeniaTrue.isInfinite());
				obiekt.startRobot(false);
			}
		});
		
		JButton kolej = new JButton("Dodaj do szablonów");
		kolej.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				ClickerObject objekt = new ClickerObject(przycisk.getText(), wykonaniaCzas.getTimeToExecute(), powtorzenia.selectionState(), powtorzeniaTrue.getTimeBetween(), powtorzeniaTrue.getHowManyTimesToRepeat(), powtorzeniaTrue.isInfinite());
				k.addElementToList(objekt, false);
			}
		});
		accept.add(st);
		accept.add(kolej);
		add(accept);
		JPanel copyR = new JPanel();
		JLabel copy = new JLabel("© Nikodem Strawa");
		copyR.add(copy);
		add(copyR);
	}
}

class CzasWykonania extends JPanel
{
	JTextField czas;
	JComboBox<String> listaCzasu;
	public CzasWykonania()
	{
		JLabel kiedy = new JLabel("Czas do wykonania");
		czas = new JTextField("0");
		czas.setPreferredSize(new Dimension(50, 20));
		listaCzasu = new JComboBox<>();
		listaCzasu.addItem("milisekund");
		listaCzasu.addItem("sekund");
		listaCzasu.addItem("minut");
		listaCzasu.addItem("godzin");
		add(kiedy);
		add(czas);
		add(listaCzasu);
		
	}
	public int getTimeToExecute()
	{
		if (listaCzasu.getSelectedIndex() == 0)
			return new Integer(Integer.parseInt(czas.getText())) ;
		else if (listaCzasu.getSelectedIndex() == 1)
			return new Integer(Integer.parseInt(czas.getText())) * 1000;
		else if (listaCzasu.getSelectedIndex() == 2)
			return new Integer(Integer.parseInt(czas.getText())) * 60000;
		else
			return new Integer(Integer.parseInt(czas.getText())) * 3600000;
	}
}
class PowtorzPanel extends JPanel
{
	private JRadioButton repFalse;
	private JRadioButton repTrue;
	public PowtorzPanel(RepeatTruePanel obiekt) 
	{
		JLabel repeat = new JLabel("Powtarzanie");
		ButtonGroup czyRepeat = new ButtonGroup();
		repFalse = new JRadioButton("Nie");
		repFalse.setSelected(true);
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				obiekt.setEnableState(repTrue.isSelected());
			}
		};
		repFalse.addActionListener(listener);
		repTrue = new JRadioButton("Tak");
		repTrue.addActionListener(listener);
		czyRepeat.add(repFalse);
		czyRepeat.add(repTrue);
		add(repeat);
		add(repFalse);
		add(repTrue);
		
		 
	}
	public boolean selectionState()
	{
		return repTrue.isSelected();
	}
}

class RepeatTruePanel extends JPanel
{
	
	private boolean theState;
	private JComboBox<String> WlistaCzasu;
	private JRadioButton infinite;
	private JTextField coile;
	private JTextField ile;
	public RepeatTruePanel(boolean isEnable) 
	{
		
		theState = isEnable;
		JLabel wykonywanie = new JLabel("Wykonywaæ co");
		JLabel ileRazy = new JLabel("ile razy");
		coile = new JTextField();
		coile.setEnabled(theState);
		coile.setPreferredSize(new Dimension(50, 20));
		ile = new JTextField();
		ile.setEnabled(theState);
		ile.setPreferredSize(new Dimension(50, 20));
		WlistaCzasu = new JComboBox<>();
		WlistaCzasu.addItem("milisekund");
		WlistaCzasu.addItem("sekund");
		WlistaCzasu.addItem("minut");
		WlistaCzasu.addItem("godzin");
		WlistaCzasu.setEnabled(theState);
		infinite = new JRadioButton("nieskoñczonoœæ");
		infinite.setEnabled(theState);
		infinite.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ile.setEnabled(!infinite.isSelected());
				ile.setText(null);
			}
		});
		add(wykonywanie);
		add(coile);
		add(WlistaCzasu );
		add(ileRazy);
		add(ile);
		add(infinite);
	}
	public void setEnableState(boolean isEnable)
	{
		theState = isEnable;
		coile.setEnabled(theState);
		WlistaCzasu.setEnabled(theState);
		infinite.setEnabled(theState);
		ile.setEnabled(theState);
		
		if(!theState)
		{
			coile.setText(null);
			ile.setText(null);
			infinite.setSelected(false);
		}
	}
	public int getTimeBetween()
	{
		if (theState)
		{
			if (WlistaCzasu.getSelectedIndex() == 0)
				return new Integer(Integer.parseInt(coile.getText()));
			else if (WlistaCzasu.getSelectedIndex() == 1)
				return new Integer(Integer.parseInt(coile.getText())) * 1000;
			else if (WlistaCzasu.getSelectedIndex() == 2)
				return new Integer(Integer.parseInt(coile.getText())) * 60000;
			else
				return new Integer(Integer.parseInt(coile.getText())) * 3600000;
		}
		else return 0;
		
	}
	public int getHowManyTimesToRepeat()
	{
		if (theState && !isInfinite())
			return new Integer(Integer.parseInt(ile.getText()));
		else
			return 0;
	}
	public boolean isInfinite()
	{
		return infinite.isSelected();
	}
}

class QueuePanel extends JPanel
{
	
	private DefaultListModel<String> opisyObiektow;
	private JList<String> lista;
	private ArrayList<ClickerObject> listaObiektow = new ArrayList<>();
	private ArrayList<Boolean> typyUrzaden = new ArrayList<>();
	private Properties listaSave;
	private File fileList;
	int descriptionsNumber;
	public QueuePanel(ClickerFrame thisFrame) 
	{
		
		setLayout(new BorderLayout());

		JPanel opisy = new JPanel(new GridLayout(1,3,-59,50));
		JLabel deviceType = new JLabel("Urz¹denie");
		JLabel cobutton = new JLabel("Przycisk/Wspó³rzêdne");
		JLabel timeDes = new JLabel("               Czas \n w miliseknundach");
		opisy.add(deviceType);
		opisy.add(cobutton);
		opisy.add(timeDes);
		
		File userHomeDir = new File(System.getProperty("user.home"), "clickerNS");
		if (!userHomeDir.exists())
			userHomeDir.mkdir();
		
		listaSave = new Properties();
		fileList = new File(userHomeDir, "listaSzablonow.properties");
		
		if (fileList.exists())
		{
			try 
			{
				FileInputStream in = new FileInputStream(fileList);
				listaSave.load(in);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		if (listaSave.getProperty("opisy") != null)
		descriptionsNumber = Integer.parseInt(listaSave.getProperty("opisy"));
		
		opisyObiektow = new DefaultListModel<>();
		for (int i = 0; i < descriptionsNumber; i++)
		{
			String key =  "obiekt" + i;
			String key_0 = "czymysz" + i;
			opisyObiektow.addElement(listaSave.getProperty(key));
			typyUrzaden.add(Boolean.parseBoolean(listaSave.getProperty(key_0)));
		}
		for (int i = 0; i < descriptionsNumber; i++)
		{
			if (typyUrzaden.get(i))
			{
				String key_1 = "lewamysz" + i;
				String key_2 = "dbclick" + i;
				String key_3 = "xcoords" + i;
				String key_4 = "ycoords" + i;
				String key_5 = "time" + i;
				String key_6 = "powtarzanie" + i;
				String key_7 = "timebetween" + i;
				String key_8 = "ilerazy" + i;
				String key_9 = "infinite" + i;
				listaObiektow.add(new ClickerObject(Boolean.parseBoolean(listaSave.getProperty(key_1)), Boolean.parseBoolean(listaSave.getProperty(key_2)), Integer.parseInt(listaSave.getProperty(key_3)), Integer.parseInt(listaSave.getProperty(key_4)), Integer.parseInt(listaSave.getProperty(key_5)), Boolean.parseBoolean(listaSave.getProperty(key_6)), Integer.parseInt(listaSave.getProperty(key_7)),Integer.parseInt(listaSave.getProperty(key_8)), Boolean.parseBoolean(listaSave.getProperty(key_9))));
			}
			else
			{
				String key_1 = "przycisk" + i;
				String key_2 = "time" + i;
				String key_3 = "powtarzanie" + i;
				String key_4 = "timebetween" + i;
				String key_5 = "ilerazy" + i;
				String key_6 = "infinite" + i;
				listaObiektow.add(new ClickerObject(listaSave.getProperty(key_1), Integer.parseInt(listaSave.getProperty(key_2)), Boolean.parseBoolean(listaSave.getProperty(key_3)), Integer.parseInt(listaSave.getProperty(key_4)),Integer.parseInt(listaSave.getProperty(key_5)), Boolean.parseBoolean(listaSave.getProperty(key_6))));
			}
		}
		lista = new JList<>(opisyObiektow);
		
		lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
		
		lista.setVisibleRowCount(-1);
		
		JScrollPane scroll = new JScrollPane(lista);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setPreferredSize(new Dimension(150,300));
		
		
		
		JPanel przyciski = new JPanel();
		JButton start = new JButton("Start");
		start.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				listaObiektow.get(lista.getSelectedIndex()).startRobot(typyUrzaden.get(lista.getSelectedIndex()));
			}
		});
		JButton delete = new JButton("Usuñ");
		delete.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				typyUrzaden.remove(lista.getSelectedIndex());
				listaObiektow.remove(lista.getSelectedIndex());
				opisyObiektow.remove(lista.getSelectedIndex()); // UWAGA usuwajac element z listy przed jego Obiketem czy typemUrz¹denia spowoduje b³¹d poniewa¿ ten index nie bêdzie ju¿ istnia³ wiêc element z listy który bêdzie usuwany bêdzie mia³ index -1
				
				
			}
		});
		przyciski.add(start);
		przyciski.add(delete);
		
		add(opisy, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		add(przyciski, BorderLayout.SOUTH);
		

	}
	
	public void addElementToList(ClickerObject obiekt, boolean myszka)
	{
		opisyObiektow.addElement(obiekt.getDescription(myszka));
		listaObiektow.add(obiekt);
		typyUrzaden.add(myszka);
		System.out.println(typyUrzaden.toString());
	}
	
	public void saveProperties()
	{
		for (int i = 0; i < opisyObiektow.size(); i++)
		{
			String key =  "obiekt" + i;
			String key_0 = "czymysz" + i;
			
			listaSave.put(key_0, Boolean.toString(typyUrzaden.get(i)));
			if (typyUrzaden.get(i))
			{
				String key_1 = "lewamysz" + i;
				String key_2 = "dbclick" + i;
				String key_3 = "xcoords" + i;
				String key_4 = "ycoords" + i;
				String key_5 = "time" + i;
				String key_6 = "powtarzanie" + i;
				String key_7 = "timebetween" + i;
				String key_8 = "ilerazy" + i;
				String key_9 = "infinite" + i;
				listaSave.put(key_1, Boolean.toString(listaObiektow.get(i).getPrzyciskMyszy()));
				listaSave.put(key_2, Boolean.toString(listaObiektow.get(i).getDBClick()));
				listaSave.put(key_3, Integer.toString((int) listaObiektow.get(i).getXCoords()));
				listaSave.put(key_4, Integer.toString((int) listaObiektow.get(i).getYCoords()));
				listaSave.put(key_5, Integer.toString(listaObiektow.get(i).getTimeToExecute()));
				listaSave.put(key_6, Boolean.toString(listaObiektow.get(i).getIsRepeatTrue()));
				listaSave.put(key_7, Integer.toString((int) listaObiektow.get(i).getTimeBetween()));
				listaSave.put(key_8, Integer.toString((int) listaObiektow.get(i).getHowManyTimesToRepeat()));
				listaSave.put(key_9, Boolean.toString(listaObiektow.get(i).getIsInfinite()));
			}
			else
			{
				String key_1 = "przycisk" + i;
				String key_2 = "time" + i;
				String key_3 = "powtarzanie" + i;
				String key_4 = "timebetween" + i;
				String key_5 = "ilerazy" + i;
				String key_6 = "infinite" + i;
				listaSave.put(key_1, listaObiektow.get(i).getSelectedButton());
				listaSave.put(key_2, Integer.toString(listaObiektow.get(i).getTimeToExecute()));
				listaSave.put(key_3, Boolean.toString(listaObiektow.get(i).getIsRepeatTrue()));
				listaSave.put(key_4, Integer.toString((int) listaObiektow.get(i).getTimeBetween()));
				listaSave.put(key_5, Integer.toString((int) listaObiektow.get(i).getHowManyTimesToRepeat()));
				listaSave.put(key_6, Boolean.toString(listaObiektow.get(i).getIsInfinite()));
			}
			
			listaSave.put(key, opisyObiektow.get(i));
		}
		listaSave.put("opisy", Integer.toString(opisyObiektow.size()));
		try 
		{
			FileOutputStream out = new FileOutputStream(fileList);
			listaSave.store(out, "Elementy Listy");
		} 
		catch (Exception e2) 
		{
			e2.printStackTrace();
		}
	}
}

//Ramka która odpowiada za zrobienie ss'a ca³ego ekranu oraz wyœwietlenie 
//tego zdjêcia w celu zaznaczenia na nim punktu, któy bêdzie okreœla³ wspó³rzêdne klikniêcia
class ScreenFrame extends JFrame
{
	BufferedImage img;
	
	public ScreenFrame()
	{
		//okreœlenie rozmiaru ramki na full screen
		GraphicsEnvironment envitoment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice screen = envitoment.getDefaultScreenDevice();
		screen.setFullScreenWindow(this);
		try {
			final Robot robot = new Robot(screen);
			//delay na 200 by ramka g³ówna zd¹¿y³a znikn¹æ
			robot.delay(200);
			//zrobienie ss
			img = robot.createScreenCapture(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds());
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel label = new JLabel(new ImageIcon(img));
		add(label);
		
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) 
			{
			//zmiana kursora na +
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));	
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
		
	}
}

