/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import services.Student;

/**
 *
 * @author exam
 */
@WebService()
public class WS_Student {

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAllStudent")
    public List<Student> getAllStudent() {
        //TODO write your implementation code here:
        List<Student> listStudent = new ArrayList<Student>();
        Connection conn = null;
        PreparedStatement stm = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1164;databaseName=DWSJ_Exam", "sa", "1234567");
            stm = conn.prepareCall("select * from tblStudent");
            ResultSet rs = stm.executeQuery();
//            listStudent = new ArrayList<Student>();
            while (rs.next()) {
                Student st = new Student();
                st.setRollNo(rs.getString("student_rollNo"));
                st.setName(rs.getString("student_name"));
                st.setClassName(rs.getString("student_class"));
                listStudent.add(st);
            }
        } catch (Exception ex) {
            Logger.getLogger(WS_Student.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stm.close();
                conn.close();
            } catch (Exception ex) {
                Logger.getLogger(WS_Student.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return listStudent;
    }
    public static void main(String[] args) {
        List<Student> lisyt = new WS_Student().getAllStudent();
        for (Student student : lisyt) {
            System.out.println(student.getClassName());
        }
    }
    /**
     * Web service operation
     */
    @WebMethod(operationName = "addNewStudent")
    public Boolean addNewStudent(@WebParam(name = "student") Student student) {
        //TODO write your implementation code here:
        Connection conn = null;
        PreparedStatement stm = null;
        Boolean status = false;
        try {

            //chua kiem tra tinh ton tai
            if (!isExist(student)) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1164;databaseName=DWSJ_Exam", "sa", "1234567");
                stm = conn.prepareCall("INSERT INTO tblStudent(student_rollNo,student_name,student_class) VALUES(?,?,?)");
                stm.setString(1, student.getRollNo());
                stm.setString(2, student.getName());
                stm.setString(3, student.getClassName());
                int a = stm.executeUpdate();
                if (a > 0) {
                    status = true;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(WS_Student.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stm.close();
                conn.close();
            } catch (Exception ex) {
                Logger.getLogger(WS_Student.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return status;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findStudentByRollNo")
    public Student findStudentByRollNo(@WebParam(name = "rollNo") String rollNo) {
        //TODO write your implementation code here:
        Connection conn = null;
        PreparedStatement stm = null;
        Student st = null;
        try {

            //chua kiem tra tinh ton tai
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1164;databaseName=DWSJ_Exam", "sa", "1234567");
            stm = conn.prepareCall("select * from tblstudent where student_rollNo=?");
            stm.setString(1, rollNo);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                st = new Student();
                st.setRollNo(rollNo);
                st.setName(rs.getString("student_name"));
                st.setClassName(rs.getString("student_class"));
            }
        } catch (Exception ex) {
            Logger.getLogger(WS_Student.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stm.close();
                conn.close();
            } catch (Exception ex) {
                Logger.getLogger(WS_Student.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return st;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "updateStudent")
    public Boolean updateStudent(@WebParam(name = "student") Student student) {
        //TODO write your implementation code here:
        boolean status = false;
        Connection conn = null;
        PreparedStatement stm = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1164;databaseName=DWSJ_Exam", "sa", "1234567");
            if (isExist(student)) {
                stm = conn.prepareCall("update tblStudent set student_name=?,student_class = ? where student_rollno=? ");
                stm.setString(1, student.getName());
                stm.setString(2, student.getClassName());
                stm.setString(3, student.getRollNo());
                int a = stm.executeUpdate();
                if (a > 0) {
                    status = true;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(WS_Student.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stm.close();
                conn.close();
            } catch (Exception ex) {
                Logger.getLogger(WS_Student.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return status;
    }

    //ham kiem tra tinh ton tai cua student
    public Boolean isExist(Student st) {
        boolean check = false;

        Connection conn = null;
        PreparedStatement stm = null;
        try {

            //chua kiem tra tinh ton tai
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1164;databaseName=DWSJ_Exam", "sa", "1234567");
            stm = conn.prepareCall("select * from tblstudent where student_rollNo=?");
            stm.setString(1, st.getRollNo());
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                check = true;
            }
        } catch (Exception ex) {
            Logger.getLogger(WS_Student.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stm.close();
                conn.close();
            } catch (Exception ex) {
                Logger.getLogger(WS_Student.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return check;
    }
}
