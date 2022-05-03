package ThreadImpl;

import JDBCUtils.JDBCUtils;
import bean.Record;
import bean.Server;
import bean.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerThread extends Thread
{
    private Socket s;
    private Server se;
    private InputStream is;
    private PrintWriter pw;
    private static int suid=0;
    private int uid;


    public ServerThread( Server se , Socket s  ) throws IOException
    {
        synchronized (ServerThread.class)
        {
            suid++;
            this.uid = suid;
            this.se = se;
            this.s = s;
            this.pw = new PrintWriter(s.getOutputStream());
        }

    }

    @Override
    public void run()
    {
        try
        {
            //获得输入流
            this.is = s.getInputStream();
        }
        catch( IOException e)
        {
            e.printStackTrace();
        }
//        封装输入流
        Scanner in = new Scanner( is, "utf-8" );

        //循环 登录/注册
        while(true)
        {
            //如果输入流有信息
            if( in.hasNextLine() )
            {
                //获得
                String str = in.nextLine();
                //以 登录 开头
                if( str.startsWith("signIn"))
                {
                    try
                    {
                        //分割信息
                        String[] ss = str.split("#");
                        //创建 User
                        User user = new User(ss[1], ss[2]);
                        //尝试登录
                        //  如果成功
                        if(JDBCUtils.getUserByNameAndPsw(user)!=null)
                        {
                            pw.println("success");
                            pw.flush();
                            se.setUserOfCon( uid,user );
                            break;
                        }
                        //  如果失败
                        else
                        {
                            pw.println("fail");
                            pw.flush();
                            continue;
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                    //以 注册 开头
                if( str.startsWith("regist") )
                {
                    try {
                        //分割信息
                        String[] ss = str.split("#");
                        User user = new User(ss[1], ss[2]);
                        //查看用户名是否可用
                        if( JDBCUtils.getUserByName(user.getUsername()) )
                        {
                            pw.println("fail");
                            pw.flush();
                            continue;
                        }
                        //尝试注册
                        //  如果成功
                        if (JDBCUtils.InsertUserByNameAndPsw(user))
                        {
                            pw.println("success");
                            pw.flush();
                            se.setUserOfCon(uid, user);
                            continue;
                        }
                        //  如果失败
                        else
                        {
                            pw.println("fail");
                            pw.flush();
                            continue;
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }
        }

        //循环 登陆以后
        while( true )
        {
            //如果有信息
            if( in.hasNextLine() )
            {
                System.out.println("接收");
                //接受 并在服务器端记录
                    String str = in.nextLine();
                    System.out.println(str);

                    //option1  查询房间数
                    if( str.equals("option1") )
                    {
                        System.out.println("option1");
                        this.pw.println(this.se.getRoomsNum());
                        this.pw.flush();
                    }
                    else
                        //创建房间
                    if( str.equals( "option2" ) )
                    {
                        String msg ;
                        if( se.mkRoom() )
                            msg = "success";
                        else
                            msg = "fail";

                        this.pw.println(msg);
                        this.pw.flush();

                    }
                    //加入房间
                    else if( str.startsWith("option3") )
                    {
                        //System.out.println("option3!");
                        String[] strs = str.split(":");
                        int roomNum = Integer.parseInt(strs[1]);
                        String username = strs[2];
                        System.out.println(username+roomNum);
                        if(!se.changeRoom( uid , roomNum-1 ))
                        {
                            pw.println("fail");
                            pw.flush();
                        }
                        else
                        {
                            pw.println("success");
                            pw.flush();
                            //聊天
                            while( true )
                            {
                                if( in.hasNextLine() )
                                {
                                    String msg = in.nextLine();
                                    //退出房间
                                    if( msg.equals("quit") )
                                    {
                                        se.quitRoom(uid);
                                    }
                                    else
                                    {
                                        se.addRecord( new Record( msg ) );
                                        System.out.println("收到"+msg);
                                        se.updateClient(uid);
                                    }

                                }

                            }


                        }

                    }

            }
        }

    }

    public int getUid()
    {
        return uid;
    }

}
