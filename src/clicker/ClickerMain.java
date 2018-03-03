package clicker;

import java.awt.EventQueue;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

public class ClickerMain {

	public static void main(String[] args) 
	{
		
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ClickerFrame frame = new ClickerFrame();
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
			}
		});
	}

}
