
package services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for findStudentByRollNo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="findStudentByRollNo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rollNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findStudentByRollNo", propOrder = {
    "rollNo"
})
public class FindStudentByRollNo {

    protected String rollNo;

    /**
     * Gets the value of the rollNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRollNo() {
        return rollNo;
    }

    /**
     * Sets the value of the rollNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRollNo(String value) {
        this.rollNo = value;
    }

}
