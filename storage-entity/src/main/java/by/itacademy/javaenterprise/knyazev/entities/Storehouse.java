package by.itacademy.javaenterprise.knyazev.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "storehouse")
public class Storehouse {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "good_id", nullable = false)
	private Good good;
	
	@Column(length = 15)
	private String ttnNum;
	
	@Column(name = "date", updatable = false)
	private LocalDateTime dateTime;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Unit unit;
	
	@Column(nullable = false)
	private int quantity;
	
	@Column(nullable = false, precision = 9, scale = 2)
	private BigDecimal price;
	
	public enum Unit {
		кг, г, т, шт, ед
	}	
}
