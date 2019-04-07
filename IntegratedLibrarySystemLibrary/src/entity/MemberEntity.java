package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import util.exception.MemberEntityException;
import util.helper.CredentialFormatHelper;
import util.helper.CryptographicHelper;

@Entity
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "memberEntity", propOrder = {
    "memberId",
    "identityNumber",
    "securityCode",
    "firstName",
    "lastName",
    "gender",
    "age",
    "phone",
    "address",
    "salt"
})

public class MemberEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    // Although NRIC is only 9 characters, passport number can be more than this
    @Column(length = 50, nullable = false, unique = true)
    private String identityNumber;

    @Column(length = 50, nullable = false)
    private String securityCode;

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(length = 50, nullable = false)
    private String gender;

    @Column(nullable = false)
    private Integer age;

    @Column(length = 50, nullable = false)
    private String phone;

    @Column(length = 255, nullable = false)
    private String address;
    
    @Column(length = 32, nullable = false)
    private String salt;

    @XmlTransient
    @OneToMany(mappedBy = "member")
    private List<LoanEntity> loans;

    @XmlTransient
    @OneToMany(mappedBy = "member")
    private List<FineEntity> fines;

    @XmlTransient
    @OneToMany(mappedBy = "member")
    private List<ReservationEntity> reservations;

    public MemberEntity() {
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
    }

    public MemberEntity(String identityNumber, String securityCode, String firstName, String lastName, String gender, String age, String phone, String address) throws MemberEntityException {
        this();
        
        verifyFormats(identityNumber, securityCode, firstName, lastName, gender, age, phone, address);
        
        this.identityNumber = identityNumber;
        this.setSecurityCode(securityCode);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = CredentialFormatHelper.convertToStandardGenderFormat(gender);
        this.age = Integer.valueOf(age);
        this.phone = phone;
        this.address = address;
        
        this.fines = new ArrayList<FineEntity>();
        this.loans = new ArrayList<LoanEntity>();
        this.reservations = new ArrayList<ReservationEntity>();
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(securityCode + this.salt));
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<LoanEntity> getLoans() {
        return loans;
    }

    public void setLoans(List<LoanEntity> loans) {
        this.loans = loans;
    }

    public List<FineEntity> getFines() {
        return fines;
    }

    public void setFines(List<FineEntity> fines) {
        this.fines = fines;
    }

    public List<ReservationEntity> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationEntity> reservations) {
        this.reservations = reservations;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public void updateMemberEntity(String identityNumber, String securityCode, String firstName, String lastName, String gender, String age, String phone, String address) throws MemberEntityException {
        if (identityNumber.equals("")) {
            identityNumber = this.identityNumber;
        }
        
        if (firstName.equals("")) {
            firstName = this.firstName;
        }
        
        if (lastName.equals("")) {
            lastName = this.lastName;
        }
        
        if (gender.equals("")) {
            gender = this.gender;
        }
        
        if (age.equals("")) {
            age = this.age.toString();
        }
        
        if (phone.equals("")) {
            phone = this.phone;
        }
        
        if (address.equals("")) {
            address = this.address;
        }
        
        if (securityCode.equals("")) {
            verifyFormats(identityNumber, firstName, lastName, gender, age, phone, address);
        }
        else {
            verifyFormats(identityNumber, securityCode, firstName, lastName, gender, age, phone, address);
            this.setSecurityCode(securityCode);
        }
        
        this.identityNumber = identityNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = CredentialFormatHelper.convertToStandardGenderFormat(gender);;
        this.age = Integer.valueOf(age);
        this.phone = phone;
        this.address = address;
    }
    
    private void verifyFormats(String identityNumber, String firstName, String lastName, String gender, String age, String phone, String address) throws MemberEntityException {
        String errorMessage = "";
        
        if (!CredentialFormatHelper.isValidMemberIdentityNumberFormat(identityNumber)) {
            errorMessage += "Invalid ID or Duplicate Identity Number! Please try again.\n";
        }
        
        if (!CredentialFormatHelper.isValidNameFormat(firstName)) {
            errorMessage += "Invalid First Name. Please try again.\n";
        }
        
        if (!CredentialFormatHelper.isValidNameFormat(lastName)) {
            errorMessage += "Invalid Last Name. Please try again.\n";
        }
        
        if (!CredentialFormatHelper.isValidGenderFormat(gender)) {
            errorMessage += "Invalid Gender Format. Please try again.\n";
        }
        
        if (!CredentialFormatHelper.isValidAgeFormat(age)) {
            errorMessage += "Invalid Age Format. Please try again.\n";
        }
        
        if (!CredentialFormatHelper.isValidPhoneFormat(phone)) {
            errorMessage += "Invalid Phone Format. Please try again.\n";
        }
        
        if (!CredentialFormatHelper.isValidAddressFormat(address)) {
            errorMessage += "Invalid Address Format. Please try again.\n";
        }
        
        if (!errorMessage.equals("")) {
            throw new MemberEntityException(errorMessage);
        }
    }
    
    private void verifyFormats(String identityNumber, String securityCode, String firstName, String lastName, String gender, String age, String phone, String address) throws MemberEntityException {
        String errorMessage = "";
        
        try {
            verifyFormats(identityNumber, firstName, lastName, gender, age, phone, address);
        }
        catch (MemberEntityException ex) {
            errorMessage += ex.getMessage();
        }
        
        if (!CredentialFormatHelper.isValidSecurityCodeFormat(securityCode)) {
            errorMessage += "Invalid Security Code. Please try again.\n";
        }
        
        if (!errorMessage.equals("")) {
            throw new MemberEntityException(errorMessage);
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getMemberId() != null ? getMemberId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MemberEntity)) {
            return false;
        }
        MemberEntity other = (MemberEntity) object;
        if ((this.getMemberId() == null && other.getMemberId() != null) || (this.getMemberId() != null && !this.getMemberId().equals(other.getMemberId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.MemberEntity[ id=" + getMemberId() + " ]";
    }
}
