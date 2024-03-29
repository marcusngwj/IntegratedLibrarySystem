package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import util.exception.StaffEntityException;
import util.helper.CredentialFormatHelper;
import util.helper.CryptographicHelper;

@Entity
@XmlRootElement
@XmlType(name = "staffEntity", propOrder = {
    "staffId",
    "firstName",
    "lastName",
    "username",
    "password"
})

public class StaffEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Column(length = 50, nullable = false)
    private String password;
    
    @Column(length = 32, nullable = false)
    private String salt;

    public StaffEntity() {
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
    }

    public StaffEntity(String firstName, String lastName, String username, String password) throws StaffEntityException {
        this();
        
        verifyFormats(firstName, lastName, username, password);
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.setPassword(password);
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
    }
    
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public void updateStaff(String firstName, String lastName, String username, String password) throws StaffEntityException {
        if (firstName.equals("")) {
            firstName = this.firstName;
        }
        
        if (lastName.equals("")) {
            lastName = this.lastName;
        }
        
        if (username.equals("")) {
            username = this.username;
        }
        
        if (password.equals("")) {
            verifyFormats(firstName, lastName, username);
        }
        else {
            verifyFormats(firstName, lastName, username, password);
            this.setPassword(password);
        }
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }
    
    private void verifyFormats(String firstName, String lastName, String username) throws StaffEntityException {
        String errorMessage = "";
        
        if (!CredentialFormatHelper.isValidNameFormat(firstName)) {
            errorMessage += "Invalid First Name. Please try again.\n";
        }
        
        if (!CredentialFormatHelper.isValidNameFormat(lastName)) {
            errorMessage += "Invalid Last Name. Please try again.\n";
        }
        
        if (!CredentialFormatHelper.isValidUsernameFormat(username)) {
            errorMessage += "Invalid Username. Please try again.\n";
        }
        
        if (!errorMessage.equals("")) {
            throw new StaffEntityException(errorMessage);
        }
    }
    
    private void verifyFormats(String firstName, String lastName, String username, String password) throws StaffEntityException {
        String errorMessage = "";
        
        try {
            verifyFormats(firstName, lastName, username);
        }
        catch (StaffEntityException ex) {
            errorMessage += ex.getMessage();
        }
        
        if (!CredentialFormatHelper.isValidPasswordFormat(password)) {
            errorMessage += "Invalid Password. Please try again.\n";
        }
        
        if (!errorMessage.equals("")) {
            throw new StaffEntityException(errorMessage);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getStaffId() != null ? getStaffId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the staffId fields are not set
        if (!(object instanceof StaffEntity)) {
            return false;
        }
        StaffEntity other = (StaffEntity) object;
        if ((this.getStaffId() == null && other.getStaffId() != null) || (this.getStaffId() != null && !this.getStaffId().equals(other.getStaffId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.StaffEntity[ id=" + getStaffId() + " ]";
    }
}
