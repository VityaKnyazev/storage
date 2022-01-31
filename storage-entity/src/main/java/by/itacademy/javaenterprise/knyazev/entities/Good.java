package by.itacademy.javaenterprise.knyazev.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "goods")
@NamedNativeQuery(name= "allGoods", query = "SELECT id, name, sort, description, category_id, producer_id FROM goods", resultClass = Good.class)
@NamedQuery(name = "countGoodsByCategoryId", query = "SELECT COUNT(g) FROM Good g WHERE g.category.id=?1")
public class Good {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 30, nullable = false)
	private String name;
	
	@Column(length = 30, nullable = false)
	private String sort;
	
	@Column(length = 900, nullable = false)
	private String description;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name = "category_id")	
	private Category category;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name = "producer_id")
	private Producer producer;	
	
	@OneToOne(mappedBy = "good", targetEntity = Storehouse.class, fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE})
	private Storehouse storehouse;
}
