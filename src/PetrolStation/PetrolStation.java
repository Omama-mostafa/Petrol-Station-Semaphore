package PetrolStation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

class Semaphore
{
	private int value;
	private static Random Rand = new Random();
	private boolean FreePump[];
	private Lock PumpLock;
	
	Semaphore(int PumpsNum)
	{
		value = PumpsNum;
		
		FreePump = new boolean[PumpsNum];
		for(int i = 0; i < PumpsNum; i++)
			FreePump[i] = true;
		PumpLock = new ReentrantLock();
	}
	
	public int getPump()
	{
		int foundPump = -1;
		try
		{
			PumpLock.lock();
			for(int i = 0; i < FreePump.length; i++)
			{
				if(FreePump[i])
				{
					foundPump = i;
					FreePump[i] = false;
					break;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			PumpLock.unlock();
		}
		return foundPump;
	}
	
	public void ReleasePump(int i)
	{
		PumpLock.lock();
		FreePump[i] = true;
		PumpLock.unlock();
	}
	
	synchronized void Wait(String CustName)
	{
		boolean BeWait = false;
		PetrolStation.PetrolStation.Main M = new PetrolStation.PetrolStation.Main();
		while(value <= 0)
		{
			try
			{
				sleep(Rand.nextInt(7000 - 1000) + 1000);
				System.out.println(CustName + " Arrived and waiting.");
				M.Get_String("\n" + CustName + " Arrived and waiting.");
				BeWait = true;
				wait();
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		if(!BeWait)
		{
			System.out.println(CustName + " Arrived.");
			M.Get_String("\n" + CustName + " Arrived.");
		}
		value--;
	}
	
	synchronized void Signal()
	{
		if(value <= 0)
		{
			notify();
		}
		value++;
	}
}
class Threads extends Thread
{
	private static Random Rand = new Random();
	private String Cust_name;
	private Semaphore Pump;
	
	//private JTextArea textArea_1 = new JTextArea();
	Threads(Semaphore S_Pump, String name)
	{
		this.Cust_name = name;
		this.Pump = S_Pump;
	}
	
	public void run()
	{
		PetrolStation.PetrolStation.Main M = new PetrolStation.PetrolStation.Main();
		Pump.Wait(Cust_name);
		try
		{
			int pumpID = Pump.getPump();
			sleep(Rand.nextInt(7000 - 1000) + 1000);
			System.out.println("Pump " + (pumpID + 1) + " : " + Cust_name + " Occupied.");
			M.Get_String("Pump " + (pumpID + 1) + " : " + Cust_name + " Occupied.");
			sleep(Rand.nextInt(7000 - 1000) + 1000);
			System.out.println("Pump " + (pumpID + 1) + " : " + Cust_name + " Being Served.");
			M.Get_String("Pump " + (pumpID + 1) + " : " + Cust_name + " Being Served.");
			sleep(Rand.nextInt(7000 - 1000) + 1000);
			System.out.println("Pump " + (pumpID + 1) + " : " + Cust_name + " Paying.");
			M.Get_String("Pump " + (pumpID + 1) + " : " + Cust_name + " Paying.");
			sleep(Rand.nextInt(7000 - 1000) + 1000);
			System.out.println("Pump " + (pumpID + 1) + " : " + Cust_name + " Leave.");
			M.Get_String("Pump " + (pumpID + 1) + " : " + Cust_name + " Leave.");
			Pump.ReleasePump(pumpID);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		finally
		{
			Pump.Signal();
		}
	}
}

class Main
{
	private JFrame frame1;
	private JFrame frame2;
	
	private int PumpsNum = 0;
	private int CustNum = 0;
	private ArrayList<String> CustName = new ArrayList<>();
	
	private JTextArea textArea_1;
	
	public void Get_Main()
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Main window = new Main();
					window.frame1.setVisible(true);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	Main()
	{
		initialize_Form1();
		initialize_Form2();
	}
	
	private void initialize_Form1()
	{
		frame1 = new JFrame("Start");
		frame1.setBounds(50, 50, 600, 600);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.getContentPane().setLayout(null);
		
		JLabel lblWel = new JLabel("WelCome To Petrol Station");
		lblWel.setBounds(200, 70, 200, 80);
		frame1.getContentPane().add(lblWel);
		
		JLabel lblP = new JLabel("Enter Pumps Number : ");
		lblP.setBounds(50, 150, 200, 80);
		frame1.getContentPane().add(lblP);
		
		JTextField textField_1 = new JTextField();
		textField_1.setBounds(210, 180, 200, 25);
		frame1.getContentPane().add(textField_1);
		
		JLabel lblC = new JLabel("Enter Customer Number : ");
		lblC.setBounds(50, 250, 200, 80);
		frame1.getContentPane().add(lblC);
		
		JTextField textField_2 = new JTextField();
		textField_2.setBounds(210, 280, 200, 25);
		frame1.getContentPane().add(textField_2);
		
		JLabel lblN = new JLabel("Enter Customers Name : ");
		lblN.setBounds(50, 350, 200, 80);
		frame1.getContentPane().add(lblN);
		
		JTextField textField_3 = new JTextField();
		textField_3.setBounds(210, 380, 200, 25);
		frame1.getContentPane().add(textField_3);
		
		JButton btnSubmit1 = new JButton("Continue...");
		btnSubmit1.setBounds(440, 420, 100, 30);
		frame1.getContentPane().add(btnSubmit1);
		
		btnSubmit1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				PumpsNum = Integer.parseInt(textField_1.getText());
				CustNum = Integer.parseInt(textField_2.getText());
				
				String[] S;
				String Cust = textField_3.getText();
				S = Cust.split(" ");
				for(int i = 0; i < CustNum; i++)
				{
					CustName.add(S[i]);
				}
				frame2.setVisible(true);
				frame1.setVisible(false);
			}
		});
		
	}
	
	public void Get_String(String Str)
	{
		textArea_1.append(Str);
	}
	
	
	private void initialize_Form2()
	{
		frame2 = new JFrame("Customers Status");
		frame2.setBounds(50, 50, 600, 600);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.getContentPane().setLayout(null);
		
		JButton btnSubmit1 = new JButton("Show Status.");
		btnSubmit1.setBounds(50, 50, 150, 30);
		frame2.getContentPane().add(btnSubmit1);
		
		textArea_1 = new JTextArea();
		textArea_1.setBounds(100, 100, 400, 400);
		frame2.getContentPane().add(textArea_1);
		
		btnSubmit1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				for(int i = 0; i < CustNum; i++)
				{
					Semaphore P = new Semaphore(PumpsNum);
					Threads th = new Threads(P, CustName.get(i));
					th.start();
				}
			}
		});
	}
}

public class PetrolStation
{
	public static void main(String[] args)
	{
		Main M = new Main();
		M.Get_Main();
	}
}
