/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabarektranslator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ThekME
 */
public class Helper {
    
    private static Connection c = null;
    private static String connetcionString="jdbc:sqlite:D:\\Projeler\\tabarekTranslator-GitHub\\TabarekTranslatorSourceCode\\src\\Database\\tabarekDB.db";
    
    public static String getConnectionString(){
        return connetcionString;
    }
    
    public static String[] parseSentence(String sentence){
        String[] words = sentence.split("\\s+");
        for (int i = 0; i < words.length; i++) { 
            words[i] = words[i].replaceAll("[^\\w]", "");
        } 
        return words;
    }
    
    public static String capitalize(String str,Locale locale )
    {
        if(str == null) return str;
        return str.substring(0, 1).toUpperCase(locale) + str.substring(1);
    }
    
    public static String capitalize(String str)
    {
        if(str == null) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
            
    public static Connection getSQLConnection() throws Exception {
        if(c == null){
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(getConnectionString());
        }
        return c;
    }
    
    
    
    public static String fixStr(String str){
        return str.replace("'", "''");
    }
    
    
    
    private static long b(long a,String b){
        
        for(int d=0;d<b.length()-2;d+=3)
        {
            char c=b.charAt(d+2);
            
            long c1=0;
            if('a'<=c){
                c1 = (  (int) c)-87;
            }
            else{
                c1 = Long.parseLong( String.valueOf(c)); 
            }   
            if('+'==b.charAt(d+1)){ 
                c1=a >>>c1;
            }  
            else
            { 
                c1=a <<c1;
            }
            
            if('+'==b.charAt(d)){
                a=a+ c1&4294967295L ;
            }
            else
            {
                a=a^c1;
            }  
        }
        return a; 
    }
    public static String tk(String word){
      
        int d=0;
        int e=0;
        long a=0; 
        Map<Integer, Long> g=new HashMap(); 
        String c[]=("409837.2120040981").split("\\.");
         
        Long h =Long.parseLong(c[0] );
        for(  ;e<word.length();e++){ 
            long f=word.charAt(e);
          
            if(128>f){
                g.put(d++,f) ; 
            }
            else{
                if(2048>f){
                    g.put(d++, (f>>6|192) );
                    
                }
                else{ 
                    if( (55296==(f&64512)) && ((e+1)<word.length()) && (56320==( ((int)word.charAt(e+1)) &64512)) ){ 
                       f=65536+((f&1023)<<10)+( ((int)word.charAt(++e))&1023); 
                       g.put(d++,f>>18|240);
                       g.put(d++,f>>12&63|128);  
                    }
                    else{  
                        g.put(d++,f>>12|224);  
                    } 
                    g.put(d++,f>>6&63|128);
                }
                g.put(d++,f&63|128);
            } 
        }
        a=h;

        Set keyset = g.keySet();
        int size= (int) Collections.max(keyset)+1;
         
        for(d=0;d<size;d++){
            a+=g.get(d); 
            a=b(a,"+-a^+6");
            //System.out.println(d+" -> "+a+ " - "+g.get(d)+" size :  "+size); 
        }
        
        a=b(a,"+-3^+b+-f"); 
        a^=Long.parseLong(c[1] ) ;
        
        if( 0>a ){
            a=(a&2147483647L)+2147483648L ;
        }
        a%=1000000;
        return ""+a+"."+(a^h) ;
    }
    
   
     public static String script( ){
        return  "var window = {\n" +
            "    TKK: \"409837.2120040981\" || '0'\n" +
            "};"
                + "function sM(a) {\n" +
                "    var b;\n" +
                "    if (null !== yr)\n" +
                "        b = yr;\n" +
                "    else {\n" +
                "        b = wr(String.fromCharCode(84));\n" +
                "        var c = wr(String.fromCharCode(75));\n" +
                "        b = [b(), b()];\n" +
                "        b[1] = c();\n" +
                "        b = (yr = window[b.join(c())] || \"\") || \"\"\n" +
                "    }\n" +
                "    var d = wr(String.fromCharCode(116))\n" +
                "        , c = wr(String.fromCharCode(107))\n" +
                "        , d = [d(), d()];\n" +
                "    d[1] = c();\n" +
                "    c = \"&\" + d.join(\"\") + \"=\";\n" +
                "    d = b.split(\".\");\n" +
                "    b = Number(d[0]) || 0;\n" +
                "    for (var e = [], f = 0, g = 0; g < a.length; g++) {\n" +
                "        var l = a.charCodeAt(g);\n" +
                "        128 > l ? e[f++] = l : (2048 > l ? e[f++] = l >> 6 | 192 : (55296 == (l & 64512) && g + 1 < a.length && 56320 == (a.charCodeAt(g + 1) & 64512) ? (l = 65536 + ((l & 1023) << 10) + (a.charCodeAt(++g) & 1023),\n" +
                "            e[f++] = l >> 18 | 240,\n" +
                "            e[f++] = l >> 12 & 63 | 128) : e[f++] = l >> 12 | 224,\n" +
                "            e[f++] = l >> 6 & 63 | 128),\n" +
                "            e[f++] = l & 63 | 128)\n" +
                "    }\n" +
                "    a = b;\n" +
                "    for (f = 0; f < e.length; f++)\n" +
                "        a += e[f],\n" +
                "            a = xr(a, \"+-a^+6\");\n" +
                "    a = xr(a, \"+-3^+b+-f\");\n" +
                "    a ^= Number(d[1]) || 0;\n" +
                "    0 > a && (a = (a & 2147483647) + 2147483648);\n" +
                "    a %= 1E6;\n" +
                "    return c + (a.toString() + \".\" + (a ^ b))\n" +
                "}\n" +
                "\n" +
                "var yr = null;\n" +
                "var wr = function(a) {\n" +
                "    return function() {\n" +
                "        return a\n" +
                "    }\n" +
                "}\n" +
                "    , xr = function(a, b) {\n" +
                "    for (var c = 0; c < b.length - 2; c += 3) {\n" +
                "        var d = b.charAt(c + 2)\n" +
                "            , d = \"a\" <= d ? d.charCodeAt(0) - 87 : Number(d)\n" +
                "            , d = \"+\" == b.charAt(c + 1) ? a >>> d : a << d;\n" +
                "        a = \"+\" == b.charAt(c) ? a + d & 4294967295 : a ^ d\n" +
                "    }\n" +
                "    return a\n" +
                "};";
    }
}
