package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class LoanEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @OneToOne
    @JoinColumn(name = "BOOKID", nullable = false, unique = true)
    private BookEntity book;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "MEMBERID", nullable = false)
    private MemberEntity member;
    
    public LoanEntity() {}
    
    public LoanEntity(BookEntity bookEntity, Date endDate, MemberEntity memberEntity) {
        this.book = bookEntity;
        this.endDate = endDate;
        this.member = memberEntity;
    }
    
    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public BookEntity getBook() {
        return book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getLoanId() != null ? getLoanId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoanEntity)) {
            return false;
        }
        LoanEntity other = (LoanEntity) object;
        if ((this.getLoanId() == null && other.getLoanId() != null) || (this.getLoanId() != null && !this.getLoanId().equals(other.getLoanId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.LoanEntity[ id=" + getLoanId() + " ]";
    }
}
