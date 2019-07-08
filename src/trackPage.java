import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class trackPage extends JPanel {

   private JPanel shadow,main;
   private JButton[] track = new JButton[10];
   private JButton logOut;
   int    i=0,trackNum=1;
   private ButtonListener btnL;
   private String jdbcUrl = "jdbc:mysql://203.250.148.53/Camping?useSSL=false";
   private String jdbcDriver = "com.mysql.jdbc.Driver";
   private String strUser = "16011087";
   private String strPassword = "16011087";
   private Connection conn;
   private PreparedStatement pstmt;
   private ResultSet rs;
   private String userID;
   private JLabel lblBasic, lblDeep, lblTrack, lblComplete,compImg;
   private JTable myCompletion;
   private JScrollPane jScrollPane;
   private ArrayList<SubjectVO> subList = new ArrayList<SubjectVO>();

   
   protected final JProgressBar progress1 = new JProgressBar() {
        @Override public void updateUI() {
            super.updateUI();
            setUI(new ProgressCircleUI());
            setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        }
    };
    
    protected final JProgressBar progress2 = new JProgressBar() {
        @Override public void updateUI() {
            super.updateUI();
            setUI(new ProgressCircleUI());
            setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        }
    };
    
   public trackPage(final MainFrame frame,String id) {
      
      btnL = new ButtonListener();
      setPreferredSize(new Dimension(1200, 700));
      setBounds(0,200,1200,700); 
      setLayout(null);
      userID=id;
      main = new JPanel();
      main.setBounds(20,20,980,630);
      main.setBackground(Color.white);
      main.setLayout(null);
      add(main);
      
      lblTrack = new JLabel("HCI&Visual Com");
      lblTrack.setFont(new Font("Arial",Font.BOLD,30));
      lblTrack.setHorizontalAlignment(SwingConstants.CENTER);
      lblTrack.setVerticalAlignment(SwingConstants.CENTER);
      lblTrack.setBounds(240, 30, 500, 30);
      main.add(lblTrack);
      
      main.add(progress1);
      progress1.setBounds(150, 60, 200, 200);
      
      lblBasic = new JLabel("Basic Percent");
      lblBasic.setFont(new Font("Arial",Font.BOLD,18));
      lblBasic.setVerticalAlignment(SwingConstants.CENTER);
      lblBasic.setBounds(350, 60, 140, 200);
      main.add(lblBasic);

      main.add(progress2);
      progress2.setBounds(490, 60, 200, 200);

      lblDeep = new JLabel("Deep Percent");
      lblDeep.setFont(new Font("Arial",Font.BOLD,18));
      lblDeep.setVerticalAlignment(SwingConstants.CENTER);
      lblDeep.setBounds(690, 60, 140, 200);
      main.add(lblDeep);
      
      lblComplete = new JLabel("Completed ");
      lblComplete.setFont(new Font("Arial",Font.BOLD,15));
      lblComplete.setHorizontalAlignment(SwingConstants.RIGHT);
      lblComplete.setVerticalAlignment(SwingConstants.CENTER);
      lblComplete.setBounds(700, 260, 160, 50);
      main.add(lblComplete);
      
      compImg = new JLabel(new ImageIcon("./image/check.png"));
      compImg.setHorizontalAlignment(SwingConstants.CENTER);
      compImg.setVerticalAlignment(SwingConstants.CENTER);
      compImg.setBounds(880, 260, 30, 50);
      compImg.setVisible(false);
      main.add(compImg);
      
      myCompletion = new JTable();
      myCompletion.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
      myCompletion.getTableHeader().setPreferredSize(new Dimension(myCompletion.getWidth(),50));
      myCompletion.setFont(new Font("Arial", Font.PLAIN, 15));
      myCompletion.setRowHeight(40);
      
      jScrollPane = new JScrollPane(myCompletion);//jScrollPaneRental create and add jTableRental
      jScrollPane.setBounds(40,300, 900, 300);
      main.add(jScrollPane);//add CinfoPanel
      
      showMySubject(trackNum);
      
      track[0] = new JButton("HCI&Visual Com");
      track[1] = new JButton("MultiMedia");
      track[2] = new JButton("IoT");
      track[3] = new JButton("System App");
      track[4] = new JButton("AI");
      track[5] = new JButton("VR");
      track[6] = new JButton("Info Security");
      track[7] = new JButton("Data Science");
      track[8] = new JButton("SW Education");
      track[9] = new JButton("Cyber Defense");
      
      for( i=0; i<10; i++ ) {
         track[i].setBounds(1000, 50*i+40, 170, 50);
         track[i].setFont(new Font("Arial",Font.PLAIN,18));
         track[i].setForeground(Color.white);
         track[i].setHorizontalAlignment(SwingConstants.CENTER);
         track[i].addActionListener(btnL);
         if( i%2 == 0)
             track[i].setBackground(new Color(0x222D65));
          else
             track[i].setBackground(new Color(0x253B80));
         track[i].setFocusPainted(false);
         track[i].setBorderPainted(false);
         add(track[i]);
      }

      track[0].doClick();
      
      logOut = new JButton("logout");
      logOut.setBounds(1000, 540, 170, 50);
      logOut.setHorizontalAlignment(SwingConstants.CENTER);
      logOut.setBackground(Color.white);
      logOut.setForeground(new Color(0x222D65));
      logOut.setFocusPainted(false);
      logOut.setBorderPainted(false);
      logOut.setFont(new Font("Arial",Font.PLAIN,18));
      logOut.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
           // TODO Auto-generated method stub
           frame.Change("logOut",null);
        }     
      });
      add(logOut);
      
      shadow = new JPanel();
      shadow.setBounds(25,25,980,630);
      shadow.setBackground(Color.LIGHT_GRAY);
      add(shadow);
   }
   public void showMySubject(int track) {
      
      DefaultTableModel modelSubject = new DefaultTableModel()
       { public boolean isCellEditable(int i, int c){ return false; } }; // no editable 
       
      Object[] columnsName = new Object[3];
      
      columnsName[0] = "Subject Name";
      columnsName[1] = "Level";
      columnsName[2] = "IsComplete";
       
      modelSubject.setColumnIdentifiers(columnsName);

      ArrayList<SubjectVO> addSubjectVO = subjectlist(track);
      Object[] rowData = new Object[3];
      // set row values with rental array list
      for (int i = 0; i < addSubjectVO.size(); i++) {
         rowData[0] = addSubjectVO.get(i).getName();
         if( addSubjectVO.get(i).getFlag()==1 )
            rowData[1] = "Basic";
         else
            rowData[1] = "Deep";
         if(addSubjectVO.get(i).getComplete()==1)
            rowData[2] = "Complete";
         else
            rowData[2] = "";
         modelSubject.addRow(rowData);
        }
      
      myCompletion.setModel(modelSubject);
      
      TableColumnModel columnModel = myCompletion.getColumnModel();
      columnModel.getColumn(0).setPreferredWidth(500);
      columnModel.getColumn(1).setPreferredWidth(200);
      columnModel.getColumn(2).setPreferredWidth(200);
      
      DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

      tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

      for (int i = 0; i < columnModel.getColumnCount(); i++) {

         columnModel.getColumn(i).setCellRenderer(tScheduleCellRenderer);

      }
      
      myCompletion.setModel(modelSubject);   
   }
   private void ProgressOpen(int max1,int max2) {
      
     
      if( max1 == 0 )
         progress1.setString("0%");
      else
         progress1.setString(null);
      
      if( max2 == 0 )
         progress2.setString("0%");
      else
         progress2.setString(null);
      
       progress1.setStringPainted(true);
       progress1.setForeground(new Color(0x0078d7));
       progress1.setBackground(null);
       progress1.setFont(new Font("Arial",Font.BOLD,15));
       progress1.setMaximum(max1);
     
       SwingWorker<String, Void> worker = new BackgroundTask() {
           @Override public void done() {}
       };
       worker.addPropertyChangeListener(new ProgressListener(progress1));
       worker.execute();

       lblBasic.setVisible(true);
       
       progress2.setStringPainted(true);
       progress2.setForeground(new Color(0x0078d7));
       progress2.setBackground(null);
       progress2.setFont(new Font("Arial",Font.BOLD,15));
       progress2.setMaximum(max2);

       worker.addPropertyChangeListener(new ProgressListener(progress2));
       worker.execute();

       lblDeep.setVisible(true);
   }
   public void connectDB() {
         try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(jdbcUrl, strUser, strPassword);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   public ArrayList<SubjectVO> subjectlist(int track) {
      String sql = "select * from Subject where SubjectNum in (select SubjectNum from Enroll where TrackNum=?)";
      connectDB();
      PreparedStatement pstmt = null;
         ArrayList<SubjectVO> arrSubjectVO = new ArrayList<SubjectVO>();
         try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, track);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
               SubjectVO tempSubjectVO = new SubjectVO();
               tempSubjectVO.setName(rs.getString("Name"));
               arrSubjectVO.add(tempSubjectVO);
            }

         } catch (SQLException e) {
            e.printStackTrace();
         } finally {
            try {
               if (pstmt != null && !pstmt.isClosed())
                  pstmt.close();
            } catch (SQLException e) {
               e.printStackTrace();
            }
         }

         for( int i=0; i<arrSubjectVO.size(); i++ ) {
            sql = "select * from Enroll where TrackNum=? and SubjectNum in ( select SubjectNum from Subject where Name=?)";
            try {
               pstmt = conn.prepareStatement(sql);
               pstmt.setInt(1, track);
               pstmt.setString(2, arrSubjectVO.get(i).getName());
               ResultSet rs = pstmt.executeQuery(); 
               while (rs.next()) {
                  arrSubjectVO.get(i).setFlag(rs.getInt("IsBasic"));
                     }
            } catch (SQLException e) {
            e.printStackTrace();
         } finally {
            try {
               if (pstmt != null && !pstmt.isClosed())
                  pstmt.close();
               } catch (SQLException e) {
               e.printStackTrace();
               }
            }
         }  
         
         int i=0;
         sql = "select * from Subject where SubjectNum in (select SubjectNum from Completion where StudentNum=?)";
         try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userID);
            ResultSet rs = pstmt.executeQuery(); 
            while (rs.next()) {
               for( i=0 ; i<arrSubjectVO.size(); i++ )
                  if( arrSubjectVO.get(i).getName().equals(rs.getString("Name")))
                     arrSubjectVO.get(i).setComplete(1);
          }
         } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         try {
            if (pstmt != null && !pstmt.isClosed())
               pstmt.close();
            } catch (SQLException e) {
            e.printStackTrace();
            }
         }

       return arrSubjectVO;
   
   }
   private void Calc(String trackName) {
      int count1=0;
      int count2=0;
      String sql1 = "select count(*) from Completion where StudentNum='"+userID+"' and subjectNum in (select subjectNum from Enroll where IsBasic=1 and TrackNum in (select TrackNum from Track where Name= '"+trackName+"'))";
      String sql2 = "select count(*) from Completion where StudentNum='"+userID+"' and subjectNum in (select subjectNum from Enroll where IsBasic=0 and TrackNum in (select TrackNum from Track where Name= '"+trackName+"'))";
      connectDB();
      try {
           pstmt = conn.prepareStatement(sql1);
           rs = pstmt.executeQuery();
           while(rs.next())
              count1=rs.getInt("count(*)");
           pstmt = conn.prepareStatement(sql2);
           rs = pstmt.executeQuery();
           while(rs.next())
              count2=rs.getInt("count(*)");
        } catch (SQLException e) {
           e.printStackTrace();
        }
      if( count1==3 && count2 >=6 ) {
         lblComplete.setText("Completed");
         compImg.setVisible(true);
      }
      else if( count2 < 6 ){
         lblComplete.setText("Deep Status : "+count2);
         compImg.setVisible(false);
      }
      switch(trackName) {
      case "HCI&VisualComputing" :
         if(count1!=0 && count2!=0)
            ProgressOpen(300/count1, 800/count2 ); 
         else if(count1!=0)
            ProgressOpen(300/count1,0);
         else if(count2!=0)
            ProgressOpen(0,800/count2);
         else ProgressOpen(0,0);
         break;
      case "Multimedia" :
         if(count1!=0 && count2!=0)
            ProgressOpen(300/count1, 800/count2 ); 
         else if(count1!=0)
            ProgressOpen(300/count1,0);
         else if(count2!=0)
            ProgressOpen(0,800/count2);
         else ProgressOpen(0,0);
         break;
      case "IoT" :
         if(count1!=0 && count2!=0)
            ProgressOpen(300/count1, 900/count2 ); 
         else if(count1!=0)
            ProgressOpen(300/count1,0);
         else if(count2!=0)
            ProgressOpen(0,900/count2);
         else ProgressOpen(0,0);
         break;
      case "SystemApplication" :
         if(count1!=0 && count2!=0)
            ProgressOpen(300/count1, 900/count2 ); 
         else if(count1!=0)
            ProgressOpen(300/count1,0);
         else if(count2!=0)
            ProgressOpen(0,900/count2);
         else ProgressOpen(0,0);
         break;
      case "AI" :
         if(count1!=0 && count2!=0)
              ProgressOpen(300/count1, 1100/count2 ); 
           else if(count1!=0)
              ProgressOpen(300/count1,0);
           else if(count2!=0)
              ProgressOpen(0,1100/count2);
           else ProgressOpen(0,0);
         break;
      case "VR" :
         if(count1!=0 && count2!=0)
              ProgressOpen(300/count1, 800/count2 ); 
           else if(count1!=0)
              ProgressOpen(300/count1,0);
           else if(count2!=0)
              ProgressOpen(0,800/count2);
           else ProgressOpen(0,0);
         break;
      case "InformationProtection" :
         if(count1!=0 && count2!=0)
              ProgressOpen(300/count1, 700/count2 ); 
           else if(count1!=0)
              ProgressOpen(300/count1,0);
           else if(count2!=0)
              ProgressOpen(0,700/count2);
           else ProgressOpen(0,0);
         break;
      case "DataScience" :
         if(count1!=0 && count2!=0)
              ProgressOpen(300/count1, 900/count2 ); 
           else if(count1!=0)
              ProgressOpen(300/count1,0);
           else if(count2!=0)
              ProgressOpen(0,900/count2);
           else ProgressOpen(0,0);
         break;
      case "SWEducation" :
         if(count1!=0 && count2!=0)
              ProgressOpen(300/count1, 700/count2 ); 
           else if(count1!=0)
              ProgressOpen(300/count1,0);
           else if(count2!=0)
              ProgressOpen(0,700/count2);
           else ProgressOpen(0,0);
         break;
      case "CyberDefense" :
         if(count1!=0 && count2!=0)
              ProgressOpen(1200/count1, 0/count2 ); 
           else if(count1!=0)
              ProgressOpen(1200/count1,0);
           else if(count2!=0)
              ProgressOpen(0,0/count2);
           else ProgressOpen(0,0);
         break;
      }
      
   }
   private class ButtonListener implements ActionListener {
         private String getTextValue;

         public void actionPerformed(ActionEvent event) {
           JButton b = (JButton) event.getSource();
           String menu = b.getText();
            switch(menu) {
            case "HCI&Visual Com" :
               Calc("HCI&VisualComputing"); trackNum=1; showMySubject(trackNum); lblTrack.setText("HCI&Visual Com");
               break;
            case "MultiMedia" :
               Calc("Multimedia"); trackNum=2; showMySubject(trackNum); lblTrack.setText("MultiMedia");
               break;
            case "IoT":
               Calc("IoT"); trackNum=3; showMySubject(trackNum); lblTrack.setText("IoT");
                break;
            case "System App" :
               Calc("SystemApplication"); trackNum=4; showMySubject(trackNum); lblTrack.setText("System App");
               break;
            case "AI" :
               Calc("AI");trackNum=5; showMySubject(trackNum); lblTrack.setText("AI");
               break;
            case "VR":
               Calc("VR");trackNum=6; showMySubject(trackNum); lblTrack.setText("VR");
                break;
            case "Info Security":
               Calc("InformationProtection");trackNum=7; showMySubject(trackNum); lblTrack.setText("Info Security");
               break;
            case "Data Science":
               Calc("DataScience");trackNum=8; showMySubject(trackNum); lblTrack.setText("Data Science");
               break;
            case "SW Education":
               Calc("SWEducation");trackNum=9; showMySubject(trackNum); lblTrack.setText("SW Education");
               break;
            case "Cyber Defense":
               Calc("CyberDefense");trackNum=10; showMySubject(trackNum); lblTrack.setText("Cyber Defense");
               break;
            }
            
         }
      }
}

class ProgressCircleUI extends BasicProgressBarUI {
    @Override public Dimension getPreferredSize(JComponent c) {
        Dimension d = super.getPreferredSize(c);
        int v = Math.max(d.width, d.height);
        d.setSize(v, v);
        return d;
    }
    @Override public void paint(Graphics g, JComponent c) {
        Insets b = progressBar.getInsets(); // area for border
        int barRectWidth  = progressBar.getWidth()  - b.right - b.left;
        int barRectHeight = progressBar.getHeight() - b.top - b.bottom;
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double degree = 360 * progressBar.getPercentComplete();
        double sz = Math.min(barRectWidth, barRectHeight);
        double cx = b.left + barRectWidth  * .5;
        double cy = b.top  + barRectHeight * .5;
        double or = sz * .5;
        double ir = or * .5; //.8;
        Shape inner  = new Ellipse2D.Double(cx - ir, cy - ir, ir * 2, ir * 2);
        Shape outer  = new Ellipse2D.Double(cx - or, cy - or, sz, sz);
        Shape sector = new Arc2D.Double(cx - or, cy - or, sz, sz, 90 - degree, degree, Arc2D.PIE);

        Area foreground = new Area(sector);
        Area background = new Area(outer);
        Area hole = new Area(inner);

        foreground.subtract(hole);
        background.subtract(hole);

        // draw the track
        g2.setPaint(new Color(0xDDDDDD));
        g2.fill(background);

        g2.setPaint(progressBar.getForeground());
        g2.fill(foreground);
        g2.dispose();

        // Deal with possible text painting
        if (progressBar.isStringPainted()) {
            paintString(g, b.left, b.top, barRectWidth, barRectHeight, 0, b);
        }
    }
}

class BackgroundTask extends SwingWorker<String, Void> {
    private final Random rnd = new Random();
    @Override public String doInBackground() {
        int current = 0;
        int lengthOfTask = 100;
        while (current <= lengthOfTask && !isCancelled()) {
            try { // dummy task
                Thread.sleep(rnd.nextInt(5) + 1);
            } catch (InterruptedException ex) {
                return "Interrupted";
            }
            setProgress(100 * current / lengthOfTask);
            current++;
        }
        return "Done";
    }
}

class ProgressListener implements PropertyChangeListener {
    private final JProgressBar progressBar;
    protected ProgressListener(JProgressBar progressBar) {
        this.progressBar = progressBar;
        this.progressBar.setValue(0);
    }
    @Override public void propertyChange(PropertyChangeEvent e) {
        String strPropertyName = e.getPropertyName();
        if ("progress".equals(strPropertyName)) {
            progressBar.setIndeterminate(false);
            int progress = (Integer) e.getNewValue();
            progressBar.setValue(progress);
        }
    }
}