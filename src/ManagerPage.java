import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.*;

public class ManagerPage extends JPanel {
      
   private JPanel shadow,main;
   private String[] level= {"Basic","Deep"};
   private JPanel[] panels = new JPanel[4];
   private JButton[] track = new JButton[3];
   private JButton   logOut;
   private String[] list= {"HCI&Visual Computing","MultiMedia","Internet of Things","System Application","Artificial Intelligent","Virtual Reality","Information Security","Data Science","SW Education","Cyber Difense"};
   private JComboBox trackList,selectLevel;
   private int i,flag=0;
   private ButtonListener btnL;
   private String jdbcUrl = "jdbc:mysql://203.250.148.53/Camping?useSSL=false";
   private String jdbcDriver = "com.mysql.jdbc.Driver";
   private String strUser = "16011087";
   private String strPassword = "16011087";
   private Connection conn;
   private PreparedStatement pstmt;
   private ResultSet rs;
   private String trackName;;
   private JScrollPane jScollPane,scrollBar;
   private JTextField txtTrack,txtSubject,txtSelectTrack;
   private JButton btnInsert, btnDelete, btnInsertS, btnDeleteS,btnInsertCategory,btnDeleteCategory;
   private JProgressBar[] progressBar;
   private JLabel[]   subList;
   private JPanel showPercen;

   public ManagerPage( final MainFrame frame) {
   
      btnL = new ButtonListener();
      setPreferredSize(new Dimension(1200, 700));
       setBounds(0,200,1200,700); 
       setLayout(null);
         
       main = new JPanel();
       main.setBounds(20,20,980,630);
       main.setBackground(Color.white);
       main.setLayout(null);
       add(main);
       
       btnInsert = new JButton("Insert");
       btnDelete = new JButton("Delete");
       btnInsertS = new JButton("InsertS");
       btnDeleteS = new JButton("DeleteS");
       btnInsertCategory = new JButton("InsertCategory");
       btnDeleteCategory = new JButton("DeleteCategory");

       
       track[0] = new JButton("Completion");
       track[1] = new JButton("Manage_Track");
       track[2] = new JButton("Manage_Subject");
       
       for( i=0; i<3; i++ ) {
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
            
            panels[i] = new JPanel();
            panels[i].setBounds(0,0,980,630);
            panels[i].setBackground(Color.white);
            panels[i].setLayout(null);
            panels[i].setVisible(false);
            main.add(panels[i]);
         }
       panels[3] = new JPanel();
       panels[3].setBounds(0,0,980,630);
       panels[3].setBackground(Color.white);
       panels[3].setLayout(null);
       panels[3].setVisible(false);
       main.add(panels[3]);

       logOut = new JButton("logout");
       logOut.setBounds(1000, 190, 170, 50);
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
       
       showPercen = new JPanel();
       showPercen.setBounds(0, 120, 980, 630);
       showPercen.setBackground(Color.WHITE);
       showPercen.setLayout(null);
       scrollBar = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
       scrollBar.setViewportView(showPercen);
      scrollBar.setPreferredSize(new Dimension(980,530));
       scrollBar.setBounds(0, 100, 980, 530);
       scrollBar.getViewport().setBackground(Color.WHITE);
       panels[0].add(scrollBar);
       
       trackList = new JComboBox(list);
       trackList.setBackground(Color.WHITE);
       trackList.setBounds(40,40,900,50);
       trackList.setSize(900, 50);
       trackList.setFont(new Font("Arial",Font.PLAIN,20));
       ((JLabel)trackList.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
       trackList.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
         // TODO Auto-generated method stub
              subjectlist(trackList.getSelectedIndex()+1);
      }
       });
       panels[0].add(trackList);
       
      
       shadow = new JPanel();
       shadow.setBounds(25,25,980,630);
       shadow.setBackground(Color.LIGHT_GRAY);
       add(shadow);
       
       btnInsert.addActionListener(btnL);
       btnDelete.addActionListener(btnL);
       btnInsertS.addActionListener(btnL);
       btnDeleteS.addActionListener(btnL);

       track[0].doClick();
   }

   public void connectDB() {
       try {
          Class.forName(jdbcDriver);
          conn = DriverManager.getConnection(jdbcUrl, strUser, strPassword);
       } catch (Exception e) {
          e.printStackTrace();
       }
    }
   public ArrayList<TrackVO> getAttri() { // join하여 테이블을 보여주기 위한 메소드
      String sql = "select Name from Track";
      PreparedStatement pstmt = null;
      ArrayList<TrackVO> result=new ArrayList<TrackVO>();
      connectDB();
      try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) { // 테이블에서 애트리뷰트마다 형식에 맡게 값을 받아와서 각 클래스에 값을 넣어주는 부분
               TrackVO comVO = new TrackVO(rs.getString("Name"));
               result.add(comVO);
            }
         } catch(SQLException e) {
            e.printStackTrace();
         } finally {
            try {
               if (pstmt != null && !pstmt.isClosed())
                  pstmt.close();
            }catch(SQLException e) {
               e.printStackTrace();
            }
         }
      return result;
   }
   private int Calc(String SName) {
       int count1=0;
       int count2=0;
       String sql1 = "select count(*) from Completion group by SubjectNum having SubjectNum= (select SubjectNum from Subject where Name= '"+SName+"')";
       String sql2 = "select count(*) from Student";
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
       if(count1!=0 && count2!=0)
             return (count1*100/count2);
          else return 0;
       
    }

   private void subjectlist(int track) {
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
            if(progressBar!=null) {
               for(int i=0;i<progressBar.length;i++)
                  panels[0].remove(progressBar[i]);}
            if(subList!=null) {
               for(int i=0;i<subList.length;i++)
                  panels[0].remove(subList[i]);
               System.out.print(subList.length+"\n");
            }
            
            showPercen.removeAll();
            panels[0].setVisible(false);       
            panels[0].remove(showPercen);

            progressBar = new JProgressBar[arrSubjectVO.size()];
            subList = new JLabel[arrSubjectVO.size()];
            
            for( int i=0; i<arrSubjectVO.size(); i++ ) {
               progressBar[i] = new JProgressBar(0, 100);
               progressBar[i].setValue(Calc(arrSubjectVO.get(i).getName()));
               progressBar[i].setStringPainted(true);
               progressBar[i].setBackground(null);
               progressBar[i].setForeground(new Color(0x253B80));
               progressBar[i].setBounds(350, 45*i, 500, 30);
               
               subList[i] = new JLabel(arrSubjectVO.get(i).getName());
              // System.out.println("hi   " +arrSubjectVO.get(i).getName()); // 체크하기 위해 추가한거
               subList[i].setBounds(120, 45*i, 250, 30);
               subList[i].setFont(new Font("Arial",Font.PLAIN,15));
               subList[i].setVerticalAlignment(SwingConstants.CENTER);
               
               showPercen.add(subList[i]);
               showPercen.add(progressBar[i]);
            }
            panels[0].setVisible(true);
    }
   public ArrayList<TrackmnVO> allList() {
       String sql = "select Name from Track,Enroll where Track.TrackNum=Enroll.TrackNum";
       connectDB();
       PreparedStatement pstmt = null;
          ArrayList<TrackmnVO> arrTrackmnVO = new ArrayList<TrackmnVO>();
          try {
             pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();
             while (rs.next()) {
                TrackmnVO tempTrackmnVO = new TrackmnVO();
                tempTrackmnVO.setTrackName(rs.getString("Name"));
                arrTrackmnVO.add(tempTrackmnVO);
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

          int i=0;
          sql = "select Name from Subject,Enroll where Subject.SubjectNum=Enroll.SubjectNum";
             try {
                pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery(); 
                while (rs.next()) {
                   arrTrackmnVO.get(i).setSubName(rs.getString("Name"));
                   i++;
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
          i=0;
          sql = "select * from Enroll";
          try {
             pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery(); 
             while (rs.next()) {
                if(rs.getInt("IsBasic")==0)
                   arrTrackmnVO.get(i).setLevel("Basic");
                else
                   arrTrackmnVO.get(i).setLevel("Deep");
                i++;
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

        return arrTrackmnVO;
    
    }

   private void seeSubject() { 
       System.out.println(flag);
       JLabel title = new JLabel("Subject Manage");
       title.setBounds(400, 30, 200, 40);
       title.setFont(new Font("Arial",Font.BOLD,25));
       panels[1].add(title);
       
       btnInsertCategory.setBounds(30,100,460,140);
       btnInsertCategory.setBackground(new Color(0x253B80));
       btnInsertCategory.setFont(new Font("Verdana",Font.BOLD,20));
       btnInsertCategory.setForeground(Color.WHITE);
       
       btnInsertCategory.setVisible(true);
       btnDeleteCategory.setVisible(true);
       panels[1].add(btnInsertCategory);
       btnInsertCategory.addActionListener(btnL);
       
       
       btnDeleteCategory.setBounds(490, 100, 460, 140);
       btnDeleteCategory.setBackground(new Color(0x253B80));
       btnDeleteCategory.setFont(new Font("Verdana",Font.BOLD,20));
       btnDeleteCategory.setForeground(Color.WHITE);
       panels[1].add(btnDeleteCategory);
       btnDeleteCategory.addActionListener(btnL);
       
       
       JTable jTable = new JTable();
       DefaultTableModel model = new DefaultTableModel();
       
         Object[] columnsName = new Object[3];
         columnsName[0]="Track Name";
         columnsName[1]="Subject Name";
         columnsName[2]="Level";
         
         model.setColumnIdentifiers(columnsName);
         Object[] rowData = new Object[3];
        
         ArrayList<TrackmnVO> arrAllListVO = allList();

         for (int i = 0; i < arrAllListVO.size(); i++) {
            rowData[0] = arrAllListVO.get(i).getTrackName();
            rowData[1] = arrAllListVO.get(i).getSubName();
            rowData[2] = arrAllListVO.get(i).getLevel();
            model.addRow(rowData);
         }
       
         jTable.setModel(model);

         TableColumnModel columnModel = jTable.getColumnModel();
         columnModel.getColumn(0).setPreferredWidth(350);
         columnModel.getColumn(1).setPreferredWidth(450);
         columnModel.getColumn(2).setPreferredWidth(100);
         
         DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

         tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

         for (int i = 0; i < columnModel.getColumnCount(); i++) {

            columnModel.getColumn(i).setCellRenderer(tScheduleCellRenderer);

         }
         jTable.setModel(model);
         jTable.setPreferredScrollableViewportSize(jTable.getPreferredSize());
         jTable.setFillsViewportHeight(true);
         jTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
         jTable.getTableHeader().setPreferredSize(new Dimension(jTable.getWidth(),50));
         jTable.setFont(new Font("Arial", Font.PLAIN, 15));
         jTable.setRowHeight(40);
         jTable.getTableHeader().setReorderingAllowed(false);

         jScollPane = new JScrollPane(jTable);
         jScollPane.setPreferredSize(new Dimension(920, 340));
         jScollPane.setBounds(30, 260, 920, 340);
         panels[1].add(jScollPane);
      
   }

   private void seeTrack() {
          
          JLabel title = new JLabel("Track Manage");
          title.setBounds(400, 30, 200, 40);
          title.setFont(new Font("Arial",Font.BOLD,25));
          panels[2].add(title);
          
          txtTrack = new JTextField();
          txtTrack.setBounds(360, 100, 250, 50);
          txtTrack.setFont(new Font("Verdana",Font.BOLD,20));
          panels[2].add(txtTrack);
          
          btnInsert.setBounds(380, 180, 100, 50);
          btnInsert.setFont(new Font("Verdana",Font.BOLD,15));
          btnInsert.setBackground(new Color(0xEEEEEE));
          panels[2].add(btnInsert);
          btnDelete.setBounds(490, 180, 100, 50);
          btnDelete.setFont(new Font("Verdana",Font.BOLD,15));
          btnDelete.setBackground(new Color(0xEEEEEE));
          panels[2].add(btnDelete);
          
          JTable jTable = new JTable();
          DefaultTableModel model = new DefaultTableModel();
          
            Object[] columnsName = new Object[1];
            columnsName[0]="Track Name";
            model.setColumnIdentifiers(columnsName);
            Object[] rowData = new Object[1];
           
            ArrayList<TrackVO> arrJoinVO = getAttri();

            for (int i = 0; i < arrJoinVO.size(); i++) {
               rowData[0] = arrJoinVO.get(i).getTrackName();
               model.addRow(rowData);
            }
            jTable.setModel(model);
            
            TableColumnModel columnModel = jTable.getColumnModel();
            columnModel.getColumn(0);
            
            
            DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

            tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

            for (int i = 0; i < columnModel.getColumnCount(); i++) {

               columnModel.getColumn(i).setCellRenderer(tScheduleCellRenderer);

            }
            jTable.setModel(model);
            jTable.setPreferredScrollableViewportSize(jTable.getPreferredSize());
            jTable.setFillsViewportHeight(true);
            jTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
            jTable.getTableHeader().setPreferredSize(new Dimension(jTable.getWidth(),50));
            jTable.setFont(new Font("Arial", Font.PLAIN, 15));
            jTable.setRowHeight(40);
            jTable.getTableHeader().setReorderingAllowed(false);

            jScollPane = new JScrollPane(jTable);
            jScollPane.setPreferredSize(new Dimension(920, 340));
            jScollPane.setBounds(30, 250, 920, 320);
            panels[2].add(jScollPane);
         
      }
   public void insertFunc(String newTrackName) {
	      String sql = "INSERT INTO Track (TrackNum, Name) VALUES (NULL,?)";
	      PreparedStatement pstmt = null;
	      connectDB();
	      try {
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, newTrackName);
	            pstmt.executeUpdate();
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
	   public void insertFuncS(String TrackName,String newSubName,int level) {
	         String TrackNum=null,SubjectNum=null;
	         String sql = "INSERT INTO Subject (SubjectNum, Name) VALUES (NULL,?)";
	         
	         PreparedStatement pstmt = null;
	         connectDB();
	         try {
	               pstmt = conn.prepareStatement(sql);
	               pstmt.setString(1, newSubName);
	               pstmt.executeUpdate();
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
	        
	            sql = "select TrackNum from Track where Name=?";
	            pstmt = null;
	            try {
	               pstmt = conn.prepareStatement(sql);
	               pstmt.setString(1, TrackName);
	              ResultSet rs = pstmt.executeQuery();
	              while (rs.next()) {
	                 TrackNum = rs.getString("TrackNum");
	              }}catch (SQLException e) {
	              e.printStackTrace();
	              }finally {
	                 try {
	                     if (pstmt != null && !pstmt.isClosed())
	                        pstmt.close();
	                  } catch (SQLException e) {
	                     e.printStackTrace();
	                  }
	                 }
	            
	            sql = "select SubjectNum from Subject where Name=?";
	         pstmt = null;
	         try {
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, newSubName);
	             ResultSet rs = pstmt.executeQuery();
	             while (rs.next()) {
	                SubjectNum = rs.getString("SubjectNum");
	                }}
	         catch (SQLException e) {
	            e.printStackTrace();
	         }finally {
	            try {
	               if (pstmt != null && !pstmt.isClosed())
	                  pstmt.close();
	               } catch (SQLException e) {
	                  e.printStackTrace();
	                  }
	            }
	         
	         sql = "INSERT INTO Enroll (EnrollNum, TrackNum, SubjectNum,IsBasic) VALUES (NULL,?,?,?)";
	         pstmt = null;

	         try {
	               pstmt = conn.prepareStatement(sql);
	               pstmt.setString(1, TrackNum);
	               pstmt.setString(2, SubjectNum);
	               if( level==1)
	                  pstmt.setInt(3, 1);
	               else
	                  pstmt.setInt(3, 0);
	               pstmt.executeUpdate();
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

   public void deleteFunc(String TrackName) {
	      String sql = "DELETE FROM Track WHERE Name=?";
	      String sql2 = "DELETE FROM Enroll WHERE TrackNum in ( select TrackNum from Track Where Name=?)";
	      PreparedStatement pstmt = null;
	      PreparedStatement pstmt2 = null;
	      connectDB();
	      try {
	            pstmt2 = conn.prepareStatement(sql2);
	            pstmt2.setString(1, TrackName);
	            pstmt2.executeUpdate();
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, TrackName);
	            pstmt.executeUpdate();
	            
	         } catch (SQLException e) {
	         } finally {
	            try {
	               if (pstmt != null && !pstmt.isClosed())
	                  pstmt.close();
	            } catch (SQLException e) {
	            }
	         }
	      
	   }

   public void deleteSFunc(String subjectName) {
	      String sql = "DELETE FROM Subject WHERE Name=?";
	      String sql2 = "DELETE FROM Enroll WHERE SubjectNum in (Select SubjectNum from Subject where Name=?)";
	      String sql3 = "DELETE FROM Completion Where SubjectNum in (Select SubjectNum from Subject where Name=?)";
	      PreparedStatement pstmt = null;
	      PreparedStatement pstmt2 = null;
	      PreparedStatement pstmt3 = null;
	      connectDB();
	      try {
	             pstmt3 = conn.prepareStatement(sql3);
	            pstmt3.setString(1, subjectName);
	            pstmt3.executeUpdate();
	             pstmt2 = conn.prepareStatement(sql2);
	            pstmt2.setString(1, subjectName);
	            pstmt2.executeUpdate();
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, subjectName);
	            pstmt.executeUpdate();
	         } catch (SQLException e) {
	         } finally {
	            try {
	               if (pstmt != null && !pstmt.isClosed())
	                  pstmt.close();
	            } catch (SQLException e) {
	            }
	         }
	   }

   
   private class ButtonListener implements ActionListener {
       private String getTextValue;

       public void actionPerformed(ActionEvent event) {
         JButton b = (JButton) event.getSource();
         String menu = b.getText();
         JPanel tmp;
         panels[flag].setVisible(false);
          switch(menu) {
          case "Completion":
            subjectlist(1);
             panels[0].setVisible(true);
             flag=0;
             break;
          case "Manage_Subject":
             seeSubject();
             panels[1].setVisible(true);
             flag=1;
             break;
          case "Manage_Track":
             System.out.println("ok");
             seeTrack();
             panels[2].setVisible(true);
             flag=2;
             break;
          case "Insert" :
             insertFunc(txtTrack.getText());
             System.out.println("ok");
             panels[2].remove(txtTrack); // 이전 텍스트 내용 지움
             panels[2].remove(jScollPane); // 이전 테이블 지움
             seeTrack();
             panels[2].setVisible(true);
              flag=2;
              //txtTrack.setText("");
             break;
          case "Delete" :
             deleteFunc(txtTrack.getText());
             System.out.println("ok");
             panels[2].remove(txtTrack); // 이전 텍스트 내용 지움
             panels[2].remove(jScollPane); // 이전 테이블 지움
             seeTrack();
             panels[2].setVisible(true);
              flag=2;
             break;
          case "InsertS" :
             insertFuncS(txtSelectTrack.getText(),txtSubject.getText(),selectLevel.getSelectedIndex());
              System.out.println("ok");
              panels[1].remove(txtSelectTrack);
              panels[1].remove(txtSubject);
              panels[1].remove(jScollPane);
              seeSubject();
              panels[1].setVisible(true);
              flag=1;
             break;
          case "DeleteS" :
             deleteSFunc(txtSubject.getText());
             panels[1].remove(txtSubject);
             panels[1].remove(jScollPane);
             seeSubject();
             panels[1].setVisible(true);
              flag=1;
             break;
          case "DeleteCategory":
              panels[3].removeAll();
              JLabel title = new JLabel("Subject Manage");
               title.setBounds(400, 30, 200, 40);
               title.setFont(new Font("Arial",Font.BOLD,25));
               panels[3].add(title);
                   btnInsertCategory.setVisible(false);
                   btnDeleteCategory.setVisible(false);
                   JLabel lblsubject = new JLabel("Subject Name");
                   lblsubject.setBounds(360,70,250,30);
                   lblsubject.setFont(new Font("Arial",Font.ITALIC,15));
                   lblsubject.setHorizontalAlignment(lblsubject.CENTER);
                   panels[3].add(lblsubject);
               txtSubject = new JTextField("");
               txtSubject.setBounds(360, 100, 250, 50);
               txtSubject.setFont(new Font("Verdana",Font.BOLD,20));
               panels[3].add(txtSubject);
               
               btnDeleteS.setBounds(435, 180, 100, 50);
               btnDeleteS.setFont(new Font("Verdana",Font.BOLD,15));
               btnDeleteS.setBackground(new Color(0xEEEEEE));
               panels[3].add(btnDeleteS);
               
               panels[3].setVisible(true);
               flag=3;
              break;
           case "InsertCategory":
              panels[3].removeAll();
              JLabel title1 = new JLabel("Subject Manage");
               title1.setBounds(400, 30, 200, 40);
               title1.setFont(new Font("Arial",Font.BOLD,25));
               panels[3].add(title1);
              btnInsertCategory.setVisible(false);
              btnDeleteCategory.setVisible(false);
              
              JLabel lblTrack = new JLabel("Track Name");
              lblTrack.setBounds(100,70,250,30);
              lblTrack.setFont(new Font("Arial",Font.ITALIC,15));
           lblTrack.setHorizontalAlignment(lblTrack.CENTER);
           panels[3].add(lblTrack);
           
              txtSelectTrack = new JTextField("");
               txtSelectTrack.setBounds(100, 100, 250, 50);
               txtSelectTrack.setFont(new Font("Verdana",Font.BOLD,20));
               panels[3].add(txtSelectTrack);
               
               JLabel lblSubject = new JLabel("Subject Name");
                   lblSubject.setBounds(360,70,250,30);
                   lblSubject.setFont(new Font("Arial",Font.ITALIC,15));
                   lblSubject.setHorizontalAlignment(lblSubject.CENTER);
                   panels[3].add(lblSubject);
               txtSubject = new JTextField("");
               txtSubject.setBounds(360, 100, 250, 50);
               txtSubject.setFont(new Font("Verdana",Font.BOLD,20));
               panels[3].add(txtSubject);
               
               JLabel lblLevel = new JLabel("Level");
                   lblLevel.setBounds(620,70,250,30);
                   lblLevel.setFont(new Font("Arial",Font.ITALIC,15));
                   lblLevel.setHorizontalAlignment(lblSubject.CENTER);
                   panels[3].add(lblLevel);
               selectLevel = new JComboBox(level);
               selectLevel.setBounds(620, 100, 250, 50);
               selectLevel.setBackground(Color.WHITE);
               panels[3].add(selectLevel);
             
               btnInsertS.setBounds(435, 180, 100, 50);
               btnInsertS.setFont(new Font("Verdana",Font.BOLD,15));
               btnInsertS.setBackground(new Color(0xEEEEEE));
               panels[3].add(btnInsertS);
               
               panels[3].setVisible(true);
               flag=3;
              break;

          }
          
       }
    }
}
   
