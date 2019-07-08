
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.*;
import javax.swing.*;

public class LoginPage extends JPanel {

   private JLabel enterID,enterPW;
   private JTextField fieldID;
   private JPasswordField fieldPW;
   private JButton enter;
   private String jdbcUrl = "jdbc:mysql://203.250.148.53/Camping?useSSL=false";
   private String jdbcDriver = "com.mysql.jdbc.Driver";
   private String strUser = "16011087";
   private String strPassword = "16011087";
   private Connection conn;
   private PreparedStatement pstmt;
   private ResultSet rs;
   private ButtonListener btnL;
   MainFrame ff;
   private JLabel lblcr,lblcr1,lblcr2;
   public LoginPage(final MainFrame frame) {
   
      ff=frame;
      setPreferredSize(new Dimension(1200, 700));
      setBounds(0,200,1200,700);
      setBackground(Color.WHITE);
      setLayout(null);
      btnL = new ButtonListener();
      
      enterID = new JLabel("ID");
      enterID.setFont(new Font("Arial",Font.BOLD,20));
      enterID.setHorizontalAlignment(SwingConstants.RIGHT);
      enterID.setBounds(200, 200, 200, 40);
      add(enterID);
      
      enterPW = new JLabel("Password");
      enterPW.setFont(new Font("Arial",Font.BOLD,20));
      enterPW.setHorizontalAlignment(SwingConstants.RIGHT);
      enterPW.setBounds(200, 250, 200, 40);
      add(enterPW);
      
      fieldID = new JTextField();
      fieldID.setHorizontalAlignment(JTextField.CENTER);
      fieldID.setBounds(430,200,300, 40);
      add(fieldID);
      
      fieldPW = new JPasswordField();
      fieldPW.setHorizontalAlignment(JPasswordField.CENTER);
      fieldPW.setBounds(430, 250, 300, 40);
      add(fieldPW);
      
      enter = new JButton("enter");
      enter.setBounds(750, 200, 100, 90);
      enter.setFont(new Font("Arial",Font.BOLD,20));
      enter.setHorizontalAlignment(SwingConstants.CENTER);
      enter.setFocusable(false);
      enter.addActionListener(btnL);
      add(enter);
      lblcr = new JLabel("Sejong University");
      lblcr.setForeground(new Color(0x222D65));
      lblcr.setFont(new Font("Arial",Font.BOLD,20));
      lblcr.setHorizontalAlignment(SwingConstants.RIGHT);
      lblcr.setBounds(0, 570, 1150, 40);
      add(lblcr);
      
      lblcr1 = new JLabel("Hackaton");
      lblcr1.setForeground(new Color(0x222D65));
      lblcr1.setFont(new Font("Arial",Font.BOLD,20));
      lblcr1.setHorizontalAlignment(SwingConstants.RIGHT);
      lblcr1.setBounds(0, 590, 1150, 40);
      add(lblcr1);
      
      lblcr2 = new JLabel("NTN");
      lblcr2.setForeground(new Color(0x222D65));
      lblcr2.setFont(new Font("Arial",Font.BOLD,20));
      lblcr2.setHorizontalAlignment(SwingConstants.RIGHT);
      lblcr2.setBounds(0, 610, 1150, 40);
      add(lblcr2);

   }
   public void connectDB() {
         try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(jdbcUrl, strUser, strPassword);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   private class ButtonListener implements ActionListener {
         private String getTextValue;

         public void actionPerformed(ActionEvent event) {
            Object obj = event.getSource();
            getTextValue = new String();
            if (obj == enter) {
                 try {
                     if (!checkUser(fieldID.getText())) {// 데이터 베이스에 아이디가 없는 경우
                        fieldID.setText("");
                        fieldPW.setText("");
                     } else if (checkUser(fieldID.getText())) {
                        if (!checkPassword(fieldPW.getText())) {// 데이터 베이스에 아이디는 존재하지만 비밀번호가 틀린 경우
                           fieldPW.setText("");
                        } else {
                           //saveId = fieldID.getText();
                            ff.Change("Access",fieldID.getText());
                           fieldID.setText("");
                           fieldPW.setText("");
                       }
                     }
                  } catch (IOException e) {
                     e.printStackTrace();
                  }
            } 
         }
        }
         private boolean checkUser(String userId) throws IOException {
             String savedId = new String();
             boolean check = false;
             String sql = "select * from Login";//데이터 베이스 멤버 데이터 선택
             connectDB();
             try {
                pstmt = conn.prepareStatement(sql);

                rs = pstmt.executeQuery();

                while (rs.next()) {
                   savedId = rs.getString("id");//데이터 베이스 아이디를 읽어옴
                   if (userId.equals(savedId))
                      check = true;// 데이터 베이스에 아이디가 존재 시에 true 값을 반환
                }

             } catch (SQLException e) {
                e.printStackTrace();
             }

            return check;
          }
         private boolean checkPassword(String userPassword) throws IOException {
             String savedPassword = new String();
             String savedId = new String();
             boolean check = false;
             
             String sql = "select * from Login";

             try {

                connectDB();
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                savedId = fieldID.getText();
                
                while (rs.next()) {
                   if(savedId.equals(rs.getString("id"))) {//데이터 베이스에 아이디가 존재하면 
                      savedPassword = rs.getString("password");//데이터 베이스에서 해당 아이디의 비밀번호를 읽어옴
                      if (userPassword.equals(savedPassword)) {//비밀번호가 일치시
                         check = true;//아이디 비밀번호가 다 존재시에 true 값을 반환
                      }
                   }
                }
                   
             } catch (SQLException e) {
                e.printStackTrace();
             }

             return check;
          }
   }