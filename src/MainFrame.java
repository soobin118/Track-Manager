
import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {

   private JLabel lblTitle,welcome;
   private JPanel title;
   private LoginPage login;
   private trackPage track;
   private ManagerPage   manager;
   private ImageIcon   titleImage;
   public int modeFlag; 
   
   public static void main(String[] args) {
      new MainFrame();
   }	

   public MainFrame() {
      setTitle("hackNTNtrack");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setPreferredSize(new Dimension(1200, 900));
      setBackground(Color.WHITE);
      setLayout(null);
      setResizable(false);
   
      title = new JPanel();
      title.setBounds(0,0,1200,200);
      title.setBackground(new Color(0x222D65));
      title.setLayout(null);
      add(title);
      
      titleImage = new ImageIcon("./image/titleImg.png");
      lblTitle = new JLabel(titleImage);
      lblTitle.setBounds(200, 0, 800, 200);
      title.add(lblTitle);
      
      welcome = new JLabel("");
      welcome.setBounds(1000, 160, 200, 40);
      welcome.setFont(new Font("Arial",Font.PLAIN,18));
      welcome.setForeground(Color.white);
      welcome.setHorizontalAlignment(SwingConstants.LEFT);
      welcome.setVerticalAlignment(SwingConstants.CENTER);
      title.add(welcome);
      
      login = new LoginPage(this);     
   
      getContentPane().add(login); 

      pack();
      setVisible(true);
   }
   
   public void Change(String PanelName,String ID) {

      System.out.println(ID);
      if( PanelName.equals("Access")) {
       if( ID.equals("root")) {
          login.setVisible(false);
          manager = new ManagerPage(this);
             getContentPane().add(manager);
             modeFlag=1;
       }
       else {
             login.setVisible(false);
             track = new trackPage(this,ID);
             getContentPane().add(track);
             modeFlag=0;
       }  
       welcome.setText("Welcome, "+ID);
         
      }
      else if( PanelName.equals("logOut")) {
         if( modeFlag==1) 
             manager.setVisible(false);
         else
            track.setVisible(false);
          login = new LoginPage(this);     
          getContentPane().add(login); 
          
          welcome.setText("");

      }
      pack();
      setVisible(true);
   }
   
}