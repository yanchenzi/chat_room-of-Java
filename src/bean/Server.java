package bean;

import ThreadImpl.ServerThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server
{
    private ServerSocket ss;
    private final int MAX_RECORDS = 20;
    private final int MAX_CONNECTS = 40;
    private final int MAX_ROOMS = 20;
    private final int MAX_NUM_ROOM = 10;
    private int port;
    private volatile int count;
    private volatile int roomCount;
    private LinkedList<Record> list;

    private LinkedList<Con>[] rooms;
    private HashMap<Integer,Con> hash;
    private HashSet<String> names;

    public Server()
    {
        port = 8819;
        this.names = new HashSet<String>();
        this.list = new LinkedList<>();
        this.rooms = new LinkedList[20];
        this.hash = new HashMap();
        this.roomCount = 0;
    }

    public synchronized void print( String str )
    {
        System.out.println(str);
    }

    public boolean changeRoom( int uid, int roomNum)
    {
        if(  roomNum>=roomCount || rooms[roomNum].size()>=MAX_NUM_ROOM )
            return false;
        Con con = hash.get(uid);
        con.setRoomNum(roomNum);
        rooms[roomNum].add(con);
        return true;
    }

    private synchronized  void roomCountAdd()
    {
        this.roomCount ++;
    }


    private boolean init()
    {
        count = 0;
        try
        {
            this.ss = new ServerSocket( port );
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void start()
    {
        init();

        while( true )
        {
            if( count < MAX_CONNECTS  )
            {
                try
                {
                    Socket ac = ss.accept();
//                    InputStream is = ac.getInputStream();
//                    Scanner scanner = new Scanner(is);
//                    String username;
//                    while( true )
//                    {
//                        if( scanner.hasNextLine() )
//                        {
//                            username = scanner.nextLine();
//                            break;
//                        }
//                    }
                    countAdd(1);
//

                    ServerThread thread = new ServerThread(this, ac  );
                    int uid = thread.getUid();

                    OutputStream os = ac.getOutputStream();
                    PrintWriter pw = new PrintWriter(os);
                    pw.println(uid);
                    pw.flush();

                    Con con = new Con(ac,uid);
                    hash.put( uid , con );

                    System.out.println( uid+" 接入");
                    thread.start();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }
    }

    synchronized private void countAdd( int n )
    {
        this.count+=n;
    }

    public void addRecord(Record record)
    {
        this.list.addLast( record );
        if( list.size() >= MAX_RECORDS )
            list.pollFirst();
    }

    public void updateClient( int uid )
    {
        int roomNum = hash.get(uid).roomNum;
        LinkedList<Con> cons = rooms[roomNum];

        for( int i=0 ; i<cons.size() ; i++ )
        {
            System.out.println("发送");
            //出现连接错误 断开连接
            if( !cons.get(i).update() )
            {
                cons.remove(i);
                hash.remove( uid );
                countAdd(-1);
            }
        }
    }

    public boolean mkRoom()
    {
        if( this.roomCount < MAX_ROOMS )
        {
            this.rooms[this.roomCount++] = new LinkedList<>();
            return true;
        }

        return false;
    }

    public String getRoomsNum()
    {
        return String.valueOf(this.roomCount);
    }

    public void quitRoom(int uid)
    {
        Con con = hash.get(uid);
        int roomNum = con.getRoomNum();
        rooms[roomNum].remove(con);
        con.setRoomNum(-1);
  }

    public void setUserOfCon(int uid, User user)
    {
        this.hash.get(uid).user = user;
    }


    class Con
    {
        private boolean alive;
        private Socket s;
        private PrintWriter pw;
        private int roomNum;
        private int uid;
        private User user;


        public Con( Socket s,  int uid ) throws IOException
        {

            this.uid = uid;
            this.s = s;
            this.alive = false;
            this.pw = new PrintWriter( s.getOutputStream() );

        }

        public boolean sendMsg(String msg)
        {
            try
            {
                pw.println(msg);
                pw.flush();
            }
            catch(Exception e)
            {
                return false;
            }
            return true;
        }

        public boolean update()
        {
            try
            {
                System.out.println("发送给"+user.getUsername()+" : "+Server.this.list.getLast().toChars());
                pw.println(Server.this.list.getLast().toChars());
                pw.flush();
            }
            catch(Exception e)
            {
                return false;
            }
            return true;
        }



        public boolean isAlive()
        {
            return alive;
        }

//        public void setAlive(boolean alive)
//        {
//            this.alive = alive;
//        }
//
//        public Socket getS()
//        {
//            return s;
//        }
//
//        public void setS(Socket s)
//        {
//            this.s = s;
//        }
//
//        public PrintWriter getPw()
//        {
//            return pw;
//        }
//
//        public void setPw(PrintWriter pw) {
//            this.pw = pw;
//        }

        public int getRoomNum()
        {
            return roomNum;
        }

        public void setRoomNum(int roomNum)
        {
            this.roomNum = roomNum;
        }


    }


}

