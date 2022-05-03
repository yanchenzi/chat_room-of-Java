package bean;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Record
{
    private String username;
    private Date date;
    private String content;
    private SimpleDateFormat df;

    public Record( String record )
    {
        this.df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int idx1=-1 , idx2 =-1;
        for( int i=0 ; i<record.length();  i++ )
            if( record.charAt(i) == '#' )
            {
                idx1 = i;
                break;
            }

        for( int i=idx1+1 ; i<record.length();  i++ )
            if( record.charAt(i) == '#' )
            {
                idx2 = i;
                break;
            }

        this.username = record.substring(0 , idx1 );
        this.date = new Date( Long.parseLong( record.substring(idx1+1 , idx2) ) );
        this.content = record.substring( idx2+1 );

    }

    @Override
    public String toString()
    {
        String str = "" ;
        str += username;
        str += " ";
        str += df.format(date);
        str += '\n';
        str += "   ";
        str += content;
        return str;
    }

    public String toChars()
    {
        String str = "" ;
        str += username;
        str += "#";
        str += date.getTime();
        str += '#';
        str += content;
        return str;
    }

}



