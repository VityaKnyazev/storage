package by.itacademy.javaenterprise.knyazev.embeddables;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
	@Column(name="postal_code", nullable = false, length = 15)	
	private String postalCode;
	
	@Column(nullable = false, length = 30)	
	private String country;
	
	@Column(nullable = false, length = 30)	
	private String region;
	
	@Column(nullable = false, length = 40)	
	private String locality;
	@Column(length = 50)
	private String street;
	@Column(length = 25)
	private String building;
	
	private transient String description;
}