/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ignite.xema.mgenie.data;

// R M S
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordEnumeration;
import java.io.IOException;

import javax.microedition.rms.RecordComparator;
import ignite.xema.mgenie.screens.gn_MainFrame;

public class gn_UserData {

private static RecordStore rs = null;

public static String getUserSettings() throws  IOException {
    openLoginsDB();
        try {
            getCurrentLoginsIfAny();
        } catch (IOException e) {
            System.out.println("Error : " + e.toString() + "...");
            e.printStackTrace();
        }
    closeLoginsDB();
    return "OK";
}

public static void saveUserSettings(String uN, String uP, String isSel) {
    if (isSel == "1") { destroyLoginsDB(); }
    openLoginsDB();
        registerLogin ( uN.toString(), uP.toString(), isSel);
    closeLoginsDB();

}

public static void purgeUserSettings() {        destroyLoginsDB();}

/*  R M S   F U N C T I O N S  */
private static String getCurrentLoginsIfAny() throws  IOException {
    String uName ;

    try {
        uName = "";
        //Create a RMS
        try {
    //System.out.println("Sessions : " + rs.getNumRecords());
    if (rs.getNumRecords() > 0){
        System.out.println("Total Sessions : " + rs.getNumRecords());
        Comparator comp = new Comparator();
        RecordEnumeration ren = rs.enumerateRecords(null, comp, false);
        while (ren.hasNextElement()){
         byte [] bRec = ren.nextRecord();
          //unpack a multi-column record
          ByteArrayInputStream bais = new ByteArrayInputStream(bRec);
          DataInputStream dis = new DataInputStream(bais);
          uName = dis.readUTF();
          System.out.println("Name: " + uName);
          String uPass = dis.readUTF();
          //System.out.println("Id: "+ uID );
          String uAl = dis.readUTF();
          //System.out.println("Login: "+ uLogin);
          gn_MainFrame.lUser = uName;
          gn_MainFrame.lPass=uPass;
          if (uAl.equals("1")) { gn_MainFrame.lAutoLog = true;    }

            dis.close();
            bais.close();
       }
    }
        } catch(Exception e) {
            System.out.println(e + " EMPTY");
            uName = "";
        }
  }
  finally {  }
    return uName;
  }

public static  void registerLogin(String uName, String uPass, String uAl) {
    try{
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);
      dos.writeUTF(uName);
      dos.writeUTF(uPass);
      dos.writeUTF(uAl);
      byte [] bRec = baos.toByteArray();
      rs.addRecord(bRec,0,bRec.length);
      dos.close();
      baos.close();
        System.out.print("Session saved:" + uName + ","+ uPass +","+ uAl);
    }
    catch (Exception e) {
      System.out.print("Error in save login -> ");
      System.out.println("Exception : " + e.getMessage());
    }
    return;
  }

/****
This function will create/open session database.
****/
private static void openLoginsDB() {
// attempt to create a new RecordStore database
try {
      rs = RecordStore.openRecordStore( "xemaLoginsRMS", true );
    }
    catch( Exception e ){
  System.out.print("Error in Open logins DB ->");
  System.out.println("Exception : " + e.getMessage());
    }
}

private static void destroyLoginsDB() {
// attempt to DESTROYS RecordStore database
try {
      rs.deleteRecordStore( "xemaLoginsRMS");
    }
    catch( Exception e ){
  System.out.print("Error in destroy logins DB ->");
  System.out.println("Exception : " + e.getMessage());
    }
}

/****
This function closes the session DB if it is open.
****/
private static void closeLoginsDB() {
    if (rs != null) {
      try {
          rs.closeRecordStore();
      } catch (Exception e) {
        System.out.print("Error in close logins DB -> ");
        System.out.println("Exception : " + e.getMessage());
      }
    }
}

/*--------------------------------------------------
* Compares two records to determine sort order
*-------------------------------------------------*/
static class Comparator implements RecordComparator
{
  public int compare(byte[] rec1, byte[] rec2)
  {
    String str1 = new String(rec1), str2 = new String(rec2);

    int result = str1.compareTo(str2);
    if (result == 0)
      return RecordComparator.EQUIVALENT;
    else if (result < 0)
      return RecordComparator.PRECEDES;
    else
      return RecordComparator.FOLLOWS;
  }
}
}
