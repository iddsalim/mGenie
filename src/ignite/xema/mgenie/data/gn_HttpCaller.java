/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ignite.xema.mgenie.data;

import javax.microedition.io.HttpConnection;
import javax.microedition.io.Connector;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author xema
 */
public class gn_HttpCaller  {

    public static String callServerSync(final String serverURL) {
HttpConnection c = null;
InputStream is = null;
StringBuffer sb = new StringBuffer();
try {
  c = (HttpConnection)Connector.open(serverURL,Connector.READ_WRITE, true);
  c.setRequestMethod(HttpConnection.GET); //default
  is = c.openInputStream(); // transition to connected!
  int ch = 0;
  for(int ccnt=0; ccnt < 150; ccnt++) { // get the title.
    ch = is.read();
    if (ch == -1){
      break;
    }
    sb.append((char)ch);
  }
}
catch (IOException x){
    x.printStackTrace();
}
catch (Exception x){
    x.printStackTrace();
}
finally{
     try     {

        if(is!= null) {is.close();}
        if(c != null) { c.close(); }

     } catch (IOException x){
          x.printStackTrace();
     }
}
return sb.toString();
}


static public String urlEncode(String sUrl)
{
    StringBuffer urlOK = new StringBuffer();
    for(int i=0; i<sUrl.length(); i++)
    {
        char ch=sUrl.charAt(i);
        switch(ch)
        {
            case '<': urlOK.append("%3C"); break;
            case '>': urlOK.append("%3E"); break;
            case '/': urlOK.append("%2F"); break;
            case ' ': urlOK.append("%20"); break;
            case '+': urlOK.append("%2B"); break;
            case ':': urlOK.append("%3A"); break;
            case '-': urlOK.append("%2D"); break;
            default: urlOK.append(ch); break;
        }
    }
    return urlOK.toString();
}
/*
    private void callServer(final String serverURL) {
//        new Thread(new Runnable() {
//            public void run() {
    HttpConnection httpConn = null;
    InputStream is = null;
    OutputStream os = null;

    try {
      // Open an HTTP Connection object
      httpConn = (HttpConnection)Connector.open(url);

      // Setup HTTP Request
      httpConn.setRequestMethod(HttpConnection.GET);
      httpConn.setRequestProperty("User-Agent", "Profile/MIDP-1.0 Confirguration/CLDC-1.0");

      /** Initiate connection and check for the response code. If the
        response code is HTTP_OK then get the content from the target
      * * /
      int respCode = httpConn.getResponseCode();

      if (respCode == httpConn.HTTP_OK) {
        StringBuffer sb = new StringBuffer();
        os = httpConn.openOutputStream();
        is = httpConn.openDataInputStream();
        int chr;
        while ((chr = is.read()) != -1)
          sb.append((char) chr);

        // Web Server just returns the birthday in mm/dd/yy format.
        //System.out.println(name+"'s Birthday is " + sb.toString());
      } else {
        System.out.println("Error in opening HTTP Connection. Error#" + respCode);
      }
      catch(Exception e ){}
      catch(java.io.IOException e ){}
      finally {
        if(is!= null) {is.close();}
        if(os != null) {os.close(); }
        if(httpConn != null) { httpConn.close(); }
    }
    }
        //}).start();
  }
*/
}
