package by.itacademy.javaenterprise.knyazev.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import by.itacademy.javaenterprise.knyazev.converters.StatusTypeConverter;
import by.itacademy.javaenterprise.knyazev.converters.UnitTypeConverter;
import by.itacademy.javaenterprise.knyazev.utils.Status;
import by.itacademy.javaenterprise.knyazev.utils.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchases")
public class Purchase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE })
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "storehouse_id")
	private Storehouse storehouse;

	@Column(name = "date", updatable = false, nullable = false)
	private LocalDateTime dateTime;

	@Column(nullable = false)
	@Convert(converter = UnitTypeConverter.class)
	private Unit unit;

	@Column(nullable = false, precision = 9, scale = 3)
	private BigDecimal quantity;

	@Column(nullable = false, precision = 11, scale = 2)
	private BigDecimal price;

	@Column(nullable = false)
	@Convert(converter = StatusTypeConverter.class)
	private Status status;
}
