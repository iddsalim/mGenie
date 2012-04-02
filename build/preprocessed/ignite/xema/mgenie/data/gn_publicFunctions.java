/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ignite.xema.mgenie.data;

/**
 *
 * @author haxor
 */
import java.util.Vector;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.Connector;
import java.util.Stack;
import javax.microedition.lcdui.Image;

public class gn_publicFunctions {


public static String nameFromEmail (String em) {
    int index = em.toString().indexOf('@');
    int start = 0;
    int end = index;
    String nameOnly = em.toString().substring(start, end);   // Str
    return nameOnly;
}

public static String performWebServiceCall(String url, String urlAction,  String dataArr) throws IOException {
  HttpConnection c = null;
  InputStream is = null;
  OutputStream os = null;
  StringBuffer b = new StringBuffer();
  String t = null;
  try {
     c = (HttpConnection)Connector.open(url + "?pw=3aac9dc609b455ca3e4a48aea31a0820&janta=" + urlAction);
     c.setRequestMethod(HttpConnection.POST);
     c.setRequestProperty("CONTENT-TYPE","application/x-www-form-urlencoded");
     c.setRequestProperty("User-Agent","GBK-App/Xema-1.0");
     c.setRequestProperty("Content-Language", "en-US");
     os = c.openOutputStream();

     // send input
     byte postmsg[] = dataArr.getBytes();
     for(int i=0;i<postmsg.length;i++) {
        os.write(postmsg[i]);
     }
     os.flush();
     is = c.openDataInputStream();
     int ch;
     // receive output
     while ((ch = is.read()) != -1) {
        b.append((char) ch);
       // System.out.println((char)ch);
     }
     t = b.toString();//, 1024, 0);
  } finally {
     if(is!= null) {  is.close();  }
     if(os != null) { os.close();  }
     if(c != null) {  c.close();   }
  }
  //System.out.println("FINAL : "+t);
  return (t);
}

public static String[] split(String toSplit,char delim,boolean ignoreEmpty)
{
    StringBuffer buffer = new StringBuffer();
    Stack stringStack = new Stack();

    for(int i = 0;i<toSplit.length();i++)
    {
        if(toSplit.charAt(i) != delim){ buffer.append((char)toSplit.charAt(i));}
        else
        {
            if(buffer.toString().trim().length() == 0 && ignoreEmpty) { }
            else { stringStack.addElement(buffer.toString());}
            buffer = new StringBuffer();
        }
    }

    if(buffer.length() != 0) {
        stringStack.addElement(buffer.toString());
    }

    System.out.println("Stack Size :" + stringStack.size());

    String [] split = new String[stringStack.size()];
    for(int i = 0;i<split.length;i++)
    {
        split[i] = (String)stringStack.pop();
    }

    stringStack = null;
    buffer = null;
    return split;
}

public static  String[] splitFriendsSearchResult(String original, String separator) {
    Vector nodes = new Vector();
    //System.out.println("split start...................");
    // Parse nodes into vector
    int index = original.indexOf(separator);
    while(index>=0) {
        nodes.addElement( original.substring(0, index) );
        original = original.substring(index+separator.length());
        index = original.indexOf(separator);
    }
    // Get the last node
    nodes.addElement(original);

    // Create splitted string array
    int i =0;
        if( nodes.size()>0 ) {
            for(int loop=0; loop<nodes.size(); loop++)
            {
                if (!nodes.elementAt(loop).toString().equals(null) && !nodes.elementAt(loop).toString().equals("") ){
                    i++;
                }
            }
        }

    String[] result = new String[i];
        i =0;
        if( nodes.size()>0 ) {
            for(int loop=0; loop<nodes.size(); loop++)
            {
                if (!nodes.elementAt(loop).toString().equals(null) && !nodes.elementAt(loop).toString().equals("") ){
                    result[i] = (String)nodes.elementAt(loop);
                    System.out.println(result[loop]);
                    i++;
                }
            }
        }
    //System.out.println("split end. Nodes :" + nodes.size() + ", result : "+ result.length);
    nodes = null;
    return result;
}

/*public static Image Base64ToImage(String base64String)
{
  // Convert Base64 String to byte[]
  byte[] imageBytes = base64String.getBytes();// Convert.FromBase64String(base64String);
  //MemoryStream ms = new MemoryStream(imageBytes, 0,
//    imageBytes.Length);

  // Convert byte[] to Image
  //ms.Write(imageBytes, 0, imageBytes.length);
  Image image = Image.createImage(imageBytes);// FromStream(ms, true);
  return image;
}*/
}
