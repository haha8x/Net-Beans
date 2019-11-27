/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

/**
 *
 * @author exam
 */
public class Student {

    private String rollNo;
    private String name;
    private String className;

    public Student() {
    }

    public Student(String rollNo, String name, String className) {
        this.rollNo = rollNo;
        this.name = name;
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }
}
