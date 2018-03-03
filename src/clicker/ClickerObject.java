package clicker;

import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class ClickerObject implements NativeKeyListener
{
	private boolean przyciskMyszy;
	private boolean podwojnyKlik;
	private int xCoords;
	private int yCoords;
	private int timeToExecute;
	private boolean repeat;
	private int timeBetween;
	private int powtorzenia;
	private boolean isInfinite;
	private String selectedButton;
	private Thread robotThread;
	private boolean Stop = false;
	public ClickerObject(boolean przyciskMyszy, boolean podwojnyKlik, int xCoords, int yCoords, int timeToExecute, boolean repeat, int timeBetween, int powtorzenia, boolean isInfinite) 
	{
		this.przyciskMyszy = przyciskMyszy;
		this.podwojnyKlik = podwojnyKlik;
		this.xCoords = xCoords;
		this.yCoords = yCoords;
		this.timeToExecute = timeToExecute;
		this.repeat = repeat;
		this.timeBetween = timeBetween;
		this.isInfinite = isInfinite;
		this.powtorzenia = powtorzenia;
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			System.err.println("Nie dziala");
		}
		
		GlobalScreen.addNativeKeyListener(this); 
	}
	public ClickerObject(String selectedButton, int timeToExecute, boolean repeat, int timeBetween, int powtorzenia, boolean isInfinite)
	{
		this.selectedButton = selectedButton;
		this.timeToExecute = timeToExecute;
		this.repeat = repeat;
		this.timeBetween = timeBetween;
		this.isInfinite = isInfinite;
		this.powtorzenia = powtorzenia;
		
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			System.err.println("Nie dziala");
		}
		
		GlobalScreen.addNativeKeyListener(this); 
	}
	
	public void startRobot(boolean isMyszka)
	{
		
		
		GraphicsEnvironment envitoment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice screen = envitoment.getDefaultScreenDevice();
		try {
			final Robot robot = new Robot(screen);
			robotThread = new Thread()
			{
				public void run() 
				{
					try {
						Thread.sleep(timeToExecute);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Stop = false;
					if (isMyszka)
						runMouseRobot(robot);
					else
						runKeyboardRobot(robot);
						
				};
			};
			robotThread.start();
			
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void runMouseRobot(Robot robot)
	{
		int przyciskM;
		int powtarzanie = 1;
		if (repeat)
			powtarzanie = powtorzenia;
		if (isInfinite)
		{
			while(!Stop)
			{
				try {
					Thread.sleep(timeBetween);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				robot.mouseMove(xCoords, yCoords);
				if (przyciskMyszy)
				{
					przyciskM = InputEvent.BUTTON1_MASK;
				}
				else
				{
					przyciskM = InputEvent.BUTTON3_DOWN_MASK;
				}
				robot.mousePress(przyciskM);
				robot.mouseRelease(przyciskM);
				if (podwojnyKlik)
				{
					robot.mousePress(przyciskM);
					robot.mouseRelease(przyciskM);
				}
			}
		}
		else
		for (int i = 1; i <= powtarzanie; i++)
		{
			if (Stop)
				return;
			try {
				Thread.sleep(timeBetween);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			robot.mouseMove(xCoords, yCoords);
			if (przyciskMyszy)
			{
				przyciskM = InputEvent.BUTTON1_MASK;
			}
			else
			{
				przyciskM = InputEvent.BUTTON3_DOWN_MASK;
			}
			robot.mousePress(przyciskM);
			robot.mouseRelease(przyciskM);
			if (podwojnyKlik)
			{
				robot.mousePress(przyciskM);
				robot.mouseRelease(przyciskM);
			}
		}
		
	}
	public void runKeyboardRobot(Robot robot)
	{
		String tablicaZnakow = "ABCDEFGHIJKLMNOPRSTUWXYZQV1234567890-=!@#$^*()_+[];',./:\\`F1F2F3F4F5F6F7F8F9F10F11F12";
		int indexOfChar = tablicaZnakow.indexOf(new String(selectedButton).toUpperCase());
		int keyOfChar[] =  {KeyEvent.VK_A,KeyEvent.VK_B,KeyEvent.VK_C,KeyEvent.VK_D,KeyEvent.VK_E,KeyEvent.VK_F,KeyEvent.VK_G,KeyEvent.VK_H,KeyEvent.VK_I,KeyEvent.VK_J,
				KeyEvent.VK_K,KeyEvent.VK_L,KeyEvent.VK_M,KeyEvent.VK_N,KeyEvent.VK_O,KeyEvent.VK_P,KeyEvent.VK_R,KeyEvent.VK_S,KeyEvent.VK_T,KeyEvent.VK_U,KeyEvent.VK_W,
				KeyEvent.VK_X,KeyEvent.VK_Y,KeyEvent.VK_Z,KeyEvent.VK_Q,KeyEvent.VK_V,KeyEvent.VK_1,KeyEvent.VK_2,KeyEvent.VK_3,KeyEvent.VK_4,KeyEvent.VK_5,KeyEvent.VK_6,
				KeyEvent.VK_7,KeyEvent.VK_8,KeyEvent.VK_9,KeyEvent.VK_0,KeyEvent.VK_MINUS,KeyEvent.VK_EQUALS,KeyEvent.VK_EXCLAMATION_MARK,KeyEvent.VK_AT,KeyEvent.VK_NUMBER_SIGN,KeyEvent.VK_DOLLAR,KeyEvent.VK_CIRCUMFLEX,KeyEvent.VK_MULTIPLY,
				KeyEvent.VK_LEFT_PARENTHESIS,KeyEvent.VK_RIGHT_PARENTHESIS,KeyEvent.VK_UNDERSCORE,KeyEvent.VK_ADD,KeyEvent.VK_OPEN_BRACKET,KeyEvent.VK_CLOSE_BRACKET,KeyEvent.VK_SEMICOLON,
				KeyEvent.VK_QUOTE,KeyEvent.VK_COMMA,KeyEvent.VK_PERIOD,KeyEvent.VK_SLASH,KeyEvent.VK_COLON,KeyEvent.VK_BACK_SLASH, KeyEvent.VK_BACK_QUOTE, KeyEvent.VK_F1,KeyEvent.VK_F2,KeyEvent.VK_F3,KeyEvent.VK_F4,KeyEvent.VK_F5,KeyEvent.VK_F6,KeyEvent.VK_F7,KeyEvent.VK_F8,KeyEvent.VK_F9,KeyEvent.VK_F10,KeyEvent.VK_F11,KeyEvent.VK_F12};
		
		char c = selectedButton.charAt(0);
		int powtarzanie = 1;
		if (repeat)
			powtarzanie = powtorzenia;
		
		if (isInfinite)
		{
			while(!Stop)
			{
				try {
					Thread.sleep(timeBetween);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				switch (selectedButton) 
				{		
				case "Space ":
					robot.keyPress(KeyEvent.VK_SPACE);
					robot.keyRelease(KeyEvent.VK_SPACE);
					break;
				case "Space":
					robot.keyPress(KeyEvent.VK_SPACE);
					robot.keyRelease(KeyEvent.VK_SPACE);
					break;
				case "Ctrl":
					robot.keyPress(KeyEvent.VK_CONTROL);
					robot.keyRelease(KeyEvent.VK_CONTROL);
					break;
				case "ALT":
					robot.keyPress(KeyEvent.VK_ALT);
					robot.keyRelease(KeyEvent.VK_ALT);
					break;
				case "SHIFT":
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case "ESc":
					robot.keyPress(KeyEvent.VK_ESCAPE);
					robot.keyRelease(KeyEvent.VK_ESCAPE);
					break;
				case "DEL":
					robot.keyPress(KeyEvent.VK_DELETE);
					robot.keyRelease(KeyEvent.VK_DELETE);
					break;
				case "WIn":
					robot.keyPress(KeyEvent.VK_WINDOWS);
					robot.keyRelease(KeyEvent.VK_WINDOWS);
					break;
				default:
					if (Character.isUpperCase(c) && indexOfChar <= 58)
						robot.keyPress(KeyEvent.VK_SHIFT);
					if (indexOfChar > 58)
					{
						if (indexOfChar < 77)
						{
							int pom = (indexOfChar - 58)/2;
							robot.keyPress(keyOfChar[indexOfChar - pom]);
							robot.keyRelease(keyOfChar[indexOfChar - pom]);
						}
						else if (indexOfChar == 79)
						{
							robot.keyPress(keyOfChar[68]);
							robot.keyRelease(keyOfChar[68]);
						}
						else
						{
							robot.keyPress(keyOfChar[69]);
							robot.keyRelease(keyOfChar[69]);
						}
						
					}
					else
					{
						robot.keyPress(keyOfChar[indexOfChar]);
						robot.keyRelease(keyOfChar[indexOfChar]);
					}
					if (Character.isUpperCase(c) && indexOfChar <= 58)
						robot.keyRelease(KeyEvent.VK_SHIFT);
					break;
				}
			}
		}
		else
		for (int i = 1; i <= powtarzanie; i++)
		{
			if (Stop)
				return;
			try {
				Thread.sleep(timeBetween);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch (selectedButton) 
			{		
			case "Space ":
				robot.keyPress(KeyEvent.VK_SPACE);
				robot.keyRelease(KeyEvent.VK_SPACE);
				break;
			case "Space":
				robot.keyPress(KeyEvent.VK_SPACE);
				robot.keyRelease(KeyEvent.VK_SPACE);
				break;
			case "Ctrl":
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				break;
			case "ALT":
				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyRelease(KeyEvent.VK_ALT);
				break;
			case "SHIFT":
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyRelease(KeyEvent.VK_SHIFT);
				break;
			case "ESc":
				robot.keyPress(KeyEvent.VK_ESCAPE);
				robot.keyRelease(KeyEvent.VK_ESCAPE);
				break;
			case "DEL":
				robot.keyPress(KeyEvent.VK_DELETE);
				robot.keyRelease(KeyEvent.VK_DELETE);
				break;
			case "WIn":
				robot.keyPress(KeyEvent.VK_WINDOWS);
				robot.keyRelease(KeyEvent.VK_WINDOWS);
				break;
			default:
				if (Character.isUpperCase(c) && indexOfChar <= 58)
					robot.keyPress(KeyEvent.VK_SHIFT);
				if (indexOfChar > 58)
				{
					if (indexOfChar < 77)
					{
						int pom = (indexOfChar - 58)/2;
						robot.keyPress(keyOfChar[indexOfChar - pom]);
						robot.keyRelease(keyOfChar[indexOfChar - pom]);
					}
					else if (indexOfChar == 79)
					{
						robot.keyPress(keyOfChar[68]);
						robot.keyRelease(keyOfChar[68]);
					}
					else
					{
						robot.keyPress(keyOfChar[69]);
						robot.keyRelease(keyOfChar[69]);
					}
					
				}
				else
				{
					robot.keyPress(keyOfChar[indexOfChar]);
					robot.keyRelease(keyOfChar[indexOfChar]);
				}
				if (Character.isUpperCase(c) && indexOfChar <= 58)
					robot.keyRelease(KeyEvent.VK_SHIFT);
				break;
			}
		}
		
		
		
		
	}
	public String getDescription(boolean isMyszka)
	{
		
		
		
				
		if (isMyszka)
		{
			int lengthOfCoords = new String(Integer.toString(xCoords) + Integer.toString(yCoords)).length();
			String numerOfSpaces[] = {"", "              ", "            ", "        ", "       ", "     ","   ",""}; // Tablica która dopasowywuje liczbe spacji do liczby znaków wspó³rzêdnych tak by w JList by³y one wyœwietlane w odpowiednch miejscach pod sob¹
			return new String(" Mysz                                    " + xCoords + "|" + yCoords + "                                          " + numerOfSpaces[lengthOfCoords -1] + timeToExecute);
		}
			
		else
		{
			if (selectedButton.equals("Space "))
				selectedButton = "Space";
			int lengthOfButtonChars = selectedButton.length();
			if (selectedButton.equals("SHIFT"))
				lengthOfButtonChars = 0;
			String tableOfSpaces[] = {"  ", "           ", "         ", "     ", "      ", ""};

			return new String(" Klawiatura                         " + selectedButton + "                                                  " + tableOfSpaces[lengthOfButtonChars] +  timeToExecute);
		}
			
	}
	public boolean getPrzyciskMyszy()
	{
		return przyciskMyszy;
	}
	public boolean getDBClick()
	{
		return podwojnyKlik;
	}
	public int getXCoords()
	{
		return xCoords;
	}
	public int getYCoords()
	{
		return yCoords;
	}
	public int getTimeToExecute()
	{
		return timeToExecute;
	}
	public boolean getIsRepeatTrue()
	{
		return repeat;
	}
	public int getTimeBetween()
	{
		return timeBetween;
	}
	public boolean getIsInfinite()
	{
		return isInfinite;
	}
	public String getSelectedButton()
	{
		return selectedButton;
	}
	public int getHowManyTimesToRepeat()
	{
		return powtorzenia;
	}
	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) 
	{
		if (arg0.getKeyCode() == NativeKeyEvent.VC_INSERT) 
		{
			Stop = true;
            try {
				GlobalScreen.unregisterNativeHook();
			} catch (NativeHookException e) 
            {
				
			} 
		}
	}
	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
