import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

public class Start {
    public static void main(String []args){
        ChangeInsideCodeControl control=new ChangeInsideCodeControl();
        try {
            control.run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }
}

    
    
    
    
    

    
    
    
    