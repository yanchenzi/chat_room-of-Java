package ThreadImpl;

import GUIUtils.GUI;
import bean.Client;
import bean.Record;

import java.io.*;
import java.util.Date;
import java.util.Scanner;

public class ClientThread extends Thread
{
    private Client client;

    public ClientThread( Client client )
    {
        this.client = client;
    }

    @Override
    public void run()
    {
//        boolean local = false;
        InputStream is = client.getIs();
//        OutputStream os = client.getOs();
        PrintWriter pw = client.getLogpw();



        while( true )
        {

            Scanner in = new Scanner(is,"utf-8");
            while( true )
            {
                    if( in.hasNextLine() )
                    {
                        {
                            String str = in.nextLine();
                            if (check(str))
                            {
                                client.addRecord(new Record(str));
                                //System.out.println("收到" + str );

                                while( true )
                                {
                                    if( !client.getIsWriting() )
                                    {
                                        GUI.printRecords(client.getList());
                                        break;
                                    }
                                }


                                synchronized(pw)
                                {
                                    pw.println(new Date().toString() + " 接收到来自服务器的 " + str);
                                    pw.flush();
                                }
                            }
                        }

                    }


            }
        }
    }


    private boolean check(String str)
    {
//        TO-DO
        return str.length()>10;
    }

}
