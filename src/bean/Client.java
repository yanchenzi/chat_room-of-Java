package bean;

import GUIUtils.GUI;
import ThreadImpl.ClientThread;
import sun.font.TrueTypeFont;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Scanner;

public class Client
{

    private final int MAX_TIME = 20;
    private final int MAX_RECORDS = 100;
    private Socket sk;
    private InputStream is;
    private OutputStream os;
    private int port;
    private String addr;
    private LinkedList<Record> list;
    private PrintWriter logpw;
    private PrintWriter pw;
    private Scanner in;
    private int uid;
    private volatile boolean isWriting;
    private User user;

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Client()
    {
        this.sk = new Socket();
        this.list = new LinkedList<>();
        //this.user = new User("闫琛","123456789");
    }

    public void start()
    {
        //连接 Socket  初始化流
        if(!connect())
            return;

        //登入 or 注册

        Scanner scanner = new Scanner(System.in);

//      System.out.println("$1 -- 登录 $2 -- 注册");

        while( true )
        {
            GUI.printHelloOptions();
            String option = scanner.nextLine();
            if (option.equals("$1"))
            {
                GUI.ptintUsernameAndPsw();
//                System.out.println("请分别输入用户名和密码,以一个空格分割");
                String str = scanner.nextLine();
                String[] ss = str.split(" ");
                User user = new User(ss[0], ss[1]);
                if(SignIn(user))
                {
                    this.user = user;
//                    System.out.println("登陆成功，按回车键继续");
                    GUI.printSigninSuccess();//创建输出流
                    try
                    {
                        this.logpw = new PrintWriter("src/logof"+user.getUsername());
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    scanner.nextLine();
                    break;
                }
                else
                {
//                  System.out.println("登陆失败，按回车键继续");
                    GUI.printSigninFail();
                    scanner.nextLine();
                }
            }
            else
            if( option.equals("$2") )
            {
//                System.out.println("请分别输入用户名和密码,以一个空格分割");
                GUI.ptintUsernameAndPsw();
                String str = scanner.nextLine();
                String[] ss = str.split(" ");
                User user = new User(ss[0], ss[1]);
                if( Regist(user) )
                {
                    this.user = user;
//                    System.out.println("注册成功，按回车键继续");
                    GUI.registSuccess();
                    scanner.nextLine();

                    continue;
                }
                else
                {
//                    System.out.println("注册失败，按回车键继续");
                    GUI.registFail();
                    scanner.nextLine();
                }
            }

        }


        while( true )
        {
            //输出选项
            GUI.printOptions();
            //
            String option = scanner.nextLine();
            if( option.equals("$1") )
            {
//                System.out.println("$1!");
                String resp;

                    pw.println("option1");
                    pw.flush();

                    while( true )
                    {
                        if( in.hasNextLine() )
                        {
                            resp = in.nextLine();
                            break;
                        }
                    }

                    System.out.println("=================================================");
                    System.out.println("             目前有"+resp+"个房间");
                    System.out.println("=================================================");
                try
                {
                    Thread.sleep(500);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

            }
            else if( option.equals( "$2" ) )
            {
                String resp;

                //获得锁等待响应
                synchronized (this)
                {
                    pw.println("option2");
                    pw.flush();

                    while( true )
                    {
                        if( in.hasNextLine() )
                        {
                            resp = in.nextLine();
                            break;
                        }

                    }

                }

                if( resp.equals("success") )
                {

                    System.out.println("=================================================");
                    System.out.println("               创建房间成功");
                    System.out.println("=================================================");
                    try
                    {
                        Thread.sleep(500);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {

                    System.out.println("=================================================");
                    System.out.println("                创建房间失败");
                    System.out.println("=================================================");
                    try
                    {
                        Thread.sleep(500);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
            else if( option.equals("$3") )//加入房间
            {
                    GUI.clear();
                    System.out.println("=================================================");
                    System.out.println("                请输入房间号");
                    System.out.println("=================================================");

                    int roomNum = scanner.nextInt();
                    scanner.nextLine();
                    if( roomNum<=0 || roomNum>20 )
                    {

                        GUI.clear();
                        System.out.println("=================================================");
                        System.out.println("             加入房间失败");
                        System.out.println("=================================================");
                        try
                        {
                            Thread.sleep(500);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        pw.println("option3:"+roomNum+":"+user.getUsername());
                        pw.flush();
                        String resp;
                        while( true )
                        {
                            if( in.hasNextLine() )
                            {
                                resp = in.nextLine();
                                System.out.println(resp);
                                break;

                            }
                        }
                        if( resp.equals("fail") )
                        {
                            GUI.clear();
                            System.out.println("=================================================");
                            System.out.println("                  加入房间失败");
                            System.out.println("=================================================");
                            try
                            {
                                Thread.sleep(500);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                            //->重新回到主界面
                        }
                        else
                        {
                            GUI.clear();
                            System.out.println("=================================================");
                            System.out.println("                  加入房间成功");
                            System.out.println("=================================================");
                            try
                            {
                                Thread.sleep(500);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                            GUI.printOptionsOfChating();

                            //开启一个监听线程
                            ClientThread thread = new ClientThread(this);
                            thread.start();

                            //主线程↓
                            while(true)
                            {
//            System.out.println("聊天");

                                String optionOfChating = scanner.nextLine();
                                if( optionOfChating.equals("$1") )
                                {
                                    System.out.println("=================================================");
                                    System.out.println(  "您的内容: ");
                                    String msg = scanner.nextLine();
                                    //发送消息
                                    try
                                    {
                                        chat(msg);
                                    }
                                    catch (InterruptedException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                else if(optionOfChating.equals("$2"))
                                {
                                    //退出房间
                                    quitRoom();
                                    break;
                                }
                                else
                                {
                                    System.out.println("=================================================");
                                    System.out.println("                    非法选项!");
                                    System.out.println("=================================================");
                                    GUI.printOptionsOfChating();
                                }

                            }
                        }
                    }

            }
            //客户端的主线程，直接显示在 输入框/GUI 里，所以 输入\输出 要考虑锁
            else if( option.equals("$4") )
            {
                GUI.clear();
                System.out.println("=================================================");
                System.out.println(       user.getUsername()+",再见！");
                System.out.println("=================================================");
                return;
            }

        }


    }

    private void quitRoom()
    {
        pw.println( "quit" );
        pw.flush();
    }

    private boolean SignIn( User user )//登录
    {
        pw.println("signIn#"+user.getUsername()+"#"+user.getPassword());
        pw.flush();
        String resp = "";
        while( true )
        {
            if( in.hasNextLine() )
            {
                resp = in.nextLine();
                break;
            }
        }
        return resp.equals("success");
    }


    private boolean Regist( User user )//注册
    {
        System.out.println("!");
        pw.println("regist#"+user.getUsername()+"#"+user.getPassword());
        pw.flush();
        String resp = "";
        while( true )
        {
            if( in.hasNextLine() )
            {
                resp = in.nextLine();
//                System.out.println(resp);
                break;
            }
        }
        return resp.equals("success");
    }


    public InputStream getIs()
    {
        return is;
    }

    public void addRecord(Record re)
    {

        if(list.size() >= MAX_RECORDS)
            list.pollFirst();

        list.addLast(re);

    }


    private boolean init()//初始化socekt
    {

        try
        {
//            this.logpw = new PrintWriter("src/log");
            this.is = sk.getInputStream();
            this.os = sk.getOutputStream();
            this.pw = new PrintWriter(os);
            this.in = new Scanner( is , "utf-8" );
        }
        catch( Exception e )
        {
            e.printStackTrace();
            return false;
        }
        return true;

    }


    private boolean connect()//连接
    {
        try
        {
            Properties pro = new Properties();
            InputStream is = test.class.getClassLoader().getResourceAsStream("config");
            pro.load(is);
            this.addr = pro.getProperty("address");
            this.port = Integer.parseInt(pro.getProperty("port"));
            is.close();

            this.sk.connect(
                    new InetSocketAddress(addr,port) , MAX_TIME );
            init();
            while( true )
            {
                if( in.hasNextLine() )
                {
                    this.uid = Integer.parseInt(in.nextLine());
                    break;
                }
            }
//            this.pw.println(user.getUsername());
//            this.pw.flush();

        }
        catch( Exception e )
        {
            System.out.println("连接失败，请稍后重试");
            return false;
        }

        return true;

    }

    private void chat( String msg ) throws InterruptedException {
        System.out.println("chat");
        this.isWriting = true;
//        PrintWriter pw = new PrintWriter(os);
//        Scanner scanner = new Scanner(System.in);
//        String msg = scanner.nextLine();
        Record record = new Record(user.getUsername()+"#" + new Date().getTime() + "#" + msg);
        pw.println( record.toChars() );
        pw.flush();
        Thread.sleep(300);
        GUI.printRecords(list);
        synchronized(this.logpw)
        {

            this.logpw.println(new Date().toString() + " 发送给服务器    " +record.toChars());
            this.logpw.flush();

        }
        this.isWriting = false;

    }


    public boolean getIsWriting()
    {
        return this.isWriting;
    }

    public PrintWriter getLogpw()
    {
        return logpw;
    }

    public LinkedList<Record> getList()
    {
        return list;
    }

    public OutputStream getOs()
    {
        return os;
    }

}
