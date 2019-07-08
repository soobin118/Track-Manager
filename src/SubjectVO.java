import java.io.UnsupportedEncodingException;

public class SubjectVO {

   private String name;
   private int    flag;
   private int    complete;
   
   public SubjectVO() {
      name = null;
      flag = 0 ;
      complete = 0;
   }
   
   public void setName(String name){
	   this.name=name;
   }
   public String getName() {
      return this.name;
   }
   public void setFlag(int flag) {
      this.flag = flag;
   }
   public int getFlag() {
      return this.flag;
   }
   public void setComplete(int complete) {
      this.complete = complete;
   }
   public int getComplete() {
      return this.complete;
   }
}