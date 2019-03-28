package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class FineEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fineId;

    @Column(nullable = false)
    private Long amount;

    @ManyToOne
    @JoinColumn(name = "MEMBERID", nullable = false)
    private MemberEntity member;

    public FineEntity() {}
    
    public FineEntity(Long amount, MemberEntity member) {
        this.amount = amount;
        this.member = member;
    }
    
    public Long getFineId() {
        return fineId;
    }

    public void setFineId(Long fineId) {
        this.fineId = fineId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
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
        hash += (getFineId() != null ? getFineId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FineEntity)) {
            return false;
        }
        FineEntity other = (FineEntity) object;
        if ((this.getFineId() == null && other.getFineId() != null) || (this.getFineId() != null && !this.getFineId().equals(other.getFineId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FineEntity[ id=" + getFineId() + " ]";
    }

}
