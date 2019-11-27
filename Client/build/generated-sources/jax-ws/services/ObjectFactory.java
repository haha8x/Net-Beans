
package services;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the services package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetAllStudentResponse_QNAME = new QName("http://services/", "getAllStudentResponse");
    private final static QName _AddNewStudentResponse_QNAME = new QName("http://services/", "addNewStudentResponse");
    private final static QName _AddNewStudent_QNAME = new QName("http://services/", "addNewStudent");
    private final static QName _FindStudentByRollNo_QNAME = new QName("http://services/", "findStudentByRollNo");
    private final static QName _UpdateStudent_QNAME = new QName("http://services/", "updateStudent");
    private final static QName _GetAllStudent_QNAME = new QName("http://services/", "getAllStudent");
    private final static QName _IsExist_QNAME = new QName("http://services/", "isExist");
    private final static QName _FindStudentByRollNoResponse_QNAME = new QName("http://services/", "findStudentByRollNoResponse");
    private final static QName _UpdateStudentResponse_QNAME = new QName("http://services/", "updateStudentResponse");
    private final static QName _IsExistResponse_QNAME = new QName("http://services/", "isExistResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: services
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UpdateStudent }
     * 
     */
    public UpdateStudent createUpdateStudent() {
        return new UpdateStudent();
    }

    /**
     * Create an instance of {@link GetAllStudentResponse }
     * 
     */
    public GetAllStudentResponse createGetAllStudentResponse() {
        return new GetAllStudentResponse();
    }

    /**
     * Create an instance of {@link FindStudentByRollNo }
     * 
     */
    public FindStudentByRollNo createFindStudentByRollNo() {
        return new FindStudentByRollNo();
    }

    /**
     * Create an instance of {@link AddNewStudentResponse }
     * 
     */
    public AddNewStudentResponse createAddNewStudentResponse() {
        return new AddNewStudentResponse();
    }

    /**
     * Create an instance of {@link IsExistResponse }
     * 
     */
    public IsExistResponse createIsExistResponse() {
        return new IsExistResponse();
    }

    /**
     * Create an instance of {@link GetAllStudent }
     * 
     */
    public GetAllStudent createGetAllStudent() {
        return new GetAllStudent();
    }

    /**
     * Create an instance of {@link Student }
     * 
     */
    public Student createStudent() {
        return new Student();
    }

    /**
     * Create an instance of {@link AddNewStudent }
     * 
     */
    public AddNewStudent createAddNewStudent() {
        return new AddNewStudent();
    }

    /**
     * Create an instance of {@link FindStudentByRollNoResponse }
     * 
     */
    public FindStudentByRollNoResponse createFindStudentByRollNoResponse() {
        return new FindStudentByRollNoResponse();
    }

    /**
     * Create an instance of {@link UpdateStudentResponse }
     * 
     */
    public UpdateStudentResponse createUpdateStudentResponse() {
        return new UpdateStudentResponse();
    }

    /**
     * Create an instance of {@link IsExist }
     * 
     */
    public IsExist createIsExist() {
        return new IsExist();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllStudentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "getAllStudentResponse")
    public JAXBElement<GetAllStudentResponse> createGetAllStudentResponse(GetAllStudentResponse value) {
        return new JAXBElement<GetAllStudentResponse>(_GetAllStudentResponse_QNAME, GetAllStudentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddNewStudentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "addNewStudentResponse")
    public JAXBElement<AddNewStudentResponse> createAddNewStudentResponse(AddNewStudentResponse value) {
        return new JAXBElement<AddNewStudentResponse>(_AddNewStudentResponse_QNAME, AddNewStudentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddNewStudent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "addNewStudent")
    public JAXBElement<AddNewStudent> createAddNewStudent(AddNewStudent value) {
        return new JAXBElement<AddNewStudent>(_AddNewStudent_QNAME, AddNewStudent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindStudentByRollNo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "findStudentByRollNo")
    public JAXBElement<FindStudentByRollNo> createFindStudentByRollNo(FindStudentByRollNo value) {
        return new JAXBElement<FindStudentByRollNo>(_FindStudentByRollNo_QNAME, FindStudentByRollNo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateStudent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "updateStudent")
    public JAXBElement<UpdateStudent> createUpdateStudent(UpdateStudent value) {
        return new JAXBElement<UpdateStudent>(_UpdateStudent_QNAME, UpdateStudent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllStudent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "getAllStudent")
    public JAXBElement<GetAllStudent> createGetAllStudent(GetAllStudent value) {
        return new JAXBElement<GetAllStudent>(_GetAllStudent_QNAME, GetAllStudent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsExist }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "isExist")
    public JAXBElement<IsExist> createIsExist(IsExist value) {
        return new JAXBElement<IsExist>(_IsExist_QNAME, IsExist.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindStudentByRollNoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "findStudentByRollNoResponse")
    public JAXBElement<FindStudentByRollNoResponse> createFindStudentByRollNoResponse(FindStudentByRollNoResponse value) {
        return new JAXBElement<FindStudentByRollNoResponse>(_FindStudentByRollNoResponse_QNAME, FindStudentByRollNoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateStudentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "updateStudentResponse")
    public JAXBElement<UpdateStudentResponse> createUpdateStudentResponse(UpdateStudentResponse value) {
        return new JAXBElement<UpdateStudentResponse>(_UpdateStudentResponse_QNAME, UpdateStudentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsExistResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "isExistResponse")
    public JAXBElement<IsExistResponse> createIsExistResponse(IsExistResponse value) {
        return new JAXBElement<IsExistResponse>(_IsExistResponse_QNAME, IsExistResponse.class, null, value);
    }

}
