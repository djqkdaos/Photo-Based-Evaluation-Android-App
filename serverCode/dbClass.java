
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test;

import java.sql.*;
import org.apache.log4j.Logger;
import org.json.JSONObject;
/**
 *
 * @author Kim
 */
public class dbclass {
    private Logger logger = Logger.getLogger(getClass());

    String rtval=null;
    String rtval2=null;
    String rtval3=null;
    
    void photoupload(String hash, String id, String nowdate, String msg) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // 2. DB와 연결
            String url = "jdbc:mysql://localhost:3306/cloth";
            String user = "root", pwd = "autoset";
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            Statement stmt1 = con.createStatement();
            ResultSet rs = null;
            ResultSet rs1 = null;
            String sql;
            sql = "insert into photolist values ('" + hash + "','0', '0', '"+nowdate+"', '"+id+"', '"+msg+"')";
            logger.info(sql);
            stmt.executeUpdate(sql);
            sql = "insert into pointuplist values ('"+hash+"','0','System')";
            stmt.executeUpdate(sql);
            con.close(); // DB와 연결된 자원 반납 (중요)

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            logger.error("DB클래스에서 에러발생 : "+ e);
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
           logger.error("DB클래스에서 에러발생 : "+ e);
        }
        
    }
    void myphoto(String mhash) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // 2. DB와 연결
            logger.info("이까지 실행");
            String url = "jdbc:mysql://localhost:3306/cloth";
            String user = "root", pwd = "autoset";
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            Statement stmt1 = con.createStatement();
            ResultSet rs = null;
            ResultSet rs1 = null;
            String sql;
            String wDate;
            sql = "select * from photolist where uploadusername='"+mhash+"'";
            logger.info("sql 쿼리 : "+sql);
            JSONObject myobj = new JSONObject();
           rs = stmt.executeQuery(sql);
            
            ResultSetMetaData rsmd = rs.getMetaData();
            rs.first();
            int tmp = rs.getRow();
            rs.last();
            int tmp2 = rs.getRow();
            int tmp3,tmp4,tmp5;
            rs.first();
            logger.info("tmp1 : "+tmp+"/tmp2 : "+tmp2);
            myobj.put("allcount",tmp2);
            
            for(int i=tmp;i<=tmp2;i++){
                logger.info("이까지 실행"+i);
                 myobj.put("hash"+i,rs.getString("photohash"));
                 logger.info("hash"+i+"/"+rs.getString("photohash"));
                 myobj.put("msg"+i,rs.getString("msg"));
                 logger.info("msg"+i+"/"+rs.getString("msg"));
                 tmp3 = rs.getInt("totalpoint");
                 logger.info("tmp3:"+tmp3);
                 tmp4 = rs.getInt("upmember");
                 logger.info("tmp4:"+tmp4);
                 if(tmp3 == 0){
                     tmp5 = 0;
                    logger.info("tmp5:"+tmp5);
                    myobj.put("point"+i,tmp5);
                 }else{
                    tmp5 = tmp3/tmp4;
                    logger.info("tmp5:"+tmp5);
                    myobj.put("point"+i,tmp5);
                 }
                 wDate = rs.getString("uploaddate");
                 logger.info("Date"+wDate);
                 myobj.put("Date"+i,wDate);
                 
                 logger.info("myobj:"+myobj);
                if(rs.isLast()){
                    break;
                }else{
                    rs.next();
                }
            }
            logger.info("나의 사진 결과 : "+myobj.toString());
            rtval = myobj.toString();
            con.close(); // DB와 연결된 자원 반납 (중요)

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            logger.error("DB클래스에서 에러발생 : "+ e);
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
           logger.error("DB클래스에서 에러발생 : "+ e);
        }
        
    }
    void randphoto(String randphotoid) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // 2. DB와 연결
            String url = "jdbc:mysql://localhost:3306/cloth";
            String user = "root", pwd = "autoset";
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            Statement stmt1 = con.createStatement();
            ResultSet rs = null;
            String photohash=null;
            String sql;
            sql = "select * from photolist l inner join pointuplist u on l.photohash = u.photohash where l.upmember<11 and u.pointuser = 'System' and l.uploadusername != '"+randphotoid+"' order by rand() limit 1";
            logger.info("sql 요청"+sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                 photohash = rs.getString("photohash");
            }
            rtval = photohash;
            con.close(); // DB와 연결된 자원 반납 (중요)

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            logger.error("DB클래스에서 에러발생 : "+ e);
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
           logger.error("DB클래스에서 에러발생 : "+ e);
        }
        
    }
    /*void delete(String dhash) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // 2. DB와 연결
            String url = "jdbc:mysql://localhost:11112/appcreate";
            String user = "root", pwd = "wjdqhqhdks1!";
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            Statement stmt1 = con.createStatement();
            ResultSet rs = null;
            ResultSet rs1 = null;
            String sql;
            String phohash = null;
            String wDate = null;
            String msg = null;
            sql = "delete from photolist where photohash = '"+dhash+"'";
            stmt.executeUpdate(sql);
            sql = "delete from pointuplist where photohash = '"+dhash+"'";
            stmt1.executeUpdate(sql); 
            con.close(); // DB와 연결된 자원 반납 (중요)

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            logger.error("DB클래스에서 에러발생 : "+ e);
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
           logger.error("DB클래스에서 에러발생 : "+ e);
        }
        
    }*/
    void photoview(String hash) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // 2. DB와 연결
            String url = "jdbc:mysql://localhost:3306/cloth";
            String user = "root", pwd = "autoset";
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            Statement stmt1 = con.createStatement();
            ResultSet rs = null;
            ResultSet rs1 = null;
            String sql;
            String phohash = null;
            String wDate = null;
            String msg = null;
            sql = "select * from photolist where photohash='"+hash+"'";
            rs = stmt.executeQuery(sql);
            logger.info(sql);
            while (rs.next()) {
                 phohash = rs.getString("photohash");
                 logger.info("phohash"+phohash);
                 wDate = rs.getString("uploaddate");
                 logger.info("Date"+wDate);
                 msg = rs.getString("msg");
                 logger.info("msg"+msg);
            }
            rtval = wDate;
            rtval2 = phohash;
            rtval3 = msg;
            logger.info("dbc 로그"+wDate+phohash+msg);
            con.close(); // DB와 연결된 자원 반납 (중요)

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            logger.error("DB클래스에서 에러발생 : "+ e);
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
           logger.error("DB클래스에서 에러발생 : "+ e);
        }
        
    }
    void photopointup(String hash, String id, String point) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // 2. DB와 연결
            String url = "jdbc:mysql://localhost:3306/cloth";
            String user = "root", pwd = "autoset";
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            Statement stmt1 = con.createStatement();
            ResultSet rs = null;
            ResultSet rs1 = null;
            String uploaddate=null;
            String userid = null;
            String sql;
            sql = "update photolist set totalpoint = totalpoint + "+point+", upmember = upmember+1 where photohash = '"+hash+"'";
            stmt.executeUpdate(sql);
            //while (rs.next()) {
            //    userid = rs.getString("pointid");
            //}
            //if (userid.equals(id)){
            //    rtval="er";
            //}else{      
            //sql = "insert into photolist values ('" + hash + "','0','"++"\\"+hash+"', '"+"')";
            
            sql = "insert into pointuplist values ('" + hash + "','"+point+"','"+id+"')";
            stmt1.executeUpdate(sql);
            con.close(); // DB와 연결된 자원 반납 (중요)
            //stmt.close();
            stmt1.close();
            //rs.close();
            //};
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            logger.error("DB클래스에서 에러발생 : "+ e);
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
           logger.error("DB클래스에서 에러발생 : "+ e);
        }
        
    }/*
    void photoupload(String hash, String id, String nowdate) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // 2. DB와 연결
            String url = "jdbc:mysql://localhost:11112/appcreate";
            String user = "root", pwd = "wjdqhqhdks1!";
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            Statement stmt1 = con.createStatement();
            ResultSet rs = null;
            ResultSet rs1 = null;
            String sql;
            sql = "insert into photolist values ('" + hash + "','0','"+nowdate+"\\"+hash;
            stmt.executeQuery(sql);
            
            
            String rthash = null; //db결과용 해쉬값
            int rtviruscount = 0; //db결과용 카운트
            String rthash1 = null; //db결과용 해쉬값
            int rtviruscount1 = 0;//db결과용 카운트
            sql = "SELECT * FROM filescan where sha256 = '" + hash + "'";
            
            logger.info("sql 쿼리 : "+sql);
            while (rs.next()) {
                rthash = rs.getString("sha256");
                rtviruscount = rs.getInt("viruscount");
            }
            if (rthash == null) {//검사된적이있는지 확인
                rtval = "indetermined";//검사된적없음
                sql = "insert into filescan (sha256, filedata) values ('"+hash+"',now())";
                stmt.executeUpdate(sql);
                logger.info("sql 쿼리 : "+sql);
            } else if (rthash != null) {//검사된적이 있으면 동작
                if (rtviruscount == -1) {
                    rtval = "scanning";
                } else {
                    sql = "select * from filescan where adddate(filedata, interval 7 day) < now() and sha256 = '" + hash + "'";
                    rs1 = stmt1.executeQuery(sql);
                    logger.info("sql 쿼리 : "+sql);
                    while (rs1.next()) {
                        rthash1 = rs1.getString("sha256");
                        rtviruscount1 = rs1.getInt("viruscount");
                    }
                    if (rthash1 != null) {
                        rtval = "rescan";
                    } else {
                        if (rtviruscount >= 2) {
                            rtval = "unsafe";
                        } else if (rtviruscount < 2) {
                            rtval = "safe";
                        }
                    }

                }
            }

            con.close(); // DB와 연결된 자원 반납 (중요)

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            logger.error("DB클래스에서 에러발생 : "+ e);
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
           logger.error("DB클래스에서 에러발생 : "+ e);
        }

    }*/
    /*
    void urlsc(String rturl){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("드라이버 로딩 성공!!");
            // 2. DB와 연결
            String url = "jdbc:mysql://localhost:3306/antivirus";
            String user = "root", pwd = "wjdqhqhdks";
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            Statement stmt1 = con.createStatement();
            ResultSet rs = null;
            ResultSet rs1 = null;
            String sql;
            String rsUrl = null;
            int rsUrlviruscount = 0;
            String rsUrl1 = null;
            int rsUrlviruscount1 = 0;
            sql = "SELECT * FROM urlscan where urlvlaue = '" + rturl + "'";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                rsUrl = rs.getString("urlvlaue");
                rsUrlviruscount = rs.getInt("urlviruscount");
            }
            if (rsUrl == null) {//검사된적이있는지 확인
                rtval = "indetermined";//검사된적없음
                sql = "insert into urlscan (urlvlaue, urldata) values ('"+rturl+"',now())";
                stmt.executeUpdate(sql);
            }
             else if(rsUrl != null){
                     if(rsUrlviruscount==-1){
                         rtval="scanning";
                     }else{
                          sql = "select * from urlscan where adddate(urldata, interval 3 day) < now() and urlvlaue = '" + rturl + "'";
                          rs1 = stmt1.executeQuery(sql);
                           while (rs1.next()) {
                                rsUrl1 = rs1.getString("urlvlaue");
                                rsUrlviruscount1 = rs1.getInt("urlviruscount");
                           }
                           if(rsUrl1!=null){
                               rtval="rescan";
                           }else{
                               if (rsUrlviruscount >= 2) {
                                rtval = "unsafe";
                                } else if (rsUrlviruscount < 2) {
                                    rtval = "safe";
                                }
                            }
                     }
             }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            logger.error("DB클래스에서 에러발생 : "+ e);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            logger.error("DB클래스에서 에러발생 : "+ e);
        }
    }
    void vtfileupdate(String updatehash, String updateviruscount){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("드라이버 로딩 성공!!");
            // 2. DB와 연결
            String url = "jdbc:mysql://localhost:3306/antivirus";
            String user = "root", pwd = "wjdqhqhdks";
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            Statement stmt1 = con.createStatement();
            ResultSet rs = null;
            ResultSet rs1 = null;
            String sql;
            sql = "update filescan set viruscount = "+updateviruscount+" where sha256 = '"+updatehash+"'";
            logger.info(sql);
            stmt.executeUpdate(sql);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            logger.error("DB클래스에서 에러발생 : "+ e);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            logger.error("DB클래스에서 에러발생 : "+ e);
        }
    }
   void vturlupdate(String updateurl, int updateurlviruscount){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("드라이버 로딩 성공!!");
            // 2. DB와 연결
            String url = "jdbc:mysql://localhost:3306/antivirus";
            String user = "root", pwd = "wjdqhqhdks";
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            Statement stmt1 = con.createStatement();
            ResultSet rs = null;
            ResultSet rs1 = null;
            String sql;
            sql = "update urlscan set urlviruscount = "+updateurlviruscount+" where urlvlaue = '"+updateurl+"'";
            stmt.executeUpdate(sql);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
           logger.error("DB클래스에서 에러발생 : "+ e);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            logger.error("DB클래스에서 에러발생 : "+ e);
        }
    }
   
   void gogititle(String gogi){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("드라이버 로딩 성공!!");
            // 2. DB와 연결
            String url = "jdbc:mysql://localhost:3306/antivirus";
            String user = "root", pwd = "wjdqhqhdks";
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement stmt = con.createStatement();
            ResultSet rs = null;
            String sql;
            sql = "select gonggititle from gonggi";
            rs = stmt.executeQuery(sql);
            String title[] = {null,null};
            int t=0;
            while (rs.next()) {
                              title[t] = rs.getString("gonggititle");
                              t++;
                           }
            rtval=title[0];
            rtval2=title[1];
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
           logger.error("DB클래스에서 에러발생 : "+ e);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            logger.error("DB클래스에서 에러발생 : "+ e);
        }

}*/}
