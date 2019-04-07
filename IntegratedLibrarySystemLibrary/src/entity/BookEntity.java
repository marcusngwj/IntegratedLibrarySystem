package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import util.exception.BookEntityException;
import util.helper.CredentialFormatHelper;

@Entity
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "bookEntity", propOrder = {
    "bookId",
    "title",
    "isbn",
    "year"
})

public class BookEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(length = 255, nullable = false)
    private String title;

    // Although ISBN is universal unique, but there might be more than one same book at the library at a time?
    @Column(length = 50, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private Integer year;

    @XmlTransient
    @OneToOne(mappedBy = "book", optional = true)
    private LoanEntity loan;

    @XmlTransient
    @OneToMany(mappedBy = "book")
    private List<ReservationEntity> reservations;

    public BookEntity() {
    }

    public BookEntity(String title, String isbn, String year) throws BookEntityException {
        verifyFormats(title, isbn, year);
        
        this.title = title;
        this.isbn = isbn;
        this.year = Integer.valueOf(year);
    }
    
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public LoanEntity getLoan() {
        return loan;
    }

    public void setLoan(LoanEntity loan) {
        this.loan = loan;
    }

    public List<ReservationEntity> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationEntity> reservations) {
        this.reservations = reservations;
    }
    
    public void updateStaff(String title, String isbn, String year) throws BookEntityException {
        if (title.equals("")) {
            title = this.title;
        }
        
        if (isbn.equals("")) {
            isbn = this.isbn;
        }
        
        if (year.equals("")) {
            year = this.year.toString();
        }
        
        verifyFormats(title, isbn, year);
        
        this.title = title;
        this.isbn = isbn;
        this.year = Integer.valueOf(year);
    }
    
    private void verifyFormats(String title, String isbn, String year) throws BookEntityException {
        String errorMessage = "";
        
        if (!CredentialFormatHelper.isValidBookTitleFormat(title)) {
            errorMessage += "Invalid Title. Please try again.\n";
        }
        
        if (!CredentialFormatHelper.isValidIsbnFormat(isbn)) {
            errorMessage += "Invalid ISBN. Please try again.\n";
        }
        
        if (!CredentialFormatHelper.isValidYearFormat(year)) {
            errorMessage += "Invalid Year. Please try again.\n";
        }
        
        if (!errorMessage.equals("")) {
            throw new BookEntityException(errorMessage);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getBookId() != null ? getBookId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BookEntity)) {
            return false;
        }
        BookEntity other = (BookEntity) object;
        if ((this.getBookId() == null && other.getBookId() != null) || (this.getBookId() != null && !this.getBookId().equals(other.getBookId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.BookEntity[ id=" + getBookId() + " ]";
    }
}
