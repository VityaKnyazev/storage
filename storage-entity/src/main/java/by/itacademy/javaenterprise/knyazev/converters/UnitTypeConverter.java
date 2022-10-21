package by.itacademy.javaenterprise.knyazev.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import by.itacademy.javaenterprise.knyazev.utils.Unit;

@Converter
public class UnitTypeConverter implements AttributeConverter<Unit, String> {

	@Override
	public String convertToDatabaseColumn(Unit unit) {
		if (unit == null) {
			return null;
		}
		
		switch (unit) {
		case г :
			return Unit.г.name();
		case кг :
			return Unit.кг.name();
		case ед :
			return Unit.ед.name();
		case т :
			return Unit.т.name();
		case шт :
			return Unit.шт.name();
		default:
			throw new IllegalArgumentException(unit.name() + " not supported.");
		}		
		
	}

	@Override
	public Unit convertToEntityAttribute(String dbUnit) {
		if (dbUnit == null) {
			return null;
		}
		
		if (dbUnit.equals(Unit.г.name())) {
			return Unit.г;
		} else if (dbUnit.equals(Unit.кг.name())) {
			return Unit.кг;
		}  else if (dbUnit.equals(Unit.ед.name())) {
			return Unit.ед;
		}  else if (dbUnit.equals(Unit.т.name())) {
			return Unit.т;
		}  else if (dbUnit.equals(Unit.шт.name())) {
			return Unit.шт;
		} else {
			throw new IllegalArgumentException(dbUnit + " not supported.");
		}	
	}

}
