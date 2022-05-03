package GUIUtils;

import bean.Record;

import java.util.Deque;
import java.util.LinkedList;

public class GUI
{
    public static void printHelloOptions()
    {
        GUI.clear();
        System.out.println("=================================================");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|             $1 -- 登录 $2 -- 注册              |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("=================================================");
    }

    static public void clear()
    {
        for( int i=0 ; i<80 ; i++ )
        {
            System.out.println();
        }
    }

    public static void print( String Msg )
    {
        clear();
        System.out.print( Msg );
    }

    public static void printOptionsOfChating()
    {
        System.out.println("=================================================");
        System.out.println("|     $1 -- 输入聊天内容  $2 -  退出房间           |");
        System.out.println("=================================================");
    }


    public static void printRecords( LinkedList<Record> list )
    {
        clear();
        System.out.println("=================================================");
        for( int i=0 ; i<list.size() ; i++ )
        {
            System.out.println( list.get(i).toString() );
            System.out.println("=================================================");
        }
        printOptionsOfChating();

    }

    public static void printOptions()
    {
        GUI.clear();
        System.out.println("=================================================");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|     $1 -- 查看当前房间数  $2 -- 创建新房间       |");
        System.out.println("|                                               |");
        System.out.println("|      $3 -- 加入房间      $4 -- 退出程序         |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("=================================================");


    }

    public static void ptintUsernameAndPsw()
    {
        GUI.clear();
        System.out.println("=================================================");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|      请分别输入用户名和密码,以一个空格分割         |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("=================================================");
    }

    public static void printSigninSuccess()
    {
        GUI.clear();
        System.out.println("=================================================");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|              登陆成功，按回车键继续              |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("=================================================");
    }

    public static void printSigninFail()
    {
        GUI.clear();
        System.out.println("=================================================");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|              登陆失败，按回车键继续              |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("=================================================");
    }

    public static void registSuccess()
    {
        GUI.clear();
        System.out.println("=================================================");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|              注册成功 ，按回车键继续              |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("=================================================");
    }

    public static void registFail()
    {
        GUI.clear();
        System.out.println("=================================================");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|              注册失败，按回车键继续              |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("|                                               |");
        System.out.println("=================================================");
    }
}
